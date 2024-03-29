/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.examples.runtime.emf.clipboard.actions;

import org.eclipse.gmf.examples.runtime.emf.clipboard.transfer.EmfTransfer;
import org.eclipse.gmf.examples.runtime.emf.clipboard.transfer.EmfTransferType;
import org.eclipse.gmf.runtime.emf.clipboard.core.ClipboardUtil;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;



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
