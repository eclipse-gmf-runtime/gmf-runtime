/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies.TerminalCanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GatedShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author qili
 *
 * Abstract editpart class for logic shapes
 */
public abstract class TerminalOwnerShapeEditPart 
	extends GatedShapeEditPart 
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
