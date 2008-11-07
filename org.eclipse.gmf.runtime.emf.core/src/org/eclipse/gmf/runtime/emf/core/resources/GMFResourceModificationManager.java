/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.emf.core.resources;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.commands.operations.UndoContext;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.NotificationFilter;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomainEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomainListenerImpl;
import org.eclipse.emf.transaction.TransactionalEditingDomain.Lifecycle;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.IWorkspaceCommandStack;
import org.eclipse.emf.workspace.ResourceUndoContext;
import org.eclipse.gmf.runtime.emf.core.internal.l10n.EMFCoreMessages;
import org.eclipse.osgi.util.NLS;

/**
 * Manages the <code>isModified</code> state of resources in a given editing
 * domain as operations are executed, undone and redone on the operation
 * history.
 * <P>
 * This allows clients to use the <code>isModified</code> state of a resource to
 * determine whether or not the resource is dirty and can be saved.
 * 
 * @author ldamus
 */
public class GMFResourceModificationManager {

	/**
	 * Keeps track of the modification manager for each editing domain. Only one
	 * modification manager can ever be created for a given editing domain. Keys
	 * are WeakReferences because the modification manager has a reference back
	 * to its editing domain key.
	 */
	private static Map<TransactionalEditingDomain, WeakReference<GMFResourceModificationManager>> managerRegistry = new WeakHashMap<TransactionalEditingDomain, WeakReference<GMFResourceModificationManager>>();

	/**
	 * Creates a new resource modification manager for <code>domain</code>, if
	 * the <code>domain</code>'s command stack is integrated with an
	 * <code>IOperationHistory</code>. The <code>isModified</code> state of a
	 * resource in <code>domain</code> will be set to <code>false</code> when
	 * the last operation affecting that resource is undone on the history.
	 * 
	 * @param domain
	 *            the editing domain
	 * @return the resource modification manager, or <code>null</code> if
	 *         <code>domain</code> is not integrated with an operation history
	 */
	public static synchronized GMFResourceModificationManager manage(
			TransactionalEditingDomain domain) {

		// make sure we only instantiate one manager per editing domain
		WeakReference<GMFResourceModificationManager> reference = managerRegistry
				.get(domain);
		GMFResourceModificationManager result = reference != null ? reference
				.get() : null;

		if (result == null) {
			CommandStack stack = domain.getCommandStack();

			if (stack instanceof IWorkspaceCommandStack) {
				IOperationHistory history = ((IWorkspaceCommandStack) stack)
						.getOperationHistory();

				if (history != null) {
					final GMFResourceModificationManager manager = new GMFResourceModificationManager(
							domain, history);
					managerRegistry.put(domain,
							new WeakReference<GMFResourceModificationManager>(
									manager));
					result = manager;

					// dispose the modification manager when the domain is
					// disposed
					Lifecycle lifecycle = TransactionUtil.getAdapter(domain,
							Lifecycle.class);

					if (lifecycle != null) {
						lifecycle
								.addTransactionalEditingDomainListener(new TransactionalEditingDomainListenerImpl() {
									@Override
									public void editingDomainDisposing(
											TransactionalEditingDomainEvent event) {
										manager.dispose();
									}
								});
					}
				}
			}
		}
		return result;
	}

	/**
	 * A filter matching "resource is no longer modified" events.
	 */
	private static final NotificationFilter RESOURCE_UNMODIFIED = new NotificationFilter.Custom() {

		public boolean matches(Notification notification) {
			return (notification.getNotifier() instanceof Resource)
					&& (notification.getFeatureID(Resource.class) == Resource.RESOURCE__IS_MODIFIED)
					&& notification.getOldBooleanValue()
					&& !notification.getNewBooleanValue();
		}
	};

	private TransactionalEditingDomain domain;
	private IOperationHistory history;
	private ResourceSetListener domainListener;
	private IOperationHistoryListener historyListener;
	private Map<Resource, IUndoContext> saveContexts;
	private IUndoableOperation currentOperation;

	/**
	 * Private constructor to prevent instantiation by clients. Clients must use
	 * {@link #manage(TransactionalEditingDomain)} to construct a new instance.
	 * 
	 * @param domain
	 *            the editing domain
	 * @param history
	 *            the operation history
	 */
	private GMFResourceModificationManager(TransactionalEditingDomain domain,
			IOperationHistory history) {

		this.domain = domain;
		this.history = history;

		domain.addResourceSetListener(getDomainListener());
		history.addOperationHistoryListener(getHistoryListener());
	}

	/**
	 * Gets the resource set listener listener, which manages the save-point
	 * context for operations executed on the history when the resource is saved
	 * or unloaded.
	 * 
	 * @return the resource set listener
	 */
	private ResourceSetListener getDomainListener() {

		if (domainListener == null) {
			domainListener = new ResourceSetListenerImpl(RESOURCE_UNMODIFIED
					.or(NotificationFilter.RESOURCE_UNLOADED)) {

				@Override
				public void resourceSetChanged(ResourceSetChangeEvent event) {

					for (Notification n : event.getNotifications()) {
						Resource resource = (Resource) n.getNotifier();

						switch (n.getFeatureID(Resource.class)) {

						case Resource.RESOURCE__IS_MODIFIED:
							applySaveContext(resource);
							break;

						case Resource.RESOURCE__IS_LOADED:
							disposeSaveContext(resource);
							break;
						}
					}
				}

				@Override
				public boolean isPostcommitOnly() {
					return true;
				}
			};
		}
		return domainListener;
	}

