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

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangeChildPropertyValueRequest;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IWorkbenchPage;

/**
 * An abstract base class that represents a boolean-based action 
 * that is applicable to specific shape compartments
 * @author melaasar
 *
 */
public abstract class IndividualCompartmentAction
	extends BooleanPropertyAction {

	/**
	 * The targeted compartment semantic hint
	 */
	private final String compartmentSemanticHint;

	/**
	 * @param workbenchPage the active workbenchPage 
	 * @param compartmentSemanticHint the hint indicating the compartment type	 
	 */
	public IndividualCompartmentAction(
		IWorkbenchPage workbenchPage,
		String compartmentSemanticHint) {
		super(
			workbenchPage,
			MetaModelUtil.getID(NotationPackage.eINSTANCE.getView_Visible()),
			DiagramUIActionsMessages.ConstrainedFlowLayoutEditPolicy_changeVisibilityCommand_label);
		Assert.isNotNull(compartmentSemanticHint);
		this.compartmentSemanticHint = compartmentSemanticHint;
	}

	
	protected Request createTargetRequest() {
		return new ChangeChildPropertyValueRequest(
			getPropertyName(),
			getPropertyId(),
			getCompartmentSemanticHint());
		
	}


	/**
	 * Returns the request compartment semantic hint
	 * 
	 * @return The request compartment semantic hint
	 */
	protected String getCompartmentSemanticHint() {
		return compartmentSemanticHint;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.internal.PropertyChangeAction#getPropertyValue(org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart, java.lang.String)
	 */
	protected Object getPropertyValue(
		final IGraphicalEditPart editPart,
		final String thePropertyId) {
		return MEditingDomainGetter.getMEditingDomain((View)editPart.getModel()).runAsRead( new MRunnable() {
			public Object run() {
				ENamedElement element = MetaModelUtil.getElement(thePropertyId);
				if (element instanceof EStructuralFeature){
					View view  = editPart.getNotationView();
					if (view !=null){
						View childView = ViewUtil.getChildBySemanticHint(view,getCompartmentSemanticHint());
						if (childView!=null){
							return ViewUtil.getStructuralFeatureValue(childView,(EStructuralFeature)element);
						}
					}
				}
				return null;
			}
		});
	}
}
