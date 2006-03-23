/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.LED;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author qili
 *
 * 
 */
public class LEDEditPolicy extends ComponentEditPolicy{
	
	private static final String
	INCREMENT_REQUEST = "Increment", //$NON-NLS-1$
	DECREMENT_REQUEST = "Decrement"; //$NON-NLS-1$

public Command getCommand(Request request) {
	if (INCREMENT_REQUEST.equals(request.getType()))
		return getIncrementDecrementCommand(true);
	if (DECREMENT_REQUEST.equals(request.getType()))
		return getIncrementDecrementCommand(false);
	return super.getCommand(request);
}

protected Command getIncrementDecrementCommand(boolean type){
	IncrementDecrementCommand command = new IncrementDecrementCommand(((IGraphicalEditPart)getHost()).getEditingDomain(), type);
	command.setChild(((View)(getHost().getModel())).getElement());
	return new EtoolsProxyCommand(command);
}

/* (non-Javadoc)
 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(org.eclipse.gef.Request)
 */
public EditPart getTargetEditPart(Request request) {
	if (INCREMENT_REQUEST.equals(request.getType())
			|| DECREMENT_REQUEST.equals(request.getType()))
		return getHost();
	return null;
}
	
static class IncrementDecrementCommand 
	extends AbstractTransactionalCommand {
	
	boolean isIncrement = true;
	LED child = null;
	
	public IncrementDecrementCommand(TransactionalEditingDomain editingDomain, boolean increment){
		super(editingDomain, "Logic Value Change", null); //$NON-NLS-1$
		isIncrement=increment;
	}
	
	public void setChild(EObject child){
		this.child=(LED)child;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand#doExecuteWithResult(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
		throws ExecutionException {
		
		int value = child.getValue();
		if(isIncrement){
			if(value==15)value=-1;
			child.setValue(value+1);
		}else{
			if(value==0)value=16;
			child.setValue(value-1);
		}
		
		return CommandResult.newOKCommandResult();
	}

}

}
