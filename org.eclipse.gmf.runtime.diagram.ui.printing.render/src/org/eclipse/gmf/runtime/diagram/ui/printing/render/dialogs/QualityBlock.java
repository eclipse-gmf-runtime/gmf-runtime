/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.render.dialogs;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.l10n.DiagramUIPrintingMessages;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.model.PrintOptions;
import org.eclipse.jface.databinding.swt.SWTObservables;
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

	QualityBlock(IDialogUnitConverter dluConverter,
			DataBindingContext bindings, PrintOptions options) {
		super(dluConverter);

		this.bindings = bindings;
		this.options = options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.printing.internal.dialogs.DialogBlock#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent) {
		final Realm realm = bindings.getValidationRealm();

		Composite result = group(parent,
				DiagramUIPrintingMessages.JPSOptionsDialog_Quality);
		layout(result, 2);

		Button highRadio = radio(result,
				DiagramUIPrintingMessages.JPSOptionsDialog_QualityHigh);
		layoutSpanHorizontal(highRadio, 4);

		Button mediumRadio = radio(result,
				DiagramUIPrintingMessages.JPSOptionsDialog_QualityMedium);
		layoutSpanHorizontal(mediumRadio, 4);

		Button lowRadio = radio(result,
				DiagramUIPrintingMessages.JPSOptionsDialog_QualityLow);
		layoutSpanHorizontal(lowRadio, 4);

		bindings.bindValue(SWTObservables.observeSelection(highRadio),
				BeansObservables.observeValue(realm, options,
						PrintOptions.PROPERTY_QUALITY_HIGH), null, null);

		bindings.bindValue(SWTObservables.observeSelection(mediumRadio),
				BeansObservables.observeValue(realm, options,
						PrintOptions.PROPERTY_QUALITY_MED), null, null);

		bindings.bindValue(SWTObservables.observeSelection(lowRadio),
				BeansObservables.observeValue(realm, options,
						PrintOptions.PROPERTY_QUALITY_LOW), null, null);

		return result;
	}
}
