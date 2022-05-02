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

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintOptions;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * A section of the options print dialog that handles the print quality.
 * 
 * @author James Bruck (jbruck)
 */
public class QualityBlock extends DialogBlock {

	private final DataBindingContext bindings;
	private final PrintOptions options;
	
	private Binding qualityHighBinding;
	private Binding qualityMedBinding;
	private Binding qualityLowBinding;

	QualityBlock(IDialogUnitConverter dluConverter,
			DataBindingContext bindings, PrintOptions options) {
		super(dluConverter);

		this.bindings = bindings;
		this.options = options;
	}

	@Override
	public Control createContents(Composite parent) {
		final Realm realm = bindings.getValidationRealm();

		Composite result = group(parent, DiagramUIPrintingMessages.JPSOptionsDialog_Quality);
		layout(result, 2);

		Button highRadio = radio(result, DiagramUIPrintingMessages.JPSOptionsDialog_QualityHigh);
		layoutSpanHorizontal(highRadio, 4);

		Button mediumRadio = radio(result, DiagramUIPrintingMessages.JPSOptionsDialog_QualityMedium);
		layoutSpanHorizontal(mediumRadio, 4);

		Button lowRadio = radio(result, DiagramUIPrintingMessages.JPSOptionsDialog_QualityLow);
		layoutSpanHorizontal(lowRadio, 4);

		qualityHighBinding = bindings.bindValue(
		        WidgetProperties.buttonSelection().observe(highRadio),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_QUALITY_HIGH).observe(realm, options), null, null);

		qualityMedBinding = bindings.bindValue(
		        WidgetProperties.buttonSelection().observe(mediumRadio),
                BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_QUALITY_MED).observe(realm, options), null, null);

		qualityLowBinding = bindings.bindValue(
		        WidgetProperties.buttonSelection().observe(lowRadio),
                BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_QUALITY_LOW).observe(realm, options), null, null);

		return result;
	}
	
	@Override
	public void dispose() {
		bindings.removeBinding(qualityHighBinding);
		qualityHighBinding.dispose();
		bindings.removeBinding(qualityLowBinding);
		qualityLowBinding.dispose();
		bindings.removeBinding(qualityMedBinding);
		qualityMedBinding.dispose();
	}
}
