/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.diagram.ui.editparts.GateEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.GateFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.ShapeCompartmentDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * Drag and Drop editpolicy that targets the gate elements registered
 * with the host editpart.
 * The default implementation is to prevent droping gate elements.
 * 
 * @see #setHost(EditPart)
 * @author mhanner
 */
public class GatedShapeCompartmentDragDropEditPolicy extends ShapeCompartmentDropEditPolicy {
	/**
	 * Iterates through the supplied  editpart list and filters each
	 * editpart into the appropriate editpart list.
	 * @param elements iterator for an EditParts collection
	 * @param gateEditParts list to be populated with gate editparts
	 * @param editParts list to be populated with non gate editparts
	 */
	private void FilterEditParts( Iterator elements, List gateEditParts, List editParts ) {
		Assert.isNotNull( gateEditParts );
		Assert.isNotNull( editParts );
		while ( elements.hasNext() ) {
			Object ep = elements.next();
			if ( isGateEditPart(ep) ) {
				gateEditParts.add(ep);
			}
			else {
				editParts.add(ep);
			}
		}
	}
	
	/**
	 * Filters the supplied request editparts into the appropriate list.
	 * @param request the request
	 * @param gateEditParts list to be populated with gate editparts
	 * @param editParts list to be populated with non gate editparts
	 */
	protected void filterEditParts( GroupRequest request, List gateEditParts, List editParts ) {
		FilterEditParts( request.getEditParts().iterator(), gateEditParts, editParts );
	}

	/**
	 * Filters the supplied requests elements into the appropriate list.
	 * @param request the request
	 * @param gateEditParts list to be populated with gate editparts
	 * @param editParts list to be populated with non gate editparts
	 */
	protected void filterEditParts( DropObjectsRequest request, List gateEditParts, List editParts ) {
		FilterEditParts( request.getObjects().iterator(), gateEditParts, editParts );
	}
	
	/** 
	 * Return <tt>true</tt> if the supplied object is a {@link GateEditPart};
	 * otherwise <tt>false</tt>.  Clients may wish to override to provide more
	 * involved checking.  This method is used by the <code>filterEditParts</code> 
	 * methods.
	 * @param object object being tested
	 * @return <tt>true</tt> if the supplied object is a <code>GateEditPart</code>;
	 * otherwise <tt>false</tt>.
	 */
	protected boolean isGateEditPart( Object object ) {
		return object instanceof GateEditPart && ((GateEditPart)object).getMainFigure() instanceof GateFigure;
	}
	
	ChangeBoundsRequest cloneRequest( ChangeBoundsRequest request ) {
		ChangeBoundsRequest req = new ChangeBoundsRequest();
		req.setType(request.getType());
		req.setEditParts(request.getEditParts());
		req.setMoveDelta(request.getMoveDelta());
		req.setSizeDelta(request.getSizeDelta());
		req.setLocation(request.getLocation());
		req.setResizeDirection(request.getResizeDirection());
		return req;
	}
	
	DropObjectsRequest cloneRequest( DropObjectsRequest request ) {
		DropObjectsRequest req = new DropObjectsRequest();
		req.setType(request.getType());
		req.setObjects(request.getObjects());
		req.setAllowedDetail(request.getAllowedDetail());
		req.setLocation(request.getLocation());
		req.setRequiredDetail(request.getRequiredDetail());
		return req;
	}
	
