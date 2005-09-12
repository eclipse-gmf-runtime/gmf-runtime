/******************************************************************************
 * Copyright (c) 2000, 2005  IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document;

import java.io.ByteArrayInputStream;

import org.eclipse.core.filebuffers.manipulation.ContainerCreator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceRuleFactory;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.l10n.EditorMessages;


/**
 * Shared document provider specialized for file resources (<code>IFile</code>).
 * <p>
 * This class should be subclassed for different types of documents.</p>
 */
public abstract class FileDocumentProvider
	extends StorageDocumentProvider {

	/**
	 * The runnable context for that provider.
	 */
	private WorkspaceOperationRunner fOperationRunner;
	/**
	 * The scheduling rule factory.
	 */
	protected IResourceRuleFactory fResourceRuleFactory;

	/**
	 * Runnable encapsulating an element state change. This runnable ensures
	 * that a element change failed message is sent out to the element state listeners
	 * in case an exception occurred.
	 *
	 * @since 2.0
	 */
	protected class SafeChange implements Runnable {

		/** The input that changes. */
		private IFileEditorInput fInput;

		/**
		 * Creates a new safe runnable for the given input.
		 *
		 * @param input the input
		 */
		public SafeChange(IFileEditorInput input) {
			fInput= input;
		}

		/**
		 * Execute the change.
		 * Subclass responsibility.
		 *
		 * @param input the input
		 * @throws Exception an exception in case of error
		 */
		protected void execute(IFileEditorInput input) throws Exception {
			// overriden
		}

		/*
		 * @see java.lang.Runnable#run()
		 */
		public void run() {

			if (getElementInfo(fInput) == null) {
				fireElementStateChangeFailed(fInput);
				return;
			}

			try {
				execute(fInput);
			} catch (Exception e) {
				fireElementStateChangeFailed(fInput);
			}
		}
	}


	/**
	 * Synchronizes the document with external resource changes.
	 */
	protected class FileSynchronizer implements IResourceChangeListener, IResourceDeltaVisitor {

		/** The file editor input. */
		protected IFileEditorInput fFileEditorInput;
		/**
		 * A flag indicating whether this synchronizer is installed or not.
		 *
		 * @since 2.1
		 */
		protected boolean fIsInstalled= false;

		/**
		 * Creates a new file synchronizer. Is not yet installed on a resource.
		 *
		 * @param fileEditorInput the editor input to be synchronized
		 */
		public FileSynchronizer(IFileEditorInput fileEditorInput) {
			fFileEditorInput= fileEditorInput;
		}

		/**
		 * Returns the file wrapped by the file editor input.
		 *
		 * @return the file wrapped by the editor input associated with that synchronizer
		 */
		protected IFile getFile() {
			return fFileEditorInput.getFile();
		}

		/**
		 * Installs the synchronizer on the input's file.
		 */
		public void install() {
			getFile().getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
			fIsInstalled= true;
		}

		/**
		 * Uninstalls the synchronizer from the input's file.
		 */
		public void uninstall() {
			getFile().getWorkspace().removeResourceChangeListener(this);
			fIsInstalled= false;
		}

		/*
		 * @see IResourceChangeListener#resourceChanged(IResourceChangeEvent)
		 */
		public void resourceChanged(IResourceChangeEvent e) {
			IResourceDelta delta= e.getDelta();
			try {
				if (delta != null && fIsInstalled)
					delta.accept(this);
			} catch (CoreException x) {
				handleCoreException(x, EditorMessages.FileDocumentProvider_resourceChanged);
			}
		}

		/*
		 * @see IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			if (delta == null)
				return false;

			delta= delta.findMember(getFile().getFullPath());

			if (delta == null)
				return false;

			Runnable runnable= null;

			switch (delta.getKind()) {
				case IResourceDelta.CHANGED:
					FileInfo info= (FileInfo) getElementInfo(fFileEditorInput);
					if (info == null || info.fCanBeSaved)
						break;

					boolean isSynchronized= computeModificationStamp(getFile()) == info.fModificationStamp;
					if ((IResourceDelta.ENCODING & delta.getFlags()) != 0 && isSynchronized) {
						runnable= new SafeChange(fFileEditorInput) {
							protected void execute(IFileEditorInput input) throws Exception {
								handleElementContentChanged(input);
							}
						};
					}

					if (runnable != null && (IResourceDelta.CONTENT & delta.getFlags()) != 0 && !isSynchronized) {
						runnable= new SafeChange(fFileEditorInput) {
							protected void execute(IFileEditorInput input) throws Exception {
								handleElementContentChanged(input);
							}
						};
					}
					break;

				case IResourceDelta.REMOVED:
					if ((IResourceDelta.MOVED_TO & delta.getFlags()) != 0) {
						final IPath path= delta.getMovedToPath();
						runnable= new SafeChange(fFileEditorInput) {
							protected void execute(IFileEditorInput input) throws Exception {
								handleElementMoved(input, path);
							}
						};
					} else {
						info= (FileInfo) getElementInfo(fFileEditorInput);
						if (info != null && !info.fCanBeSaved) {
							runnable= new SafeChange(fFileEditorInput) {
								protected void execute(IFileEditorInput input) throws Exception {
									handleElementDeleted(input);
								}
							};
						}
					}
					break;
			}

			if (runnable != null)
				update(runnable);

			return false;
		}

		/**
		 * Posts the update code "behind" the running operation.
		 *
		 * @param runnable the update code
		 */
		protected void update(Runnable runnable) {

			if (runnable instanceof SafeChange)
				fireElementStateChanging(fFileEditorInput);

			IWorkbench workbench= PlatformUI.getWorkbench();
			IWorkbenchWindow[] windows= workbench.getWorkbenchWindows();
			if (windows != null && windows.length > 0) {
				Display display= windows[0].getShell().getDisplay();
				display.asyncExec(runnable);
			} else {
				runnable.run();
			}
		}
	}



	/**
	 * Bundle of all required information to allow files as underlying document resources.
	 */
	protected class FileInfo extends StorageInfo {

		/** The file synchronizer. */
		public FileSynchronizer fFileSynchronizer;
		/** The time stamp at which this provider changed the file. */
		public long fModificationStamp= IResource.NULL_STAMP;

		/**
		 * Creates and returns a new file info.
		 *
		 * @param document the document
		 * @param model the annotation model
		 * @param fileSynchronizer the file synchronizer
		 */
		public FileInfo(IDocument document, FileSynchronizer fileSynchronizer) {
			super(document);
			fFileSynchronizer= fileSynchronizer;
		}
	}


	/**
	 * Creates and returns a new document provider.
	 */
	public FileDocumentProvider() {
		super();
		fResourceRuleFactory= ResourcesPlugin.getWorkspace().getRuleFactory();
	}

	/**
	 * Checks whether the given resource has been changed on the
	 * local file system by comparing the actual time stamp with the
	 * cached one. If the resource has been changed, a <code>CoreException</code>
	 * is thrown.
	 *
	 * @param cachedModificationStamp the cached modification stamp
	 * @param resource the resource to check
	 * @throws org.eclipse.core.runtime.CoreException if resource has been changed on the file system
	 */
	protected void checkSynchronizationState(long cachedModificationStamp, IResource resource) throws CoreException {
		if (cachedModificationStamp != computeModificationStamp(resource)) {
			Status status= new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IResourceStatus.OUT_OF_SYNC_LOCAL, EditorMessages.FileDocumentProvider_error_out_of_sync, null);
			throw new CoreException(status);
		}
	}

	/**
	 * Computes the initial modification stamp for the given resource.
	 *
	 * @param resource the resource
	 * @return the modification stamp
	 */
	protected long computeModificationStamp(IResource resource) {
		long modificationStamp= resource.getModificationStamp();

		IPath path= resource.getLocation();
		if (path == null)
			return modificationStamp;

		modificationStamp= path.toFile().lastModified();
		return modificationStamp;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocumentProvider#getModificationStamp(java.lang.Object)
	 */
	public long getModificationStamp(Object element) {

		if (element instanceof IFileEditorInput) {
			IFileEditorInput input= (IFileEditorInput) element;
			return computeModificationStamp(input.getFile());
		}

		return super.getModificationStamp(element);
	}

	/*
	 * @see IDocumentProvider#getSynchronizationStamp(Object)
	 */
	public long getSynchronizationStamp(Object element) {

		if (element instanceof IFileEditorInput) {
			FileInfo info= (FileInfo) getElementInfo(element);
			if (info != null)
				return info.fModificationStamp;
		}

		return super.getSynchronizationStamp(element);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#doSynchronize(java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doSynchronize(Object element, IProgressMonitor monitor)  throws CoreException {
		if (element instanceof IFileEditorInput) {

			IFileEditorInput input= (IFileEditorInput) element;

			FileInfo info= (FileInfo) getElementInfo(element);
			if (info != null) {

				if (info.fFileSynchronizer != null) {
					info.fFileSynchronizer.uninstall();
					refreshFile(input.getFile(), monitor);
					info.fFileSynchronizer.install();
				} else {
					refreshFile(input.getFile(), monitor);
				}

				handleElementContentChanged((IFileEditorInput) element);
			}
			return;

		}
		super.doSynchronize(element, monitor);
	}

	/*
	 * @see IDocumentProvider#isDeleted(Object)
	 */
	public boolean isDeleted(Object element) {

		if (element instanceof IFileEditorInput) {
			IFileEditorInput input= (IFileEditorInput) element;

			IPath path= input.getFile().getLocation();
			if (path == null)
				return true;

			return !path.toFile().exists();
		}

		return super.isDeleted(element);
	}

	/**
	 * Initializes the given document with the given stream using the given encoding.
	 *
	 * @param document the document to be initialized
	 * @param contentStream the stream which delivers the document content
	 * @param encoding the character encoding for reading the given stream
	 * @throws CoreException if the given stream can not be read
	 * @since 2.0
	 */
	protected abstract void saveDocumentToFile(IDocument document, IFile file, boolean overwrite, IProgressMonitor monitor) throws CoreException;

	/*
	 * @see AbstractDocumentProvider#doSaveDocument(IProgressMonitor, Object, IDocument, boolean)
	 */
	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite) throws CoreException {
		if (element instanceof IFileEditorInput) {

			IFileEditorInput input= (IFileEditorInput) element;
			FileInfo info= (FileInfo) getElementInfo(element);
			IFile file= input.getFile();

			if (file.exists()) {

				if (info != null && !overwrite)
					checkSynchronizationState(info.fModificationStamp, file);

				// inform about the upcoming content change
				fireElementStateChanging(element);
				try {
					saveDocumentToFile(document, file, overwrite, monitor);
				} catch (CoreException x) {
					// inform about failure
					fireElementStateChangeFailed(element);
					throw x;
				} catch (RuntimeException x) {
					// inform about failure
					fireElementStateChangeFailed(element);
					throw x;
				}

				// If here, the editor state will be flipped to "not dirty".
				// Thus, the state changing flag will be reset.

				if (info != null) {
					info.fModificationStamp= computeModificationStamp(file);
				}

			} else {
				try {
					monitor.beginTask(EditorMessages.FileDocumentProvider_task_saving, 3000);
					ContainerCreator creator = new ContainerCreator(file.getWorkspace(), file.getParent().getFullPath());
					creator.createContainer(new SubProgressMonitor(monitor, 1000));
					file.create(new ByteArrayInputStream("".getBytes()), false, new SubProgressMonitor(monitor, 1000)); //$NON-NLS-1$
					saveDocumentToFile(document, file, overwrite, new SubProgressMonitor(monitor, 1000));
				}
				finally {
					monitor.done();
				}
			}
		} else {
			super.doSaveDocument(monitor, element, document, overwrite);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#createElementInfo(java.lang.Object)
	 */
	protected ElementInfo createElementInfo(Object element) throws CoreException {
		if (element instanceof IFileEditorInput) {

			IFileEditorInput input= (IFileEditorInput) element;

			try {
				refreshFile(input.getFile());
			} catch (CoreException x) {
				handleCoreException(x, EditorMessages.FileDocumentProvider_createElementInfo);
			}

			IDocument d= null;
			IStatus s= null;

			try {
				d= createDocument(element);
			} catch (CoreException x) {
				handleCoreException(x, EditorMessages.FileDocumentProvider_createElementInfo);
				s= x.getStatus();
				d= createEmptyDocument();
			}
			
			FileSynchronizer f= new FileSynchronizer(input);
			f.install();

			FileInfo info= createFileInfo(d, f);
			info.fModificationStamp= computeModificationStamp(input.getFile());
			info.fStatus= s;

			return info;
		}

		return super.createElementInfo(element);
	}
	
	protected FileInfo createFileInfo(IDocument document, FileSynchronizer synchronizer) {
		return new FileInfo(document, synchronizer);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#disposeElementInfo(java.lang.Object, org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider.ElementInfo)
	 */
	protected void disposeElementInfo(Object element, ElementInfo info) {
		if (info instanceof FileInfo) {
			FileInfo fileInfo= (FileInfo) info;
			if (fileInfo.fFileSynchronizer != null)
				fileInfo.fFileSynchronizer.uninstall();
		}

		super.disposeElementInfo(element, info);
	}

	/**
	 * Updates the element info to a change of the file content and sends out
	 * appropriate notifications.
	 *
	 * @param fileEditorInput the input of a document editor
	 */
	protected void handleElementContentChanged(IFileEditorInput fileEditorInput) {
		FileInfo info= (FileInfo) getElementInfo(fileEditorInput);
		if (info == null)
			return;

		IDocument document= createEmptyDocument();
		IStatus status= null;

		try {

			try {
				refreshFile(fileEditorInput.getFile());
			} catch (CoreException x) {
				handleCoreException(x, EditorMessages.FileDocumentProvider_handleElementContentChanged);
			}

			setDocumentContent(document, fileEditorInput);

		} catch (CoreException x) {
			status= x.getStatus();
		}

		Object newContent= document.getContent();

		if ( !newContent.equals(info.fDocument.getContent())) {

			// set the new content and fire content related events
			fireElementContentAboutToBeReplaced(fileEditorInput);

			removeUnchangedElementListeners(fileEditorInput, info);

			info.fDocument.removeDocumentListener(info);
			info.fDocument.setContent(newContent);
			info.fCanBeSaved= false;
			info.fModificationStamp= computeModificationStamp(fileEditorInput.getFile());
			info.fStatus= status;

			addUnchangedElementListeners(fileEditorInput, info);

			fireElementContentReplaced(fileEditorInput);

		} else {

			removeUnchangedElementListeners(fileEditorInput, info);

			// fires only the dirty state related event
			info.fCanBeSaved= false;
			info.fModificationStamp= computeModificationStamp(fileEditorInput.getFile());
			info.fStatus= status;

			addUnchangedElementListeners(fileEditorInput, info);

			fireElementDirtyStateChanged(fileEditorInput, false);
		}
	}

	/**
	 * Initializes the given document with the given stream using the given encoding.
	 *
	 * @param document the document to be initialized
	 * @param contentStream the stream which delivers the document content
	 * @param encoding the character encoding for reading the given stream
	 * @throws CoreException if the given stream can not be read
	 * @since 2.0
	 */
	protected void setDocumentContent(IDocument document, Object content) throws CoreException {
		document.setContent(content);
	}
	/**
	 * Sends out the notification that the file serving as document input has been moved.
	 *
	 * @param fileEditorInput the input of an document editor
	 * @param path the path of the new location of the file
	 */
	protected void handleElementMoved(IFileEditorInput fileEditorInput, IPath path) {
		IWorkspace workspace= ResourcesPlugin.getWorkspace();
		IFile newFile= workspace.getRoot().getFile(path);
		fireElementMoved(fileEditorInput, newFile == null ? null : new FileEditorInput(newFile));
	}

	/**
	 * Sends out the notification that the file serving as document input has been deleted.
	 *
	 * @param fileEditorInput the input of an document editor
	 */
	protected void handleElementDeleted(IFileEditorInput fileEditorInput) {
		fireElementDeleted(fileEditorInput);
	}

	/*
	 * @see AbstractDocumentProvider#getElementInfo(Object)
	 * It's only here to circumvent visibility issues with certain compilers.
	 */
	protected ElementInfo getElementInfo(Object element) {
		return super.getElementInfo(element);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#doValidateState(java.lang.Object, java.lang.Object)
	 */
	protected void doValidateState(Object element, Object computationContext) throws CoreException {
		if (element instanceof IFileEditorInput) {
			IFileEditorInput input= (IFileEditorInput) element;
			FileInfo info= (FileInfo) getElementInfo(input);
			if (info != null) {
				IFile file= input.getFile();
				if (file.isReadOnly()) { // do not use cached state here
					IWorkspace workspace= file.getWorkspace();
					workspace.validateEdit(new IFile[] { file }, computationContext);
				}
			}
		}

		super.doValidateState(element, computationContext);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocumentProvider#isModifiable(java.lang.Object)
	 */
	public boolean isModifiable(Object element) {
		if (!isStateValidated(element)) {
			if (element instanceof IFileEditorInput)
				return true;
		}
		return super.isModifiable(element);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#doResetDocument(java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void doResetDocument(Object element, IProgressMonitor monitor) throws CoreException {
		if (element instanceof IFileEditorInput) {
			IFileEditorInput input= (IFileEditorInput) element;
			try {
				refreshFile(input.getFile(), monitor);
			} catch (CoreException x) {
				handleCoreException(x,EditorMessages.FileDocumentProvider_resetDocument);
			}
		}

		super.doResetDocument(element, monitor);
	}

	/**
	 * Refreshes the given file resource.
	 *
	 * @param file
	 * @throws CoreException if the refresh fails
	 * @since 2.1
	 */
	protected void refreshFile(IFile file) throws CoreException {
		refreshFile(file, getProgressMonitor());
	}

	/**
	 * Refreshes the given file resource.
	 *
	 * @param file the file to be refreshed
	 * @param monitor the progress monitor
	 * @throws  org.eclipse.core.runtime.CoreException if the refresh fails
	 * @since 3.0
	 */
	protected void refreshFile(IFile file, IProgressMonitor monitor) throws CoreException {
		try {
			file.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (OperationCanceledException x) {
			// ignore
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDocumentProvider#isSynchronized(java.lang.Object)
	 */
	public boolean isSynchronized(Object element) {
		if (element instanceof IFileEditorInput) {
			FileInfo info = null;
			if ((info = (FileInfo)getElementInfo(element)) != null) {
				IFileEditorInput input= (IFileEditorInput) element;
				IResource resource= input.getFile();
				return (info.fModificationStamp == computeModificationStamp(resource)) && resource.isSynchronized(IResource.DEPTH_ZERO);
			}
			return false;
		}
		return super.isSynchronized(element);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#getOperationRunner(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IRunnableContext getOperationRunner(IProgressMonitor monitor) {
		if (fOperationRunner == null)
			fOperationRunner = new WorkspaceOperationRunner();
		fOperationRunner.setProgressMonitor(monitor);
		return fOperationRunner;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#getResetRule(java.lang.Object)
	 */
	protected ISchedulingRule getResetRule(Object element) {
		if (element instanceof IFileEditorInput) {
			IFileEditorInput input= (IFileEditorInput) element;
			return fResourceRuleFactory.modifyRule(input.getFile());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#getSaveRule(java.lang.Object)
	 */
	protected ISchedulingRule getSaveRule(Object element) {
		if (element instanceof IFileEditorInput) {
			IFileEditorInput input= (IFileEditorInput) element;
			return computeSchedulingRule(input.getFile());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#getSynchronizeRule(java.lang.Object)
	 */
	protected ISchedulingRule getSynchronizeRule(Object element) {
		if (element instanceof IFileEditorInput) {
			IFileEditorInput input= (IFileEditorInput) element;
			return fResourceRuleFactory.refreshRule(input.getFile());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#getValidateStateRule(java.lang.Object)
	 */
	protected ISchedulingRule getValidateStateRule(Object element) {
		if (element instanceof IFileEditorInput) {
			IFileEditorInput input= (IFileEditorInput) element;
			return fResourceRuleFactory.validateEditRule(new IResource[] { input.getFile() });
		}
		return null;
	}

	/**
	 * Computes the scheduling rule needed to create or modify a resource. If
	 * the resource exists, its modify rule is returned. If it does not, the
	 * resource hierarchy is iterated towards the workspace root to find the
	 * first parent of <code>toCreateOrModify</code> that exists. Then the
	 * 'create' rule for the last non-existing resource is returned.
	 *
	 * @param toCreateOrModify the resource to create or modify
	 * @return the minimal scheduling rule needed to modify or create a resource
	 */
	private ISchedulingRule computeSchedulingRule(IResource toCreateOrModify) {
		if (toCreateOrModify.exists())
			return fResourceRuleFactory.modifyRule(toCreateOrModify);

		IResource parent= toCreateOrModify;
		do {
			 /*
			 * XXX This is a workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=67601
			 * IResourceRuleFactory.createRule should iterate the hierarchy itself.
			 */
			toCreateOrModify= parent;
			parent= toCreateOrModify.getParent();
		} while (parent != null && !parent.exists());

		return fResourceRuleFactory.createRule(toCreateOrModify);
	}
}
