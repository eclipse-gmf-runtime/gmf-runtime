/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.render.dialogs;

import org.eclipse.gmf.runtime.common.ui.action.actions.IPrintActionHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.printpreview.PrintPreviewHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.PrintHelperUtil;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.actions.EnhancedPrintActionHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.internal.printpreview.RenderedPrintPreviewHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintOptions;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A section of the JPS print dialog that handles extra actions. In this case,
 * we contribute print preview capabilities.
 * 
 * @author Christian Damus (cdamus)
 * @author James Bruck (jbruck)
 */
class ActionsBlock extends DialogBlock {
	private final PrintOptions options;

	private Button printPreview;
	private PrintPreviewHelper printPreviewHelper;
	private IPrintActionHelper printActionHelper;

	ActionsBlock(IDialogUnitConverter dluConverter, PrintOptions options) {
		super(dluConverter);
		this.options = options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.printing.internal.dialogs.DialogBlock#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent) {
		printPreview = new Button(parent, SWT.PUSH);
		printPreview.setData(new Integer(2));
		printPreview
				.setText(DiagramUIPrintingMessages.JPSPrintDialog_Button_PrintPreview);

		printPreview.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				buttonPressed(((Integer) event.widget.getData()).intValue());
			}
		});
		layoutVerticalIndent(layoutAlignLeft(printPreview));

		return printPreview;
	}

	/**
	 * Bring up the print preview with printing disabled.
	 * 
	 * @param buttonId
	 */
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case -1:
			break;
		default:
			PrintPreviewHelper previewHelper = getPrintPreviewHelper();
						
			PrintHelperUtil.setScale(options.getScaleFactor());
			PrintHelperUtil.setScaleToWidth(options.getFitToPagesWidth());
			PrintHelperUtil.setScaleToHeight(options.getFitToPagesHeight());
			previewHelper.enablePrinting(false);
			
			if (options.isPercentScaling()) {
				previewHelper.setPercentScaling(options.getScaleFactor());
			} else {
				previewHelper.setFitToPage(options.getFitToPagesWidth(),
						options.getFitToPagesHeight());
			}
			
			previewHelper.doPrintPreview(getPrintActionHelper());
			
			options.setScaleFactor(PrintHelperUtil.getScale());
			options.setFitToPagesWidth(PrintHelperUtil.getScaleToWidth());
			options.setFitToPagesHeight(PrintHelperUtil.getScaleToHeight());
		}
	}

	/**
	 * Return the print preview helper responsible for performing the print
	 * preview.
	 * 
	 * @return PrintPreviewHelper the print preview helper.
	 */
	private PrintPreviewHelper getPrintPreviewHelper() {
		if (printPreviewHelper == null) {
			printPreviewHelper = new RenderedPrintPreviewHelper();
		}
		return printPreviewHelper;
	}

	private IPrintActionHelper getPrintActionHelper() {
		if (printActionHelper == null) {
			printActionHelper = new EnhancedPrintActionHelper();
		}
		return printActionHelper;
	}

	@Override
	public void dispose() {
		// nothing special to dispose
	}

}
