/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.actions;

import org.eclipse.ui.IWorkbenchPart;

/**
 * An interface to implement for bringing up the print dialog.
 * 
 * See ModelerPrintActionHelper for an explanation on why I use it for print
 * preview.
 * 
 * @author Wayne Diu, wdiu
 */
public interface IPrintActionHelper {

	/**
	 * The method that brings up the print dialog
	 * 
	 * @param workbenchPart the workbench part containing the diagram to
	 * print
	 */
	public void doPrint(IWorkbenchPart workbenchPart);
}
