/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;

import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;

/**
 * A resizable edit policy that understands REQ_DRAG and REQ_DROP requests 
 * 
 * @author melaasar
 */
public class ResizableEditPolicyEx extends ResizableEditPolicy {

	/**
	 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(org.eclipse.gef.Request)
	 */
	public void eraseSourceFeedback(Request request) {
		if (RequestConstants.REQ_DROP.equals(request.getType()))
			eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
		else
			super.eraseSourceFeedback(request);
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#showSourceFeedback(org.eclipse.gef.Request)
	 */
	public void showSourceFeedback(Request request) {
		if (RequestConstants.REQ_DROP.equals(request.getType()))
			showChangeBoundsFeedback((ChangeBoundsRequest) request);
		else
			super.showSourceFeedback(request);
	}

	/**
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#addSelectionHandles()
	 */
	protected void addSelectionHandles() {
		super.addSelectionHandles();
		Iterator iter = handles.iterator();
		while (iter.hasNext()) {
			Handle handle = (Handle) iter.next();
			if (handle.getDragTracker().getClass() == DragEditPartsTracker.class)
				replaceHandleDragEditPartsTracker(handle);
		}
	}

	/**
	 * Replaces the handle's default DragEditPartsTracker with the extended
	 * DragEditPartsTrackerEx 
	 * @param handle
	 */
	protected void replaceHandleDragEditPartsTracker(Handle handle) {
		if (handle instanceof AbstractHandle) {
			AbstractHandle h = (AbstractHandle) handle;
			h.setDragTracker(new DragEditPartsTrackerEx(getHost()));
		}
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		if (understandsRequest(request))
			return getHost();
		return super.getTargetEditPart(request);
	}

}
