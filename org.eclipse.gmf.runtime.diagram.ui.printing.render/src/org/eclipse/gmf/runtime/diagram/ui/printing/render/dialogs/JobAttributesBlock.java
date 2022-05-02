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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * A section of the options print dialog that handles the job attributes.
 * 
 * @author James Bruck (jbruck)
 */
public class JobAttributesBlock extends DialogBlock {
	private final DataBindingContext bindings;
	private final PrintOptions options;
	private Binding jobNameBinding;

	JobAttributesBlock(IDialogUnitConverter dluConverter,
			DataBindingContext bindings, PrintOptions options) {
		super(dluConverter);

		this.bindings = bindings;
		this.options = options;
	}

	@Override
	public Control createContents(Composite parent) {
		final Realm realm = bindings.getValidationRealm();

		Composite result = group(parent,
				DiagramUIPrintingMessages.JPSOptionsDialog_JobAttributes);
		layout(result, 2);

		layoutHorizontalIndent(layoutAlignRight(label(result,
				DiagramUIPrintingMessages.JPSOptionsDialog_JobName)));
		Text jobName = text(result, 80);

		jobNameBinding = bindings.bindValue(
		        WidgetProperties.text(SWT.Modify).observe(jobName),
				BeanProperties.value(PrintOptions.class, PrintOptions.PROPERTY_JOB_NAME).observe(realm, options), null, null);

		return result;
	}
	
	@Override
	public void dispose() {
		bindings.removeBinding(jobNameBinding);
		jobNameBinding.dispose();	
	}
}
