/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/



package org.eclipse.gmf.runtime.diagram.ui.printing.render.util;

import java.util.List;

import org.eclipse.gmf.runtime.common.ui.printing.IPrintHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.dialogs.JPSPrintDialog;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintOptions;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.ui.PlatformUI;

/**
 * Default implementation of a print-helper.
 * 
 * @author Christian W. Damus (cdamus)
 */
public class PrintHelper implements IPrintHelper {
	private final PrintOptions options = new PrintOptions();

	public PrintHelper() {
		initPrintOptions();
	}

	/**
	 * Initialize the default options.
	 */
	private void initPrintOptions() {
		options.setPercentScaling(true);
		options.setScaleFactor(100);
		options.setFitToPagesWidth(1);
		options.setFitToPagesHeight(1);

		options.setAllPages(true);
		options.setRangeFrom(1);
		options.setRangeTo(1);

		options.setCopies(1);
		options.setCollate(false);
		
		options.setQualityHigh(true);
		options.setSideOneSided(true);
		options.setChromaticityColor(true);
				
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.printing.IPrintHelper#openPrintDlg(java.util.List)
	 */
	public PrinterData openPrintDlg(List diagramList) {
		PrinterData result = null;
		JPSPrintDialog dlg = new JPSPrintDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow(), options);

		if (dlg.open() == IDialogConstants.OK_ID) {
			if (options.getDestination() != null) {
				result = options.getDestination().getPrinterData();
			}
		} else {
			// revert
			initPrintOptions();
		}
		return result;
	}

	public boolean getDlgCollate() {
		return options.isCollate();
	}

	public int getDlgNumberOfCopies() {
		return options.getCopies();
	}

	public int getDlgPagesFrom() {
		return options.getRangeFrom();
	}

	public int getDlgPagesTo() {
		return options.getRangeTo();
	}

	public boolean getDlgPrintRangeAll() {
		return options.isAllPages();
	}

	public boolean getDlgPrintRangePages() {
		return !getDlgPrintRangeAll();
	}

	public int getDlgScaleFitToM() {
		return options.getFitToPagesWidth();
	}

	public int getDlgScaleFitToN() {
		return options.getFitToPagesHeight();
	}

	public int getDlgScalePercent() {
		return options.isPercentScaling() ? options.getScaleFactor() : -1;
	}

	public void setDlgOrientation(boolean landscape) {
		// TODO Auto-generated method stub
	}

	public void setDlgPaperSize(int index, double width, double length) {
		// TODO Auto-generated method stub
	}

	public boolean getDlgDiagramPrintRangeCurrent() {
		// TODO Not supported by the JPS dialog
		return true;
	}

	public boolean getDlgDiagramPrintRangeSelection() {
		// TODO Not supported by the JPS dialog
		return false;
	}

	public boolean isDlgDiagramSelected(int index) {
		// TODO Not supported by the JPS dialog
		return false;
	}

	public boolean getDlgDiagramPrintRangeAll() {
		// TODO Not supported by the JPS dialog
		return false;
	}
	
	public PrintOptions getPrintOptions(){
		return options;
	}

}
