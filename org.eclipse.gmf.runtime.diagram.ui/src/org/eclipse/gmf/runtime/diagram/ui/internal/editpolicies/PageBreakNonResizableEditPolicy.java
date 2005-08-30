/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.PageBreakEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.PageBreaksFigure;

/**
 * Edit policy that handles the _feedback of the page breaks figure
 * while it is being dragged.  The _feedback changes the number of pages,
 * size and location of the feeback figure based on the location of the 
 * diagram bounds.
 * 
 * @author jcorchis
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class PageBreakNonResizableEditPolicy
    extends SelectionHandlesEditPolicy {

    //private PageBreaksFigure _feedback = null;
    private Point _paperSize;
    private Rectangle _newBounds;
    private int _rows, _cols;
//    private PositionConstants _direction;

    private PageBreaksFigure _feedback;
    private Rectangle _originalLocation;

    protected IFigure createDragSourceFeedbackFigure() {
        IFigure pageBreaksFeedbackFigure = createPageBreakFeedbackFigure();

        if (pageBreaksFeedbackFigure != null) {
            addFeedback(pageBreaksFeedbackFigure);
        }

        return pageBreaksFeedbackFigure;
    }

    /**
     * Method createTextFeedbackFigure.
     * @return IFigure
     */
    protected IFigure createPageBreakFeedbackFigure() {
        PageBreaksFigure feedBackFigure =
            new PageBreaksFigure(PageBreaksFigure.FEEDBACK);
        PageBreakEditPart pageBreakEditPart = (PageBreakEditPart) getHost();
        PageBreaksFigure pageBreakFigure =
            (PageBreaksFigure) pageBreakEditPart.getFigure();
        _paperSize = pageBreakEditPart.getPageSize();
        feedBackFigure.setPageCount(
            pageBreakFigure.getPageCount().x,
            pageBreakFigure.getPageCount().y);
        feedBackFigure.setBounds(getBounds());
        _rows = pageBreakFigure.getPageCount().x;
        _cols = pageBreakFigure.getPageCount().y;

        _paperSize = ((PageBreakEditPart) getHost()).getPageSize();

        return feedBackFigure;

    }

    protected List createSelectionHandles() {
        return java.util.Collections.EMPTY_LIST;
    }

    public void deactivate() {
        if (_feedback != null) {
            removeFeedback(_feedback);
            _feedback = null;
        }
        hideFocus();
        super.deactivate();
    }

    /**
     * Erase _feedback indicating that the receiver object is 
     * being dragged.  This method is called when a drag is
     * completed or cancelled on the receiver object.
     * @param dragTracker com.ibm.etools.gef.tools.DragTracker The drag 
     * tracker of the tool performing the drag.
     */
    protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
        if (_feedback != null) {
            removeFeedback(_feedback);
        }
        _feedback = null;
        _originalLocation = null;
    }

    /**
     * Erase _feedback indicating that the receiver object is 
     * being dragged.  This method is called when a drag is
     * completed or cancelled on the receiver object.
     * @param dragTracker com.ibm.etools.gef.tools.DragTracker The drag tracker
     * of the tool performing the drag.
     */
    public void eraseSourceFeedback(Request request) {
        if (REQ_MOVE.equals(request.getType()))
            eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
    }

    private Rectangle getBounds() {
        return ((GraphicalEditPart) getHost()).getFigure().getBounds();
    }

    public Command getCommand(Request request) {
        if (REQ_MOVE.equals(request.getType())) {
            getHost().activate();
            return getMoveCommand((ChangeBoundsRequest) request);
        }

        return null;
    }

    /**
     * Return the Figure to be used to paint the drag source
     * _feedback.
     */
    protected IFigure getDragSourceFeedbackFigure() {
        if (_feedback == null) {
            IFigure fig = ((GraphicalEditPart) getHost()).getFigure();
            _originalLocation = new Rectangle(fig.getBounds());
            _feedback = (PageBreaksFigure) createDragSourceFeedbackFigure();
        }
        return _feedback;
    }

    /**
     * Overridden from GEF to support zoom.
     * Had to scale the delta into device coordinates
     */
    protected Command getMoveCommand(ChangeBoundsRequest request) {

        ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_MOVE_CHILDREN);
        req.setEditParts(getHost());

        IFigure pageBreaksFigure = ((GraphicalEditPart) getHost()).getFigure();
        Rectangle moveDelta =
            new Rectangle(
                0,
                0,
                request.getMoveDelta().x,
                request.getMoveDelta().y);
        pageBreaksFigure.translateToRelative(moveDelta);

        Rectangle sizeDelta =
            new Rectangle(
                0,
                0,
                request.getSizeDelta().width,
                request.getSizeDelta().height);
        pageBreaksFigure.translateToRelative(sizeDelta);

        req.setMoveDelta(new Point(moveDelta.width, moveDelta.height));
        req.setSizeDelta(sizeDelta.getSize());
        req.setLocation(request.getLocation());
        return getHost().getParent().getCommand(req);
    }

    /**
     * Overridden from GEF to support zoom.
     * 
     * Display _feedback to indicate that the receiver object
     * is being dragged.
     * @param dragTracker com.ibm.etools.gef.tools.DragTracker The drag 
     * tracker of the tool performing the drag.
     */
    protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
        IFigure p = getDragSourceFeedbackFigure();

        Rectangle moveDelta =
            new Rectangle(
                0,
                0,
                request.getMoveDelta().x,
                request.getMoveDelta().y);
        p.translateToRelative(moveDelta);

        Rectangle r =
            _originalLocation.getTranslated(
                new Point(moveDelta.width, moveDelta.height));

        r.width += request.getSizeDelta().width;
        r.height += request.getSizeDelta().height;

        ((GraphicalEditPart) getHost()).getFigure().translateToAbsolute(r);
        p.translateToRelative(r);
        ((PageBreaksFigure) p).setPageCount(_rows, _cols);
        p.setBounds(r);
    }

    public void showSourceFeedback(Request request) {
        if (REQ_MOVE.equals(request.getType()))
            showChangeBoundsFeedback((ChangeBoundsRequest) request);
    }

    public boolean understandsRequest(Request request) {
        if (REQ_MOVE.equals(request.getType()))
            return true;
        return false;
    }

	//    /**
	//     * Calculates the _feedback bounds based on the move delta
	//     * @param moveDelta the move vector
	//     * @return the _feedback size and location
	//     */
	//    private Rectangle calculateFeedbackBounds(Point moveDelta) {
	//        // Create a copy of the current _feedback bounds to use in the
	// calculation
	//        PageBreaksFigure feedBackFigure =
	//            (PageBreaksFigure) getDragSourceFeedbackFigure();
	//        Rectangle currentBounds = feedBackFigure.getBounds().getCopy();
	//        int c = feedBackFigure.getPageCount().y;
	//        int r = feedBackFigure.getPageCount().x;
	//
	//        PageBreakEditPart e = (PageBreakEditPart) getHost();
	//        Rectangle diagramBounds =
	//            ((DiagramEditPart) e.getParent()).getChildrenBounds();
	//
	//        // Drag to the WEST
	//        if (moveDelta.x < 0) {
	//            if (currentBounds.right() < diagramBounds.right()) {
	//                // Add page to the EAST
	//                int effectiveWidth =
	//                    Math.abs(diagramBounds.x - currentBounds.x)
	//                        + diagramBounds.width;
	//                float cols = ((float) effectiveWidth) / _paperSize.x;
	//                int requiredCols = (int) Math.ceil(Math.abs(cols));
	//                int xOffset = Math.round(requiredCols * _paperSize.x);
	//                c = requiredCols;
	//                currentBounds.setBounds(
	//                    new Rectangle(
	//                        currentBounds.x,
	//                        currentBounds.y,
	//                        xOffset,
	//                        currentBounds.height));
	//            }
	//
	//            // Remove page from the WEST?
	//            int effectiveWidth =
	//                Math.abs(diagramBounds.right() - currentBounds.right())
	//                    + diagramBounds.width;
	//            float cols = ((float) effectiveWidth) / _paperSize.x;
	//            int requiredCols = (int) Math.ceil(Math.abs(cols));
	//            int xOffset = Math.round(requiredCols * _paperSize.x);
	//            if (c > requiredCols) {
	//                c = requiredCols;
	//                currentBounds.setBounds(
	//                    new Rectangle(
	//                        currentBounds.right() - xOffset,
	//                        currentBounds.y,
	//                        xOffset,
	//                        currentBounds.height));
	//            }
	//        } else if (moveDelta.x > 0) {
	//            // Drag to the EAST
	//            if (currentBounds.x > diagramBounds.x) {
	//                // Add page to the WEST
	//                int effectiveWidth =
	//                    Math.abs(currentBounds.right() - diagramBounds.right())
	//                        + diagramBounds.width;
	//                float cols = ((float) effectiveWidth) / _paperSize.x;
	//                int requiredCols = (int) Math.ceil(Math.abs(cols));
	//                int xOffset = Math.round(requiredCols * _paperSize.x);
	//                c = requiredCols;
	//                currentBounds.setLocation(
	//                    currentBounds.right() - xOffset,
	//                    currentBounds.y);
	//                currentBounds.setSize(xOffset, currentBounds.height);
	//            }
	//
	//            // Remove page from the EAST?
	//            int effectiveWidth =
	//                Math.abs(currentBounds.x - diagramBounds.x)
	//                    + diagramBounds.width;
	//            float cols = ((float) effectiveWidth) / _paperSize.x;
	//            int requiredCols = (int) Math.ceil(Math.abs(cols));
	//            if (c > requiredCols) {
	//                c = requiredCols;
	//                int xOffset = Math.round(requiredCols * _paperSize.x);
	//                currentBounds.setSize(xOffset, currentBounds.height);
	//
	//            }
	//        }
	//        // Drag to the SOUTH
	//        if (moveDelta.y > 0) {
	//            if (currentBounds.y > diagramBounds.y) {
	//                // Add page to the NORTH
	//                int effectiveHeight =
	//                    Math.abs(currentBounds.bottom() - diagramBounds.bottom())
	//                        + diagramBounds.height;
	//                float rows = ((float) effectiveHeight) / _paperSize.y;
	//                int requiredRows = (int) Math.ceil(Math.abs(rows));
	//                int yOffset = Math.round(requiredRows * _paperSize.y);
	//                r = requiredRows;
	//                currentBounds.setLocation(
	//                    currentBounds.x,
	//                    currentBounds.bottom() - yOffset);
	//                currentBounds.setSize(currentBounds.width, yOffset);
	//            }
	//
	//            // Remove from the SOUTH?
	//            int effectiveHeight =
	//                Math.abs(diagramBounds.y - currentBounds.y)
	//                    + diagramBounds.height;
	//            float rows = ((float) effectiveHeight) / _paperSize.y;
	//            int requiredRows = (int) Math.ceil(Math.abs(rows));
	//            int yOffset = Math.round(requiredRows * _paperSize.y);
	//            if (r > requiredRows) {
	//                r = requiredRows;
	//                currentBounds.setSize(currentBounds.width, yOffset);
	//            }
	//        } else
	//
	//            // Drag to the NORTH
	//            if (moveDelta.y < 0) {
	//                if (currentBounds.bottom() < diagramBounds.bottom()) {
	//                    // Add page to the SOUTH
	//                    int effectiveHeight =
	//                        Math.abs(diagramBounds.y - currentBounds.y)
	//                            + diagramBounds.height;
	//                    float rows = ((float) effectiveHeight) / _paperSize.y;
	//                    int requiredRows = (int) Math.ceil(Math.abs(rows));
	//                    int yOffset = Math.round(requiredRows * _paperSize.y);
	//                    r = requiredRows;
	//                    currentBounds.setSize(currentBounds.width, yOffset);
	//                }
	//
	//                // Remove from the NORTH?
	//                int effectiveWidth =
	//                    Math.abs(currentBounds.bottom() - diagramBounds.bottom())
	//                        + diagramBounds.height;
	//                float rows = ((float) effectiveWidth) / _paperSize.y;
	//                int requiredRows = (int) Math.ceil(Math.abs(rows));
	//                int yOffset = Math.round(requiredRows * _paperSize.y);
	//                if (r > requiredRows) {
	//                    r = requiredRows;
	//                    currentBounds.setLocation(
	//                        currentBounds.x,
	//                        currentBounds.bottom() - yOffset);
	//                    currentBounds.setSize(currentBounds.width, yOffset);
	//                }
	//            }
	//
	//        _newBounds = currentBounds;
	//        _cols = c;
	//        _rows = r;
	//
	//        return currentBounds;
	//    }

	/**
	 * @return Returns the _newBounds.
	 */
	protected Rectangle getNewBounds() {
		return _newBounds;
	}
	/**
	 * @return Returns the _paperSize.
	 */
	protected Point getPaperSize() {
		return _paperSize;
	}
	/**
	 * @return Returns the _rows.
	 */
	protected int getRows() {
		return _rows;
	}
/**
 * @return Returns the _feedback.
 */
protected PageBreaksFigure getFeedback() {
	return _feedback;
}
	/**
	 * @return Returns the _originalLocation.
	 */
	protected Rectangle getOriginalLocation() {
		return _originalLocation;
	}
}
