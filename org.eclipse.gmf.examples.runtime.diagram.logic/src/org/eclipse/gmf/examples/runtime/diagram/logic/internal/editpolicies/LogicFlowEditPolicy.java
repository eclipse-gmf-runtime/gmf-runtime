/******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gmf.runtime.diagram.core.commands.AddCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableEditPolicyEx;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author qili
 *
 * An EditPolicy for use with {@link org.eclipse.draw2d.FlowLayout}.
 */
public class LogicFlowEditPolicy
	extends FlowLayoutEditPolicy
{

protected Command createAddCommand(EditPart child, EditPart after) {
	int index = getHost().getChildren().indexOf(after);
    TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost()).getEditingDomain();
	AddCommand command = new AddCommand(editingDomain, new EObjectAdapter((View)getHost().getModel()),
										new EObjectAdapter((View)child.getModel()), index);
	return new ICommandProxy(command);
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