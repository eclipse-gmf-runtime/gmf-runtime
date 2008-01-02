/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.editpolicies;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.AutomaticRouter;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointLocator;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.gef.AccessibleHandleProvider;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
import org.eclipse.gef.handles.BendpointCreationHandle;
import org.eclipse.gef.handles.BendpointMoveHandle;
import org.eclipse.gef.requests.BendpointRequest;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.FeedbackConnection;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.gef.ui.internal.handles.BendpointCreationInvisibleHandle;
import org.eclipse.gmf.runtime.gef.ui.internal.handles.BendpointMoveHandleEx;
import org.eclipse.gmf.runtime.gef.ui.internal.handles.LineSegMoveInvisibleHandle;

/**
 * This EditPolicy defines the behavior of Bendpoints on a Connection.
 */
/*
 * @canBeSeenBy org.eclipse.gmf.runtime.gef.ui.*
 */
abstract public class ConnectionBendpointEditPolicy
	extends SelectionHandlesEditPolicy
	implements PropertyChangeListener {

	private static List NULL_CONSTRAINT = new ArrayList();
	private static final int STICKY_TOLERANCE_DP = 6;

	private LineMode lineSegMode = LineMode.OBLIQUE;
	
	static private class FeedbackState {
		public List originalConstraint;
		public Point ref1 = new Point();
		public Point ref2 = new Point();
		public boolean isDeleting = false;
		public boolean isOutsideSource = false;
		public boolean isOutsideTarget = false;
		public boolean init = false;
	}
	
	private FeedbackState feedbackState = null;
	
	private FeedbackState getFeedbackState() {
		if (feedbackState == null) {
			feedbackState = new FeedbackState();
		}
		
		return feedbackState;
	}
	
	private boolean useRealtimeFeedback() {
		return false;
	}
	
	/**
	 * Constructor for EditPolicy
	 * @param lineSegMode
	 */
	public ConnectionBendpointEditPolicy(LineMode lineSegMode) {
		super();
		this.lineSegMode = lineSegMode;
	}

	/**
	 * @return Returns the lineSegMode.
	 */
	public LineMode getLineSegMode() {
		return lineSegMode;
	}

	/** 
	 * Adds a PropertyChangeListener to the Connection so we can react
	 * to point changes in the connection.  
	 *
	 * @see SelectionHandlesEditPolicy#activate()
	 */
	public void activate() {
		super.activate();
		getConnection().addPropertyChangeListener(
			Connection.PROPERTY_POINTS,
			this);
	}

	/**
	 * @return <code>Connection</code> representing drag source feedback
	 */
	protected Connection createDragSourceFeedbackConnection() {
		if (useRealtimeFeedback()) {
			// Use the actual figure for feedback
			return getConnection();
		} else {
			// Use a ghost rectangle for feedback
			FeedbackConnection r = new FeedbackConnection(getConnection());
			addFeedback(r);
			return r;
		}
	}

	/**
	 * Adds selection handles to the connection for the bendpoints.  In this class,
	 * this method just decides if it is appropriate to add the handles, and then
	 * calls on the superclass to do the dirty work.s
	 */
	protected void addSelectionHandles() {
		if (handles == null)
			super.addSelectionHandles();
		else {
			int count = handles.size();
			int points = getConnection().getPoints().size();
			if (count != points * 2 - 3)
				super.addSelectionHandles();
		}
	}

	/**
	 * @return list of manual handles
	 */
	protected List createManualHandles() {
		List list = new ArrayList();
		ConnectionEditPart connEP = (ConnectionEditPart) getHost();
		PointList points = getConnection().getPoints();
		for (int i = 1; i < points.size() - 1; i++) {
			addInvisibleCreationHandle(list, connEP, i - 1);
			list.add(
				new BendpointMoveHandleEx(
					connEP,
					i,
					new BendpointLocator(getConnection(), i)));
		}		
		addInvisibleCreationHandle(list, connEP, points.size() - 2);
		return list;
	}

	/**
	 * Method addInvisibleCreationHandle.
	 * This handle is necessary for the accessibility feature to allow keyboard navigation to
	 * the add bendpoint feature.
	 * @param list
	 * @param connEP
	 * @param i
	 */
	protected void addInvisibleCreationHandle(
		List list,
		ConnectionEditPart connEP,
		int i) {
		if (getLineSegMode() != LineMode.OBLIQUE) {
			list.add(new LineSegMoveInvisibleHandle(connEP, i));
		} else {
			list.add(new BendpointCreationInvisibleHandle(connEP, i));
		}
	}

	/**
	 * Creates selection handles for the bendpoints.  Explicit (user-defined)
	 * bendpoints will have {@link BendpointMoveHandle}s on them with a single 
	 * {@link BendpointCreationHandle} between 2 consecutive explicit bendpoints.
	 * If implicit bendpoints (such as those created by the {@link AutomaticRouter})
	 * are used, one {@link BendpointCreationHandle} is placed in the middle
	 * of the Connection.
	 */
	protected List createSelectionHandles() {
		List list = new ArrayList();
		list = createManualHandles();
		return list;
	}

	/**
	 * Removes this from the Connection's list of PropertyChangeListeners.
	 *
	 * @see SelectionHandlesEditPolicy#deactivate()
	 */
	public void deactivate() {
		getConnection().removePropertyChangeListener(
			Connection.PROPERTY_POINTS,
			this);

		super.deactivate();
	}

	/**
	 * Erases bendpoint feedback.  Since the original figure is used
	 * for feedback, we just restore the original constraint that
	 * was saved before feedback started to show.
	 */
	protected void eraseConnectionFeedback(
		BendpointRequest request,
		boolean removeFeedbackFigure) {
		restoreOriginalConstraint();
		getFeedbackState().originalConstraint = null;
		if (removeFeedbackFigure)
			feedbackState = null;
	}

	/**
	 * Erases feedback, when appropriate.
	 *
	 * @see #eraseConnectionFeedback(BendpointRequest, boolean)
	 */
	public void eraseSourceFeedback(Request request) {
		if (REQ_MOVE_BENDPOINT.equals(request.getType())
			|| REQ_CREATE_BENDPOINT.equals(request.getType()))
			eraseConnectionFeedback((BendpointRequest) request, true);
	}

	/**
	 * Returns the appropriate Command for the request type given.  Handles
	 * creating, moving and deleting bendpoints.  The actual creation of the
	 * command is taken care of by subclasses implementing the appropriate
	 * methods.
	 *
	 * @see #getCreateBendpointCommand(BendpointRequest)
	 * @see #getMoveBendpointCommand(BendpointRequest)
	 * @see #getDeleteBendpointCommand(BendpointRequest)
	 */
	public Command getCommand(Request request) {
		if (REQ_MOVE_BENDPOINT.equals(request.getType())) {
			if (getLineSegMode() != LineMode.OBLIQUE) {
				return getMoveLineSegCommand((BendpointRequest) request);
			} else {
				if (getFeedbackState().isDeleting)
					return getDeleteBendpointCommand(
						(BendpointRequest) request);
				return getMoveBendpointCommand((BendpointRequest) request);
			}
		}
		if (REQ_CREATE_BENDPOINT.equals(request.getType()))
			return getCreateBendpointCommand((BendpointRequest) request);

		return null;
	}

	/**
	 * Returns the Connection associated with this EditPolicy.
	 */
	protected Connection getConnection() {
		return (Connection) ((ConnectionEditPart) getHost()).getFigure();
	}

	/**
	 * @return Point cached value representing the first reference point.
	 */
	private Point getFirstReferencePoint() {
		return getFeedbackState().ref1;
	}

	/**
	 * @return Point cached value representing the second reference point.
	 */
	private Point getSecondReferencePoint() {
		return getFeedbackState().ref2;
	}

	/**
	 * Utility method to determine if point p passes through the line segment 
	 * defined by p1 and p2.
	 * 
	 * @param p1 Point that is the first point in the line segment to test against.
	 * @param p2 Point that is the second point in the line segment to test against.
	 * @param p Point that is tested to see if it falls in the line segment defined by p1 and p2.
	 * @return true if line segment contains Point p, false otherwise.
	 */
	private boolean lineContainsPoint(Point p1, Point p2, Point p) {
		LineSeg line = new LineSeg(p1, p2);
		return line.containsPoint(p, getStickyTolerance() / 3);
	}

	/**
	 * Adds selection handles to the Connection, if it is selected, when the points 
	 * property changes.  Since we only listen for changes in the points property, 
	 * this method is only called when the points of the Connection have changed.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (getHost().getSelected() != EditPart.SELECTED_NONE) {
//			int count = handles.size();
//			int points = getConnection().getPoints().size();
//			if (count != points * 2 - 3)
				addSelectionHandles();
		}
	}

	/**
	 * Restores the original constraint that was saved before feedback
	 * began to show.
	 */
	protected void restoreOriginalConstraint() {
		if (getFeedbackState().originalConstraint != null) {
			Assert.isTrue(getFeedbackState().originalConstraint.size() >= 2);
			getConnection().setRoutingConstraint(
				getFeedbackState().originalConstraint);
		}
	}

	/**
	 * Since the original figure is used for feedback, this method saves the 
	 * original constraint, so that is can be restored when the feedback is
	 * erased.
	 */
	protected void saveOriginalConstraint() {
		getFeedbackState().originalConstraint = (List)
			getConnection().getRoutingConstraint();
		if (getFeedbackState().originalConstraint == null)
			getFeedbackState().originalConstraint = NULL_CONSTRAINT;

		if (getLineSegMode() != LineMode.OBLIQUE && !getFeedbackState().init) {
			// Update the constraint based on the current figure
			List newConstraint = new ArrayList(getFeedbackState().originalConstraint.size());
			PointList pts =
				PointListUtilities.copyPoints(getConnection().getPoints());
			//OrthogonalRouterUtilities.resetEndPointsToCenter(getConnection(), pts);

			for (int i = 0; i < pts.size(); i++) {
				Bendpoint abp = new AbsoluteBendpoint(pts.getPoint(i));
				newConstraint.add(abp);
			}

			Assert.isTrue(getFeedbackState().originalConstraint.size() >= 2);
			getConnection().setRoutingConstraint(
				newConstraint);

			// reset booleans
			getFeedbackState().isOutsideSource = false;
			getFeedbackState().isOutsideTarget = false;
		} else {
			// if the constraint and the connection figure points list don't match then reset the constraint
			// based on the connection figure list.  This could happen in certain cases - sepcifically when
			// fan router detects a collision or when a self relation is routed.
			int nConstraintSize = getFeedbackState().originalConstraint.size();
			PointList pts = getConnection().getPoints();
			int nPointSize = pts.size();

			if (!getFeedbackState().init && nConstraintSize != nPointSize) {

				while (getFeedbackState().originalConstraint.size() > 0) {
					getFeedbackState().originalConstraint.remove(0);
				}

				for (int i = 0; i < pts.size(); i++) {
					Bendpoint bpNew = new AbsoluteBendpoint(pts.getPoint(i));
					getFeedbackState().originalConstraint.add(i, bpNew);
				}
			}

			Assert.isTrue(getFeedbackState().originalConstraint.size() >= 2);
			getConnection().setRoutingConstraint(
				new ArrayList(getFeedbackState().originalConstraint));
		}
		
		getFeedbackState().init = true;
	}

	/**
	 * Method setReferencePoints.
	 * This method will calculate the two end reference points for a point that is
	 * being moved or created.  The reference points are used to determine if the 
	 * request point can be deleted or not (for straight line tolerance).
	 * 
	 * @param request BendpointRequest object containing index information.
	 */
	private void setReferencePoints(BendpointRequest request) {
		if (getFeedbackState().originalConstraint == null) {
			saveOriginalConstraint();
		}

		List constraint = (List)
			getConnection().getRoutingConstraint();
		Bendpoint bp = (Bendpoint) constraint.get(Math.max(0, request.getIndex() - 1));
		getFeedbackState().ref1 = bp.getLocation();

		bp = (Bendpoint) constraint.get(Math.min(request.getIndex() + 1, constraint.size() -1));
		getFeedbackState().ref2 = bp.getLocation();
	}

	private void setNewFeedbackConstraint(List constraint) {
		Assert.isTrue(constraint.size() >= 2);
		getConnection().setRoutingConstraint(constraint);
	}

	/**
	 * Shows feedback when a bendpoint is being created.  The original figure
	 * is used for feedback and the original constraint is saved, so that it
	 * can be restored when feedback is erased.
	 */
	protected void showCreateBendpointFeedback(BendpointRequest request) {
		Point p = new Point(request.getLocation());
		List constraint;
		
		getConnection().translateToRelative(p);

		Bendpoint bp = new AbsoluteBendpoint(p);
		if (getFeedbackState().originalConstraint == null) {
			saveOriginalConstraint();
			constraint = (List)
				getConnection().getRoutingConstraint();
			constraint.add(request.getIndex() + 1, bp);
		} else {
			constraint = (List)
				getConnection().getRoutingConstraint();
		}

		stickyStraightLineFeedback(constraint, request.getIndex() + 1, bp);

		setNewFeedbackConstraint(constraint);
	}

	/**
	 * Shows feedback when a bendpoint is being deleted.  This method is
	 * only called once when the bendpoint is first deleted, not every
	 * mouse move.  The original figure is used for feedback and the original 
	 * constraint is saved, so that it can be restored when feedback is erased.
	 */
	protected void showDeleteBendpointFeedback(BendpointRequest request) {
		if (getFeedbackState().originalConstraint == null) {
			saveOriginalConstraint();
			List constraint = (List)
				getConnection().getRoutingConstraint();
			constraint.remove(request.getIndex());

			setNewFeedbackConstraint(constraint);
		}
	}

	/**
	 * Shows feedback when a bendpoint is being moved.  Also checks to see if the bendpoint 
	 * should be deleted and then calls {@link #showDeleteBendpointFeedback(BendpointRequest)}
	 * if needed.  The original figure is used for feedback and the original constraint is 
	 * saved, so that it can be restored when feedback is erased.
	 */
	protected void showMoveBendpointFeedback(BendpointRequest request) {		
		Point p = new Point(request.getLocation());
		if (!getFeedbackState().isDeleting) {
			setReferencePoints(request);
		}

		getConnection().translateToRelative(p);
		Bendpoint bp = new AbsoluteBendpoint(p);

		if (getFeedbackState().originalConstraint == null) {
			saveOriginalConstraint();
		}

		if (lineContainsPoint(getFirstReferencePoint(),
			getSecondReferencePoint(),
			p)) {
			if (!getFeedbackState().isDeleting) {
				getFeedbackState().isDeleting = true;
				eraseConnectionFeedback(request, false);
				showDeleteBendpointFeedback(request);
			}
			return;
		}
		if (getFeedbackState().isDeleting) {
			getFeedbackState().isDeleting = false;
			eraseConnectionFeedback(request, false);
		}

		List constraint = (List)
			getConnection().getRoutingConstraint();
		stickyStraightLineFeedback(constraint, request.getIndex(), bp);

		setNewFeedbackConstraint(constraint);
	}

	/**
	 * This method will set the constraint with the given bendpoint, with the additional behavior of
	 * "sticking" the point around a tolerance to a straight line.  If it's within a tolerance of the
	 * previous point, stick it to the horizontal or vertical coordinates that make it straight.
	 */
	protected void stickyStraightLineFeedback(
		List constraint,
		int nIndex,
		Bendpoint bp) {
		Point ptLoc = new Point(bp.getLocation());
		
		int sticky_tolerance = getStickyTolerance();
		
		if (nIndex > 0) {
			Point ptPrev;
			if ((nIndex - 1) == 0) {
				ptPrev =
					getConnection()
						.getSourceAnchor()
						.getReferencePoint();
				getConnection().translateToRelative(ptPrev);
			} else
				ptPrev = ((Bendpoint) constraint.get(nIndex - 1)).getLocation();

			if (Math.abs(ptPrev.x - ptLoc.x) < sticky_tolerance)
				ptLoc.x = ptPrev.x;
			if (Math.abs(ptPrev.y - ptLoc.y) < sticky_tolerance)
				ptLoc.y = ptPrev.y;
		}

		if (nIndex < constraint.size() - 1) {
			Point ptNext;
			if ((nIndex + 1) == (constraint.size() - 1)) {
				ptNext =
					getConnection()
						.getTargetAnchor()
						.getReferencePoint();
				getConnection().translateToRelative(ptNext);
			} else
				ptNext = ((Bendpoint) constraint.get(nIndex + 1)).getLocation();

			if (Math.abs(ptNext.x - ptLoc.x) < sticky_tolerance)
				ptLoc.x = ptNext.x;
			if (Math.abs(ptNext.y - ptLoc.y) < sticky_tolerance)
				ptLoc.y = ptNext.y;
		}

		if (!ptLoc.equals(bp.getLocation())) {
			Bendpoint bpNew = new AbsoluteBendpoint(ptLoc);
			constraint.set(nIndex, bpNew);
		} else {
			constraint.set(nIndex, bp);
		}
	}

	private int getStickyTolerance() {
		int sticky_tolerance = MapModeUtil.getMapMode(getConnection()).DPtoLP(STICKY_TOLERANCE_DP);
		return sticky_tolerance;
	}

	/**
	 * Shows feedback, when appropriate.  Calls a different method
	 * depending on the request type.
	 *
	 * @see #showCreateBendpointFeedback(BendpointRequest)
	 * @see #showMoveBendpointFeedback(BendpointRequest)
	 */
	public void showSourceFeedback(Request request) {
		if (getLineSegMode() != LineMode.OBLIQUE) {
			if (REQ_CREATE_BENDPOINT.equals(request.getType())) {
				showMoveLineSegFeedback((BendpointRequest) request);
			} else if (REQ_MOVE_BENDPOINT.equals(request.getType())) {
				showMoveOrthogonalBenspointFeedback((BendpointRequest) request);
			}
			
		} else {
			if (REQ_MOVE_BENDPOINT.equals(request.getType()))
				showMoveBendpointFeedback((BendpointRequest) request);
			else if (REQ_CREATE_BENDPOINT.equals(request.getType()))
				showCreateBendpointFeedback((BendpointRequest) request);
		}

		super.showSourceFeedback(request);
	}

	/**
	 * Method getBendpointsChangedCommand.
	 * This method will return a SetBendpointsCommand with the points retrieved from
	 * the user feedback in the figure.
	 * @param request BendpointRequest from the user gesture for moving / creating a bendpoint
	 * @return Command SetBendpointsCommand that contains the point changes for the connection.
	 */
	abstract protected Command getBendpointsChangedCommand(BendpointRequest request);

	protected Command getCreateBendpointCommand(BendpointRequest request) {
		return getBendpointsChangedCommand(request);
	}

	protected Command getMoveBendpointCommand(BendpointRequest request) {
		return getBendpointsChangedCommand(request);
	}

	protected Command getDeleteBendpointCommand(BendpointRequest request) {
		return getBendpointsChangedCommand(request);
	}

	protected final LineSeg getLineSeg(List bendPoints, int nIndex) {
		Point pt1 =
			new Point(((Bendpoint) bendPoints.get(nIndex - 1)).getLocation());
		Point pt2 =
			new Point(((Bendpoint) bendPoints.get(nIndex)).getLocation());

		return new LineSeg(pt1, pt2);
	}

	/**
	 * @param bendPoints
	 * @param nIndex
	 * @param newLine
	 */
	protected void setLineSeg(List bendPoints, int nIndex, LineSeg newLine) {
		Bendpoint bp1 = new AbsoluteBendpoint(newLine.getOrigin());
		Bendpoint bp2 = new AbsoluteBendpoint(newLine.getTerminus());
		
		bendPoints.set(nIndex - 1, bp1);
		bendPoints.set(nIndex, bp2);
	}

	/**
	 * @param request
	 * @return move line segment command
	 */
	protected Command getMoveLineSegCommand(BendpointRequest request) {
		return getBendpointsChangedCommand(request);
	}


	/**
	* Method lineOutsideSource.
	* Utility method to determine if the constraint needs to be adjusted becauase the line is
	* outside of the source bounds.
	* 
	* @param line LineSeg defining the new line moved by the user gesture
	* @return boolean true if origin of line lies outside the starting source element, false otherwise.
	*/
	protected boolean lineOutsideSource(LineSeg line) {

		// check if end points are outside of bounds and if so - add a new point
		PrecisionRectangle startRect =
			new PrecisionRectangle(FigureUtilities.getAnchorableFigureBounds(getConnection().getSourceAnchor().getOwner()));
		getConnection().getSourceAnchor().getOwner().translateToAbsolute(
			startRect);
		if (getLineSegMode().equals(LineMode.ORTHOGONAL_CONSTRAINED)) {
			if (line.isHorizontal()) {
				startRect.shrink(0, 2);
			} else {
				startRect.shrink(2, 0);
			}
		}
		
		getConnection().translateToRelative(startRect);
		/*
		 * Rectangle needs to be expanded by the "odd" number below because the number after
		 * translations could be N.999999999... 
		 */
		if (!startRect.expand(0.000001, 0.000001).contains(new PrecisionPoint(line.getOrigin()))) {
			return true;
		}

		return false;
	}

	/**
	* Method lineOutsideTarget.
	* Utility method to determine if the constraint needs to be adjusted because the line is
	* outside of the target bounds.
	* 
	* @param line LineSeg defining the new line moved by the user gesture.
	* @return boolean true if terminus of line lies outside the target element, false otherwise.
	*/
	protected boolean lineOutsideTarget(LineSeg line) {

		// check if end points are outside of bounds and if so - add a new point
		PrecisionRectangle endRect =
			new PrecisionRectangle(FigureUtilities.getAnchorableFigureBounds(getConnection().getTargetAnchor().getOwner()));
		getConnection().getTargetAnchor().getOwner().translateToAbsolute(
			endRect);
		if (getLineSegMode().equals(LineMode.ORTHOGONAL_CONSTRAINED)) {
			if (line.isHorizontal()) {
				endRect.shrink(0, 2);
			} else {
				endRect.shrink(2, 0);
			}
		}
		
		/*
		 * Rectangle needs to be expanded by the "odd" number below because the number after
		 * translations could be N.999999999... 
		 */
		getConnection().translateToRelative(endRect);
		if (!endRect.expand(0.00001, 0.00001).contains(new PrecisionPoint(line.getTerminus()))) {
			return true;
		}

		return false;
	}

	/**
	* Method removeOutsideSourceFeedback.
	* Removes a bendpoint from the beginning of the constraint.
	* 
	* @param constraint List of bendpoints that the source point will be added too.
	*/
	protected void removeOutsideSourceFeedback(List constraint) {
		constraint.remove(0);
	}

	/**
	* Method removeOutsideTargetFeedback.
	* Removes a bendpoint from the end of the constraint.
	* 
	* @param constraint List of bendpoints that the target point will be added too.
	*/
	protected void removeOutsideTargetFeedback(List constraint) {
		constraint.remove(constraint.size() - 1);
	}
	
	/**
	 * Draws feedback for moving a bend point of a rectilinear connection
	 * 
	 * @param request Benndpoint request
	 */
	private void showMoveOrthogonalBenspointFeedback(BendpointRequest request) {
		if (getFeedbackState().originalConstraint == null) {
			saveOriginalConstraint();
		}
		
		Point ptLoc = new Point(request.getLocation());
		List constraint = (List)
			getConnection().getRoutingConstraint();
		
		getConnection().translateToRelative(ptLoc);
		
		int index =
		getFeedbackState().isOutsideSource ? request.getIndex() + 1 : request.getIndex();
		
		Point previous = ((Bendpoint)constraint.get(index - 1)).getLocation();
		Point moving = ((Bendpoint)constraint.get(index)).getLocation();
		Point next = ((Bendpoint)constraint.get(index + 1)).getLocation();
		
		LineSeg originalFirst = new LineSeg(previous.getCopy(), moving.getCopy());
		LineSeg originalSecond = new LineSeg(moving.getCopy(), next.getCopy());
		
		Dimension diff = ptLoc.getDifference(moving);
		
		if (originalFirst.isHorizontal()) {
			previous.y += diff.height;
			next.x += diff.width;
		} else {
			previous.x += diff.width;
			next.y += diff.height;
		}
		
		LineSeg movedFirst = new LineSeg(previous, ptLoc.getCopy());
		LineSeg movedSecond = new LineSeg(ptLoc.getCopy(), next);
		
		index = adjustOutsideBoundsLineFeedback(movedFirst, index - 1, constraint, originalFirst);
		constraint.set(index, new AbsoluteBendpoint(movedFirst.getOrigin()));
		constraint.set(index + 1, new AbsoluteBendpoint(movedFirst.getTerminus()));
		
		index = adjustOutsideBoundsLineFeedback(movedSecond, index + 1, constraint, originalSecond);
		constraint.set(index + 1, new AbsoluteBendpoint(movedSecond.getTerminus()));
		
		getConnection().setRoutingConstraint(constraint);	}

	/**
	* Shows feedback when a line segment is being moved.  Also checks to see if the bendpoint 
	* should be deleted and then calls {@link #showDeleteBendpointFeedback(BendpointRequest)}
	* if needed.  The original figure is used for feedback and the original constraint is 
	* saved, so that it can be restored when feedback is erased.
	*/
	protected void showMoveLineSegFeedback(BendpointRequest request) {

		if (getFeedbackState().originalConstraint == null) {
			saveOriginalConstraint();
		}

		Point ptLoc = new Point(request.getLocation());
		List constraint = (List)
			getConnection().getRoutingConstraint();
		
		getConnection().translateToRelative(ptLoc);
		
		// adjust request index to account for source bendpoint if needed
		int index =
			getFeedbackState().isOutsideSource ? request.getIndex() + 1 : request.getIndex();

		LineSeg moveLine = getLineSeg(constraint, index + 1);
		LineSeg newLine = moveLine.getParallelLineSegThroughPoint(ptLoc);
				
		index = adjustOutsideBoundsLineFeedback(newLine, index, constraint, moveLine);
		
		setLineSeg(constraint, index + 1, newLine);
		
		getConnection().setRoutingConstraint(constraint);
	}
	
	/**
	 * adjustOutsideBoundsLineFeedback
	 * Method to handle feedback where the line is dragged outside of the source or target shapes bounding box.
	 * 
	 * @param newLine LineSeg representing the line currently being manipulated.
	 * @param index the index
	 * @param constraint List of Bendpoint objects that is the constraint to the gesture.
	 * @param moveLine original segment that is being manipulated
	 * @return int new index value after the constraint and feedback have been adjusted.
	 */
	private int adjustOutsideBoundsLineFeedback(LineSeg newLine, int index, List constraint, LineSeg moveLine) {
		if (getLineSegMode().equals(LineMode.ORTHOGONAL_CONSTRAINED)) {
			// merely enforce the fact that we can't adjust the line outside the bounds of the source and target.
			if ((index == 0 && lineOutsideSource(newLine)) ||
				((index + 1 == constraint.size() - 1)&& lineOutsideTarget(newLine))) {
				newLine.setOrigin(moveLine.getOrigin());
				newLine.setTerminus(moveLine.getTerminus());
			}

			return index;
		}
		
		boolean bRemoveSource = false;
		boolean bRemoveTarget = false;
		boolean bSetNewSource = false;
		boolean bSetNewTarget = false;

		// Check source to see if we need to add a bendpoint
		if (index == 0 && lineOutsideSource(newLine)) {
			if (!getFeedbackState().isOutsideSource) {
				getFeedbackState().isOutsideSource = true;
				bSetNewSource = true;
			}
		} else if (index == 1 && getFeedbackState().isOutsideSource && !lineOutsideSource(newLine)) {
			getFeedbackState().isOutsideSource = false;
			bRemoveSource = true;
		}
		
		// Check target to see if we need to add a bendpoint
		int checkTargetIndex = index + 1 + (getFeedbackState().isOutsideTarget ? 1 : 0);
		if ((checkTargetIndex == constraint.size() - 1)
			&& lineOutsideTarget(newLine)) {
			if (!getFeedbackState().isOutsideTarget) {
				getFeedbackState().isOutsideTarget = true;
				bSetNewTarget = true;
			}
		} else if (checkTargetIndex == constraint.size() - 2 && getFeedbackState().isOutsideTarget
				&& !lineOutsideTarget(newLine)) {
			getFeedbackState().isOutsideTarget = false;
			bRemoveTarget = true;
		}
		if (bRemoveSource) {
			removeOutsideSourceFeedback(constraint);
			index--;
		}
		
		if (bRemoveTarget) {
			removeOutsideTargetFeedback(constraint);
		}

		if (bSetNewSource) {
			showOutsideSourceFeedback(newLine, moveLine, constraint);
			index++;
		}

		if (bSetNewTarget) {
			showOutsideTargetFeedback(newLine, moveLine, constraint);
		}
		return index;
	}
	
	/**
	* Method showOutsideSourceFeedback.
	* Adds a bendpoint to the beginning of the constraint.
	* Also adjusts the new segment with respect to added constraint
	* 
	* @param constraint List of bendpoints that the source point will be added too.
	*/
	private void showOutsideSourceFeedback(LineSeg newLine, LineSeg moveLine, List constraint) {
		Connection conn = (Connection)getHostFigure();
		ConnectionAnchor anchor = conn.getSourceAnchor();
		PrecisionPoint startPoint = new PrecisionPoint(anchor.getOwner().getBounds().getCenter());
		anchor.getOwner().translateToAbsolute(startPoint);
		conn.translateToRelative(startPoint);
		PrecisionRectangle bounds = new PrecisionRectangle(anchor.getOwner().getBounds());
		anchor.getOwner().translateToAbsolute(bounds);
		conn.translateToRelative(bounds);
		Point origin = new Point(newLine.getOrigin());
		if (moveLine.isHorizontal()) {
			origin.x = startPoint.x;
		} else {
			origin.y = startPoint.y;
		}
		newLine.setOrigin(origin);
		constraint.add(0, new AbsoluteBendpoint(startPoint));
		
	}

	/**
	* Method showOutsideTargetFeedback.
	* Adds a bendpoint to the end of the constraint.
	* Also adjusts the new segment with respect to added constraint
	* 
	* @param constraint List of bendpoints that the target point will be added too.
	*/
	private void showOutsideTargetFeedback(LineSeg newLine, LineSeg moveLine, List constraint) {
		Connection conn = (Connection)getHostFigure();
		ConnectionAnchor anchor = conn.getTargetAnchor();
		PrecisionPoint endPoint = new PrecisionPoint(anchor.getOwner().getBounds().getCenter());
		anchor.getOwner().translateToAbsolute(endPoint);
		conn.translateToRelative(endPoint);
		PrecisionRectangle bounds = new PrecisionRectangle(anchor.getOwner().getBounds());
		anchor.getOwner().translateToAbsolute(bounds);
		conn.translateToRelative(bounds);
		Point terminus = new Point(newLine.getTerminus()); 
		if (moveLine.isHorizontal()) {
			terminus.x = endPoint.x;
		} else {
			terminus.y = endPoint.y;
		}
		newLine.setTerminus(terminus);
		constraint.add(new AbsoluteBendpoint(endPoint));
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
