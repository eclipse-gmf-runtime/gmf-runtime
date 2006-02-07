/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.render.actions;

import org.eclipse.gmf.runtime.common.ui.action.actions.IPrintActionHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.actions.PrintPreviewAction;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.internal.printpreview.RenderedPrintPreviewHelper;

/**
 * This is the action for rendered print preview. It opens a dialog showing how
 * the diagram will look when printed. The <code>PrintPreviewHelper</code>
 * passed into the constructor does the actual work of the print preview. The
 * <code>IPrintActionHelper</code> passed into the constructor does the actual
 * work of showing the print settings dialog and doing the print if the user
 * were to initiate a print from within the print preview dialog.
 * 
 * @author Wayne Diu, wdiu
 */
public class RenderedPrintPreviewAction extends PrintPreviewAction {
	
	/**
	 * Creates a new instance.
	 * 
	 * @param printActionHelper
	 *            the helper class used to show the print settings dialog and
	 *            perform the actual printing if the user were to print from
	 *            within the print preview dialog.
	 */
	public RenderedPrintPreviewAction(IPrintActionHelper printActionHelper) {
		super(printActionHelper, new RenderedPrintPreviewHelper());
	}

}