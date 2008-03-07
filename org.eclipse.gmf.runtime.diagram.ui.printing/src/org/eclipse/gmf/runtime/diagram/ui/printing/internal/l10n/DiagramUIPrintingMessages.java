/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * An accessor class for externalized strings.
 *
 * @author cmahoney
 */
public final class DiagramUIPrintingMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages";//$NON-NLS-1$

	private DiagramUIPrintingMessages() {
		// Do not instantiate
	}

	public static String PrintPreview_Title;
	public static String PrintPreview_NotEnabled;
	public static String PrintPreview_PagesToolItem;
	public static String PrintPreview_PrintToolItem;
	public static String PrintPreview_LeftToolItem;
	public static String PrintPreview_RightToolItem;
	public static String PrintPreview_UpToolItem;
	public static String PrintPreview_DownToolItem;
	public static String PrintPreview_CloseToolItem;
	public static String PrintPreview_1Up;
	public static String PrintPreview_2Up;
	public static String PrintPreview_4Up;
	public static String PrintPreview_ActionLabel;
	public static String PrintPreview_NoPrinterInstalled;
	public static String Print_MessageDialogTitle;
	public static String Print_MessageDialogMessage;
	public static String DiagramPrinterUtil_DLLErrorTitle;
	public static String DiagramPrinterUtil_DLLErrorMessage_part1;
	public static String DiagramPrinterUtil_DLLErrorMessage_part2;
	public static String DiagramPrinterUtil_DLLErrorMessage_part3;
	public static String DiagramPrinterUtil_DLLErrorMessage_part4;
	public static String JPSPrintDialog_Title;
	public static String JPSPrintDialog_Button_PrintPreview;
	public static String JPSPrintDialog_Copies;
	public static String JPSPrintDialog_NumberOfCopies;
	public static String JPSPrintDialog_Collate;
	public static String JPSPrintDialog_Printer;
	public static String JPSPrintDialog_Name;
	public static String JPSPrintDialog_Properties;
	public static String JPSPrintDialog_PrintToFile;
	public static String JPSPrintDialog_PrintRange;
	public static String JPSPrintDialog_All;
	public static String JPSPrintDialog_Pages;
	public static String JPSPrintDialog_From;
	public static String JPSPrintDialog_To;
	public static String JPSPrintDialog_Scaling;
	public static String JPSPrintDialog_Adjust;
	public static String JPSPrintDialog_FitTo;
	public static String JPSPrintDialog_PagesWide;
	public static String JPSPrintDialog_PagesTall;
	public static String JPSDiagramPrinterUtil_ErrorTitle;
	public static String JPSDiagramPrinterUtil_ErrorMessage;
				
	static {
		NLS.initializeMessages(BUNDLE_NAME, DiagramUIPrintingMessages.class);
	}
}