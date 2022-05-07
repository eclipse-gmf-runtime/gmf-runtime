/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.examples.runtime.common.service.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.examples.runtime.common.service.application.CreateWidgetOperation;
import org.eclipse.gmf.examples.runtime.common.service.application.IWidgetProvider;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;


/**
 * Super Widget provider which provides for Widget order sizes between 50 and 5000.
 * 
 */
public class SuperWidgetProvider
	extends AbstractProvider
	implements IWidgetProvider {

	/** 
	 * Returns <code>true</code> if the operation is a CreateWidgetOperation with
	 * an order size between 100 and 10000 widgets.
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof CreateWidgetOperation) {
			int orderSize = ((CreateWidgetOperation)operation).getOrderSize();
			return (orderSize >= 100 && orderSize <= 10000);
		}
		return false;
	}
	
	/**
	 * Create the number of Widgets requested.
	 * @see org.eclipse.gmf.examples.runtime.common.service.application.IWidgetProvider#createWidget(int)
	 */
	public Object createWidget(int orderSize) {
		List widgets = new ArrayList(orderSize);
		for (int i = 0; i < orderSize; i++) {
			widgets.add(i, "Widget");//$NON-NLS-1$
		}
		return widgets;
	}
}
