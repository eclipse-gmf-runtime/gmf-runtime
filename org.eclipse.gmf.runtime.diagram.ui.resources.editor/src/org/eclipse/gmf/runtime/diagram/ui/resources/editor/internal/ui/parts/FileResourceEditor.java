/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.ui.parts;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IEditorInput;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util.Util;
import com.ibm.xtools.notation.Diagram;

/**
 * An editor without IDE dependencies.  The expected input is an
 * IFileResourceEditorInput.
 * 
 * Subclasses must implement
 * abstract protected IFile getIFileForSaveAs(IProgressMonitor progressMonitor);
 * 
 * @author wdiu, Wayne Diu
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.resources.editor.*
 * @deprecated
 */
public abstract class FileResourceEditor extends Editor {
	
	/**
	 * Checks if the specified input for this editor is supported.
	 * 
	 * @param input IEditorInput to check
	 * @return boolean true if the input is supported, false if it isn't 
	 */
	protected boolean checkInput(IEditorInput input) {
		return getEditorInput() instanceof IFileResourceEditorInput; 
	}
	
	/**
	 * Returns true if the IEditorInput for this editor is file based.
	 * Returns false if the IEditorInput for this editor isn't file based.
	 * 
	 * @return boolean true if the IEditorInput for this editor is file based,
	 * false if the IEditorInput for this editor isn't file based.
	 */
	protected boolean isFileBased() {
		return getEditorInput() instanceof IFileResourceEditorInput;
	}	
	
	/**
	 * Returns an IEditorInput based on the specified IFile.
	 * 
	 * @param file the new IEditorInput will be based on this IFile. 
	 * @return IEditorInput based on the specified IFile. 
	 */
	protected IEditorInput makeInputFromFile(IFile file) {
		return new FileResourceEditorInput(file);
	}
	
	/**
	 * Returns an IEditorInput based on the specified IFile.
	 * 
	 * @param file the new IEditorInput will be based on this IFile. 
	 * @return IEditorInput based on the specified IFile. 
	 */
	protected IEditorInput makeInputFromFileAndDiagram(IFile file, Diagram diagram) {
		return new FileResourceDiagramEditorInput(file, diagram);
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
		return new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {
				try {
					doSave(file, clone, monitor);
				} catch (Exception e) {
					Util.reportException(e, null, null);
				}
			}
		};
	}

	/**
	 * Return the IFile for this editor.
	 * @param input the IEditorInput
	 * 
	 * @return IFile file for this editor input
	 */
	protected IFile getFile(IEditorInput input) {
		assert checkInput(input);
		return ((IFileResourceEditorInput) input).getFile();
	}
}
