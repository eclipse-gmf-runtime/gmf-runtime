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

package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import java.util.List;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.Request;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * 
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.actions.*
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public abstract class PropertyChangeAction extends PresentationAction {

	// id of the property this action will change
	private String propertyId = null;

	// name of the property this action will change
	private String propertyName = null;

	/**
	 * Constructs a new property change action
	 * 
	 * @param workbenchPage The workbench page
	 * @param propertyName The property name
	 * @param propertyId The property id
	 */
	public PropertyChangeAction(
		IWorkbenchPage workbenchPage,
		String propertyId,
		String propertyName) {
		super(workbenchPage);
		Assert.isNotNull(propertyId);
		Assert.isNotNull(propertyName);
		setPropertyId(propertyId);
		setPropertyName(propertyName);
	}

	/**
	 * Returns the propertyId.
	 * @return - property id
	 */
	protected String getPropertyId() {
		return propertyId;
	}

	/**
	 * Sets the propertyId.
	 * @param - The propertyId to set
	 */
	protected void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * Returns the propertyName.
	 * @return - property name
	 */
	protected String getPropertyName() {
		return propertyName;
	}

	/**
	 * Sets the propertyName.
	 * @param - The propertyName to set
	 */
	protected void setPropertyName(String string) {
		propertyName = string;
	}

	/**
	 * Creates a new target request
	 * 
	 * @return the new target request
	 */
	protected Request createTargetRequest() {
		return new ChangePropertyValueRequest(
			getPropertyName(),
			getPropertyId());
	}

	/**
	 * updates the target request. 
	 * Clients should call this method whenever the request 
	 * is expected to be changed
	 */
	protected void updateTargetRequest() {
		ChangePropertyValueRequest request =
			(ChangePropertyValueRequest) getTargetRequest();
		request.setValue(getNewPropertyValue());
	}

	/**
	 * Returns the property value of the given property id of the current operation set's
	 * 
	 * The default implementation returns the current property value of the 
	 * primary object in the operation set if not empty and <code>null</code> otherwise
	 *  
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.internal.PropertyChangeAction#getNewPropertyValue()
	 */
	protected Object getOperationSetPropertyValue(String id) {
		List set = getOperationSet();
		if (!set.isEmpty()) {
			IGraphicalEditPart primaryEditPart =
				(IGraphicalEditPart) set.get(set.size() - 1);
			return getPropertyValue(primaryEditPart, id);
		}
		return null;
	}

	/**
	 * A utility method to return the value of a given property for a given editpart
	 * 
	 * @param editPart The editpart
	 * @return The current value of the editpart's given property
	 */
	protected Object getPropertyValue(
		final IGraphicalEditPart editPart,
		final String thePropertyId) {

		return MEditingDomainGetter.getMEditingDomain((View)editPart.getModel()).runAsRead( new MRunnable() {
			public Object run() {
				ENamedElement element = MetaModelUtil.getElement(thePropertyId);
				if (element instanceof EStructuralFeature)
					return editPart.getStructuralFeatureValue((EStructuralFeature)element);
				return null;
			}
		});
	}

	/**
	 * Gets the new property value (usualy from the item's control)
	 * 
	 * @return the new property value
	 */
	protected abstract Object getNewPropertyValue();

	/**
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractContributionItem#isSelectionListener()
	 */
	protected boolean isSelectionListener() {
		return true;
	}

}
