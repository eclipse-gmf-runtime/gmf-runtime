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
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.AlignmentRequest;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GateEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.GateFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.GateFigure.GateLocator;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Edit policy to restrict gate movement along border of Gated item.
 * 
 * @author jbruck
 */
public class GateNonResizableEditPolicy extends NonResizableEditPolicyEx {

	/**
	 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(org.eclipse.gef.Request)
	 */
	public void eraseSourceFeedback(Request request) {
		if ( (REQ_MOVE.equals(request.getType()) && isDragAllowed())
				|| REQ_CLONE.equals(request.getType())
				|| REQ_ADD.equals(request.getType())
				|| RequestConstants.REQ_DROP.equals(request.getType()) )
			eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
	}

	/**
	 * Calls other methods as appropriate.
	 * @see org.eclipse.gef.EditPolicy#showSourceFeedback(org.eclipse.gef.Request)
	 */
	public void showSourceFeedback(Request request) {
		if ( (REQ_MOVE.equals(request.getType()) && isDragAllowed())
				|| REQ_ADD.equals(request.getType())
				|| REQ_CLONE.equals(request.getType())
				|| RequestConstants.REQ_DROP.equals(request.getType()) )
			showChangeBoundsFeedback((ChangeBoundsRequest) request);
	}

	/**
	 * Shows or updates feedback for a change bounds request.
	 * @param request the request
	 */
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		GateEditPart gateEP = (GateEditPart) getHost();
		GateLocator gateLocator = (GateFigure.GateLocator) gateEP.getLocator();
		IFigure feedback = getDragSourceFeedbackFigure();
		PrecisionRectangle rect = new PrecisionRectangle(
				getInitialFeedbackBounds().getCopy());
		getHostFigure().translateToAbsolute(rect);
		rect.translate(request.getMoveDelta());
		rect.resize(request.getSizeDelta());
		getHostFigure().translateToRelative(rect);
		Rectangle realLocation = gateLocator.locateOnBorder(rect.getCopy());
		getHostFigure().translateToAbsolute(realLocation);
		feedback.translateToRelative(realLocation);
		feedback.setBounds(realLocation);
	}

	/**
	 * Returns the command contribution to a change bounds request. 
	 * @param request the change bounds requesgt
	 * @return the command contribution to the request
	 */
	protected Command getMoveCommand(ChangeBoundsRequest request) {
		GateEditPart gateEP = (GateEditPart)getHost();
		GateLocator gateLocator = (GateFigure.GateLocator)gateEP.getLocator();
	
		PrecisionRectangle rect = new PrecisionRectangle(getInitialFeedbackBounds().getCopy());
		getHostFigure().translateToAbsolute(rect);
		rect.translate(request.getMoveDelta());
		rect.resize(request.getSizeDelta());
		
		getHostFigure().translateToRelative(rect);
		Rectangle realLocation = gateLocator.locateOnBorder(rect.getCopy());
		Point location = gateLocator.getRelativeToBorder(realLocation.getTopLeft());

		ICommand moveCommand = 
			new SetBoundsCommand(
				PresentationResourceManager.getI18NString("Commands.MoveElement"),//$NON-NLS-1$
				new EObjectAdapter((View) getHost().getModel()),
				location);
		return new EtoolsProxyCommand(moveCommand);
	}
	/** Return <tt>null</tt> to avoid handling the request. */
	protected Command getAlignCommand(AlignmentRequest request) {
		return null;
	}
}