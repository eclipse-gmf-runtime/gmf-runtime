/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.examples.runtime.common.service.application;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * Concrete operation for creating Widgets.
 * 
 */
public class CreateWidgetOperation
	implements IOperation {

	private int orderSize;

	/**
	 * Creates an instance of the CreateWidgetOperation with the specified 
	 * order size.
	 * @param orderSize the order size requested.
	 */
	public CreateWidgetOperation(int orderSize) {
		this.orderSize = orderSize;
	}

	/**
	 * Delegates the creation of the Widgets to the Provider.
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IWidgetProvider) provider).createWidget(orderSize);
	}

	/**
	 * Returns the order size for this CreateWidgetOperation.
	 * @return Returns the orderSize.
	 */
	public int getOrderSize() {
		return orderSize;
	}
}