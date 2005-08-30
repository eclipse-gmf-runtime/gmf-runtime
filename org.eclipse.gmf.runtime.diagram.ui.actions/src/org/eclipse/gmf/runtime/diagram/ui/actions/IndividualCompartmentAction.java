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

import java.util.Collections;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.util.Assert;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.Messages;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TopGraphicEditPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.notation.View;

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
		super(workbenchPage, Properties.ID_ISVISIBLE, Messages.getString("ConstrainedFlowLayoutEditPolicy.changeVisibilityCommand.label")); //$NON-NLS-1$);
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
