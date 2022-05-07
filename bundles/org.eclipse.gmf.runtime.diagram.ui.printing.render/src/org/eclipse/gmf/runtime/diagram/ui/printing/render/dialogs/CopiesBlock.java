/******************************************************************************
 * Copyright (c) 2008, 2022 IBM Corporation and others.
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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintOptions;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * A section of the JPS print dialog that handles the number of copies of a
 * diagram to print.
 * 
 * @author Christian Damus (cdamus)
 * @author James Bruck (jbruck)
 */
class CopiesBlock extends DialogBlock {

	private final DataBindingContext bindings;
	private final PrintOptions options;

	private final Image collateOnImage = DiagramUIPrintingPluginImages.COLLATE_ON
			.createImage();
	private final Image collateOffImage = DiagramUIPrintingPluginImages.COLLATE_OFF
			.createImage();
	
	CopiesBlock(IDialogUnitConverter dluConverter, DataBindingContext bindings,
			PrintOptions options) {
		super(dluConverter);

		this.bindings = bindings;
		this.options = options;
	}

	@Override
	public Control createContents(Composite parent) {
		final Realm realm = bindings.getValidationRealm();

		Composite result = group(parent,
				DiagramUIPrintingMessages.JPSPrintDialog_Copies);
		layout(result, 2);

		label(result, DiagramUIPrintingMessages.JPSPrintDialog_NumberOfCopies);
		Spinner copiesSpinner = spinner(result, 1, 999);

		bindings.bindValue(WidgetProperties.spinnerSelection().observe(copiesSpinner),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_COPIES).observe(realm, options), null, null);

		final Label collateImageButton = new Label(result, SWT.CENTER
				| SWT.SHADOW_NONE);

		layoutAlignRight(collateImageButton);
		collateImageButton.setImage(collateOffImage);

		Button collateCheck = check(result,
				DiagramUIPrintingMessages.JPSPrintDialog_Collate);

		bindings.bindValue(WidgetProperties.buttonSelection().observe(collateCheck),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_COLLATE).observe(realm, options), null, null);

		collateCheck.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent arg0) {
				// do nothing
			}
			public void widgetSelected(SelectionEvent arg0) {
				if (options.isCollate()) {
					collateImageButton.setImage(collateOnImage);
				} else {
					collateImageButton.setImage(collateOffImage);
				}
			}
		});

		return result;
	}
		
	/**
	 * Dispose of images.
	 */
	public void dispose() {
		collateOnImage.dispose();
		collateOffImage.dispose();
	}

}