	/**
	 * Honours <code>DropElementRequest</code>s.  <code>GateEditPart</code>s
	 * are removed from the supplied request and forwarded to the
	 * <code>getDropGatesCommand()</code> method while the remaining
	 * elements are forwarded to <code>getDropElementsCommand()</code>
	 */
	public Command getCommand(Request request) {
		
		List gateEditParts = new ArrayList();
		List editParts = new ArrayList();
		if (RequestConstants.REQ_DRAG.equals(request.getType()) ) {
			filterEditParts( (ChangeBoundsRequest)request, gateEditParts, editParts );
			CompoundCommand cc = new CompoundCommand();
			ChangeBoundsRequest cbr = cloneRequest((ChangeBoundsRequest)request);
			if ( !editParts.isEmpty() ) {
				cbr.setEditParts(editParts);
				cc.add( super.getDragCommand(cbr));
			}
			
			if ( !gateEditParts.isEmpty() ) {
				cbr.setEditParts(gateEditParts);
				cc.add( getDropGatesCommand(cbr));
			}
			return cc.isEmpty() ? null : cc.unwrap();
		}
		else if (RequestConstants.REQ_DROP.equals(request.getType()) ) {
			filterEditParts( (ChangeBoundsRequest)request, gateEditParts, editParts );
			CompoundCommand cc = new CompoundCommand();
			ChangeBoundsRequest cbr = cloneRequest((ChangeBoundsRequest)request);
			if ( !editParts.isEmpty() ) {
				cbr.setEditParts(editParts);
				cc.add( super.getDropCommand(cbr));
			}
			
			if ( !gateEditParts.isEmpty() ) {
				cbr.setEditParts(gateEditParts);
				cc.add( getDropGatesCommand(cbr));
			}
			return cc.isEmpty() ? null : cc.unwrap();
		} 
		else if ( RequestConstants.REQ_DROP_OBJECTS.equals(request.getType())) {
			DropObjectsRequest dndRequest = (DropObjectsRequest) request;
			dndRequest.setRequiredDetail(getRequiredDragDetail(dndRequest));
			
			filterEditParts( (DropObjectsRequest)request, gateEditParts, editParts );
			
			CompoundCommand cc = new CompoundCommand();
			
			DropObjectsRequest der = cloneRequest((DropObjectsRequest)request);
			if ( !editParts.isEmpty() ) {
				der.setObjects( editParts );
				cc.add( super.getCommand(der));
			}
			
			if ( !gateEditParts.isEmpty() ) {
				der.setObjects( gateEditParts );
				cc.add( getDropGatesCommand(der));
			}
			return cc.isEmpty() ? null : cc.unwrap();
		}
		return super.getCommand(request);
	}
	
	/** Return a command to <i>drop</i> the elements contained in the supplied request. */
	protected Command getDropCommand(ChangeBoundsRequest request) {
		List gateEditParts = new ArrayList();
		List editParts = new ArrayList();
		filterEditParts( request, gateEditParts, editParts );
		
		CompoundCommand cc = new CompoundCommand();
		
		ChangeBoundsRequest cbr = new ChangeBoundsRequest(request.getType() );
		if ( !editParts.isEmpty() ) {
			cbr.setEditParts( editParts );
			cc.add( super.getDropCommand(cbr));
		}
		
		if ( !gateEditParts.isEmpty() ) {
			cbr.setEditParts( gateEditParts );
			cc.add( getDropGatesCommand(cbr));
		}
		return cc.isEmpty() ? null : cc.unwrap();
	}
	
	/**
	 * Return an {@link UnexecutableCommand} to disable <i>droping</i> gates.
	 * @param request
	 * @return an {@link UnexecutableCommand} or <tt>null</tt> if the request contains no editparts.
	 */
	protected Command getDropGatesCommand( ChangeBoundsRequest request ) {
		return request.getEditParts().isEmpty() ? null :UnexecutableCommand.INSTANCE;
	}
	
	/**
	 * Return an {@link UnexecutableCommand} to disable <i>droping</i> gates.
	 * @param request
	 * @return an {@link UnexecutableCommand} or <tt>null</tt> if the request contains no elementds.
	 */
	protected Command getDropGatesCommand( DropObjectsRequest request ) {
		return request.getObjects().isEmpty() ? null :UnexecutableCommand.INSTANCE;
	}
	
	/**
	 * Return an {@link UnexecutableCommand} to disable <i>dragging</i> gates.
	 * @param request
	 * @return an {@link UnexecutableCommand} or <tt>null</tt> if the request contains no editparts.
	 */
	protected Command getDragGatesCommand( ChangeBoundsRequest request ) {
		return request.getEditParts().isEmpty() ? null :UnexecutableCommand.INSTANCE;
	}
}
