/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.parts;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.EditorIDEPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.ui.parts.Editor;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util.Util;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;


/**
 * An editor suited for use in an IDE.  The expected input is an
 * IFileEditorInput.
 * 
 * @author wdiu, Wayne Diu, refactored from qli's Editor
 * @deprecated
 */
public class IDEEditor extends Editor {

	/**
	 * Checks if the specified input for this editor is supported.
	 * 
	 * @param input IEditorInput to check
	 * @return boolean true if the input is supported, false if it isn't 
	 */
	protected boolean checkInput(IEditorInput input) {
		//we do not support IStorageEditorInput directly
		return input instanceof IFileEditorInput;
	}

	/**
	 * Returns true if the IEditorInput for this editor is file based.
	 * Returns false if the IEditorInput for this editor isn't file based.
	 * 
	 * @return boolean true if the IEditorInput for this editor is file based,
	 * false if the IEditorInput for this editor isn't file based.
	 */
	protected boolean isFileBased() {
		return getEditorInput() instanceof IFileEditorInput;
	}

	/**
	 * Returns an IEditorInput based on the specified IFile.
	 * 
	 * @param file the new IEditorInput will be based on this IFile. 
	 * @return IEditorInput based on the specified IFile. 
	 */
	protected IEditorInput makeInputFromFile(IFile file) {
		return new FileEditorInput(file);
	}
	
	/**
	 * Returns an IEditorInput based on the specified IFile.
	 * 
	 * @param file the new IEditorInput will be based on this IFile. 
	 * @return IEditorInput based on the specified IFile. 
	 */
	protected IEditorInput makeInputFromFileAndDiagram(IFile file, Diagram diagram) {
		return new IDEEditorInput(file, diagram);
	}	
	
	/**
	 * Returns an IEditorInput based on the specified IFile.
	 * 
	 * @param file the new IEditorInput will be based on this IFile. 
	 * @return IEditorInput based on the specified IFile. 
	 */
	protected IEditorInput makeInputFromFileAndDiagram(IFile file) {
		return new FileEditorInput(file);
	}	

	/**
	 * Returns an IRunnableWithProgress that performs a save operation.
	 * 
	 * @param IFile the file to be saved
	 * @param clone true to clone the file, false not to
	 * @param progressMonitor the IProgressMonitor used for the save
	 * operation
	 */
	protected IRunnableWithProgress getSaveRunnable(final IFile file, final boolean clone, final IProgressMonitor progressMonitor) {
		return new WorkspaceModifyOperation() {
			public void execute(final IProgressMonitor monitor)
				throws CoreException {
				try {
					IDEEditor.super.doSave(file, clone, monitor);
				} catch (Exception e) {
					Util.reportException(e, null, null, EditorIDEPlugin.getPluginId());
				}
			}
		};
	}
	
	/**
	 * Displays the Eclipse IDE's save as dialog in order to obtain the
	 * IFile for saving.
	 * 
	 * @param progressMonitor the IProgressMonitor used for the save as
	 * operation
	 * @return IFile the IFile that the contents of the editor will be saved in 
	 */
	protected IFile getIFileForSaveAs(IProgressMonitor progressMonitor) {
		SaveAsDialog dialog =
			new SaveAsDialog(getSite().getWorkbenchWindow().getShell());
		// Only set the original file if it is file based.
		if(isFileBased()) 
			dialog.setOriginalFile(getFile());
		dialog.open();
		IPath path = dialog.getResult();

		if (path == null) {
			if (progressMonitor != null)
				progressMonitor.setCanceled(true);
			return null;
		}

		return
			ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		
	}
	
	/**
	 * Returns an IFile for the specified editor input.
	 * 
	 * @param IEditorInput to get the IFile from.
	 */
	protected IFile getFile(IEditorInput input) {
		assert checkInput(input);
		return ((IFileEditorInput) input).getFile();
	}
	
}