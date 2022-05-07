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
 * A section of the JPS print dialog that adds range checking.
 * 
 * @author Christian Damus (cdamus)
 * @author James Bruck (jbruck)
 */
class RangeBlock extends DialogBlock {
	private final DataBindingContext bindings;
	private final PrintOptions options;

	RangeBlock(IDialogUnitConverter dluConverter, DataBindingContext bindings, PrintOptions options) {
		super(dluConverter);
		this.bindings = bindings;
		this.options = options;
	}

	@Override
	public Control createContents(Composite parent) {
		final Realm realm = bindings.getValidationRealm();

		Composite result = group(parent, DiagramUIPrintingMessages.JPSPrintDialog_PrintRange);
		layout(result, 4);

		Button allRadio = radio(result, DiagramUIPrintingMessages.JPSPrintDialog_All);
		layoutSpanHorizontal(allRadio, 4);

		bindings.bindValue(
		        WidgetProperties.buttonSelection().observe(allRadio),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_ALL_PAGES).observe(realm, options));

		Button rangeRadio = radio(result, DiagramUIPrintingMessages.JPSPrintDialog_Pages);
		layoutSpanHorizontal(rangeRadio, 4);

		IObservableValue<Boolean> rangeValue = new ComputedValue<>(realm) {
			protected Boolean calculate() {
				IObservableValue<Object> observableValue = BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_ALL_PAGES).observe(realm, options);
                return !((Boolean) observableValue.getValue());
			}
		};
		bindings.bindValue(WidgetProperties.buttonSelection().observe(rangeRadio), rangeValue);

		layoutHorizontalIndent(label(result, DiagramUIPrintingMessages.JPSPrintDialog_From));
		Text textFrom = text(result, 20);
		
		layoutHorizontalIndent(label(result, DiagramUIPrintingMessages.JPSPrintDialog_To));
		Text textTo = text(result, 20);

		bindings.bindValue(
		        WidgetProperties.text(SWT.Modify).observe(textFrom),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_RANGE_FROM).observe(realm, options));
		bindings.bindValue(WidgetProperties.enabled().observe(textFrom), rangeValue);
		
		bindings.bindValue(WidgetProperties.text(SWT.Modify).observe(textTo),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_RANGE_TO).observe(realm, options));
		bindings.bindValue(WidgetProperties.enabled().observe(textTo), rangeValue);

		return result;
	}
	
	@Override
	public void dispose() {
		// nothing special to dispose currently		
	}
}
