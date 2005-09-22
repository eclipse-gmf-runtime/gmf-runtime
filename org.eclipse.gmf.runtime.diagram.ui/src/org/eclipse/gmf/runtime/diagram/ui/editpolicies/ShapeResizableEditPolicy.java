/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.AccessibleHandleProvider;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.ResizeHandle;
import org.eclipse.gef.tools.ResizeTracker;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.internal.commands.IPropertyValueDeferred;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.internal.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.ChangeBoundsDeferredRequest;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A resizable editpolicy for resizable shape editparts
 * 1- It customizes the selection handles
 * 2- It handles the autosize and deferred change bounds request
 *  
 * @author melaasar
 */
public class ShapeResizableEditPolicy extends ResizableEditPolicyEx {

	/**
	 * Customize the selection handles
	 * @see org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		GraphicalEditPart part = (GraphicalEditPart) getHost();
		List selectionhandles = new ArrayList(9);
		MoveHandle moveHandle = new MoveHandle(part);
		moveHandle.setBorder(null);
		selectionhandles.add(moveHandle);
		selectionhandles.add(createHandle(part, PositionConstants.EAST));
		selectionhandles.add(createHandle(part, PositionConstants.SOUTH_EAST));
		selectionhandles.add(createHandle(part, PositionConstants.SOUTH));
		selectionhandles.add(createHandle(part, PositionConstants.SOUTH_WEST));
		selectionhandles.add(createHandle(part, PositionConstants.WEST));
		selectionhandles.add(createHandle(part, PositionConstants.NORTH_WEST));
		selectionhandles.add(createHandle(part, PositionConstants.NORTH));
		selectionhandles.add(createHandle(part, PositionConstants.NORTH_EAST));
		return selectionhandles;
	}
	
	/**
	 * Create resize handle with a normalized resize tracker
	 * @param owner the owner edit part
	 * @param direction the handle direction
	 * @return handle
	 */
	protected Handle createHandle(GraphicalEditPart owner, int direction) {
		ResizeHandle handle = new ResizeHandle(owner, direction);
		handle.setDragTracker(
			new ResizeTracker(owner, direction));
		return handle;
	}

	/**
	 * Cfreates a new AutoSize comamnd
	 * @param request
	 * @return command
	 */
	protected Command getAutoSizeCommand(Request request) {
 		ICommand resizeCommand = 
 			new SetBoundsCommand(
 				PresentationResourceManager.getI18NString("SetAutoSizeCommand.Label"),//$NON-NLS-1$
 				new EObjectAdapter((View) getHost().getModel()),
				new Dimension(-1, -1)); 
		return new EtoolsProxyCommand(resizeCommand);
	}

	/**
	 * Method getMoveDeferredCommand.
	 * @param request
	 * @return Command
	 */
	protected Command getMoveDeferredCommand(ChangeBoundsDeferredRequest request) {
		final class SetDeferredPropertyCommand extends AbstractModelCommand {
			private IAdaptable newValue;
			private IAdaptable viewAdapter;
			/**
			 * constructor
			 * @param label
			 * @param viewAdapter
			 * @param newValue
			 */
			public SetDeferredPropertyCommand(String label, IAdaptable viewAdapter, IAdaptable newValue) {
				super(label, null);
				this.viewAdapter = viewAdapter;
				this.newValue = newValue;
			}
			protected CommandResult doExecute(IProgressMonitor progressMonitor) {
				if (null == viewAdapter || null == newValue)
					return newCancelledCommandResult();
				
				View view = (View) viewAdapter.getAdapter(View.class);
				Point p = (Point) newValue.getAdapter(IPropertyValueDeferred.class);
				ViewUtil.setPropertyValue(view,Properties.ID_POSITIONX, new Integer(p.x));
				ViewUtil.setPropertyValue(view,Properties.ID_POSITIONY, new Integer(p.y));
				
				// clear for garbage collection
				viewAdapter = null;
				newValue = null;
				return newOKCommandResult();
			}
		}
		SetDeferredPropertyCommand cmd = new SetDeferredPropertyCommand(PresentationResourceManager.getI18NString("ShapeResizableEditPolicy.MoveDeferredCommand.label"),new EObjectAdapter((View)getHost().getModel()), request.getLocationAdapter()); //$NON-NLS-1$
		return new EtoolsProxyCommand(cmd);
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#getCommand(Request)
	 */
	public Command getCommand(Request request) {
		if (RequestConstants.REQ_AUTOSIZE.equals(request.getType()))
			return getAutoSizeCommand(request);
		if (RequestConstants.REQ_MOVE_DEFERRED.equals(request.getType()))
			return getMoveDeferredCommand(
				(ChangeBoundsDeferredRequest) request);
		return super.getCommand(request);
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#understandsRequest(Request)
	 */
	public boolean understandsRequest(Request request) {
		if (RequestConstants.REQ_AUTOSIZE.equals(request.getType())
			|| RequestConstants.REQ_MOVE_DEFERRED.equals(request.getType()))
			return true;
		return super.understandsRequest(request);
	}

	/**
	 * Creates the figure used for feedback.
	 * @return the new feedback figure
	 */
	protected IFigure createDragSourceFeedbackFigure() {
		// Use a ghost rectangle for feedback
		RectangleFigure r = new RectangleFigure();
		FigureUtilities.makeGhostShape(r);
		r.setLineStyle(Graphics.LINE_DOT);
		r.setForegroundColor(ColorConstants.white);
		r.setBounds(getInitialFeedbackBounds());
		addFeedback(r);
		return r;
	}
	
	/**
	 * Override for AccessibleHandleProvider when deactivated
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=69316
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == AccessibleHandleProvider.class)
			//handles == null when deactivated
			if (handles == null) {
				return null;
			}
		return super.getAdapter(key);
	}	
}
