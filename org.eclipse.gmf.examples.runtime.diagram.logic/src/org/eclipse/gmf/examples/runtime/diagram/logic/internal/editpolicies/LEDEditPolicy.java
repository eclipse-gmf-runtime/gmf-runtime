/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.LED;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.exceptions.MSLActionAbandonedException;
import org.eclipse.gmf.runtime.emf.core.util.OperationUtil;
import com.ibm.xtools.notation.View;


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
	IncrementDecrementCommand command = new IncrementDecrementCommand(type);
	command.setChild(((View)(getHost().getModel())).getElement());
	return command;
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
	extends org.eclipse.gef.commands.Command{
	
	boolean isIncrement = true;
	LED child = null;
	
	public IncrementDecrementCommand(boolean increment){
		super("Logic Value Change"); //$NON-NLS-1$
		isIncrement=increment;
	}
	
	public void setChild(EObject child){
		this.child=(LED)child;
	}
	
	public void execute(){
		try {
			boolean undoIntervalOpened = false;
			if (!OperationUtil.isUndoIntervalOpen()){
				OperationUtil.openUndoInterval();
				undoIntervalOpened = true;
			}
			OperationUtil.runAsWrite(new MRunnable() {
				public Object run(){
					int value = child.getValue();
					if(isIncrement){
						if(value==15)value=-1;
						child.setValue(value+1);
					}else{
						if(value==0)value=16;
						child.setValue(value-1);
					}
					return child;
				}
			}
			);
			if (undoIntervalOpened)
				OperationUtil.closeUndoInterval();
		} catch (MSLActionAbandonedException e) {
			e.printStackTrace();
		}
	}
	
	public void undo(){
		isIncrement=!isIncrement;
		execute();
		isIncrement=!isIncrement;
	}
	
	public void redo(){
		execute();
	}
}

}
