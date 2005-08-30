/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DirectEditRequest;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConstrainedToolbarLayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IContainedEditPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.notation.View;

/*
 * @canBeSeenBy %partners
 */
/**
 * the top graphic controller
 * @author mmostafa
 *
 */
public abstract class TopGraphicEditPart extends GraphicalEditPart implements IContainedEditPart {

	/**
	 * constructor
	 * @param view the view controlled by this edit part 
	 */
	public TopGraphicEditPart(View view) {
		super(view);
	}

	protected void createDefaultEditPolicies() {
		super.createDefaultEditPolicies();
		installEditPolicy(
			EditPolicy.LAYOUT_ROLE,
			new ConstrainedToolbarLayoutEditPolicy());
	}

	/**
	 * getResizableCompartments()
	 * @return List
	 */
	public List getResizableCompartments() {
		List resizableChildren = new ArrayList();
	    Iterator it = getChildren().iterator();
	    while( it.hasNext() ) {
	        Object child = it.next();
	        if ( child instanceof ResizableCompartmentEditPart) {
				resizableChildren.add( child );
	        }
	    }
	    
		return resizableChildren;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#performDirectEditRequest(org.eclipse.gef.requests.DirectEditRequest)
	 */
	protected void performDirectEditRequest(Request request) {
		EditPart editPart = this;
		if (request instanceof DirectEditRequest){
			Point p = new Point(((DirectEditRequest)request).getLocation());
			getFigure().translateToRelative(p);
			IFigure fig = getFigure().findFigureAt(p);
			editPart =(EditPart) getViewer().getVisualPartMap().get(fig);
		}
		if (editPart == this) {
			editPart= (EditPart)MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead( new MRunnable() {
				public Object run() {
					return getPrimaryChildEditPart();
				}
			});
			if (editPart != null){
				editPart.performRequest(request);
			}
		}
	}

	/**
	 * @see org.eclipse.gef.EditPart#understandsRequest(org.eclipse.gef.Request)
	 */
	public boolean understandsRequest(Request req) {
		return RequestConstants.REQ_DIRECT_EDIT == req.getType()
			|| super.understandsRequest(req);
	}

}
