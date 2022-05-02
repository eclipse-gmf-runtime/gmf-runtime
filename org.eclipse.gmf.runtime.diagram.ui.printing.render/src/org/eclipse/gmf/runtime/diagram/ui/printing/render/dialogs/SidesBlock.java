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
 * A section of the options print dialog that handles the duplex/single sided printing.
 * 
 * @author James Bruck (jbruck)
 */
public class SidesBlock extends DialogBlock {

	private final DataBindingContext bindings;
	private final PrintOptions options;
	private Binding oneSidedBinding;
	private Binding tumbleBinding;
	private Binding duplexBinding;

	SidesBlock(IDialogUnitConverter dluConverter, DataBindingContext bindings,
			PrintOptions options) {
		super(dluConverter);

		this.bindings = bindings;
		this.options = options;
	}

	@Override
	public Control createContents(Composite parent) {
		final Realm realm = bindings.getValidationRealm();

		Composite result = group(parent, DiagramUIPrintingMessages.JPSOptionsDialog_Sides);
		layout(result, 2);

		Button oneSideRadio = radio(result, DiagramUIPrintingMessages.JPSOptionsDialog_SidesOneSided);
		layoutSpanHorizontal(oneSideRadio, 4);

		Button tumbleRadio = radio(result, DiagramUIPrintingMessages.JPSOptionsDialog_SidesTumble);
		layoutSpanHorizontal(tumbleRadio, 4);

		Button duplexRadio = radio(result, DiagramUIPrintingMessages.JPSOptionsDialog_SidesDuplex);
		layoutSpanHorizontal(duplexRadio, 4);

		oneSidedBinding = bindings.bindValue(
		        WidgetProperties.buttonSelection().observe(oneSideRadio),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_SIDES_ONESIDED).observe(realm, options));

		tumbleBinding = bindings.bindValue(
		        WidgetProperties.buttonSelection().observe(tumbleRadio),
		        BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_SIDES_TUMBLE).observe(realm, options));

		duplexBinding = bindings.bindValue(
		        WidgetProperties.buttonSelection().observe(duplexRadio),
                BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_SIDES_DUPLEX).observe(realm, options));

		return result;
	}

	@Override
	public void dispose() {
		bindings.removeBinding(oneSidedBinding);
		oneSidedBinding.dispose();
		bindings.removeBinding(tumbleBinding);
		tumbleBinding.dispose();	
		bindings.removeBinding(duplexBinding);
		duplexBinding.dispose();
	}

}
