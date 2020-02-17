/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.actions;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * An action to insert new objects into a container.
 */
public class InsertAction
	extends SelectionAction
{


	/**
	 * the action id
	 */
	public static final String ID = ActionIds.ACTION_INSERT_SEMANTIC;

/**
 * Constructs a <code>DeleteAction</code> using the specified part.
 * @param part The part for this action
 */
public InsertAction(IWorkbenchPart part) {
	super(part);
	setId(ID);
}

/**
 * Initializes this action's text and images.
 */
protected void init() {
	super.init();
	setEnabled(false);
}

/**
 * Create a command to insert the new objects.
 * @param objects The objects to be deleted.
 * @return The command to insert the new object.
 */
public Command createInsertCommand(List objects) {
	if (objects.isEmpty())
		return null;
	if (objects.size() > 1)
		return null;
	if (!(objects.get(0) instanceof EditPart))
		return null;

	GroupRequest insertReq = new GroupRequest(RequestConstants.REQ_INSERT_SEMANTIC);

	return ((EditPart)(objects.get(0))).getCommand(insertReq);
}

/**
 * Returns <code>true</code> if the selected objects can
 * be deleted.  Returns <code>false</code> if there are
 * no objects selected or the selected objects are not
 * {@link EditPart}s.
 * @return <code>true</code> if the command should be enabled
 */
protected boolean calculateEnabled() {
	Command cmd = createInsertCommand(getSelectedObjects());
	if (cmd == null)
		return false;
	return cmd.canExecute();
}

/**
 * Performs the delete action on the selected objects.
 */
public void run() {
	Command cmd = createInsertCommand(getSelectedObjects());
	execute(cmd);
	
	Collection newObjects = DiagramCommandStack.getReturnValues(cmd);
	Iterator i = newObjects.iterator();
	if (i.hasNext()) {
		Object obj = i.next();

		IGraphicalEditPart host = (IGraphicalEditPart) getSelectedObjects().get(0);
		EditPart elementEP = host.findEditPart(host, (EObject)obj);
		if (elementEP == null) {
			// try the host's parent if the creation created a sibling
			IGraphicalEditPart parent = (IGraphicalEditPart)host.getParent();
			elementEP = parent.findEditPart(parent, (EObject)obj);
		}
		
		if (elementEP != null && getWorkbenchPart() instanceof IDiagramWorkbenchPart) {
			final EditPart newEP = elementEP;
			final IDiagramGraphicalViewer viewer = ((IDiagramWorkbenchPart)getWorkbenchPart()).getDiagramGraphicalViewer();
			
			// automatically put the first shape into edit-mode
			Display.getCurrent().asyncExec(new Runnable() {
				public void run() {
					viewer.setSelection(new StructuredSelection(newEP));
					Request der = new Request(RequestConstants.REQ_DIRECT_EDIT);
					newEP.performRequest(der);
				}
			});
		}
	}
}

}



