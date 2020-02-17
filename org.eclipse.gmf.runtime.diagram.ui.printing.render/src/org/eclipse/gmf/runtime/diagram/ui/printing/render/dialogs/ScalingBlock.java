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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintOptions;
import org.eclipse.jface.databinding.swt.SWTObservables;
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

	ScalingBlock(IDialogUnitConverter dluConverter,
			DataBindingContext bindings, PrintOptions options) {
		super(dluConverter);

		this.bindings = bindings;
		this.options = options;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.printing.internal.dialogs.DialogBlock#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent) {
		final Realm realm = bindings.getValidationRealm();

		Composite result = group(parent,
				DiagramUIPrintingMessages.JPSPrintDialog_Scaling);
		layout(result, 5);

		Button adjustRadio = radio(result,
				DiagramUIPrintingMessages.JPSPrintDialog_Adjust);
		layoutSpanHorizontal(adjustRadio, 2);
		Text textScale = text(result, 20);
		layoutSpanHorizontal(blank(result), 2);

		final IObservableValue scalingValue = BeansObservables.observeValue(
				realm, options, PrintOptions.PROPERTY_PERCENT_SCALING);
		
		bindings.bindValue(SWTObservables.observeSelection(adjustRadio),
				scalingValue, null, null);

		bindings.bindValue(SWTObservables.observeText(textScale, SWT.Modify),
				BeansObservables.observeValue(realm, options,
						PrintOptions.PROPERTY_SCALE_FACTOR), null, null);
		bindings.bindValue(SWTObservables.observeEnabled(textScale),
				scalingValue, null, null);

		Button fitToRadio = radio(result,
				DiagramUIPrintingMessages.JPSPrintDialog_FitTo);
		
		IObservableValue fitToValue = new ComputedValue(realm) {
			protected Object calculate() {
				return Boolean.valueOf(!((Boolean) scalingValue.getValue())
						.booleanValue());
			}
		};
		
		bindings.bindValue(SWTObservables.observeSelection(fitToRadio),
				fitToValue, null, null);

		layoutHorizontalIndent(layoutAlignRight(label(result,
				DiagramUIPrintingMessages.JPSPrintDialog_PagesWide)));
		
		Text textWide = text(result, 20);
		
		layoutHorizontalIndent(layoutAlignRight(label(result,
				DiagramUIPrintingMessages.JPSPrintDialog_PagesTall)));
		Text textTall = text(result, 20);

		bindings.bindValue(SWTObservables.observeText(textWide, SWT.Modify),
				BeansObservables.observeValue(realm, options,
						PrintOptions.PROPERTY_FIT_TO_WIDTH), null, null);
		
		bindings.bindValue(SWTObservables.observeEnabled(textWide), fitToValue,
				null, null);
		
		bindings.bindValue(SWTObservables.observeText(textTall, SWT.Modify),
				BeansObservables.observeValue(realm, options,
						PrintOptions.PROPERTY_FIT_TO_HEIGHT), null, null);
		
		bindings.bindValue(SWTObservables.observeEnabled(textTall), fitToValue,
				null, null);

		return result;
	}
	
	@Override
	public void dispose() {
		// nothing special to dispose currently	
	}
}
