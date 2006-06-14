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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.AlignmentRequest;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.IBorderItemLocator;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Edit policy to restrict border item movement.
 * 
 * @author jbruck, cmahoney
 */
public class BorderItemSelectionEditPolicy
	extends NonResizableEditPolicyEx {

	public void eraseSourceFeedback(Request request) {
		if ((REQ_MOVE.equals(request.getType()) && isDragAllowed())
			|| REQ_CLONE.equals(request.getType())
			|| REQ_ADD.equals(request.getType())
			|| RequestConstants.REQ_DROP.equals(request.getType()))
			eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
	}

	/**
	 * Calls other methods as appropriate.
	 */
	public void showSourceFeedback(Request request) {
		if ((REQ_MOVE.equals(request.getType()) && isDragAllowed())
			|| REQ_ADD.equals(request.getType())
			|| REQ_CLONE.equals(request.getType())
			|| RequestConstants.REQ_DROP.equals(request.getType()))
			showChangeBoundsFeedback((ChangeBoundsRequest) request);
	}

	/**
	 * Shows or updates feedback for a change bounds request.
	 * 
	 * @param request
	 *            the request
	 */
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		IBorderItemEditPart borderItemEP = (IBorderItemEditPart) getHost();
		IBorderItemLocator borderItemLocator = borderItemEP.getBorderItemLocator();
		
		if (borderItemLocator != null) {
			IFigure feedback = getDragSourceFeedbackFigure();
			PrecisionRectangle rect = new PrecisionRectangle(
				getInitialFeedbackBounds().getCopy());
			getHostFigure().translateToAbsolute(rect);
			rect.translate(request.getMoveDelta());
			rect.resize(request.getSizeDelta());
			getHostFigure().translateToRelative(rect);
			Rectangle realLocation = borderItemLocator.getValidLocation(rect
				.getCopy(), borderItemEP.getFigure());
			getHostFigure().translateToAbsolute(realLocation);
			feedback.translateToRelative(realLocation);
			feedback.setBounds(realLocation);
		}
	}

	/**
	 * Returns the command contribution to a change bounds request.
	 * 
	 * @param request
	 *            the change bounds requesgt
	 * @return the command contribution to the request
	 */
	protected Command getMoveCommand(ChangeBoundsRequest request) {
		IBorderItemEditPart borderItemEP = (IBorderItemEditPart) getHost();
		IBorderItemLocator borderItemLocator = borderItemEP.getBorderItemLocator();
		
		if (borderItemLocator != null) {
			PrecisionRectangle rect = new PrecisionRectangle(
				getInitialFeedbackBounds().getCopy());
			getHostFigure().translateToAbsolute(rect);
			rect.translate(request.getMoveDelta());
			rect.resize(request.getSizeDelta());

			getHostFigure().translateToRelative(rect);
			Rectangle realLocation = borderItemLocator.getValidLocation(rect
				.getCopy(), borderItemEP.getFigure());

			Point parentOrigin = ((IGraphicalEditPart) borderItemEP.getParent())
				.getFigure().getBounds().getTopLeft();
			Dimension d = realLocation.getTopLeft().getDifference(parentOrigin);
			Point location = new Point(d.width, d.height);

			ICommand moveCommand = new SetBoundsCommand(borderItemEP.getEditingDomain(),
				DiagramUIMessages.Commands_MoveElement, new EObjectAdapter(
					(View) getHost().getModel()), location);
			return new ICommandProxy(moveCommand);
		}
		return null;
	}

	/** Return <tt>null</tt> to avoid handling the request. */
	protected Command getAlignCommand(AlignmentRequest request) {
		return null;
	}

}