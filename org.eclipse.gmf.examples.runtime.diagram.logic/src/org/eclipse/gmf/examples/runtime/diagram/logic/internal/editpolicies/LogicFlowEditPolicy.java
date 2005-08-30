/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gmf.runtime.diagram.core.commands.AddCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableEditPolicyEx;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author qili
 *
 * An EditPolicy for use with {@link org.eclipse.draw2d.FlowLayout}.
 */
public class LogicFlowEditPolicy
	extends org.eclipse.gef.editpolicies.FlowLayoutEditPolicy
{

protected Command createAddCommand(EditPart child, EditPart after) {
	int index = getHost().getChildren().indexOf(after);
	AddCommand command = new AddCommand(new EObjectAdapter((View)getHost().getModel()),
										new EObjectAdapter((View)child.getModel()), index);
	return new EtoolsProxyCommand(command);
}

/**
 * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
 */
protected EditPolicy createChildEditPolicy(EditPart child) {
	ResizableEditPolicyEx policy = new ResizableEditPolicyEx();
	policy.setResizeDirections(0);
	return policy;
}

protected Command createMoveChildCommand(EditPart child, EditPart after) {
	return null;
}

protected Command getCreateCommand(CreateRequest request) {
	return null;
}

protected Command getDeleteDependantCommand(Request request) {
	return null;
}

protected Command getOrphanChildrenCommand(Request request) {
	return null;
}

}