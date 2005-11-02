/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies.TerminalCanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.BorderedShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author qili
 *
 * Abstract editpart class for logic shapes
 */
public abstract class TerminalOwnerShapeEditPart 
	extends BorderedShapeEditPart 
	implements ITerminalOwnerEditPart
{
		
	/**
	 * @param view
	 */
	public TerminalOwnerShapeEditPart(View view) {
		super(view);
		// TODO Auto-generated constructor stub
	}
	
	protected void createDefaultEditPolicies(){
		super.createDefaultEditPolicies();
	
		installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, 
			new TerminalCanonicalEditPolicy());
	}

}
