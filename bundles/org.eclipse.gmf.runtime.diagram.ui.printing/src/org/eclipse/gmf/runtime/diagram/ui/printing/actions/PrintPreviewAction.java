/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.actions;

import org.eclipse.gmf.runtime.common.ui.action.actions.IPrintActionHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.printpreview.PrintPreviewHelper;
import org.eclipse.jface.action.Action;

/**
 * This is the action for print preview. It opens a dialog showing how the
 * diagram will look when printed. The <code>PrintPreviewHelper</code> passed
 * into the constructor does the actual work of the print preview. The
 * <code>IPrintActionHelper</code> passed into the constructor does the actual
 * work of showing the print settings dialog and doing the print if the user
 * were to initiate a print from within the print preview dialog.
 * 
 * @author Wayne Diu, wdiu
 */
public class PrintPreviewAction
	extends Action {

	/**
	 * ID of the print preview action.
	 */
	public static final String ID = "printPreviewAction"; //$NON-NLS-1$

	/**
	 * Print action helper used if the user does a print from within the print preview dialog.
	 */
	private IPrintActionHelper printActionHelper;
	
	/**
	 * The <code>PrintPreviewHelper</code> on which <code>doPrintPreview()</code> is called.
	 */
	private PrintPreviewHelper printPreviewHelper;

	/**
	 * Creates a new instance.
	 * 
	 * @param printActionHelper
	 *            the helper class used to show the print settings dialog and do
	 *            a print if the user were to try to print from within the print
	 *            preview dialog.
	 * @param printPreviewHelper
	 *            the helper class that creates that creates the print preview
	 *            dialog
	 */
	protected PrintPreviewAction(IPrintActionHelper printActionHelper, PrintPreviewHelper printPreviewHelper) {
		setId(ID);
		setText(DiagramUIPrintingMessages.PrintPreview_ActionLabel);
		setPrintActionHelper(printActionHelper);
		setPrintPreviewHelper(printPreviewHelper);
	}
	
	/**
	 * Creates a new instance.
	 * 
	 * @param printActionHelper
	 *            the helper class used to show the print settings dialog and
	 *            perform the actual printing if the user were to print from
	 *            within the print preview dialog.
	 */
	public PrintPreviewAction(IPrintActionHelper printActionHelper) {
		this(printActionHelper, new PrintPreviewHelper());
	}
	
	/**
	 * Opens a dialog showing how the diagram will look when printed. Uses the
	 * print preview helper and optionally the print action helper.
	 */
	public void run() {
		getPrintPreviewHelper().doPrintPreview(getPrintActionHelper());
	}

	/**
	 * Return the print action helper that helps perform the print action in
	 * the print preview dialog.
	 * @return IPrintActionHelper the print action helper.
	 */
	private IPrintActionHelper getPrintActionHelper() {
		return printActionHelper;
	}

	/**
	 * Set the print action helper that helps perform the print action in the
	 * print preview dialog.
	 * @param printActionHelper the IPrintActionHelper
	 */
	protected void setPrintActionHelper(IPrintActionHelper printActionHelper) {
		this.printActionHelper = printActionHelper;
	}

	/**
	 * Return the print preview helper responsible for performing the print
	 * preview.
	 * @return PrintPreviewHelper the print preview helper.
	 */
	private PrintPreviewHelper getPrintPreviewHelper() {
		return printPreviewHelper;
	}

	/**
	 * Set the print preview helper responsible for performing the print
	 * preview.
	 * @return printPreviewHelper the PrintPreviewHelper
	 */
	protected void setPrintPreviewHelper(PrintPreviewHelper printPreviewHelper) {
		this.printPreviewHelper = printPreviewHelper;
	}    
    
}