	/**
	 * Gets the operation history listener, which manages the
	 * <code>isModified</code> state of the resources.
	 * 
	 * @return the operation history listener
	 */
	private IOperationHistoryListener getHistoryListener() {

		if (historyListener == null) {
			historyListener = new IOperationHistoryListener() {

				public void historyNotification(OperationHistoryEvent event) {
					int type = event.getEventType();

					switch (type) {

					case OperationHistoryEvent.ABOUT_TO_EXECUTE:
					case OperationHistoryEvent.ABOUT_TO_UNDO:
					case OperationHistoryEvent.ABOUT_TO_REDO:
						// Remember the operation in order to apply the
						// save context to it if the isModified is set to false
						// during execute, undo or redo. For undo, the save
						// context goes on next undoable operation on the
						// history.
						currentOperation = event.getOperation();
						break;

					case OperationHistoryEvent.OPERATION_NOT_OK:
						currentOperation = null;
						break;

					case OperationHistoryEvent.DONE: {
						currentOperation = null;

						IUndoableOperation operation = event.getOperation();
						Set<Resource> affectedResources = getAffectedResourcesInDomain(operation);

						for (Resource r : affectedResources) {
							ResourceUndoContext context = new ResourceUndoContext(
									domain, r);
							IUndoableOperation[] undoHistory = history
									.getUndoHistory(context);

							if (undoHistory.length >= history.getLimit(context)) {
								// We've reached the limit for this context;
								// initialize the save context to indicate that
								// we can't undo to the last saved state
								getSaveContext(r);
							}
						}
						break;
					}

					case OperationHistoryEvent.UNDONE:
					case OperationHistoryEvent.REDONE: {
						currentOperation = null;

						IUndoableOperation operation = event.getOperation();
						Set<Resource> affectedResources = getAffectedResourcesInDomain(operation);

						for (Resource r : affectedResources) {
							IUndoContext saveContext = getSaveContexts().get(r);
							IUndoableOperation nextUndoableOperation = getNextUndoableOperation(r);

							boolean atStart = saveContext == null
									&& nextUndoableOperation == null;
							
							boolean atSaveContext = saveContext != null
									&& nextUndoableOperation != null
									&& nextUndoableOperation
											.hasContext(saveContext);

							if (atStart || atSaveContext) {
								r.setModified(false);
							}
						}
					}
					}
				}
			};
		}
		return historyListener;
	}

	private Map<Resource, IUndoContext> getSaveContexts() {
		if (saveContexts == null) {
			saveContexts = new HashMap<Resource, IUndoContext>();
		}
		return saveContexts;
	}

	private IUndoableOperation getNextUndoableOperation(Resource resource) {
		return history.getUndoOperation(new ResourceUndoContext(domain,
				resource));
	}

	private IUndoContext getSaveContext(final Resource resource) {
		IUndoContext saveContext = getSaveContexts().get(resource);

		if (saveContext == null) {
			saveContext = new UndoContext() {
				@Override
				public String getLabel() {
					return NLS.bind(EMFCoreMessages.saveContextLabel, resource
							.getURI());
				}

				@Override
				public String toString() {
					return getLabel();
				}
			};

			getSaveContexts().put(resource, saveContext);
		}
		return saveContext;
	}

	private Set<Resource> getAffectedResourcesInDomain(
			IUndoableOperation operation) {

		Set<Resource> result = new HashSet<Resource>();
		Set<Resource> affectedResources = ResourceUndoContext
				.getAffectedResources(operation);

		for (Resource resource : affectedResources) {
			ResourceSet resourceSet = resource.getResourceSet();

			if (domain.getResourceSet().equals(resourceSet)) {
				result.add(resource);
			}
		}
		return result;
	}

	private void applySaveContext(Resource resource) {
		IUndoContext saveContext = getSaveContexts().get(resource);

		if (saveContext != null) {
			// Remove the save context from existing operations
			IUndoableOperation[] undoableOperations = history
					.getUndoHistory(saveContext);
			for (IUndoableOperation op : undoableOperations) {
				op.removeContext(saveContext);
			}

			IUndoableOperation[] redoableOperations = history
					.getRedoHistory(saveContext);
			for (IUndoableOperation op : redoableOperations) {
				op.removeContext(saveContext);
			}
		}

		IUndoableOperation operation = null;
		IUndoableOperation nextUndoable = getNextUndoableOperation(resource);

		if (currentOperation != null) {

			if (currentOperation == nextUndoable) {
				// we're undoing; get the previous operation on the history
				IUndoableOperation[] undoableOperations = history
						.getUndoHistory(new ResourceUndoContext(domain,
								resource));

				for (int i = undoableOperations.length - 1; i >= 0; i--) {
					if (currentOperation != undoableOperations[i]) {
						operation = undoableOperations[i];
						break;
					}
				}
			} else {
				operation = currentOperation;
			}
		} else {
			operation = nextUndoable;
		}

		if (operation != null) {
			// apply the save context
			operation.addContext(getSaveContext(resource));

		} else {
			// clear the save context; required if we save after undoing the
			// last thing on the stack
			getSaveContexts().remove(resource);
		}
	}

	private void disposeSaveContext(Resource resource) {
		IUndoContext saveContext = getSaveContexts().get(resource);

		if (saveContext != null) {
			history.dispose(saveContext, true, true, true);
			getSaveContexts().remove(resource);
		}
	}

	private void dispose() {

		managerRegistry.remove(domain);

		if (saveContexts != null) {
			for (Resource r : saveContexts.keySet()) {
				disposeSaveContext(r);
			}
		}
		if (domainListener != null) {
			domain.removeResourceSetListener(domainListener);
		}
		if (historyListener != null) {
			history.removeOperationHistoryListener(historyListener);
		}

		currentOperation = null;
		domain = null;
		domainListener = null;
		history = null;
		historyListener = null;
		saveContexts = null;
	}

}
