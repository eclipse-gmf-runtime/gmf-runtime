/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.ArrayList;
import java.util.Collections;
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
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.NotationPackage;
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
	 * Return a list of all resizable Compartment edit parts that exist in the
	 * children list of this edit part
	 * getResizableCompartments()
	 * @return List of <code>EditPart<code>
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
	 * Gets all children, of this <code>EditPart<code>'s model, that had a 
	 * <code>DrawerStyle</code> installed on them 
	 * @return List of <code>View<code>s
	 */
	public List getResizableNotationViews() {
		View view = getNotationView();
		if (view!=null){
			List resizableChildren = new ArrayList();
			Iterator childrenIterator = view.getChildren().iterator();
			while (childrenIterator.hasNext()){
				View child = (View)childrenIterator.next();
				if (child instanceof Node &&
					((Node)child).getStyle(NotationPackage.eINSTANCE.getDrawerStyle())!=null){
					resizableChildren.add(child);
				}
			}
			return resizableChildren;
		}
		return Collections.EMPTY_LIST;
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
