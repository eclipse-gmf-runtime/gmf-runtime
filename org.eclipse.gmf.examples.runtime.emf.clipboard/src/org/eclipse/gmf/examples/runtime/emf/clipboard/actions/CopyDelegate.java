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

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;

import org.eclipse.gmf.runtime.emf.clipboard.core.ClipboardUtil;
import org.eclipse.gmf.examples.runtime.emf.clipboard.transfer.EmfTransfer;
import org.eclipse.gmf.examples.runtime.emf.clipboard.transfer.EmfTransferType;



/**
 * Action delegate for the Library-metamodel-aware Copy action.
 */
public class CopyDelegate
	extends AbstractClipboardDelegate {

	/**
	 * Initializes me.
	 */
	public CopyDelegate() {
		super();
	}

	/**
	 * Copies the selected elements to the clipboard, in string form.  No hints
	 * are required for the copy operation.
	 */
	protected void doRun(Clipboard clipboard) {
		String clipString = ClipboardUtil.copyElementsToString(
			getSelectedObjects(),
			null,
			null);
		
		if (clipString == null) {
			return;
		}
		
		// use the customer EMF data transfer type provided by this plug-in
		clipboard.setContents(
			new EmfTransferType[] {new EmfTransferType(clipString)},
			new Transfer[] {EmfTransfer.getInstance()});
	}
}
