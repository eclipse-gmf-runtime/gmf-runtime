/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.handles;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;

/**
 * This locator is used to locate all the handles around the shape the closest
 * distance to the mouse location. It spaces the handles a certain distance
 * apart. Call setEastWestSouth(true) to indicate that the connector handle can
 * only be located on the east, west, or south side of the reference figure.
 * 
 * @author cmahoney
 */
public class ConnectorHandleLocator
	implements Locator {

	/** number of pixels between connector handles */
	private static int HANDLE_SPACING = 20;

	/** the MARGIN to leave by the edge of the parent figure */
	private static int MARGIN = 2;

	/** list of connector handles for the shape */
	private List handles = new ArrayList();

	/** a point in the shape used when locating the handles */
	private Point cursorPosition = null;

	/** the parent figure */
	private IFigure reference = null;

	/** the side on which the handles will appear, value from PositionConstants */
	private int side = PositionConstants.WEST;

	/** the point on the border where the handles should appear */
	private Point borderPoint = new Point(0,0);

	/**
	 * flag to indicate if the connector handle should only be on the east,
	 * west, or south side
	 */
	private boolean bEastWestSouth = false;
	
	/**
	 * Constructor for ConnectorHandleLocator.
	 * @param reference the figure
	 * @param cursorPosition the handles will be located on the border of the
	 * shape as close to this position as possible
	 */
	public ConnectorHandleLocator(IFigure reference, Point cursorPosition) {
		this.reference = reference;
		this.cursorPosition = cursorPosition;
	}

	/**
	 * Adds a connector handle to the list of handles.
	 * @param handle the <code>ConnectorHandle</code> to add
	 */
	public void addHandle(ConnectorHandle handle) {
		handles.add(handle);
	}

	/**
	 * Sets the point on the West, East, North or South edge of the reference
	 * figure nearest to the cursor position passed in to the constructor. Sets
	 * the side variable for which side the handles will be on.
	 */
	private void resetBorderPointAndSideAllDirections() {

		Rectangle bounds = getReferenceFigureBounds();
		int westDiff = cursorPosition.x - bounds.getLeft().x;
		int eastDiff = bounds.getRight().x - cursorPosition.x;
		int southDiff = bounds.getBottom().y - cursorPosition.y;
		int northDiff = cursorPosition.y - bounds.getTop().y;
		
		double minLR = Math.min(eastDiff, westDiff);
		double minUD = Math.min(southDiff, northDiff);
		// which directions is closest North/South or East/West?
		boolean bUp=(minUD < minLR);
				
		if(bUp)
		{
			if(northDiff < southDiff)
			{
				// re-evaluate if the north is closer than the east since the
				// handles will be placed in the north-west corner because of
				// the action bar.
				if (eastDiff < westDiff
					&& eastDiff < cursorPosition.getDistance(bounds
						.getTopLeft())) {
					borderPoint.setLocation(bounds.x + bounds.width,
						cursorPosition.y);
					side = PositionConstants.EAST;
				} else {
					borderPoint.setLocation(bounds.x, bounds.y);
					side = PositionConstants.NORTH;
				}
			}
			else
			{
				borderPoint.setLocation(cursorPosition.x, bounds.y + bounds.height);
				side = PositionConstants.SOUTH;
			}
		}
		else
		{
			if(westDiff < eastDiff)
			{
				borderPoint.setLocation(bounds.x, cursorPosition.y);
				side = PositionConstants.WEST;	
			}
			else
			{
				//
				borderPoint.setLocation(bounds.x + bounds.width, cursorPosition.y);
				side = PositionConstants.EAST;
			}
		}
	}
	
	/**
	 * Sets the point on the West, East, South edge of the reference figure
	 * nearest to the cursor position passed in to the constructor. Sets the
	 * side variable for which side the handles will be on.
	 */
	private void resetBorderPointAndSideEastWestSouth() {
		Rectangle bounds = getReferenceFigureBounds();

		// Get the point on the West, East, or South edge of the reference figure 
		// nearest to the cursor position.
		int westDiff = cursorPosition.x - bounds.x;
		int eastDiff = bounds.x + bounds.width - cursorPosition.x;
		int southDiff = bounds.y + bounds.height - cursorPosition.y;

		int min = Math.min(westDiff, eastDiff);
		if (westDiff > 30 && eastDiff > 30) { // favour East and West
			min = Math.min(min, southDiff);
		}

		if (min == westDiff) {
			borderPoint.setLocation(bounds.x, cursorPosition.y);
			side = PositionConstants.WEST;
		} else if (min == eastDiff) {
			borderPoint.setLocation(bounds.x + bounds.width, cursorPosition.y);
			side = PositionConstants.EAST;
		} else {
			borderPoint.setLocation(cursorPosition.x, bounds.y + bounds.height);
			side = PositionConstants.SOUTH;
		}

	}

	/**
	 * Resets the border point and side variables where the connector handles
	 * will appear, based on the cursor location.
	 */
	private void resetBorderPointAndSide()
	{
		if(isEastWestSouth())
			resetBorderPointAndSideEastWestSouth();
		else
			resetBorderPointAndSideAllDirections();	
	}
	

	/**
	 * Gets the side (West, South, or East) on which the handles will appear,
	 * using the cursor position passed in to the constructor.
	 * @return the side, a value in PositionContstants
	 */
	public int getBorderSide() {
		resetBorderPointAndSide();
		return side;
	}

	/**
	 * Locates the figure on the appropriate edge of the parent depending on
	 * which side is closest to the reference point. Then adjusts the figure
	 * depending on the number of handles that this locator has so that the
	 * handles are spaced out from the center along the edge of the shape.
	 * 
	 * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
	 */
	public void relocate(IFigure target) {
		Rectangle bounds = getReferenceFigureBounds();
		resetBorderPointAndSide();

		Point borderPointTranslated = borderPoint.getCopy();
		reference.translateToAbsolute(bounds);
		target.translateToRelative(bounds);
		reference.translateToAbsolute(borderPointTranslated);
		target.translateToRelative(borderPointTranslated);

		int width = target.getBounds().width;
		int halfWidth = width / 2;

		int height = target.getBounds().height;
		int halfHeight = height / 2;
		
		// adjust border point if the handles will extend past the bounds of the
		// shape
		if (side == PositionConstants.WEST || side == PositionConstants.EAST) {
			int halfTotalHeight = (height * handles.size() + HANDLE_SPACING
				* (handles.size() - 1)) / 2;
			if (borderPointTranslated.y - halfTotalHeight < bounds.getTop().y) {
				borderPointTranslated.y = bounds.getTop().y + halfTotalHeight;
			} else if (borderPointTranslated.y + halfTotalHeight > bounds
				.getBottom().y) {
				borderPointTranslated.y = bounds.getBottom().y
					- halfTotalHeight;
			}
		} else {
			int halfTotalWidth = (width * handles.size() + HANDLE_SPACING
				* (handles.size() - 1)) / 2;
			if (borderPointTranslated.x - halfTotalWidth < bounds.getLeft().x) {
				borderPointTranslated.x = bounds.getLeft().x + halfTotalWidth;
			} else if (borderPointTranslated.x + halfTotalWidth > bounds
				.getRight().x) {
				borderPointTranslated.x = bounds.getRight().x - halfTotalWidth;
			}
		}

		// set location based on side
		if (side == PositionConstants.WEST) {
			target.setLocation(borderPointTranslated
				.getTranslated(new Dimension(-(width + MARGIN), -halfHeight)));
		} else if (side == PositionConstants.EAST) {
			target.setLocation(borderPointTranslated
				.getTranslated(new Dimension(MARGIN, -halfHeight)));
		} else if(side == PositionConstants.SOUTH) {
			target.setLocation(borderPointTranslated
				.getTranslated(new Dimension(-halfWidth, MARGIN)));
		} else if (side == PositionConstants.NORTH) {
			target.setLocation(borderPointTranslated
				.getTranslated(new Dimension(MARGIN, -height-MARGIN)));
		}
		
		// space out handles
		int index = handles.indexOf(target);
		double centerOffset = index - (handles.size() / 2.0) + 0.5;

		if (side == PositionConstants.WEST || side == PositionConstants.EAST) {
			target.translate(0, (int) (centerOffset * HANDLE_SPACING));
		} else {
			target.translate((int) (centerOffset * HANDLE_SPACING), 0);
		}
		
	}

	/**
	 * Gets the bounding rectangle of the reference figure.
	 * @return the bounds
	 */
	private Rectangle getReferenceFigureBounds() {
		Rectangle bounds = reference instanceof HandleBounds ? ((HandleBounds) reference)
			.getHandleBounds().getCopy()
			: reference.getBounds().getCopy();
		return bounds;
	}

	/**
	 * Gets the reference figure.
	 * @return Returns the reference.
	 */
	protected IFigure getReference() {
		return reference;
	}
	
	/**
	 * Gets the cursor position.
	 * @return Returns the cursorPosition.
	 */
	protected Point getCursorPosition() {
		return cursorPosition;
	}
	
	/**
	 * Can the connector handle can only be located on the east, west, or south
	 * side of the reference figure.
	 * 
	 * @return Returns true if the connector handle can only be located on the
	 *         east, west, or south side of the reference figure; false
	 *         otherwise.
	 */
	public boolean isEastWestSouth() {
		return bEastWestSouth;
	}

	/**
	 * Set to true if the connector handle can only be located on the east,
	 * west, or south side of the reference figure.
	 * 
	 * @param eastWestSouth
	 *            The bEastWestSouth to set.
	 */
	public void setEastWestSouth(boolean eastWestSouth) {
		bEastWestSouth = eastWestSouth;
	}
}
