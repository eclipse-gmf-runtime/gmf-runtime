/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.wizards;


import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.l10n.EditorMessages;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.util.IDEEditorUtil;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.DiagramFileCreator;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.util.EditorUtil;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
;

/**
 * @author qili
 * @canBeSeenBy %level1
 * 
 * Create Diagram Wizard Page
 */
public abstract class EditorWizardPage
	extends WizardNewFileCreationPage {
	
	/** workbench */
	protected final IWorkbench workbench;
	
	/**
	 * the selection holding the elements to be displayed
	 */
	private final IStructuredSelection selection;
	
	/**
	 * the diagram file for the newlt created diagram
	 */
	protected IFile diagramFile;
	
	/**
	 * A flag indicating whether or not to open the editor of the newly created diagram
	 * The default is true
	 */
	protected boolean openNewlyCreatedDiagramEditor;

	/**
	 * EditorWizardPage constructor
	 *
	 * @param pageName the page name
	 * @param aWorkbench
	 *            workbench the workbench this page will be displayed in
	 * @param selection
	 *            selection the current selection
	 */
	public EditorWizardPage(String pageName, IWorkbench aWorkbench,
			IStructuredSelection selection) {
		super(pageName, selection);
		this.workbench = aWorkbench;
		this.selection = selection;
		this.diagramFile = null;
		this.openNewlyCreatedDiagramEditor = true;
	}

	/**
	 * @param monitor the <code>IProgressMonitor</code> to use to indicate progress and check for cancellation
	 * @return boolean indicating whether the creation and opening the Diagram was successful
	 */
	public boolean doFinish(IProgressMonitor monitor) {
		diagramFile = createAndOpenDiagram(
				getContainerFullPath(),
				getFileName(),
				getInitialContents(),
				getDiagramKind(),
				workbench.getActiveWorkbenchWindow(),
				monitor,
				true);

		return diagramFile != null;
	}

	/**
	 * @param containerPath
	 * @param fileName
	 * @param initialContents
	 * @param kind
	 * @param dWindow
	 * @param progressMonitor
	 * @param saveDiagram
	 * @return
	 */
	public IFile createAndOpenDiagram(
			IPath containerPath,
			String fileName,
			InputStream initialContents,
			String kind,
			IWorkbenchWindow dWindow,
			IProgressMonitor progressMonitor,
			boolean saveDiagram) {
		return IDEEditorUtil.createAndOpenDiagram(
				getDiagramFileCreator(),
				containerPath,
				fileName,
				initialContents,
				kind,
				dWindow,
				progressMonitor,
				isOpenNewlyCreatedDiagramEditor(),
				saveDiagram);
	}
	
	/**
	 * Answers whether or not this Wizard will open the editor of the newly created diagram
	 * @return Returns the openNewlyCreatedDiagramEditor.
	 */
	public boolean isOpenNewlyCreatedDiagramEditor() {
		return openNewlyCreatedDiagramEditor;
	}
	
	/**
	 * Sets a flag indicating whether or not the newly created diagram editor should be opened 
	 * @param openNewlyCreatedDiagramEditor The openNewlyCreatedDiagramEditor to set.
	 */
	public void setOpenNewlyCreatedDiagramEditor(
			boolean openNewlyCreatedDiagramEditor) {
		this.openNewlyCreatedDiagramEditor = openNewlyCreatedDiagramEditor;
	}
	
	
	/**
	 * Performs the operations necessary to create and open the diagram
	 * @return boolean indicating whether the creation and opening the Diagram was successful 
	 */
	public boolean finish() {
		final boolean[] result = new boolean[1];
		WorkspaceModifyOperation op = new WorkspaceModifyOperation(null) {
			protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
				result[0] = doFinish(monitor);
			}
		};

		try {
			getContainer().run(false, true, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof CoreException) {
				ErrorDialog.openError(
					getContainer().getShell(),
					EditorMessages.EditorWizardPage_DialogInternalErrorTitle,
					null,	// no special message
					((CoreException) e.getTargetException()).getStatus());
			}
			else {
				// CoreExceptions are handled above, but unexpected runtime exceptions and errors may still occur.
				Trace.catching(EditorPlugin.getInstance(), EditorDebugOptions.EXCEPTIONS_CATCHING, getClass(), "finish", e.getTargetException()); //$NON-NLS-1$
				Log.error(EditorPlugin.getInstance(), EditorStatusCodes.ERROR, EditorMessages.WizardPage_DIAGRAM_CREATION_FAIL_EXC_, e.getTargetException());
			}
			return false;
		}
		return result[0];
	}
	
	/**
	 * Accessor for the diagram's file
	 * @return IFile the file owning the diagram
	 */
	public final IFile getDiagramFile() {
		return diagramFile;
	}
	
	/**
	 * @return the default file name
	 */
	protected String getDefaultFileName() {
		return null;
	}

	/**
	 * @return the diagram file creator
	 */
	public abstract DiagramFileCreator getDiagramFileCreator();

	/**
	 * @return the diagram kind
	 */
	protected String getDiagramKind() {
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#getInitialContents()
	 */
	protected InputStream getInitialContents() {
		return EditorUtil.getInitialContents();
	}
	
	/**
	 * Returns the current file name as entered by the user, or its anticipated
	 * initial value.
	 *
	 * @return the file name, its anticipated initial value, or <code>null</code>
	 *   if no file name is known
	 */
	public String getFileName() {
		String fileName = super.getFileName();

		if (fileName != null)
			fileName = getDiagramFileCreator().appendExtensionToFileName(fileName);

		return fileName;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
	 */
	protected boolean validatePage() {
		if (super.validatePage()) {
			// do additional validation on the anticipated filename
			String fileName = getFileName();

			if (fileName == null)
				return false;

			IPath path = getContainerFullPath().append(fileName);

			if (ResourcesPlugin.getWorkspace().getRoot().exists(path)) {
				setErrorMessage(EditorMessages.WizardPage_Message_FileExists_ERROR_);
				return false;
			}

			// Some characters reserved in URI should be checked here. :,#,\,/
			URI fileURI = URI.createFileURI(path.toString());
			String ext = fileURI.fileExtension(); 

			if (ext != null
				&& getDiagramFileCreator().getExtension().equals("." + ext)) { //$NON-NLS-1$
				return true;
			}

			setErrorMessage(NLS.bind(EditorMessages.EditorWizardPage_InvalidFilename, fileName));
		}

		return false;
	}

	/**
	 * Accessor for selected elements
	 * @return IStructuredSelection the selection holding the elements to be displayed on the diagram 
	 */
	public final IStructuredSelection getSelection() {
		return selection;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);

		IPath path = getContainerFullPath();

		if (null != path) {
			String fileName = getDiagramFileCreator().getUniqueFileName(path,
				getDefaultFileName());

			setFileName(fileName);
		}

		setPageComplete(validatePage());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#initialPopulateContainerNameField()
	 */
	protected void initialPopulateContainerNameField() {
		IPath path = EditorUtil.getDefaultDiagramPath(selection.toList(), getDiagramKind());

		if (path == null) {
			super.initialPopulateContainerNameField();

			path = getContainerFullPath();

			if (path == null) {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IProject[] projects = root.getProjects();

				path = root.getFullPath();

				for (int i = 0; i < projects.length; ++i) {
					IProject project = projects[i];

					if (project.isOpen()) {
						path = project.getFullPath();
						break;
					}
				}
			}
		}

		setContainerFullPath(path);
 	}

}

