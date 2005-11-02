/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.ui.parts;


import java.io.File;
import java.text.MessageFormat;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.common.core.command.CommandManager;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.action.ActionManager;
import org.eclipse.gmf.runtime.common.ui.resources.FileChangeManager;
import org.eclipse.gmf.runtime.common.ui.services.editor.EditorService;
import org.eclipse.gmf.runtime.common.ui.services.icon.IconService;
import org.eclipse.gmf.runtime.common.ui.util.SelectionRefresher;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorWithFlyOutPalette;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.views.PropertiesBrowserPage;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.l10n.EditorResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.notationprovider.EMFNotationModelFactory;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.notationprovider.EmfNotationException;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.palette.PaletteContent;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util.RunnableQueue;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util.Util;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.properties.IPropertySheetPage;


/**
 * The Diagram Editor
 *
 * @author qili
 * @canBeSeenBy %level1
 * @deprecated Please refer to org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.parts.DiagramDocumentEditor
 */
public abstract class Editor
	extends DiagramEditorWithFlyOutPalette {

	/** Cache of the active workbench part. */
	private IWorkbenchPart activeWorkbenchPart = null;

	/** the last modified time stamp of the file */
	private long modificationStamp = IResource.NULL_STAMP;

	/** is modification stamp checking enabled */
	private boolean modificationStampEnabled = true;

	/** if the use selects no to reload file changed dialog, next save will
	 * overwrite changes */
	private boolean updateConflict = false;

	private ResourceTracker resourceListener = new ResourceTracker();
	private PartListener partListener = new PartListener();
	private IProject project;
	private boolean savePreviouslyNeeded = false;

	// This class listens to changes to the file system in the workspace, and
	// makes changes accordingly.
	// 1) An open, saved file gets deleted -> close the editor
	// 2) An open file gets renamed or moved -> change the editor's input accordingly
	class ResourceTracker
		implements IResourceChangeListener, IResourceDeltaVisitor {
		boolean disabled = false;
		
		public void disable() {
			disabled = true;
		}
		
		public void enable() {
			disabled = false;
		}
		
		public boolean isDisabled() {
			return disabled;
		}
		
		public void resourceChanged(IResourceChangeEvent event) {
			if(disabled == false) {
				IResourceDelta delta = event.getDelta();
				try {
					if (delta != null)
						delta.accept(this);
				} catch (CoreException exception) {
					// What should be done here? 
					Log.warning(
						EditorPlugin.getInstance(),
						IStatus.WARNING,
						exception.getMessage(),
						exception);
				}
			}
		}
		public boolean visit(IResourceDelta delta) {
			if (delta == null
				|| !delta.getResource().equals(getFile()))
				return true;

			if (delta.getKind() == IResourceDelta.CHANGED) {
				if ((IResourceDelta.CONTENT & delta.getFlags()) != 0) {
					if (!isSaveOnCloseNeeded()
						&& computeModificationStamp() != modificationStamp) {
						Display display = getSite().getShell().getDisplay();
						display.asyncExec(new Runnable() {
							public void run() {
								handleEditorInputChanged(false);
							}
						});
					}
				}
			} else if (delta.getKind() == IResourceDelta.REMOVED) {
				if ((IResourceDelta.MOVED_TO & delta.getFlags()) == 0) {
					// if the file was deleted
					// NOTE: The case where an open, unsaved file is deleted is being handled by the
					// PartListener added to the Workbench in the initialize() method.
//					if (!isDirty()) {
						getSite()
							.getShell()
							.getDisplay()
							.syncExec(new Runnable() {
							public void run() {
								closeEditor(false);
							}
						});
//					}
				} else { // else if it was moved or renamed
					final IFile newFile =
						ResourcesPlugin.getWorkspace().getRoot().getFile(
							delta.getMovedToPath());
					Display display = getSite().getShell().getDisplay();
					RunnableQueue.addRunnableToHead(new Runnable() {
						public void run() {
							refreshEditor(makeInputFromFile(newFile));
						}
					});
					display.asyncExec(RunnableQueue.runner);
					setModificationStamp(IResource.NULL_STAMP);
				}
			}
			return false;
		}
	}

	class PartListener extends ShellAdapter implements IPartListener {

		/** Indicates whether activation handling is currently be done. */
		private boolean isHandlingActivation = false;

		public void partActivated(IWorkbenchPart part) {
			setActiveWorkbenchPart(part);
			if (part != Editor.this)
				return;
			handleActivation();
		}
		public void partBroughtToTop(IWorkbenchPart part) {
			//Do nothing
		}
		public void partClosed(IWorkbenchPart part) {
			//Do nothing
		}
		public void partDeactivated(IWorkbenchPart part) {
			//Do nothing
		}
		public void partOpened(IWorkbenchPart part) {
			//Do nothing
		}
		public void shellActivated(ShellEvent e) {
			handleActivation();
		}
		private void handleActivation() {
			if (isHandlingActivation || !isModificationStampEnabled())
				return;

			isHandlingActivation = true;
			try {
				handleEditorPartActivation();
			} finally {
				isHandlingActivation = false;
			}
		}
	}
	
	/**
	 * Checks if the specified input for this editor is supported.
	 * 
	 * @param input IEditorInput to check
	 * @return boolean true if the input is supported, false if it isn't 
	 */
	abstract protected boolean checkInput(IEditorInput input);
	
	/**
	 * Returns true if the IEditorInput for this editor is file based.
	 * Returns false if the IEditorInput for this editor isn't file based.
	 * 
	 * @return boolean true if the IEditorInput for this editor is file based,
	 * false if the IEditorInput for this editor isn't file based.
	 */
	abstract protected boolean isFileBased();
	
	/**
	 * Returns an IEditorInput based on the specified IFile.
	 * 
	 * @param file the new IEditorInput will be based on this IFile. 
	 * @return IEditorInput based on the specified IFile. 
	 */
	abstract protected IEditorInput makeInputFromFile(IFile file);
	
	/**
	 * Returns an IEditorInput based on the specified IFile.
	 * 
	 * @param file the new IEditorInput will be based on this IFile. 
	 * @return IEditorInput based on the specified IFile. 
	 */
	abstract protected IEditorInput makeInputFromFileAndDiagram(IFile file, Diagram diagram);
	
	/**
	 * Returns an IRunnableWithProgress that performs a save operation.
	 * 
	 * @param IFile the file to be saved
	 * @param clone true to clone the file, false not to
	 * @param progressMonitor the IProgressMonitor used for the save
	 * operation
	 */
	abstract protected IRunnableWithProgress getSaveRunnable(final IFile file, final boolean clone, final IProgressMonitor progressMonitor);
	
	/**
	 * Displays the Eclipse IDE's save as dialog in order to obtain the
	 * IFile for saving.
	 * 
	 * @param progressMonitor the IProgressMonitor used for the save as
	 * operation
	 * @return IFile the IFile that the contents of the editor will be saved in 
	 */
	abstract protected IFile getIFileForSaveAs(IProgressMonitor progressMonitor);

	/**
	 * Return the IFile for this editor.  Subclasses should override to return
	 * the file from their editor input.
	 * @param input the IEditorInput
	 * 
	 * @return IFile file for this editor input
	 */
	abstract protected IFile getFile(IEditorInput input);	

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor#configureDiagramEditDomain()
	 */
	protected void configureDiagramEditDomain() {
		super.configureDiagramEditDomain();
		DiagramEditDomain editDomain =
			(DiagramEditDomain) getDiagramEditDomain();
		editDomain.setActionManager(new ActionManager(CommandManager.getDefault()));
	}

	public IProject getProject() {
		return project;
	}

	public Editor() {
		super();
	}

	public void init(IEditorSite site, IEditorInput input)
		throws PartInitException {
		if (!checkInput(input))
			throw new PartInitException("Invalid Input: Must be IFileEditorInput"); //$NON-NLS-1$
		super.init(site, input);
	}

	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		//initializeGraphicalViewerDND();
	}
	
	protected void setOldFormatMode() {
		getDiagramEditPart().disableEditMode();
	}

	public void setInput(IEditorInput input) {
//		 The workspace never changes for an editor.  So, removing and re-adding the
		// resourceListener is not necessary.  But it is being done here for the sake
		// of proper implementation.  Plus, the resourceListener needs to be added
		// to the workspace the first time around.
		if(checkInput(input)) {
			MEditingDomain editingDomain = null;
			if (isFileBased() && input != null) {
				removeResourceChangeListener();
				Diagram oldDiagram = getDiagram();
				if(oldDiagram != null) {
					Resource oldResource = oldDiagram.eResource();
					if(oldResource != null)
						(editingDomain = MEditingDomainGetter.getMEditingDomain(oldResource)).unloadResource(oldResource);
				}
			}
	
			IFile file = getFile(input);
			Diagram diagram = null;
	
			try {
				//TODO revist, the client should supply an editing domain to use, 
				//for now we'll deafult to using the default editing domain				
				diagram = (editingDomain == null) ? EMFNotationModelFactory
					.load(file)
					: EMFNotationModelFactory.load(file, editingDomain);
			} catch (EmfNotationException e) {
				Trace.catching(EditorPlugin.getInstance(), EditorDebugOptions.EXCEPTIONS_CATCHING, getClass(), "setInput", e); //$NON-NLS-1$
				Log.error(EditorPlugin.getInstance(), EditorStatusCodes.ERROR, EditorResourceManager.getI18NString("Editor.EDITOR_OPEN_EXC_"), e); //$NON-NLS-1$
			}

			if(diagram != null) {
				project = file.getProject();
				
				super.setInput(makeInputFromFileAndDiagram(file, diagram));
		
				if (getEditorInput() != null) {
					addResourceChangeListener();
					setPartName(getFile().getName());
					refreshDiagramLabel();
					updateDirtyFlag();
				}
			} else {
				throw new RuntimeException(EditorResourceManager.getI18NString("Editor.EDITOR_OPEN_EXC_")); //$NON-NLS-1$
			}
		} 
	}

	/**
	 * Method refreshDiagramLabel.
	 */
	protected void refreshDiagramLabel() {
		try {
			MEditingDomainGetter.getMEditingDomain(getDiagram()).runAsRead(new MRunnable() {

				public Object run() {
					EObjectAdapter adapter = new EObjectAdapter(getDiagram());
					setTitleImage(IconService.getInstance().getIcon(adapter));
					return null;
				}
			});
		} catch (Exception e) {
			Log.error(EditorPlugin.getInstance(), IStatus.ERROR, e
				.getMessage(), e);
		}

	}

	/**
	 * Method doSave.
	 * @param file
	 * @param progressMonitor
	 * @throws Exception
	 */
	protected void doSave(IFile file, boolean clone, IProgressMonitor progressMonitor)
		throws Exception {

		// this is not an extenal resource modification, so temporarily turn off listening
		removeResourceChangeListener();

		/* check if the file is writable... */
		if (file.isReadOnly()) {
			String title = DiagramResourceManager.getI18NString("DiagramEditor.save.readonly.dialog.title"); //$NON-NLS-1$
			String message = DiagramResourceManager.getI18NString("DiagramEditor.save.readonly.dialog.message"); //$NON-NLS-1$
			MessageDialog.openError(
				getSite().getShell(),
				title,
				MessageFormat.format(message, new Object[] { file.getName()}));
			return;
		}

		try {
			progressMonitor.setTaskName(""); //$NON-NLS-1$

			Diagram diagram =
				((IDiagramWorkbenchPart) this).getDiagram();

			//save the notation part
			EMFNotationModelFactory.save(
				file,
				diagram,
				clone,
				new SubProgressMonitor(progressMonitor, 1));
			setModificationStamp(computeModificationStamp());
		} finally {
			if (progressMonitor != null)
				progressMonitor.done();

			// enable back resource listening
			addResourceChangeListener();

			updateDirtyFlag();
		}
	}

	public boolean isSaveOnCloseNeeded() {
		Resource model = ((EObject)getDiagram()).eResource();
		if (model != null) {
			return model.isModified();
		}
		// RATLC00520612: looks like a bug in Eclipse SaveAction
		// This method is called even after the Editor has been disposed
		// Assert.isTrue(false);
		return false;
	}

	public void dispose() {
		getCommandStack().removeCommandStackListener(this);
		getSite().getWorkbenchWindow().getPartService().removePartListener(
			partListener);
		getSite().getShell().removeShellListener(partListener);
		partListener = null;
		removeResourceChangeListener();
		resourceListener = null;
		project = null;
		super.dispose();

		Resource model = ((EObject)getDiagram()).eResource();
		if (model != null) {
			MEditingDomainGetter.getMEditingDomain(model).unloadResource(model);
		}
		
		activeWorkbenchPart = null;
	}
	
	protected void setSite(IWorkbenchPartSite site) {
		super.setSite(site);
		getSite().getWorkbenchWindow().getPartService().addPartListener(
			partListener);
		getSite().getShell().addShellListener(partListener);

	}

	/**
	 * Adds the resource listener so we can listen to extenal editor's modification to the file
	 */
	public void addResourceChangeListener() {
		if(isFileBased()) {
			IFile file = getFile();
			if(resourceListener != null)
				file.getWorkspace().addResourceChangeListener(resourceListener);
		}
	}

	/**
	 * removes the resource listener
	 */
	public void removeResourceChangeListener() {
		if(isFileBased()) {
			IFile file = getFile();
			if(resourceListener != null)
				file.getWorkspace().removeResourceChangeListener(resourceListener);
		}
	}

	/**
	 * Updates the dirty flag of the editor based on whether
	 * the editor is dirty
	 */
	public void updateDirtyFlag() {
		if (isDirty()) {
			if (!savePreviouslyNeeded()) {
				setSavePreviouslyNeeded(true);
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
		} else {
			setSavePreviouslyNeeded(false);
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	/**
	 * Reacts to command stack chages by refreshing the dirty flag
	 * @see org.eclipse.gef.commands.CommandStackListener#commandStackChanged(EventObject)
	 */
	public void commandStackChanged(EventObject e) {
		super.commandStackChanged(e);
		updateDirtyFlag();
		SelectionRefresher.refreshSelection();
	}

	private void setSavePreviouslyNeeded(boolean value) {
		savePreviouslyNeeded = value;
	}

	private boolean savePreviouslyNeeded() {
		return savePreviouslyNeeded;
	}

	private boolean handleFileDeletedEvent() {
		setModificationStampEnabled(false);
		Shell shell = getSite().getShell();
		
		resourceListener.disable();
		try {
			getFile().refreshLocal(
				IResource.DEPTH_ZERO,
				null);
		} catch (CoreException e) {
			Trace.catching(EditorPlugin.getInstance(), EditorDebugOptions.EXCEPTIONS_CATCHING, getClass(), "handleEditorInputChanged", //$NON-NLS-1$
			e);
		}
		resourceListener.enable();

		String title = DiagramResourceManager.getI18NString("DiagramEditor.handleDeleteEvent.dialog.title"); //$NON-NLS-1$
		String message = DiagramResourceManager.getI18NString("DiagramEditor.handleDeleteEvent.dialog.message"); //$NON-NLS-1$
		String[] buttons = { DiagramResourceManager.getI18NString("DiagramEditor.handleDeleteEvent.dialog.button.save"), //$NON-NLS-1$
			DiagramResourceManager.getI18NString("DiagramEditor.handleDeleteEvent.dialog.button.close")}; //$NON-NLS-1$
		MessageDialog dialog =
			new MessageDialog(
				shell,
				title,
				null,
				message,
				MessageDialog.QUESTION,
				buttons,
				0);
		if (dialog.open() == 0) {
			return performSaveAs(new NullProgressMonitor(), true);
		} else {
			closeEditor(false);
		}
		return false;
	}

	/**
	 * Handles the activation triggering a element state check in the editor.
	 */
	private void handleEditorPartActivation() {
		if (getActiveWorkbenchPart() == this) {
			long currentModificationStamp = computeModificationStamp();
			if (getModificationStamp() == IResource.NULL_STAMP) {
				setModificationStamp(currentModificationStamp);
			} else if (getModificationStamp() != currentModificationStamp) {
				handleEditorInputChanged(true);
			}
		}
	}

	/**
	 * Computes the modification stamp for the given file.
	 *
	 * @return the modification stamp
	 */
	private long computeModificationStamp() {
		if(isFileBased()) {
			IFile file = getFile();
			return computeModificationStamp(file);
		}
		return IResource.NULL_STAMP;
	}

	private boolean isDeleted() {
		IFile file = getFile();
		IPath path = file.getLocation();
		boolean existsInFileSystem = false;
		if(path != null) {
			File systemFile = new File(path.toOSString());
			existsInFileSystem = systemFile.exists();

		}
		boolean existsInWorkspace = file.exists();
		return !existsInWorkspace || !existsInFileSystem;
	}

	/**
	 * Computes the modification stamp for the given file.
	 *
	 * @return the modification stamp
	 */
	private long computeModificationStamp(IFile file) {
		long stamp = file.getModificationStamp();
		IPath path = file.getLocation();
		if (path == null)
			return stamp;
		stamp = path.toFile().lastModified();
		return stamp;
	}

	/**
	 * Handles an external change of the editor's input element.
	 * @param prompt if true, prompt the use before loading the changes into
	 * the workspace.
	 */
	private void handleEditorInputChanged(boolean prompt) {
		if(isFileBased() == false)
			return;
		
		// If an open, unsaved file was deleted, query the user to either do a "Save As"
		// or close the editor.
		if (isDeleted()) {
			getSite().getShell().getDisplay().syncExec(new Runnable() {
				public void run() {
					handleFileDeletedEvent();
				}
			});
			return;
		} else {
			boolean answer = true;
			if (prompt) {
				String title = DiagramResourceManager.getI18NString("DiagramEditor.activated.outofsync.dialog.title"); //$NON-NLS-1$
				String msg = DiagramResourceManager.getI18NString("DiagramEditor.activated.outofsync.dialog.message"); //$NON-NLS-1$
				Shell shell = getSite().getShell();
				answer = MessageDialog.openQuestion(shell, title, msg);
			}
			if (answer) {
				try {
					getFile().refreshLocal(
						IResource.DEPTH_ZERO,
						null);
				} catch (CoreException e) {
					Trace.catching(EditorPlugin.getInstance(), EditorDebugOptions.EXCEPTIONS_CATCHING, getClass(), "handleEditorInputChanged", //$NON-NLS-1$
					e);
				}

				refreshEditor(getEditorInput());

				setUpdateConflict(false);
				setModificationStamp(computeModificationStamp());

			} else {
				setUpdateConflict(true);
				setModificationStamp(computeModificationStamp());
			}
		}
	}

	/**
	 * @see org.eclipse.ui.IEditorPart#doSave(IProgressMonitor)
	 */
	public void doSave(IProgressMonitor progressMonitor) {
		if (validateUpdateConflict()) {
			try {
				IFile file = getFile();
				if (file.exists()) {
					setModificationStampEnabled(false);
					doSave(file, false, progressMonitor);
					setModificationStamp(computeModificationStamp());
					setModificationStampEnabled(true);
				} else
					performSaveAs(progressMonitor);
				
				file.refreshLocal(IResource.DEPTH_ZERO, null);
			} catch (Exception e) {
				Util.reportException(e, null, null);
			}
		}
	}

	public void doSaveAs() {
		performSaveAs(new NullProgressMonitor());
	}

	public boolean isDirty() {
		return isSaveOnCloseNeeded();
	}

	public boolean isSaveAsAllowed() {
		return true;
	}
	
	protected boolean performSaveAs(IProgressMonitor progressMonitor, boolean originalDeleted) {
		final IFile file = getIFileForSaveAs(progressMonitor); 

		boolean sameFile = false;
		if(!originalDeleted) {
			if(isFileBased()) {
				sameFile = ((IFile) ((IAdaptable) getEditorInput()).getAdapter(IFile.class))
					.getLocation()
					.equals(file.getLocation());
	
				if (!sameFile) {
					// check if the file is open in the editor
					List editors =
						EditorService.getInstance().getRegisteredEditorParts();
					for (Iterator it = editors.iterator(); it.hasNext();) {
						IEditorPart editor = (IEditorPart) it.next();
						IFile f =
							(IFile) ((IAdaptable) editor.getEditorInput()).getAdapter(
								IFile.class);
						if (f
							.getLocation()
							.toString()
							.equals(file.getLocation().toString())) {
							MessageBox mb =
								new MessageBox(
									getSite().getWorkbenchWindow().getShell(),
									SWT.OK | SWT.ICON_ERROR | SWT.APPLICATION_MODAL);
							mb.setMessage(file.getFullPath() + " " //$NON-NLS-1$
							+DiagramResourceManager.getI18NString("DiagramEditor.performSaveAs.message")); //$NON-NLS-1$
							mb.open();
							if (progressMonitor != null)
								progressMonitor.setCanceled(true);
							return false;
						}
					}
				}
			}
		}
		
		final boolean clone = originalDeleted ? originalDeleted : !sameFile;
		
		IRunnableWithProgress op = getSaveRunnable(file, clone, progressMonitor);

		try {
			new ProgressMonitorDialog(
				getSite().getWorkbenchWindow().getShell()).run(
				false,
				true,
				op);

			if (!sameFile)
				refreshEditor(makeInputFromFile(file));

			setModificationStamp(computeModificationStamp());
		} catch (Exception e) {
			Trace.catching(EditorPlugin.getInstance(), EditorDebugOptions.EXCEPTIONS_CATCHING, getClass(), "performSaveAs", //$NON-NLS-1$
			e);
		}

		if (progressMonitor != null)
			progressMonitor.setCanceled(false);
		return true;
	}

	protected boolean performSaveAs(IProgressMonitor progressMonitor) {
		return performSaveAs(progressMonitor, false);
	}

	/**
	 * validate that there are not any pending changes on the file system.
	 * If there are, prompt the use before overwriting these changes
	 * @return true if there are no update conflicts
	 */
	private boolean validateUpdateConflict() {
		boolean answer = true;
		if (isUpdateConflict()) {
			String title = DiagramResourceManager.getI18NString("DiagramEditor.save.outofsync.dialog.title"); //$NON-NLS-1$
			String msg = DiagramResourceManager.getI18NString("DiagramEditor.save.outofsync.dialog.message"); //$NON-NLS-1$
			Shell shell = getSite().getShell();
			answer = MessageDialog.openQuestion(shell, title, msg);
		}

		return answer;
	}
	/**
	 * Returns the modificationStamp.
	 * @return long
	 */
	private long getModificationStamp() {
		return modificationStamp;
	}

	/**
	 * Sets the modificationStamp.
	 * @param modificationStamp The modificationStamp to set
	 */
	private void setModificationStamp(long modificationStamp) {
		this.modificationStamp = modificationStamp;
	}

	/**
	 * Returns the updateConflict.
	 * @return boolean
	 */
	private boolean isUpdateConflict() {
		return updateConflict;
	}

	/**
	 * Sets the updateConflict.
	 * @param updateConflict The updateConflict to set
	 */
	private void setUpdateConflict(boolean updateConflict) {
		this.updateConflict = updateConflict;
	}

	/**
	 * Returns the cached active Workbench Part.
	 * @return IWorkbenchPart
	 */
	private IWorkbenchPart getActiveWorkbenchPart() {
		return activeWorkbenchPart;
	}

	/**
	 * Sets the cached active Workbench Part.
	 * @param activeWorkbenchPart The activeWorkbenchPart to set
	 */
	private void setActiveWorkbenchPart(IWorkbenchPart activePart) {
		this.activeWorkbenchPart = activePart;
	}

	/**
	 * verify that it is ok to edit this file.
	 * @param modificationRequest String to display in any error dialogs
	 * @return true of the file is ok to edit.
	 */
	public boolean okToEdit(String modificationRequest) {
		if(isFileBased()) {
			return FileChangeManager.getInstance().okToEdit(
				new IFile[] {getFile()},
				modificationRequest);
		} else {
			return false;
		}
	}

	/**
	 * Returns the modificationStampEnabled.
	 * @return boolean
	 */
	private boolean isModificationStampEnabled() {
		return modificationStampEnabled;
	}

	/**
	 * Sets the modificationStampEnabled.
	 * @param modificationStampEnabled The modificationStampEnabled to set
	 */
	private void setModificationStampEnabled(boolean modificationStampEnabled) {
		this.modificationStampEnabled = modificationStampEnabled;
	}

	/**
	 * refreshes the editor with the given file by:
	 * 0 - resets resource modification stamp
	 * 1- Flushing the command stack
	 * 2- Clearing the graphical viewer's contents
	 * 3- Setting the new File input
	 * 4- Setting the new graphical viewer's contents
	 * 5- Clearing the graphical viewer's selection
	 * @param file The new file editor input
	 */
	protected void refreshEditor(IEditorInput editorInput) {
		assert checkInput(editorInput);
		
		setModificationStamp(IResource.NULL_STAMP); // old one is no good
		getCommandStack().flush();
		clearGraphicalViewerContents();
		setInput(editorInput);
		initializeGraphicalViewerContents();
		getDiagramGraphicalViewer().deselectAll();
	}
	
	protected Object getDefaultPaletteContent() {
		Diagram diagram = getDiagram();
		Assert.isNotNull(diagram);
		PaletteContent defPaletteContent = null;
		if(diagram != null) {
			//IResource workspaceRes = getWorkspaceResource(diagram);
			//IProject project = workspaceRes != null ? workspaceRes.getProject() : null;
			defPaletteContent = new PaletteContent(null, diagram);
		}
		return defPaletteContent;
	}
	
	/**
	 * Return the IFile for this editor.  It is preferable to override the other
	 * getFile() method.
	 * 
	 * @return IFile file for this editor input
	 */
	protected IFile getFile() {
		return getFile(getEditorInput());
	}	
	
	public Object getAdapter(Class type) {
		if (type == IPropertySheetPage.class) {
			return new PropertiesBrowserPage(this);
		}
		return super.getAdapter(type);
	}
	
}