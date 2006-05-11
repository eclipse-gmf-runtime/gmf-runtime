/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions;

import org.eclipse.ui.IWorkbenchPage;

/**
 * A base action representing those that are responsible to setting boolean valued  
 *  properties
 * 
 * @author melaasar
 * 
 */
public class BooleanPropertyAction extends CheckedPropertyAction {

	/**
	 * @param workbenchPage The workbench page
	 * @param propertyName The property name
	 * @param propertyId The property id
	 */
	protected BooleanPropertyAction(
		IWorkbenchPage workbenchPage,
		String propertyId,
		String propertyName){
		super(workbenchPage, propertyId, propertyName, Boolean.TRUE);
	}

	/**
	 * Returns the new property value.
	 * 
	 * The default implementation assumes a <code>Boolean</code> property
	 * If different, subclasses must override this method
	 *  
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.actions.PropertyChangeAction#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		Boolean value = (Boolean) getOperationSetPropertyValue(getPropertyId());
		if (value != null)
			return value.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
		return null;
	}
}
