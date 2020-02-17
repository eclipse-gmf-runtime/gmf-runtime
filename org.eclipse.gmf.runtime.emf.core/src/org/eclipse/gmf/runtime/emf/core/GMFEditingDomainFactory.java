/******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.transaction.Transaction;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.emf.workspace.AbstractResourceUndoContextPolicy;
import org.eclipse.emf.workspace.IResourceUndoContextPolicy;
import org.eclipse.emf.workspace.WorkspaceEditingDomainFactory;
import org.eclipse.emf.workspace.util.WorkspaceValidateEditSupport;
import org.eclipse.gmf.runtime.common.core.command.FileModificationValidator;
import org.eclipse.gmf.runtime.common.core.command.FileModificationValidator.ISyncExecHelper;
import org.eclipse.gmf.runtime.common.core.command.FileModificationValidator.SyncExecHelper;
import org.eclipse.gmf.runtime.emf.core.internal.resources.PathmapManager;
import org.eclipse.gmf.runtime.emf.core.internal.util.EMFCoreConstants;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResource;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResourceModificationManager;
import org.eclipse.gmf.runtime.emf.core.util.CrossReferenceAdapter;


/**
 * Factory for {@link TransactionalEditingDomain}s that are properly configured
 * to support a GMF application.  This factory should be preferred over the
 * {@link org.eclipse.emf.workspace.WorkspaceEditingDomainFactory} because it
 * attaches adapters and sets up other other properties of the resource set
 * and/or editing domain on the client's behalf.
 *
 * @author Christian W. Damus (cdamus)
 */
public class GMFEditingDomainFactory extends WorkspaceEditingDomainFactory {

	static public TransactionalSyncExecHelper transactionalSyncExecHelper = new TransactionalSyncExecHelper();
	static {
		SyncExecHelper.setInstance(transactionalSyncExecHelper);
	}
			
    /**
     * The single shared instance of the GMF editing domain factory.
     */
    private static GMFEditingDomainFactory instance  = new GMFEditingDomainFactory();

    /**
     * Gets the single shared instance of the GMF editing domain factory.
     * 
     * @return the editing domain factory
     */
    public static WorkspaceEditingDomainFactory getInstance() {
        return instance;
    }
    
	public TransactionalEditingDomain createEditingDomain() {
		TransactionalEditingDomain result = super.createEditingDomain();
		configure(result);
		return result;
	}

	public TransactionalEditingDomain createEditingDomain(IOperationHistory history) {
		TransactionalEditingDomain result = super.createEditingDomain(history);
		configure(result);
		return result;
	}

	public TransactionalEditingDomain createEditingDomain(ResourceSet rset, IOperationHistory history) {
		TransactionalEditingDomain result = super.createEditingDomain(rset, history);
		configure(result);
		return result;
	}

	public TransactionalEditingDomain createEditingDomain(ResourceSet rset) {
		TransactionalEditingDomain result = super.createEditingDomain(rset);
		configure(result);
		return result;
	}

	/**
	 * Configures the specified editing domain for correct functioning in the
	 * GMF environment.
	 * 
	 * @param domain the new editing domain
	 */
	protected void configure(final TransactionalEditingDomain domain) {
		final ResourceSet rset = domain.getResourceSet();

		// ensure that the cross-referencing adapter is installed
		if (CrossReferenceAdapter.getExistingCrossReferenceAdapter(rset) == null) {
			rset.eAdapters().add(new CrossReferenceAdapter());
		}

		// ensure that the path map manager is installed
		if (PathmapManager.getExistingPathmapManager(rset) == null) {
			// Set up a delegating resource factory registry that ensures that
			//  the pathmap URI is normalized before finding a resource factory.
			final Registry existingRegistry = rset.getResourceFactoryRegistry();
			
			rset.setResourceFactoryRegistry(new Registry() {
				private Registry delegateRegistry = existingRegistry;

				public Map<String, Object> getContentTypeToFactoryMap() {
					return delegateRegistry.getContentTypeToFactoryMap();
				}

				public Map<String, Object> getExtensionToFactoryMap() {
					return delegateRegistry.getExtensionToFactoryMap();
				}

				public Factory getFactory(URI uri, String contentType) {
					if (uri != null && uri.scheme() != null && uri.scheme().equals(EMFCoreConstants.PATH_MAP_SCHEME)) {
						uri = rset.getURIConverter().normalize(uri);
					}
					return delegateRegistry.getFactory(uri, contentType);
				}

				public Factory getFactory(URI uri) {
					if (uri != null && uri.scheme() != null && uri.scheme().equals(EMFCoreConstants.PATH_MAP_SCHEME)) {
						uri = rset.getURIConverter().normalize(uri);
					}
					return delegateRegistry.getFactory(uri);
				}

				public Map<String, Object> getProtocolToFactoryMap() {
					return delegateRegistry.getProtocolToFactoryMap();
				}
			});
			
			rset.eAdapters().add(new PathmapManager());
		}
			
		TransactionalEditingDomain.DefaultOptions options = (TransactionalEditingDomain.DefaultOptions) (TransactionUtil
				.getAdapter(domain,
						TransactionalEditingDomain.DefaultOptions.class));

		Map<Object, Object> aMap = new HashMap<Object, Object>();
		aMap.put(Transaction.OPTION_VALIDATE_EDIT,
				new WorkspaceValidateEditSupport() {

					@SuppressWarnings("unchecked")
					protected IStatus doValidateEdit(Transaction transaction,
							Collection resources, Object context) {
						return GMFEditingDomainFactory.transactionalSyncExecHelper
								.approveFileModification(getFiles(resources),
										domain);
					}
				});

		options.setDefaultTransactionOptions(aMap);
		
		configureResourceModificationManagement(domain);

	}
	
