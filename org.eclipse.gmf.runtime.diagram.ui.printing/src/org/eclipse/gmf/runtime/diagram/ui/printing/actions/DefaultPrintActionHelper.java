/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.printing.actions;

import java.util.Collections;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.DiagramPrinter;

/**
 * Provides basic printing functionality. This does a print from a default print
 * dialog.
 * 
 * @author wdiu
 */
public class DefaultPrintActionHelper {

	/**
	 * Prints the diagram after a default print dialog is opened for the user.
	 * 
	 * @param editorPart
	 *            the IEditorPart containing the diagram to print
	 * @param diagramPrinter
	 *            the diagram printer that does the work of actually printing
	 *            the diagrams
	 */
	public static void doRun(IEditorPart editorPart, DiagramPrinter diagramPrinter) {
		// print the editor contents.
		final PrintDialog dialog =
			new PrintDialog(editorPart.getSite().getShell(), SWT.NULL);
		final PrinterData data = dialog.open();

		if (data == null) {
			return;
		}

		boolean isPrintToFit = MessageDialog.openQuestion(null,
			PresentationResourceManager
				.getI18NString("Print.MessageDialogTitle"), //$NON-NLS-1$
			PresentationResourceManager
				.getI18NString("Print.MessageDialogMessage")); //$NON-NLS-1$

		final Printer printer = new Printer(data);

		diagramPrinter.setPrinter(printer);
		diagramPrinter.setDisplayDPI(Display.getDefault().getDPI());
		DiagramEditPart dgrmEP = ((IDiagramWorkbenchPart) editorPart)
			.getDiagramEditPart();
		if (dgrmEP == null) {
			return;
		}
		diagramPrinter.setDiagrams(Collections.singletonList(dgrmEP
			.getDiagramView().getDiagram()));

		if (isPrintToFit) {
			diagramPrinter.setColumns(1);
			diagramPrinter.setRows(1);
		} else {
			diagramPrinter.setScaledPercent(100);
		}

		diagramPrinter.run();
		printer.dispose();
	}

	/**
	 * Private constructor prevents instantiation
	 */
	private DefaultPrintActionHelper() {
		//utility class
	}
}
