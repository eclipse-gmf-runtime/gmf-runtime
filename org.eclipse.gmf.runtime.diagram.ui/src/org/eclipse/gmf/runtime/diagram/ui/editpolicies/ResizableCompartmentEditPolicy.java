/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
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
import java.util.List;
import java.util.ListIterator;

import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ResizableCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TopGraphicEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderedNodeFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.diagram.ui.handles.CompartmentCollapseHandle;
import org.eclipse.gmf.runtime.diagram.ui.internal.handles.CompartmentResizeHandle;
import org.eclipse.jface.util.Assert;

/**
 * A resizable editpolicy for resizable compartments. The editpolicy could be
 * vertical or horizontal
 * 
 * @author melaasar
 */
public class ResizableCompartmentEditPolicy
	extends ResizableEditPolicyEx {

	private boolean horizontal;
	
	/**
	 * Listener to determine bounds changes of compartment figure and/or its container
	 * to display or hide collapse handles accordingly (Bugzilla #115905)
	 */
	private FigureListener figureListener = new FigureListener() {
		public void figureMoved(IFigure source) {
			if (handles != null) {
				ResizableCompartmentFigure compartment = getCompartmentFigure();
				Dimension minClientDimension = compartment.getMinClientDimension();
				if (source.equals(compartment))
				{
					Rectangle intersection = getGraphicalEditPart()
							.getTopGraphicEditPart().getFigure().getBounds().getCopy()
							.intersect(source.getBounds());
					
					refreshCollapseHandles(intersection.height>minClientDimension.height && intersection.width>minClientDimension.width);					
				}
				else if (source.equals(getGraphicalEditPart().getTopGraphicEditPart().getFigure()))
				{
					Rectangle intersection = source.getBounds().getCopy().intersect(compartment.getBounds());
					refreshCollapseHandles(intersection.height>minClientDimension.height && intersection.width>minClientDimension.width);
				}
			}
		}
	};
	
	/**
	 * Creates a new vertical ResizableCompartmentEditPolicy
	 */
	public ResizableCompartmentEditPolicy() {
		this(false);
	}

	/**
	 * creates a new ResizableCompartmentEditPolicy with the given orientation
	 * 
	 * @param horizontal
	 */
	public ResizableCompartmentEditPolicy(boolean horizontal) {
		this.horizontal = horizontal;
	}

	/**
	 * @return Whether the editpolicy is horizontal
	 */
	protected boolean isHorizontal() {
		return horizontal;
	}

	/**
	 * This method is used to get the collapse handle(s). Subclasses can
	 * override to provide different collapse handles
	 * 
	 * @return a list of collapse handles
	 */
	protected List createCollapseHandles() {
		IGraphicalEditPart part = (IGraphicalEditPart) getHost();

		List collapseHandles = new ArrayList();
		
		CompartmentCollapseHandle collapseHandle = new CompartmentCollapseHandle(part);
		
		TopGraphicEditPart wrapper = part.getTopGraphicEditPart();
		Rectangle intersection = null;
		ResizableCompartmentFigure compartment = getCompartmentFigure();		
		if (wrapper != null && compartment != null)
			intersection = wrapper.getFigure().getBounds().getCopy().intersect(
					compartment.getBounds());
		collapseHandle.setVisible(intersection == null
				|| (intersection.width > compartment.getMinClientDimension().width && intersection.height > compartment
						.getMinClientDimension().height));
		
		collapseHandles.add(collapseHandle);
		return collapseHandles;
	}
	
	/**
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		IGraphicalEditPart part = (IGraphicalEditPart) getHost();
		int d1 = isHorizontal() ? PositionConstants.WEST
			: PositionConstants.NORTH;
		int d2 = isHorizontal() ? PositionConstants.EAST
			: PositionConstants.SOUTH;
		List selectionHandles = new ArrayList();
		
		selectionHandles.addAll(createCollapseHandles());
		selectionHandles.add(new CompartmentResizeHandle(part, d1));
		selectionHandles.add(new CompartmentResizeHandle(part, d2));
		return selectionHandles;
	}

	/**
	 * @return the <code>ResizableCompartmentFigure</code> that is the
	 *         corresponding figure for the host edit part.
	 */
	private ResizableCompartmentFigure getCompartmentFigure() {
		ResizableCompartmentFigure compartmentFigure = null;
		if (getGraphicalEditPart() instanceof ResizableCompartmentEditPart) {
			compartmentFigure = ((ResizableCompartmentEditPart) getGraphicalEditPart())
				.getCompartmentFigure();
		} else if (getGraphicalEditPart().getFigure() instanceof ResizableCompartmentFigure) {
			compartmentFigure = (ResizableCompartmentFigure) getGraphicalEditPart()
				.getFigure();
		}
		// TODO: remove later. this is a temporary fix for defect
		// RATLC00522565
		// eventually we will put the BorderedNodeFigure inside the resizable
		// compartment
		else if (getGraphicalEditPart().getFigure() instanceof BorderedNodeFigure) {
			BorderedNodeFigure gpf = (BorderedNodeFigure) getGraphicalEditPart()
				.getFigure();
			IFigure f = gpf.getMainFigure();
			if (f instanceof ResizableCompartmentFigure) {
				compartmentFigure = (ResizableCompartmentFigure) f;
			}
		}

		return compartmentFigure;
	}

	/**
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showSelection()
	 */
	protected void showSelection() {
		super.showSelection();
		IGraphicalEditPart part = getGraphicalEditPart();
		TopGraphicEditPart topPart = part.getTopGraphicEditPart();
		ResizableCompartmentFigure compartmentFigure = getCompartmentFigure();
		if (compartmentFigure != null) {
			if (topPart != null)
			{
				topPart.getFigure().addFigureListener(figureListener);
				compartmentFigure.addFigureListener(figureListener);
			}
			if (getHost().getSelected() != EditPart.SELECTED_NONE) {
				compartmentFigure.setSelected(true);
			}
		}
	}

	/**
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideSelection()
	 */
	protected void hideSelection() {
		super.hideSelection();
		IGraphicalEditPart part = getGraphicalEditPart();
		TopGraphicEditPart topPart = part.getTopGraphicEditPart();
		ResizableCompartmentFigure compartmentFigure = getCompartmentFigure();
		if (compartmentFigure != null) {
			if (topPart != null)
			{
				topPart.getFigure().removeFigureListener(figureListener);
				compartmentFigure.removeFigureListener(figureListener);
			}
			if (getHost().getSelected() == EditPart.SELECTED_NONE) {
				compartmentFigure.setSelected(false);
			}
		}
	}

	private EditPartListener hostListener;

	private EditPartListener parentListener;

	/**
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#addSelectionListener()
	 */
	protected void addSelectionListener() {
		hostListener = new EditPartListener.Stub() {

			public void selectedStateChanged(EditPart part) {
				setSelectedState();
				setFocus(part.hasFocus());
			}
		};
		getHost().addEditPartListener(hostListener);

		parentListener = new EditPartListener.Stub() {

			public void selectedStateChanged(EditPart part) {
				setSelectedState();
			}
		};
		getParentGraphicEditPart().addEditPartListener(parentListener);
				
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#activate()
	 */
	public void activate() {
		super.activate();
		setSelectedState();
	}
	
	/**
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#removeSelectionListener()
	 */
	protected void removeSelectionListener() {
		getHost().removeEditPartListener(hostListener);
		getParentGraphicEditPart().removeEditPartListener(parentListener);
	}

	/**
	 * Determine the select state of the policy based on: 1- The select state of
	 * the compartment editpart 2- The select state of the parent graphic
	 * editpart 3- The visibility state of the compartment editpart
	 */
	protected void setSelectedState() {
		int hostState = getHost().getSelected();
		int topState = EditPart.SELECTED_NONE;

		if (getGraphicalEditPart()
			.getTopGraphicEditPart() != null) {
			topState = getGraphicalEditPart()
				.getTopGraphicEditPart().getSelected();
		}

		boolean vis = getGraphicalEditPart().getNotationView().isVisible();

		if (vis
			&& ((hostState != EditPart.SELECTED_NONE || topState != EditPart.SELECTED_NONE)))
			setSelectedState(EditPart.SELECTED);
		else
			setSelectedState(EditPart.SELECTED_NONE);
	}

	private IGraphicalEditPart getParentGraphicEditPart() {
		return (IGraphicalEditPart) getGraphicalEditPart().getParent();
	}

	private IGraphicalEditPart getGraphicalEditPart() {
		return (IGraphicalEditPart) getHost();
	}

	/**
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#showChangeBoundsFeedback(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		super.showChangeBoundsFeedback(getResizeChildrenRequest(request));
	}

	/**
	 * @see org.eclipse.gef.editpolicies.ResizableEditPolicy#getResizeCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	protected Command getResizeCommand(ChangeBoundsRequest request) {
		return getHost().getParent().getCommand(
			getResizeChildrenRequest(request));
	}

	/**
	 * Creates a new ChangeBoundsRequest that respects the min and max of the
	 * resize deltas
	 */
	private ChangeBoundsRequest getResizeChildrenRequest(ChangeBoundsRequest r) {
		Dimension delta = r.getSizeDelta();

		ResizableCompartmentFigure f = getCompartmentFigure();
		
		Dimension fd = f.getSize().getExpanded(delta);
		fd.intersect(f.getMaximumSize()).union(f.getMinimumSize());
		delta = fd.shrink(f.getSize().width, f.getSize().height);

		boolean moved = (r.getResizeDirection() & PositionConstants.NORTH_WEST) != 0;
		IFigure a = f.getAdjacentSibling(moved);
		if (a != null) {
			Dimension ad = a.getSize().getExpanded(delta.negate());
			ad.intersect(a.getMaximumSize()).union(a.getMinimumSize());
			delta = ad.shrink(a.getSize().width, a.getSize().height).negate();
		} else {
			delta.shrink(delta.width, delta.height);
		}

		ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_RESIZE_CHILDREN);
		req.setEditParts(getHost());
		req.setLocation(r.getLocation());
		req.setResizeDirection(r.getResizeDirection());
		req.setSizeDelta(delta);
		if (moved)
			req.setMoveDelta(new Point(-delta.width, -delta.height));
		return req;
	}
	
    /**
     * Refreshes collapse handles - displays them if they need to be displayed but not displaying
     * and removes them if they shouldn't be displayed, but are displayed.
     * Method assumes that handles are not <code>null</code>, i.e. handles are being displayed
     *  
     * @param shouldHaveHandles <code>true</code> if collapse handles need to be displayed 
     */
    private void refreshCollapseHandles(boolean shouldHaveHandles)
    {
    	Assert.isTrue(handles!=null);

    	for (ListIterator handlesIterator = handles.listIterator(); handlesIterator.hasNext();)
		{
    		Object handle = handlesIterator.next();
			if (handle instanceof CompartmentCollapseHandle)
				((CompartmentCollapseHandle)handle).setVisible(shouldHaveHandles);
		}
    }
        
}