	/**
	 * Configures <code>domain</code> so that the modified state
	 * of resources in the <code>domain</code> is managed as operations are
	 * executed, undone and redone on the operation history.
	 * 
	 * @param domain
	 *            the editing domain to be configured
	 * @since 1.2
	 */
	protected void configureResourceModificationManagement(
			TransactionalEditingDomain domain) {

		GMFResourceModificationManager.manage(domain);
	}
	
	@Override
	protected IResourceUndoContextPolicy getResourceUndoContextPolicy() {
		return new AbstractResourceUndoContextPolicy() {
			@Override
			protected boolean isAbstractChange(Notification notification) {
				return super.isAbstractChange(notification)
						&& GMFResource.isModifyingChange(notification);
			}
			
			@Override
			protected void resourceChange(Set<Resource> resources, Resource resource,
					Notification notification) {

				if ((notification.getFeatureID(Resource.class) == Resource.RESOURCE__IS_MODIFIED)) {
					// consider changes to isModified as affecting the resource
					resources.add(resource);
					
				} else {
					super.resourceChange(resources, resource, notification);
				}
			}
		};
	}
	
	/**
	 * A helper that knows about the specific editing domain.
	 * During the approval process, calls to validateEdit() will require the
	 * domain in order to execute in a thread safe manner.
	 * 
	 * @author James Bruck (jbruck)
	 *
	 */
	public static class TransactionalSyncExecHelper implements ISyncExecHelper {

		private final ThreadLocal<TransactionalEditingDomain> domain = new ThreadLocal<TransactionalEditingDomain>();

		private void setDomain(TransactionalEditingDomain domain) {
			this.domain.set(domain);
		}

		/**
		 * Sets the thread specific transactional domain before the approval
		 * process since subsequent calls to validateEdit() requires it and
		 * clears it afterward.
		 * 
		 * @param files
		 *            The files to be validated.
		 * 
		 * @param transactionalDomain
		 *            The current editing domain.
		 * 
		 * @return The resulting status.
		 */
		public IStatus approveFileModification(IFile[] files,
				TransactionalEditingDomain transactionalDomain) {

			setDomain(transactionalDomain);
			IStatus status = Status.OK_STATUS;
			try {
				status = FileModificationValidator
						.approveFileModification(files);
			} finally {
				setDomain(null);
			}
			return status;
		}
		
		/**
		 * Delegates to the specified domain to obtain a thread safe wrapper
		 * for the specified <code>runnable</code> 
		 * 
		 * @param runnable a runnable to execute in the context of the active
		 *     transaction, on any thread
		 *     
		 *  @return the privileged runnable if the transaction is on the current
		 *  	thread, otherwise just return itself.
		 */
		public Runnable safeRunnable(Runnable runnable) {
			if( isTransactionOnCurrentThread()) {
				return domain.get().createPrivilegedRunnable(runnable);
			}
			return null;
		}

		/**
		 * Checks if the active transaction is on the current thread.
		 * 
		 * @return true if the active transaction is on the current thread.
		 */
		private boolean isTransactionOnCurrentThread() {
			if (domain.get() != null) {
				
				Transaction tx = ((InternalTransactionalEditingDomain) domain
						.get()).getActiveTransaction();
				
				return ((tx != null) && (tx.getOwner() == Thread
						.currentThread()));
			}
			return false;
		}
	}
		
}
