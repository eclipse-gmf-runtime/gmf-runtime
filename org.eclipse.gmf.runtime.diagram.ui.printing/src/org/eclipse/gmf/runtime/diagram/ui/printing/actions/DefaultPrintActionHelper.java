/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.actions;

import java.util.Collections;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.SWTDiagramPrinter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.ui.IEditorPart;

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
	public static void doRun(IEditorPart editorPart, SWTDiagramPrinter diagramPrinter) {
		// print the editor contents.
		final PrintDialog dialog =
			new PrintDialog(editorPart.getSite().getShell(), SWT.NULL);
		final PrinterData data = dialog.open();

		if (data == null) {
			return;
		}

		boolean isPrintToFit = MessageDialog.openQuestion(null,
			DiagramUIPrintingMessages.Print_MessageDialogTitle, 
			DiagramUIPrintingMessages.Print_MessageDialogMessage); 

		final Printer printer = new Printer(data);

		diagramPrinter.setPrinter(printer);
		diagramPrinter.setDisplayDPI(dialog.getParent().getDisplay().getDPI());
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
