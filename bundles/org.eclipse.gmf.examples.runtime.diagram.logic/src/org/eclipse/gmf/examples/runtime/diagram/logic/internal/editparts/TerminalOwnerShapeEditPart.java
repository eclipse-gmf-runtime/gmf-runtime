/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies.TerminalCanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderedShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author qili
 *
 * Abstract editpart class for logic shapes
 */
public abstract class TerminalOwnerShapeEditPart 
	extends AbstractBorderedShapeEditPart 
	implements ITerminalOwnerEditPart
{
		
	/**
	 * @param view
	 */
	public TerminalOwnerShapeEditPart(View view) {
		super(view);
	}
	
	protected void createDefaultEditPolicies(){
		super.createDefaultEditPolicies();
	
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, 
			new TerminalCanonicalEditPolicy());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderedShapeEditPart#addBorderItem(org.eclipse.draw2d.IFigure, org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart)
	 */
	protected void addBorderItem(IFigure borderItemContainer,
			IBorderItemEditPart borderItemEditPart) {
		if (borderItemEditPart instanceof TerminalEditPart) {
			borderItemContainer.add(borderItemEditPart.getFigure(),
				((TerminalEditPart) borderItemEditPart).getLocator());
		} else {
			super.addBorderItem(borderItemContainer, borderItemEditPart);
		}
	}


}
