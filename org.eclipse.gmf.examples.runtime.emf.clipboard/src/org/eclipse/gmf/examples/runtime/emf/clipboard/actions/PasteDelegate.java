/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.examples.runtime.emf.clipboard.actions;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.Clipboard;

import org.eclipse.gmf.runtime.emf.clipboard.core.ClipboardUtil;
import org.eclipse.gmf.examples.runtime.emf.clipboard.ClipboardExamplePlugin;
import org.eclipse.gmf.examples.runtime.emf.clipboard.transfer.EmfTransfer;
import org.eclipse.gmf.examples.runtime.emf.clipboard.transfer.EmfTransferType;



/**
 * Action delegate for the Library-metamodel-aware Paste action.  The paste
 * action is undoable.
 */
public class PasteDelegate
	extends AbstractClipboardDelegate {

	private static final String PROBLEMS_TITLE = ClipboardExamplePlugin.getResourceString("pasteProblems.title"); //$NON-NLS-1$
	private static final String PROBLEMS_MESSAGE = ClipboardExamplePlugin.getResourceString("pasteProblems.msg"); //$NON-NLS-1$
	
	/**
	 * Initializes me.
	 */
	public PasteDelegate() {
		super();
	}

	/**
	 * Pastes elements from the clipboard.  No hints are required for this paste
	 * operation.
	 */
	protected void doRun(final Clipboard clipboard) {
		final Collection objectsPasted = new java.util.HashSet();
		
		EditingDomain domain = getEditor().getEditingDomain();
		
		CompoundCommand command = new CompoundCommand(
			ClipboardExamplePlugin.getResourceString("paste.label")); //$NON-NLS-1$
		
		// add a command that does the paste to the composite
		command.append(new RecordingCommand(
			domain,
			"", //$NON-NLS-1$
			new Runnable() {
				public void run() {
					doRun(clipboard, objectsPasted);
				}}));
		
		// another command to select the pasted elements.
		//    It does nothing on undo
		command.append(new SelectionCommand(objectsPasted));
		
		// execute the composite
		domain.getCommandStack().execute(command);
	}
	
	/**
	 * Implementation of the <code>Runnable</code>'s run method in the change
	 * command.
	 * 
	 * @param clipboard the clipboard to paste from
	 * @param objectsPasted accumulates the objects pasted
	 */
	void doRun(Clipboard clipboard, Collection objectsPasted) {
		boolean problems = false;
		
		// use the customer EMF data transfer type provided by this plug-in
		EmfTransferType data = (EmfTransferType) clipboard.getContents(
			EmfTransfer.getInstance());
		
		if (data != null) {
			String clipString = data.getString();
			
			// paste into each selected element
			for (Iterator iter = getSelectedObjects().iterator(); iter.hasNext();) {
				EObject target = (EObject) iter.next();
				
				Collection pasted = ClipboardUtil.pasteElementsFromString(
					clipString, target, null, null);
				
				if (pasted == null || pasted.isEmpty()) {
					problems = true;
				} else {
					objectsPasted.addAll(pasted);
				}
			}
		}
		
		if (problems) {
			MessageDialog.openInformation(
				getShell(), PROBLEMS_TITLE, PROBLEMS_MESSAGE);
		}
	}
	
	/**
	 * A command that selects a collection of elements in the editor whenever
	 * it is executed or redone.
	 */
	private class SelectionCommand extends AbstractCommand {
		private Collection elementsToSelect;
		
		/**
		 * Initializes me with the <code>elements</code> to select.
		 * 
		 * @param elements the elements to select
		 */
		SelectionCommand(Collection elements) {
			this.elementsToSelect = elements;
		}
		
		public void execute() {
			selectInEditor(elementsToSelect);
		}

		public void redo() {
			execute();
		}
		
		public void undo() {
			// undo is a no-op (deleting the pasted objects
			//     unselects them, anyway)
		}
		
		protected boolean prepare() {
			return true;  // nothing to prepare
		}
		
		public void dispose() {
			elementsToSelect = null;
		}
	}
}
