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
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.FilterManager;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.ReadWriteValidatorImpl;
import org.eclipse.emf.transaction.impl.TransactionValidator;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.emf.transaction.util.CompositeChangeDescription;
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
		
		public void addResourceSetListener(ResourceSetListener l) {
			if (l.getClass() == DiagramEventBroker.class) {
				assert deb == null;
				deb = (DiagramEventBroker)l;
			}
			
			super.addResourceSetListener(l);
		}
		
		public void removeResourceSetListener(ResourceSetListener l) {
			if (l.getClass() == DiagramEventBroker.class) {
				assert deb != null;
				deb = null;
			}
			
			super.removeResourceSetListener(l);
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
						//  descriptions to the originatingTransaction.
						((CompositeChangeDescription)originatingTransaction.getChangeDescription()).add(tx.getChangeDescription());
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
