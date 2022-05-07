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
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintOptions;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;


/**
 * A section of the JPS print dialog that adds scaling support.
 * 
 * @author Christian Damus (cdamus)
 * @author James Bruck (jbruck)
 */
class ScalingBlock extends DialogBlock {
	private final DataBindingContext bindings;
	private final PrintOptions options;

	ScalingBlock(IDialogUnitConverter dluConverter, DataBindingContext bindings, PrintOptions options) {
		super(dluConverter);

		this.bindings = bindings;
		this.options = options;
	}

	@Override
	public Control createContents(Composite parent) {
		final Realm realm = bindings.getValidationRealm();

		Composite result = group(parent, DiagramUIPrintingMessages.JPSPrintDialog_Scaling);
		layout(result, 5);

		Button adjustRadio = radio(result, DiagramUIPrintingMessages.JPSPrintDialog_Adjust);
		layoutSpanHorizontal(adjustRadio, 2);
		Text textScale = text(result, 20);
		layoutSpanHorizontal(blank(result), 2);

		final IObservableValue<Object> scalingValue = BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_PERCENT_SCALING).observe(realm, options);
		
		bindings.bindValue(WidgetProperties.buttonSelection().observe(adjustRadio), scalingValue);

		bindings.bindValue(
		        WidgetProperties.text(SWT.Modify).observe(textScale),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_SCALE_FACTOR).observe(realm, options));
		bindings.bindValue(WidgetProperties.enabled().observe(textScale), scalingValue);

		Button fitToRadio = radio(result, DiagramUIPrintingMessages.JPSPrintDialog_FitTo);
		
		IObservableValue<Boolean> fitToValue = new ComputedValue<>(realm) {
			protected Boolean calculate() {
				return !((Boolean) scalingValue.getValue());
			}
		};
		
		bindings.bindValue(WidgetProperties.buttonSelection().observe(fitToRadio), fitToValue);

		layoutHorizontalIndent(layoutAlignRight(label(result, DiagramUIPrintingMessages.JPSPrintDialog_PagesWide)));
		
		Text textWidget = text(result, 20);
		
		layoutHorizontalIndent(layoutAlignRight(label(result, DiagramUIPrintingMessages.JPSPrintDialog_PagesTall)));
		Text textTall = text(result, 20);

		bindings.bindValue(
		        WidgetProperties.text(SWT.Modify).observe(textWidget),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_FIT_TO_WIDTH).observe(realm, options));
		
		bindings.bindValue(WidgetProperties.enabled().observe(textWidget), fitToValue);
		
		bindings.bindValue(
		        WidgetProperties.text(SWT.Modify).observe(textTall),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_FIT_TO_HEIGHT).observe(realm, options));
		
		bindings.bindValue(WidgetProperties.enabled().observe(textTall), fitToValue);

		return result;
	}
	
	@Override
	public void dispose() {
		// nothing special to dispose currently	
	}
}
