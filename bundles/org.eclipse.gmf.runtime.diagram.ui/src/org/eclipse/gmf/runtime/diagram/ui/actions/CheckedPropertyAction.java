/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gmf.runtime.diagram.ui.internal.actions.PropertyChangeAction;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author melaasar
 *
 * A generic action for changing and reflecting checked properties
 * 
 */
public class CheckedPropertyAction extends PropertyChangeAction {

	/**
	 * The property valye
	 */
	private Object propertyValue;
	
	/**
	 * Creates a new boolean property change action isntance
	 * 
	 * @param workbenchPage The workbench page
	 * @param propertyName The property name
	 * @param propertyId The property id
	 * @param property the initial value of the property
	 */
	protected CheckedPropertyAction(
		IWorkbenchPage workbenchPage,
		String propertyId,
		String propertyName,
		Object propertyValue) {
		super(workbenchPage, propertyId, propertyName);
		Assert.isNotNull(propertyValue);
		this.propertyValue = propertyValue;
	}
    
    protected boolean isOperationHistoryListener() {
        return true;
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#getStyle()
	 */
	public int getStyle() {
		return AS_CHECK_BOX;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.IRepeatableAction#refresh()
	 */
	public void refresh() {
		super.refresh();
		setChecked(calculateChecked());
	}

	/**
	 * Calculates the check state of the action
	 * 
	 * @return <code>true</code> if action should be checked, <code>false</code> otherwise
	 */
	protected boolean calculateChecked() {
		return propertyValue.equals(getOperationSetPropertyValue(getPropertyId()));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.internal.PropertyChangeAction#getNewPropertyValue()
	 */
	protected Object getNewPropertyValue() {
		return propertyValue;
	}

}
