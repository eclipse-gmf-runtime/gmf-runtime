/******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.tools;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.tools.AbstractPopupBarTool;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This is the tool used for the AddUML commands associated with the action
 * bars. The creation tools for action bars also need to impl DragTracker since
 * the SelectionTool calls Handle.getDragTracker during mouseDown.
 * 
 * @author affrantz
 * 
 * @deprecated Use <code>PopupBarTool</code> instead. The difference
 *             between the two is that <code>AddUMLActionBarTool</code> has
 *             been removed and <code>PopupBarTool</code> is now used
 *             always. <code>AddUMLActionBarTool</code> used a request to
 *             create an element only, whereas <code>PopupBarTool</code>
 *             uses a request to create an element and view and if that does not
 *             return a command, then it tries a request to create an element
 *             only. Contact Cherie for assistance.
 *  
 */

public class AddUMLActionBarTool extends AbstractPopupBarTool implements DragTracker {

	/** When creating shapes on a dgrm using the abar, we do not
	 * want to cover the new shape with the abar, so we offset
	 * the creation pnt by a y-offset
	 * 32 is not not arbitrary it is 2x16 which is the height of an icon.
	 * and 2 pixels bigger than the height of an action-bar row.
	 */
	static private int Y_OFFSET 				= 32;	
	
	/**
	 * constructor
	 * @param epHost
	 * @param elementType
	 */
	public AddUMLActionBarTool(EditPart epHost, IElementType elementType) {
		super(epHost, elementType);
	}
	
	/**
	 * constructor
	 * @param epHost
	 * @param theRequest
	 */
	public AddUMLActionBarTool(EditPart epHost, CreateRequest theRequest) {
		super(epHost, theRequest);
	}
	
	/**
	 * @see org.eclipse.gef.tools.TargetingTool#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		
		/* if we have a request, use it */
		if (myRequest != null) {
			return myRequest;
		}
		
		View theView = (View)myHostEditPart.getModel();
		EObject hostElement = null;
		if(theView instanceof Diagram )
		{
			Diagram dv = (Diagram) theView;
			hostElement = dv.eContainer();
			CreateViewAndElementRequest theReq = new CreateViewAndElementRequest(
				getElementType(), getPreferencesHint());

			return theReq;
		} else if (theView instanceof ShapeCompartmentEditPart) {
			CreateViewAndElementRequest theReq = new CreateViewAndElementRequest(
				getElementType(), ((IGraphicalEditPart) myHostEditPart)
					.getDiagramPreferencesHint());

			return theReq;
		} else if (theView instanceof Edge) {
			CreateViewAndElementRequest theReq = new CreateViewAndElementRequest(
				getElementType(), ((IGraphicalEditPart) myHostEditPart)
					.getDiagramPreferencesHint());

			return theReq;
		}
		else 
		{	
			hostElement = ViewUtil.resolveSemanticElement(theView);
			
			CreateElementRequest theReq =
				new CreateElementRequest(hostElement, getElementType());
			
			EditCommandRequestWrapper semReq = new EditCommandRequestWrapper(theReq);

			return semReq;
		}
		

	}
	
	/**
	 * @see org.eclipse.gef.tools.TargetingTool#getCommand()
	*/
	protected Command getCommand() {

		Request theRequest = this.getTargetRequest();
		if(theRequest instanceof EditCommandRequestWrapper)
		{	
			// an EtoolsProxyCommand that wraps the ICommand of the from the semantic provider
			Command theRealCmd =
				((IGraphicalEditPart) myHostEditPart).getCommand(theRequest);
			
			// if we return a cmd that cannot execute then later downstream an NPE
			// can be generated.
			if((theRealCmd == null) || !theRealCmd.canExecute())
				return null;
			
			return theRealCmd;
		}
		else if (theRequest instanceof CreateViewAndElementRequest)
		{
			CreateViewAndElementRequest viewAndElementRequest =
				(CreateViewAndElementRequest) theRequest;
			

			Point thePoint = this.getCurrentInput().getMouseLocation();
			thePoint.y += Y_OFFSET;
			viewAndElementRequest.setLocation(thePoint);
			
			Command theCmd = myHostEditPart.getCommand(viewAndElementRequest);
			// if we return a cmd that cannot execute then later downstream an NPE
			// can be generated.
			if((theCmd == null) || !theCmd.canExecute())
				return null;
			return theCmd;
			
		}
		else if (theRequest instanceof CreateUnspecifiedTypeRequest)
		{
			CreateUnspecifiedTypeRequest theCreateUnspecifiedTypeRequest =
				(CreateUnspecifiedTypeRequest) theRequest;
			
			Point thePoint = this.getCurrentInput().getMouseLocation();
			thePoint.y += Y_OFFSET;
			theCreateUnspecifiedTypeRequest.setLocation(thePoint);

			
			Command theCmd = myHostEditPart.getCommand(theCreateUnspecifiedTypeRequest);
			
			if((theCmd == null) || !theCmd.canExecute())
				return null;
			return theCmd;	
		}
		return null;
	}	
	/**
	 * Asks the target editpart to show target feedback and sets the target feedback flag.
	 */
	protected void showTargetFeedback() {
		//After adding items to the action bar, the targeting tool sends createViewRequests
		//potentially causing incorrect feedback updates.  We must prevent these from
		//getting though.
	}
}
