/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.printing.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.common.ui.printing.IPrintHelper;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.printing.actions.DefaultPrintActionHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.DiagramPrintingDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.DiagramPrintingPlugin;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.DiagramPrintingStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramPrintingResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.DiagramPrinter;
import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * Utility for using the DiagramPrinter to print diagrams after displaying
 * a print dialog box to the user.  Diagrams are printed using the
 * DiagramPrinter and respect the settings chosen by the user in the print
 * dialog. 
 * 
 * @author wdiu, Wayne Diu
 */
public class DiagramPrinterUtil {

	/**
	 * Prints a diagram with the settings from the helper onto the printer
	 * 
	 * @param diagramPrinter
	 *            the diagram printer that does the work of actually printing the diagrams
	 * @param helper
	 *            IPrintHelper with the user's choice of settings
	 */
	private static void printDiagrams(DiagramPrinter diagramPrinter,
			IPrintHelper helper) {

		if (helper.getDlgScalePercent() == -1) {
			diagramPrinter.setRows(helper.getDlgScaleFitToM());
			diagramPrinter.setColumns(helper.getDlgScaleFitToN());
		} else {
			diagramPrinter.setScaledPercent(helper.getDlgScalePercent());
		}

		diagramPrinter.run();
	}

	/**
	 * Opens up the print diagrams dialog, allows the user to choose the
	 * settings, and prints.
	 * 
	 * @param editorPart
	 *            current editor part
	 * @param diagramMap
	 *            map of String names to Diagram objects. Should be initialized
	 *            by caller of this method. String names will show up in the
	 *            print dialog that allows the user to choose which diagrams to
	 *            print from a list.
	 * @param diagramPrinter
	 *            the diagram printer that does the work of actually printing the diagrams
	 */
	public static void printWithSettings(IEditorPart editorPart,
			Map diagramMap, DiagramPrinter diagramPrinter) {
		
		List diagramNames;
		PrinterData printerData;
		IPrintHelper helper;
		
		try {
			Class printhelperClass = Class
				.forName(IPrintHelper.PRINT_HELPER_CLASS_NAME);
			helper = (IPrintHelper) printhelperClass.newInstance();
			diagramNames = new ArrayList(diagramMap.keySet());
			printerData = helper.openPrintDlg(diagramNames);
		}
		catch (Throwable e) {
			//if there's a problem with the fragment, try doing the default
			//this is better than nothing
			Trace.catching(
				DiagramPrintingPlugin.getInstance(),
				DiagramPrintingDebugOptions.EXCEPTIONS_CATCHING,
				DiagramPrinterUtil.class,
				e.getMessage(),
				e);
			Log.warning(
				DiagramPrintingPlugin.getInstance(),
				DiagramPrintingStatusCodes.RESOURCE_FAILURE,
				e.getMessage(),
				e);

			if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), DiagramPrintingResourceManager.getI18NString("DiagramPrinterUtil.DLLErrorTitle"), //$NON-NLS-1$
				DiagramPrintingResourceManager.getI18NString("DiagramPrinterUtil.DLLErrorMessage.part1") //$NON-NLS-1$
				+"\n" //$NON-NLS-1$
				+DiagramPrintingResourceManager.getI18NString("DiagramPrinterUtil.DLLErrorMessage.part2") //$NON-NLS-1$
				+"\n" //$NON-NLS-1$
				+DiagramPrintingResourceManager.getI18NString("DiagramPrinterUtil.DLLErrorMessage.part3") //$NON-NLS-1$
				+"\n\n" //$NON-NLS-1$
				+DiagramPrintingResourceManager.getI18NString("DiagramPrinterUtil.DLLErrorMessage.part4"))) { //$NON-NLS-1$
					DefaultPrintActionHelper.doRun(editorPart, diagramPrinter);
				}

			//do not continue
			return;
		}
		
		Assert.isNotNull(diagramNames);
		Assert.isNotNull(helper);

		if (printerData != null) { //ok pressed

			//uncomment this code for debug information
			/*if (helper.getDlgDiagramPrintRangeAll()) {
			    Trace.trace(ModelerPlugin.getInstance(), "All diagrams");
			} else if (helper.getDlgDiagramPrintRangeCurrent()) {
			    Trace.trace(ModelerPlugin.getInstance(), "Current diagram");
			} else if (helper.getDlgDiagramPrintRangeSelection()) {
			    Trace.trace(ModelerPlugin.getInstance(), "Selected diagrams");
			}
			
			if (helper.getDlgScalePercent() != -1) {
			    Trace.trace(ModelerPlugin.getInstance(), 
			        "Scale to " + helper.getDlgScalePercent() + " percent");
			} else {
			    Trace.trace(ModelerPlugin.getInstance(), 
			        "Scale to "
			            + helper.getDlgScaleFitToM()
			            + "x"
			            + helper.getDlgScaleFitToN()
			            + " pages");
			}
			
			if (helper.getDlgPrintRangeAll()) {
			    Trace.trace(ModelerPlugin.getInstance(), "All");
			}
			if (helper.getDlgPrintRangePages()) {
			    Trace.trace(ModelerPlugin.getInstance(), 
			        "Pages " + helper.getDlgPagesFrom() + " to " + helper.getDlgPagesTo());
			}
			
			Trace.trace(ModelerPlugin.getInstance(), helper.getDlgNumberOfCopies() + " copies");
			
			if (helper.getDlgCollate()) {
			    Trace.trace(ModelerPlugin.getInstance(), "Collate");
			} else {
			    Trace.trace(ModelerPlugin.getInstance(), "Do not collate");
			}*/
			
			final Printer printer = new Printer(printerData);
			
			diagramPrinter.setPrinter(printer);
			diagramPrinter.setDisplayDPI(Display.getDefault().getDPI());

			if (helper.getDlgDiagramPrintRangeCurrent()) {
				DiagramEditPart dgrmEP = ((IDiagramWorkbenchPart) editorPart).getDiagramEditPart();
				assert dgrmEP != null;
				diagramPrinter.setDiagrams(Collections.singletonList(dgrmEP.getDiagramView().getDiagram()));
			} else if (helper.getDlgDiagramPrintRangeAll()) {
				diagramPrinter.setDiagrams(diagramMap.values());
			} else if (helper.getDlgDiagramPrintRangeSelection()) {
				Object obj;
				List list = new ArrayList();
				for (int i = 0; i < diagramNames.size(); i++) {
					//is the diagram selected?
					//we are only printing the selected ones
					if (helper.isDlgDiagramSelected(i)) {
						obj = diagramMap.get(diagramNames.get(i));
						Assert.isTrue(obj instanceof Diagram);

						list.add(obj);
					}
				}
				diagramPrinter.setDiagrams(list);
			}
				
			printDiagrams(diagramPrinter, helper);

			printer.dispose();
		}

	}
}