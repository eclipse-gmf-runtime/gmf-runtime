/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.printing;

import java.util.List;

import org.eclipse.swt.printing.PrinterData;

/**
 * The IPrintHelper is an interface for the platform specfic print fragments.
 * The print fragments may have these methods declared in the interface as
 * native methods, then implement this interface as wrappers around the
 * native methods.
 * 
 * @author wdiu, Wayne Diu
 */
public interface IPrintHelper {
	/**
	 * The location where I expect the print helper to be implemented.
	 * This should be in a fragment, e.g. org.eclipse.gmf.runtime.common.ui.printing.operatingsystem
	 */
	public static final String PRINT_HELPER_CLASS_NAME = "org.eclipse.gmf.runtime.common.ui.printing.PrintHelper"; //$NON-NLS-1$

	/**
	 * Opens the print dialog with the diagram list which could be null
	 * 
	 * @param diagramList the List of Diagram objects.  diagramList can
	 * be null.
	 * @return <code>PrinterData</code>
	 */
	PrinterData openPrintDlg(List diagramList);

	/**
	 * If not > 0 then user has clicked percent scaling
	 * 
	 * @return scale to m int
	 */
	int getDlgScaleFitToM();

	/**
	 * If not > 0 then user has clicked percent scaling
	 * 
	 * @return scale to n int
	 */
	int getDlgScaleFitToN();

	/**
	 * If not > 0 then user has clicked fit to m x n scaling
	 * 
	 * @return scale percent int
	 */
	int getDlgScalePercent();

	/**
	 * Returns true if the diagram at the index was selected
	 * 
	 * @param index the index of the diagrams you passed in
	 * @return true if selected, false if not selected
	 */
	boolean isDlgDiagramSelected(int index);

	//one of the three is unnecssary, but it makes more sense
	/**
	 * Returns if diagram print range all was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	boolean getDlgDiagramPrintRangeAll();

	/**
	 * Returns if diagram print range current was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	boolean getDlgDiagramPrintRangeCurrent();

	/**
	 * Returns if diagram print range selection was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	boolean getDlgDiagramPrintRangeSelection();

	/**
	 * Returns if print range all was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	boolean getDlgPrintRangeAll();

	/**
	 * Returns if print range pages was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	boolean getDlgPrintRangePages();

	/**
	 * Returns if collate was selected
	 * 
	 * @return true if selected, false if not selected
	 */
	boolean getDlgCollate();

	/**
	 * Returns the pages from int value, check if (getDlgPrintRangesPages())
	 * first.
	 * 
	 * @return int of the pages from box
	 */
	int getDlgPagesFrom();

	/**
	 * Returns the pages from int value, check if (getDlgPrintRangesPages())
	 * first.
	 * 
	 * @return int of the pages to box
	 */
	int getDlgPagesTo();

	/**
	 * Returns the number of copies the user wants to print.
	 * This is nCopies from PRINTDLG
	 * 
	 * @return int with the number of copies
	 */
	int getDlgNumberOfCopies();
}
