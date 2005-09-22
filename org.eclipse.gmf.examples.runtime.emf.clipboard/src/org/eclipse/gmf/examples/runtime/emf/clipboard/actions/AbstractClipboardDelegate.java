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

package org.eclipse.gmf.examples.runtime.emf.clipboard.actions;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.examples.extlibrary.presentation.EXTLibraryEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;


/**
 * Abstract action delegate for copy/paste actions; provides common behaviours
 * for managing the selection, etc.
 */
abstract class AbstractClipboardDelegate
	extends ActionDelegate
	implements IEditorActionDelegate {

	/**
	 * The shell this action is hosted in.
	 */
	private Shell shell = null;

	/**
	 * The active editor
	 */
	private EXTLibraryEditor editor = null;

	/**
	 * Selected {@link EObject}s.
	 */
	private Collection selectedEObjects = Collections.EMPTY_SET;

	/**
	 * Initializes me.
	 */
	protected AbstractClipboardDelegate() {
		super();
	}
	
	/**
	 * Retrieves the user's selection.
	 * 
	 * @return a collection of selected {@link EObject}s
	 */
	protected Collection getSelectedObjects() {
		return selectedEObjects;
	}
	
	/**
	 * Obtains the shell to use for opening dialogs.
	 * 
	 * @return my shell
	 */
	protected Shell getShell() {
		return shell;
	}
	
	/**
	 * Selects the specified <code>objects</code> in the current editor.
	 * 
	 * @param objects the objects to select (may be empty)
	 */
	protected void selectInEditor(Collection objects) {
		editor.setSelectionToViewer(objects);
	}
	
	/**
	 * Accesses the current editor.
	 * 
	 * @return the current editor
	 */
	protected EXTLibraryEditor getEditor() {
		return editor;
	}
	
	/**
	 * Template method that delegates to subclasses to run with a clipboard.
	 */
	public final void run(IAction action) {
		Clipboard clipboard = null;
		
		if (!getSelectedObjects().isEmpty()) {
			try {
				clipboard = new Clipboard(getShell().getDisplay());
				
				doRun(clipboard);
			} finally {
				if (clipboard != null) {
					// must clean up the clipboard that we created
					clipboard.dispose();
				}
			}
		}
	}
	
	/**
	 * Implemented by sublasses to do their copy or paste to or from the
	 * specified clipboard.
	 * 
	 * @param clipboard the clipboard.  Must not be disposed by the receiver
	 */
	protected abstract void doRun(Clipboard clipboard);

	/**
	 * Gets all of the {@link EObject}s in the current selection, if any.
	 */
	public void selectionChanged(IAction action, final ISelection selection) {
		selectedEObjects = Collections.EMPTY_SET;
		
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			selectedEObjects = new java.util.ArrayList();
			
			for (Iterator iter = structuredSelection.iterator(); iter.hasNext();) {
				Object next = iter.next();
				
				if (next instanceof EObject) {
					selectedEObjects.add(next);
				}
			}
		}
		
		action.setEnabled(!selectedEObjects.isEmpty());
	}

	/**
	 * Get the active library editor and its host shell.
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		editor = (EXTLibraryEditor) targetEditor;
		
		if (targetEditor != null) {
			this.shell = targetEditor.getSite().getShell();
		}
	}
}