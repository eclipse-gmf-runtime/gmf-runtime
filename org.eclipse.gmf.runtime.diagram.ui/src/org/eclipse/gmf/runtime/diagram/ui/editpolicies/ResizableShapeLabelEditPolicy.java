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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.LabelHelper;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Edit policy which supports
 * 
 * @author jcorchis
 */
public class ResizableShapeLabelEditPolicy
	extends ResizableShapeEditPolicy {

	private Polyline tether = null;

	/**
	 * Removes the tether from the feedback
	 * 
	 * @see NonResizableEditPolicy#eraseChangeBoundsFeedback(ChangeBoundsRequest)
	 */
	protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		super.eraseChangeBoundsFeedback(request);
		if (tether != null)
			removeFeedback(tether);
		tether = null;
	}

	/**
	 * Add the tether to the feedback
	 * 
	 * @see NonResizableEditPolicy#createDragSourceFeedbackFigure()
	 */
	protected IFigure createDragSourceFeedbackFigure() {
		IFigure feedback = super.createDragSourceFeedbackFigure();
		tether = new Polyline();
		tether.setLineStyle(Graphics.LINE_DASHDOT);
		tether.setForegroundColor(((IGraphicalEditPart) getHost().getParent())
			.getFigure().getForegroundColor());
		addFeedback(tether);
		return feedback;
	}

	protected Command getMoveCommand(ChangeBoundsRequest request) {
		Point refPoint = getReferencePoint();
		PointList pl = new PointList();
		if (getHost().getParent() instanceof AbstractConnectionEditPart) {
			pl = ((AbstractConnectionEditPart) getHost().getParent())
				.getConnectionFigure().getPoints();
		} else {
			pl.addPoint(getReferencePoint());
		}

		// translate the feedback figure
		PrecisionRectangle rect = new PrecisionRectangle(
			getInitialFeedbackBounds().getCopy());
		getHostFigure().translateToAbsolute(rect);
		rect.translate(request.getMoveDelta());
		rect.resize(request.getSizeDelta());
		getHostFigure().translateToRelative(rect);
		adjustRect(rect);

		Point normalPoint = LabelHelper.offsetFromRelativeCoordinate(
			getHostFigure(), rect, refPoint);

		ICommand moveCommand = new SetBoundsCommand(DiagramUIMessages.MoveLabelCommand_Label_Location,
			new EObjectAdapter((View) getHost().getModel()), normalPoint);
		return new EtoolsProxyCommand(moveCommand);
	}

	/**
	 * adjust the rectangle used for the move command; the default implementatin
	 * assumes Cenerting behavior, clients can override this function to change
	 * this behvior
	 * 
	 * @param rect
	 *            Rect to adjust
	 */
	protected void adjustRect(PrecisionRectangle rect) {
		// Componsate for the fact that we are using the
		// figure center
		rect.translate(rect.width / 2, rect.height / 2);
	}

	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		super.showChangeBoundsFeedback(request);

		IFigure p = getDragSourceFeedbackFigure();
		Rectangle r = p.getBounds();
		Point refPoint = getReferencePoint();

		// translate the feedback figure
		PrecisionRectangle rect = new PrecisionRectangle(
			getInitialFeedbackBounds().getCopy());
		getHostFigure().translateToAbsolute(rect);
		rect.translate(request.getMoveDelta());
		rect.resize(request.getSizeDelta());
		p.translateToRelative(rect);
		p.setBounds(rect);

		// translate the refPoint
		PrecisionRectangle ref = new PrecisionRectangle(new Rectangle(
			refPoint.x, refPoint.y, 0, 0));
		getHostFigure().translateToAbsolute(ref);
		p.translateToRelative(ref);

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

	/**
	 * @param request
	 *            the resize request
	 * @return the command contribution obtained from the parent
	 */
	protected Command getResizeCommand(ChangeBoundsRequest request) {

		PrecisionRectangle rect = new PrecisionRectangle(
			getInitialFeedbackBounds().getCopy());
		getHostFigure().translateToAbsolute(rect);
		rect.translate(request.getMoveDelta());
		rect.resize(request.getSizeDelta());
		getHostFigure().translateToRelative(rect);
		View shapeView = (View) getHost().getModel();

		ICommand resizeCommand = new SetBoundsCommand(
			DiagramUIMessages.SetLocationCommand_Label_Resize,
			new EObjectAdapter(shapeView), rect.getSize());
		return new EtoolsProxyCommand(resizeCommand);
	}

	/**
	 * Helper method to calculate the reference point on the middle of the
	 * parent edge or the point given by the location of a node.
	 * 
	 * @return the reference point
	 */
	private Point getReferencePoint() {
		if (getHost().getParent() instanceof AbstractConnectionEditPart) {
			PointList ptList = ((AbstractConnectionEditPart) getHost()
				.getParent()).getConnectionFigure().getPoints();
			return PointListUtilities.calculatePointRelativeToLine(ptList, 0,
				50, true);
		} else {
			return ((GraphicalEditPart) getHost().getParent()).getFigure()
				.getBounds().getLocation();
		}

	}

}
