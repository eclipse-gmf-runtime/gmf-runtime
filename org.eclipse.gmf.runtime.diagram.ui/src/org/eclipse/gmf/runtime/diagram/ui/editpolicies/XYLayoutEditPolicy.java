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

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.Iterator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.LayoutHelper;
import org.eclipse.gmf.runtime.diagram.ui.internal.ruler.DiagramGuide;
import org.eclipse.gmf.runtime.diagram.ui.internal.ruler.commands.ChangeGuideCommand;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Guide;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Assert;

/*
 * @canBeSeenBy %partners
 */
/**
 * the xy layout edit policy 
 * @see org.eclipse.gef.editpolicies.XYLayoutEditPolicy
 * @author sshaw
 *
 */
public class XYLayoutEditPolicy
	extends org.eclipse.gef.editpolicies.XYLayoutEditPolicy {


	/** 
	 * Called in response to a <tt>REQ_ADD</tt> (reparent) request.  
	 * Returns a <tt>SetPropertyCommand</tt> to set the <tt>child<tt>'s bounds
	 * to the supplied constraint.
	 * 
	 * @param child element being reparented.
	 * @param constraint - rectangle containing the child's bounds (location)
	 * @return a new command or null if the compound command is empty
	 * 
	 */
	protected Command createAddCommand(EditPart child, Object constraint) {
		if ( child instanceof ShapeEditPart && constraint instanceof Rectangle) {
			Rectangle rect = (Rectangle) constraint;
			
	 		ICommand boundsCommand = 
	 			new SetBoundsCommand(((ShapeEditPart) child).getEditingDomain(),
	 				DiagramUIMessages.SetLocationCommand_Label_Resize,
	 				new EObjectAdapter((View) child.getModel()),
					rect.getTopLeft()); 
			return new EtoolsProxyCommand(boundsCommand);
		}
		return null;
	}

	
	

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.requests.ChangeBoundsRequest, org.eclipse.gef.EditPart, java.lang.Object)
	 */
	protected Command createChangeConstraintCommand(
			ChangeBoundsRequest request, EditPart child, Object constraint) {
		

        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();
        
		Command cmd = createChangeConstraintCommand(child, constraint);
		View view = (View)child.getModel();
		if ((request.getResizeDirection() & PositionConstants.NORTH_SOUTH) != 0) {
			Integer guidePos = (Integer)request.getExtendedData()
					.get(SnapToGuides.KEY_HORIZONTAL_GUIDE);
			if (guidePos != null) {
				int hAlignment = ((Integer)request.getExtendedData()
						.get(SnapToGuides.KEY_HORIZONTAL_ANCHOR)).intValue();
				ChangeGuideCommand cgm = new ChangeGuideCommand(editingDomain, view, true);
				cgm.setNewGuide(findGuideAt(guidePos.intValue(), true), hAlignment);
				cmd = cmd.chain(new EtoolsProxyCommand(cgm));
			} else if (DiagramGuide.getInstance().getHorizontalGuide(view) != null) {
				// SnapToGuides didn't provide a horizontal guide, but this part is attached
				// to a horizontal guide.  Now we check to see if the part is attached to
				// the guide along the edge being resized.  If that is the case, we need to
				// detach the part from the guide; otherwise, we leave it alone.
				int alignment = DiagramGuide.getInstance().getHorizontalAlignment(view);
				int edgeBeingResized = 0;
				if ((request.getResizeDirection() & PositionConstants.NORTH) != 0)
					edgeBeingResized = -1;
				else
					edgeBeingResized = 1;
				if (alignment == edgeBeingResized) {
					ChangeGuideCommand cgm = new ChangeGuideCommand(editingDomain, view, true);
					cmd = cmd.chain(new EtoolsProxyCommand(cgm));
				}
			}
		}
		
		if ((request.getResizeDirection() & PositionConstants.EAST_WEST) != 0) {
			Integer guidePos = (Integer)request.getExtendedData()
					.get(SnapToGuides.KEY_VERTICAL_GUIDE);
			if (guidePos != null) {
				int vAlignment = ((Integer)request.getExtendedData()
						.get(SnapToGuides.KEY_VERTICAL_ANCHOR)).intValue();
				ChangeGuideCommand cgm = new ChangeGuideCommand(editingDomain, view, false);
				cgm.setNewGuide(findGuideAt(guidePos.intValue(), false), vAlignment);
				cmd = cmd.chain(new EtoolsProxyCommand(cgm));
			} else if (DiagramGuide.getInstance().getVerticalGuide(view) != null) {
				int alignment = DiagramGuide.getInstance().getVerticalAlignment(view);
				int edgeBeingResized = 0;
				if ((request.getResizeDirection() & PositionConstants.WEST) != 0)
					edgeBeingResized = -1;
				else
					edgeBeingResized = 1;
				if (alignment == edgeBeingResized) {
					ChangeGuideCommand cgm = new ChangeGuideCommand(editingDomain, view, false);
					cmd = cmd.chain(new EtoolsProxyCommand(cgm));
				}
			}
		}
		
		if (request.getType().equals(REQ_MOVE_CHILDREN)
				|| request.getType().equals(REQ_ALIGN_CHILDREN)) {
			Integer guidePos = (Integer)request.getExtendedData()
					.get(SnapToGuides.KEY_HORIZONTAL_GUIDE);
			ChangeGuideCommand cgm = new ChangeGuideCommand(editingDomain, view, true);
			if (guidePos != null) {
				int hAlignment = ((Integer)request.getExtendedData()
						.get(SnapToGuides.KEY_HORIZONTAL_ANCHOR)).intValue();
				cgm.setNewGuide(findGuideAt(guidePos.intValue(), true), hAlignment);
			}
			// If know this creates a lot of extra commands.  They are currently
			// required for attaching/detaching shapes to guides
			cmd = cmd.chain(new EtoolsProxyCommand(cgm));
			
			guidePos = (Integer)request.getExtendedData()
					.get(SnapToGuides.KEY_VERTICAL_GUIDE);
			cgm = new ChangeGuideCommand(editingDomain, view, false);
			if (guidePos != null) {
				int vAlignment = ((Integer)request.getExtendedData()
						.get(SnapToGuides.KEY_VERTICAL_ANCHOR)).intValue();
				cgm.setNewGuide(findGuideAt(guidePos.intValue(), false), vAlignment);
			}
			// If know this creates a lot of extra commands.  They are currently
			// required for attaching/detaching shapes to guides
			cmd = cmd.chain(new EtoolsProxyCommand(cgm));
		}

		return cmd;
	}
	
	/** 
	 * Called in response to a <tt>REQ_RESIZE_CHILDREN</tt> request.
	 * 
	 * This implementation creates a <tt>SetPropertyCommand</i> and sets
	 * the <tt>ID_BOUNDS</tt> property value to the supplied constraints.
	 * 
	 * @param child the element being resized.
	 * @param constraint the elements new bounds.
	 * @return {@link SetBoundsCommand}
	 */
	protected Command createChangeConstraintCommand(
		EditPart child,
		Object constraint) {
		Rectangle newBounds = (Rectangle) constraint;
		View shapeView = (View) child.getModel();

        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();
        
 		ICommand boundsCommand = 
 			new SetBoundsCommand(editingDomain,
 				DiagramUIMessages.SetLocationCommand_Label_Resize,
 				new EObjectAdapter(shapeView),
				newBounds); 
		return new EtoolsProxyCommand(boundsCommand);
	}

	/**
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#getConstraintFor(org.eclipse.gef.requests.ChangeBoundsRequest, org.eclipse.gef.GraphicalEditPart)
	 */
	protected Object getConstraintFor(
		ChangeBoundsRequest request,
		GraphicalEditPart child) {
		Rectangle rect = (Rectangle) super.getConstraintFor(request, child);
		Rectangle cons = getCurrentConstraintFor(child);
		if (request.getSizeDelta().width == 0)
			rect.width = cons.width;
		if (request.getSizeDelta().height == 0)
			rect.height = cons.height;
		return rect;
	}

	/**
	 * Called in response to a <tt>REQ_CREATE</tt> request. Returns a command
	 * to set each created element bounds and autosize properties.
	 * 
	 * @param request a create request (understands instances of {@link CreateViewRequest}).
	 * @return a command to satify the request; <tt>null</tt> if the request is not
	 * understood.
	 */
	protected Command getCreateCommand(CreateRequest request) {
		CreateViewRequest req = (CreateViewRequest) request;
        

        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();

		CompositeTransactionalCommand cc = new CompositeTransactionalCommand(
            editingDomain, DiagramUIMessages.AddCommand_Label);
        Iterator iter = req.getViewDescriptors().iterator();

		final Rectangle BOUNDS = (Rectangle) getConstraintFor(request);



		while (iter.hasNext()) {
			CreateViewRequest.ViewDescriptor viewDescriptor = (CreateViewRequest.ViewDescriptor)iter.next(); 
			Rectangle rect = getBoundsOffest(req, BOUNDS,viewDescriptor);
			cc.compose(new SetBoundsCommand(editingDomain, 
				DiagramUIMessages.SetLocationCommand_Label_Resize,
				viewDescriptor,
				rect));
		}
		
		if( cc.reduce() == null )
			return null;

		return chainGuideAttachmentCommands( request,
			new EtoolsProxyCommand(cc.reduce()));
	}

	/**
	 * Return bounds offset by some predefined amount.
	 * @param request the request
	 * @param bounds	the rectangle bounds
	 * @param viewDescriptor the view descriptor
	 * @return rectangle
	 */
	protected Rectangle getBoundsOffest( CreateViewRequest request, Rectangle bounds, CreateViewRequest.ViewDescriptor viewDescriptor ) {
		int translate = request.getViewDescriptors().indexOf(viewDescriptor) * 10;
		return bounds.getCopy().translate( translate, translate );
	}
	
	/** 
	 * <tt>null</tt> implementation: request not handled.
	 */
	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}

	/** 
	 * <tt>null</tt> implementation: request not handled.
	 */
	protected Command getOrphanChildrenCommand(Request request) {
		return null;
	}

	protected EditPolicy createChildEditPolicy(EditPart child) {
		if ( child instanceof ShapeEditPart ) {
			return ((ShapeEditPart)child).getPrimaryDragEditPolicy();
		}
		
		return null;
	}	
	
	/**
	 * Creates command for <tt>REQ_CREATE</tt> 
	 * requests only; all others requests are forwarded to the parent class.
	 * 
	 * @see #getCommand(Request) 
	 */
	public Command getCommand(Request request) {

		if (REQ_CREATE.equals(request.getType())) {

			if (request instanceof CreateViewRequest) {
				return getCreateCommand((CreateViewRequest) request);
			} else {
				return null;
			}

		}
		return super.getCommand(request);
	}
	
	/* Override to use to deal with causes where the point is UNDERFINED
	 * we will ask the layout helper to find a location for us
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#getConstraintFor(org.eclipse.gef.requests.CreateRequest)
	 */
	protected Object getConstraintFor(CreateRequest request) {
		
		Object constraint = super.getConstraintFor(request);
		
		if ( LayoutHelper.UNDEFINED.getLocation().equals(request.getLocation()) ){	
			Rectangle rect = (Rectangle)constraint;
			rect.setLocation(getLayoutHelper().getReferencePosition(getHostFigure()));
			Point point = getLayoutHelper().validatePosition(getHostFigure(),rect);
			rect.setLocation(point);
			return rect;	
		}
		return constraint;	
	}
	
	/** 
	 * Return the host's figure. 
	 * The super calls getFigure().  This is a problem when used with shapecompartments.  Instead,
	 * return getContextPane().  In shape comaprtments this will return the correct containing figure.
	 */
	protected IFigure getHostFigure() {
		return ((GraphicalEditPart)getHost()).getContentPane();
	}
	
	// layout helper used to help locate shape created with undefined points
	LayoutHelper layoutHelper = null;
	
	/** Return this layout helper. */
	private LayoutHelper getLayoutHelper() {
		if (layoutHelper == null) {
			layoutHelper = new LayoutHelper();
		}
		return layoutHelper;
	}
	
	/**
	 * @param request
	 * @param cmd
	 * @return command
	 */
	protected Command chainGuideAttachmentCommands(
			Request request, Command cmd) {
		Assert.isNotNull(request);
		Assert.isNotNull(cmd);

		EditPartViewer editPartViewer = getHost().getRoot().getViewer();
		Command result = cmd;

		CreateViewRequest req = (CreateViewRequest) request;
        
        TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
            .getEditingDomain();

		// Attach to horizontal guide, if one is given
		Integer guidePos = (Integer)request.getExtendedData()
				.get(SnapToGuides.KEY_HORIZONTAL_GUIDE);
		if (guidePos != null) {
			int hAlignment = ((Integer)request.getExtendedData()
					.get(SnapToGuides.KEY_HORIZONTAL_ANCHOR)).intValue();

			Guide guide = findGuideAt(guidePos.intValue(), true);

			Iterator iter = req.getViewDescriptors().iterator();

			while (iter.hasNext()) {
				IAdaptable desc = (IAdaptable)iter.next();
				ChangeGuideCommand cgm = new ChangeGuideCommand(editingDomain,
                    editPartViewer, desc, true);
				cgm.setNewGuide(guide, hAlignment);
				result = result.chain(new EtoolsProxyCommand(cgm));
			}
		}

		// Attach to vertical guide, if one is given
		guidePos = (Integer)request.getExtendedData()
				.get(SnapToGuides.KEY_VERTICAL_GUIDE);
		if (guidePos != null) {
			int vAlignment = ((Integer)request.getExtendedData()
					.get(SnapToGuides.KEY_VERTICAL_ANCHOR)).intValue();

			Guide guide = findGuideAt(guidePos.intValue(), false);

			Iterator iter = req.getViewDescriptors().iterator();

			while (iter.hasNext()) {
				IAdaptable desc = (IAdaptable)iter.next();

				ChangeGuideCommand cgm = new ChangeGuideCommand(editingDomain,
                    editPartViewer, desc, false);
				cgm.setNewGuide(guide, vAlignment);
				result = result.chain(new EtoolsProxyCommand(cgm));
			}
		}

		return result;
	}

	/**
	 * gets the guid at a specific pos
	 * @param pos	the position
	 * @param horizontal the horizontal flag	
	 * @return the guid
	 */
	protected Guide findGuideAt(int pos, boolean horizontal) {
		RulerProvider provider = ((RulerProvider)getHost().getViewer().getProperty(
				horizontal ? RulerProvider.PROPERTY_VERTICAL_RULER 
				: RulerProvider.PROPERTY_HORIZONTAL_RULER));
		IMapMode mm = MapModeUtil.getMapMode(((GraphicalEditPart)getHost()).getFigure());
		
		return (Guide)provider.getGuideAt(mm.LPtoDP(pos));
	}
}
