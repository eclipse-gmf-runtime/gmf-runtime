/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.internal.PropertyChangeAction#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		Boolean value = (Boolean) getOperationSetPropertyValue(getPropertyId());
		if (value != null)
			return value.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
		return null;
	}
}
