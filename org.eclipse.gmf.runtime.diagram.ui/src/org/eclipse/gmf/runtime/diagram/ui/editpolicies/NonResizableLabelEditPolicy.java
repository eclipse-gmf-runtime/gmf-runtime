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

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.LabelEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.LabelHelper;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.tools.DragEditPartsTrackerEx;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * EditPolicy which moves a label relative to it parent.
 * 
 * @author jcorchis
 */
public class NonResizableLabelEditPolicy
	extends NonResizableEditPolicyEx {

	private Polyline tether = null;

	protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		super.eraseChangeBoundsFeedback(request);
		if (tether != null)
			removeFeedback(tether);
		tether = null;
	}

	protected IFigure createDragSourceFeedbackFigure() {
		IFigure feedback = super.createDragSourceFeedbackFigure();
		tether = new Polyline();
		tether.setLineStyle(Graphics.LINE_DASHDOT);
		tether.setForegroundColor(((IGraphicalEditPart) getHost()).getFigure()
			.getForegroundColor());
		addFeedback(tether);
		return feedback;
	}

	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		super.showChangeBoundsFeedback(request);

		IFigure p = getDragSourceFeedbackFigure();
		Rectangle r = p.getBounds();
		Point refPoint = ((LabelEditPart) getHost()).getReferencePoint();

		// translate the feedback figure
		PrecisionRectangle rect = new PrecisionRectangle(
			getInitialFeedbackBounds().getCopy());
		getHostFigure().translateToAbsolute(rect);
		rect.translate(request.getMoveDelta());
		rect.resize(request.getSizeDelta());
		p.translateToRelative(rect);
		p.setBounds(rect);

		Rectangle centerMain = null;
		// TODO: remove this hack. We should be using the reference point for
		// the teher end, however,
		// the reference point is causing miscaculation when positioning. This
		// has to be redone in version 2.
		if (((IGraphicalEditPart) getHost().getParent()).getFigure() instanceof Connection) {
			centerMain = new Rectangle(refPoint.x, refPoint.y, 0, 0);
			getHostFigure().translateToAbsolute(centerMain);
			p.translateToRelative(centerMain);
		} else {
			centerMain = ((IGraphicalEditPart) getHost().getParent())
				.getFigure().getBounds().getCopy();
			centerMain.translate(centerMain.width / 2, centerMain.height / 2);
			getHostFigure().translateToAbsolute(centerMain);
			p.translateToRelative(centerMain);
		}

		PrecisionRectangle ref = new PrecisionRectangle(centerMain);

		Point midTop = new Point(r.x + r.width / 2, r.y);
		Point midBottom = new Point(r.x + r.width / 2, r.y + r.height);
		Point midLeft = new Point(r.x, r.y + r.height / 2);
		Point midRight = new Point(r.x + r.width, r.y + r.height / 2);

		Point startPoint = midTop;

		int x = r.x + r.width / 2 - refPoint.x;
		int y = r.y + r.height / 2 - refPoint.y;

		if (y > 0 && y > x && y > -x)
			startPoint = midTop;
		else if (y < 0 && y < x && y < -x)
			startPoint = midBottom;
		else if (x < 0 && y > x && y < -x)
			startPoint = midRight;
		else
			startPoint = midLeft;

		tether.setStart(startPoint);
		tether.setEnd(ref.getLocation());
	}

	protected Command getMoveCommand(ChangeBoundsRequest request) {
		LabelEditPart editPart = (LabelEditPart) getHost();
		Point refPoint = editPart.getReferencePoint();

		// translate the feedback figure
		PrecisionRectangle rect = new PrecisionRectangle(
			getInitialFeedbackBounds().getCopy());
		getHostFigure().translateToAbsolute(rect);
		rect.translate(request.getMoveDelta());
		rect.resize(request.getSizeDelta());
		getHostFigure().translateToRelative(rect);

		Point normalPoint = LabelHelper.offsetFromRelativeCoordinate(
			getHostFigure(), rect, refPoint);

		ICommand moveCommand = new SetBoundsCommand(editPart.getEditingDomain(), DiagramUIMessages.MoveLabelCommand_Label_Location,
			new EObjectAdapter((View) editPart.getModel()), normalPoint);
		return new ICommandProxy(moveCommand);
	}

	/**
	 * Overridden to use a customized drag tracker for the handles. The
	 * <code>isMove()</code> method of the drag tracker needs to be overridden
	 * as the parent of the label and connection will not be the same as the
	 * target editpart, instead it returns true always since labels can only be
	 * moved and not resized.
	 * 
	 * @return a drag tracker
	 */
	protected DragTracker createSelectionHandleDragTracker() {
		return new DragEditPartsTrackerEx(getHost()) {

			protected boolean isMove() {
				return true;
			}
		};
	}

	public EditPart getTargetEditPart(Request request) {
		if (RequestConstants.REQ_SNAP_BACK.equals(request.getType()))
			return getHost();
		return super.getTargetEditPart(request);
	}

	public boolean understandsRequest(Request request) {
		if (RequestConstants.REQ_SNAP_BACK.equals(request.getType()))
			return true;
		return super.understandsRequest(request);
	}

}
