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

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TopGraphicEditPart;
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
		super(workbenchPage, MetaModelUtil.getID(NotationPackage.eINSTANCE.getView_Visible()), Messages.getString("ConstrainedFlowLayoutEditPolicy.changeVisibilityCommand.label")); //$NON-NLS-1$);
		Assert.isNotNull(compartmentSemanticHint);
		this.compartmentSemanticHint = compartmentSemanticHint;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.PresentationAction#getTargetEdiParts(org.eclipse.gef.EditPart)
	 */
	protected List getTargetEdiParts(EditPart editpart) {
		EditPart targetEP = null;
		if (editpart instanceof TopGraphicEditPart) {
			final TopGraphicEditPart topEP = (TopGraphicEditPart) editpart;
			targetEP = (EditPart) MEditingDomainGetter.getMEditingDomain((View)editpart.getModel()).runAsRead( new MRunnable() {
				public Object run() { 
					return topEP.getChildBySemanticHint(
							getCompartmentSemanticHint());
				}
			});
		}
		return (targetEP == null)
			? Collections.EMPTY_LIST
			: Collections.singletonList(targetEP);
	}

	/**
	 * Returns the request compartment semantic hint
	 * 
	 * @return The request compartment semantic hint
	 */
	protected String getCompartmentSemanticHint() {
		return compartmentSemanticHint;
	}

}
