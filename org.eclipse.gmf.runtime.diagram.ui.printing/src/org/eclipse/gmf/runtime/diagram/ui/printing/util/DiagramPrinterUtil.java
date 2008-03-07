/******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.util;

import java.util.Map;

import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.SWTDiagramPrinter;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.SWTDiagramPrinterHelper;
import org.eclipse.ui.IEditorPart;

/**
 * Utility for using the DiagramPrinter to print diagrams after displaying
 * a print dialog box to the user.  Diagrams are printed using the
 * DiagramPrinter and respect the settings chosen by the user in the print
 * dialog. 
 * 
 * This class now delegates its functionality to helpers that isolate out the 
 * printing via SWT printing. 
 * 
 * This class is subject to change/deprecation in phase 2 of the printing enhancements.
 * 
 * @author wdiu, Wayne Diu
 */
public class DiagramPrinterUtil {

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
			Map diagramMap, SWTDiagramPrinter diagramPrinter) {

		SWTDiagramPrinterHelper.getDiagramPrinterHelper().printWithSettings(
				editorPart, diagramMap, diagramPrinter);
	}
}