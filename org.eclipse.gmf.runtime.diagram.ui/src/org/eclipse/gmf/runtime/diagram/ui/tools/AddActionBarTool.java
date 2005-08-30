/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.tools;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.diagram.ui.internal.tools.AbstractAddActionBarTool;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import com.ibm.xtools.notation.View;

/**
 * This is the tool used for the AddUML commands associated with the action bars.
 * The creation tools for action bars also need to impl DragTracker since the SelectionTool
 * calls Handle.getDragTracker during mouseDown.
 * 
 * @author affrantz, cmahoney
 * 
 */

public class AddActionBarTool extends AbstractAddActionBarTool implements DragTracker {

	/** When creating shapes on a dgrm using the abar, we do not
	 * want to cover the new shape with the abar, so we offset
	 * the creation pnt by a y-offset
	 * 32 is not not arbitrary it is 2x16 which is the height of an icon.
	 * and 2 pixels bigger than the height of an action-bar row.
	 */
	static private int Y_OFFSET 				= 32;	
	
	/**
	 * constructor
	 * @param epHost the host edit part for this tool
	 * @param elementType
	 */
	public AddActionBarTool(EditPart epHost, IElementType elementType) {
		super(epHost, elementType);
	}
	
	/**
	 * constructor
	 * @param epHost
	 * @param theRequest the create request to be used
	 */
	public AddActionBarTool(EditPart epHost, CreateRequest theRequest) {
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

		return CreateViewRequestFactory.getCreateShapeRequest(getElementType(), getPreferencesHint());
	}
	
	/**
	 * First tries to get a command based on the target request (a create view
	 * and element request). If this fails, tries to get a command with a
	 * request to create an element only.
	 * 
	 * @see org.eclipse.gef.tools.TargetingTool#getCommand()
	 */
	protected Command getCommand() {
		Request theRequest = this.getTargetRequest();

		if (theRequest instanceof CreateRequest) {
			Point thePoint = this.getCurrentInput().getMouseLocation();
			thePoint.y += Y_OFFSET;
			((CreateRequest) theRequest).setLocation(thePoint);
		}
		
		Command theCmd = myHostEditPart.getCommand(theRequest);
		// if we return a cmd that cannot execute then later downstream an
		// NPE can be generated.
		if (theCmd != null && theCmd.canExecute()) {
			return theCmd;
		}

		return getCommandToCreateElementOnly();	
	}	
	
	/**
	 * Tries to get a command to create a new semantic element only.
	 * 
	 * @return the command if valid; null otherwise
	 */
	private Command getCommandToCreateElementOnly() {
		EObject hostElement = ViewUtil
			.resolveSemanticElement((View) myHostEditPart.getModel());

		if (hostElement != null && getElementType() != null) {
			CreateElementRequest theReq = new CreateElementRequest(
				hostElement, getElementType());
			EditCommandRequestWrapper semReq = new EditCommandRequestWrapper(theReq);

			// an EtoolsProxyCommand that wraps the ICommand of the from the
			// semantic provider
			Command theRealCmd = ((IGraphicalEditPart) myHostEditPart)
				.getCommand(semReq);

			// if we return a cmd that cannot execute then later downstream an
			// NPE can be generated.
			if (theRealCmd != null && theRealCmd.canExecute()) {
				return theRealCmd;
			}
		}
		return null;
	}
	
	/**
	 * Asks the target editpart to show target feedback and sets the target
	 * feedback flag.
	 */
	protected void showTargetFeedback() {
		//After adding items to the action bar, the targeting tool sends createViewRequests
		//potentially causing incorrect feedback updates.  We must prevent these from
		//getting though.
	}
}
