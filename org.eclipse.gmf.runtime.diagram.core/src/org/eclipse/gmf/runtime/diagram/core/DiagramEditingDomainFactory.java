/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionChangeDescription;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.FilterManager;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.ReadWriteValidatorImpl;
import org.eclipse.emf.transaction.impl.TransactionValidator;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.emf.transaction.util.ConditionalRedoCommand;
import org.eclipse.emf.transaction.util.TriggerCommand;
import org.eclipse.emf.workspace.WorkspaceEditingDomainFactory;
import org.eclipse.emf.workspace.impl.WorkspaceCommandStackImpl;
import org.eclipse.gmf.runtime.diagram.core.internal.listener.NotationSemProc;
import org.eclipse.gmf.runtime.diagram.core.listener.DiagramEventBroker;
import org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory;


/**
 * Factory for {@link TransactionalEditingDomain}s that are properly configured
 * to support a GMF diagram application. This factory should be preferred over
 * the {@link GMFEditingDomainFactory} because it attaches a listener required
 * to update the notation model after changes to the semantic model. Also, it
 * handles special use cases involving the DiagramEventBroker.
 * 
 * @author cmahoney
 */
public class DiagramEditingDomainFactory
    extends GMFEditingDomainFactory {
	
	private static class DiagramEditingDomain extends TransactionalEditingDomainImpl {
		// The following variable acts as a special latch for the DiagramEventBroker
		//  listener so that we can allow it to execute in a write transaction context
		//  while handling a post-commit event.
		private InternalTransaction originatingTransaction = null;
		private DiagramEventBroker deb = null;
		private ResourceSetListener debWrapper = null;
		
		public void addResourceSetListener(ResourceSetListener l) {
			if (l.getClass() == DiagramEventBroker.class) {
				assert deb == null;
				deb = (DiagramEventBroker)l;
				debWrapper = new ResourceSetListenerImpl() {
					public boolean isAggregatePrecommitListener() {
						return deb.isAggregatePrecommitListener();
					}
					
					public boolean isPrecommitOnly() {
						return true;
					}
					
					public Command transactionAboutToCommit(ResourceSetChangeEvent event)
						throws RollbackException {
						return deb.transactionAboutToCommit(event);
					}
					
					public void resourceSetChanged(ResourceSetChangeEvent event) {
						deb.resourceSetChanged(event);
					}
					
					public NotificationFilter getFilter() {
						return deb.getFilter();
					}
					
					public boolean isPostcommitOnly() {
						return false;
					}
				};
				
				super.addResourceSetListener(debWrapper);
			} else {
				super.addResourceSetListener(l);
			}
		}
		
		public void removeResourceSetListener(ResourceSetListener l) {
			if (l.getClass() == DiagramEventBroker.class) {
				assert deb != null;
				deb = null;
				super.removeResourceSetListener(debWrapper);
				debWrapper = null;
			} else {
				super.removeResourceSetListener(l);
			}
		}
		
		public DiagramEditingDomain(AdapterFactory adapterFactory, ResourceSet resourceSet) {
			super(adapterFactory, resourceSet);
		}

		public DiagramEditingDomain(AdapterFactory adapterFactory, TransactionalCommandStack stack, ResourceSet resourceSet) {
			super(adapterFactory, stack, resourceSet);
		}

		public DiagramEditingDomain(AdapterFactory adapterFactory, TransactionalCommandStack stack) {
			super(adapterFactory, stack);
		}

		public DiagramEditingDomain(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}
		
		public void precommit(InternalTransaction tx) throws RollbackException {
			super.precommit(tx);
			
			if ((tx.getParent() == null) && (deb != null)) {
				// ensure that when the top-level transaction commits, it
				//    has a self-chaining composite command as a trigger to
				//    insert the DiagramEventBroker's post-commit changes into,
				//    so that the transaction's change description and any other
				//    AbstractEMFOperation will get the changes automatically
				Command existingTriggers = tx.getTriggers();
				if (existingTriggers instanceof CompoundCommand) {
					// nothing to do:  already a self-chaining command
				} else if (existingTriggers != null) {
					// force it to be a compound by appending a no-op
					tx.addTriggers(NOOP_TRIGGER);
				} else {
					// no triggers, yet?  have to add *two* no-ops
					tx.addTriggers(NOOP_TRIGGER);
					tx.addTriggers(NOOP_TRIGGER);
				}
			}
		}
		
		protected void postcommit(InternalTransaction tx) {
			try {
				List notifications = getValidator().getNotificationsForPostcommit(tx);
				
				if (deb != null && notifications != null && !notifications.isEmpty()) {	
					TransactionValidator originalValidator = null;
					
					// Set the latch if it has not yet been set
					if (originatingTransaction == null) {
						originatingTransaction = tx;
						originalValidator = getValidator();
						setValidator(new ReadWriteValidatorImpl());
					} else {
						// In this case we must copy over the notifications and change
						//  descriptions to the originatingTransaction.  Do this
						//  as a "late trigger command" because the trigger
						//  mechanism is already understood by some of the
						//  operations that need to undo/redo these changes
						originatingTransaction.addTriggers(new TriggerCommand(
								Collections.singletonList(
										new DiagramEventBrokerCommand(
												tx.getChangeDescription()))));
						originatingTransaction.getNotifications().addAll(notifications);
					}
					
					try {
						ArrayList cache = new ArrayList(notifications.size());
						
						List filtered = FilterManager.getInstance().select(
							notifications,
							deb.getFilter(),
							cache);
						
						HashMap options = new HashMap(originatingTransaction.getOptions());
						options.put(Transaction.OPTION_NO_UNDO, Boolean.FALSE);
						InternalTransaction newTx = startTransaction(false, options);
						deb.resourceSetChanged(
							new ResourceSetChangeEvent(
								this,
								tx,
								filtered));

						newTx.commit();
					} catch (RollbackException e) {
						// Do nothing in the rollback case, we have no change descriptions
						//  or notifications to propagate.
					} finally {
						// Undo the latch if we are top-most in the recursion.
						if (originatingTransaction == tx) {
							originatingTransaction = null;
							getValidator().dispose();
							setValidator(originalValidator);
						}
					}
				}
			} catch (InterruptedException e) {
				// Simply fall-through in this case and allow the post commit listeners
				//  to be notified.
			}
			
			// We will only call super on the top-most in the recursion.
			if (originatingTransaction == null) {
				super.postcommit(tx);
			}
		}
	}
	
	private static class DiagramEventBrokerCommand
			extends AbstractCommand
			implements ConditionalRedoCommand {
		private final TransactionChangeDescription change;
		
		DiagramEventBrokerCommand(TransactionChangeDescription change) {
			this.change = change;
		}
		
		protected boolean prepare() {
			return true;
		}
		
		public final void execute() {
			// never executed
		}

		public boolean canUndo() {
			return (change != null) && change.canApply();
		}
		
		public final void undo() {
			if (change != null) {
				change.applyAndReverse();
			}
		}
		
		public boolean canRedo() {
			return (change != null) && change.canApply();
		}
		
		public final void redo() {
			if (change != null) {
				change.applyAndReverse();
			}
		}
	}
	
	static final TriggerCommand NOOP_TRIGGER = new TriggerCommand(
			Collections.singletonList(new AbstractCommand() {
				protected boolean prepare() { return true; }
				public void execute() {}
				public boolean canUndo() { return true;	}
				// this command does not need to implement canRedo() because it
				//    is assumed to be redoable, anyway, which is what we want
				public void undo() {}
				public void redo() {}}));
	
    /**
     * The single shared instance of the GMF diagram editing domain factory.
     */
    private static DiagramEditingDomainFactory instance = new DiagramEditingDomainFactory();

    /**
     * Gets the single shared instance of the GMF diagram editing domain factory.
     * 
     * @return the editing domain factory
     */
    public static WorkspaceEditingDomainFactory getInstance() {
        return instance;
    }  
    
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.emf.core.GMFEditingDomainFactory#configure(org.eclipse.emf.transaction.TransactionalEditingDomain)
     */
    protected void configure(TransactionalEditingDomain domain) {
        super.configure(domain);
        domain.addResourceSetListener(new NotationSemProc());
    }

    public TransactionalEditingDomain createEditingDomain() {
		TransactionalEditingDomain result = createEditingDomain(OperationHistoryFactory.getOperationHistory());
		configure(result);
		return result;
    }
    
    public TransactionalEditingDomain createEditingDomain(IOperationHistory history) {
		WorkspaceCommandStackImpl stack = new WorkspaceCommandStackImpl(history);
		
		TransactionalEditingDomain result = new DiagramEditingDomain(
			new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
			stack);
		
		mapResourceSet(result);
		
		configure(result);
		return result;
    }
    
    public TransactionalEditingDomain createEditingDomain(ResourceSet rset) {
		TransactionalEditingDomain result = createEditingDomain(
			rset,
			OperationHistoryFactory.getOperationHistory());
		configure(result);
		return result;
    }
    
    public TransactionalEditingDomain createEditingDomain(ResourceSet rset, IOperationHistory history) {
		WorkspaceCommandStackImpl stack = new WorkspaceCommandStackImpl(history);
		
		TransactionalEditingDomain result = new DiagramEditingDomain(
			new ComposedAdapterFactory(
				ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
			stack,
			rset);
		
		mapResourceSet(result);
		configure(result);
		return result;
    }
}
