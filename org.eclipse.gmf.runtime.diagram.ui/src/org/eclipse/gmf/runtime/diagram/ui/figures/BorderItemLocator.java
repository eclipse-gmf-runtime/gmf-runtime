/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.jface.util.Assert;

/**
 * BorderItemLocator works closely with border items in determining position.
 * BorderItemLocators use a figure as the bounds around which the
 * borderItemFigure can be placed. This figure may not necessarily be the main
 * figure.
 * <p>
 * BorderItemLocator allows intial placement ( N/W/S/E ) and is used in
 * conjunction with BorderItemSelectionEditPolicy for feedback.
 * <p>
 * Collision detection is provided so that border items cannot overlap and
 * therefore be hidden.
 * <p>
 * Border item movement is restricted along border of bordered shape.
 * 
 * @author tisrar, jbruck, cmahoney
 */
public class BorderItemLocator
	implements IBorderItemLocator {

	/** the figure around which this border item appears */
	private IFigure parentFigure = null;

	private Rectangle constraint = new Rectangle(0, 0, 0, 0);

	private Dimension borderItemOffset = new Dimension(1, 1);

	private int preferredSide = PositionConstants.WEST;

	private int currentSide = PositionConstants.WEST;

	/**
	 * Accessor to return the constraint location of the border item.
	 * 
	 * @return the constraint
	 */
	protected Rectangle getConstraint() {
		return constraint;
	}

	/**
	 * The preferred side determines placement of figure.
	 * 
	 * @param parentFigure
	 */
	public BorderItemLocator(IFigure parentFigure) {
		Assert.isNotNull(parentFigure);
		this.parentFigure = parentFigure;
	}

	/**
	 * The preferred side determines placement of figure.
	 * 
	 * @param parentFigure
	 * @param preferredSide
	 *            the preferred side of the parent figure on which to place this
	 *            border item as defined in {@link PositionConstants}
	 */
	public BorderItemLocator(IFigure parentFigure, int preferredSide) {
		this(parentFigure);
		this.preferredSide = preferredSide;
	}
	
	/**
	 * The preferred location overrides the preferred side.
	 * 
	 * @param parentFigure
	 * @param constraint
	 */
	public BorderItemLocator(IFigure borderItem, IFigure parentFigure,
			Rectangle constraint) {
		this(parentFigure);
		setConstraint(constraint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.figures.IBorderItemLocator#resetPosition(org.eclipse.draw2d.geometry.Rectangle)
	 */
	public void setConstraint(Rectangle theConstraint) {
		this.constraint = theConstraint;

		if (theConstraint.getTopLeft().x == 0
			|| theConstraint.getTopLeft().y == 0) {
			setCurrentSideOfParent(getPreferredSideOfParent());
		}
		getParentFigure().revalidate();
	}

	/**
	 * Get the preferred location. If none has been previously set, use the
	 * preferred side to take an initial guess.
	 * 
	 * @return point
	 */
	protected Point getPreferredLocation(IFigure borderItem) {
		Point constraintLocation = getConstraint().getLocation();
		Point ptAbsoluteLocation = this.getAbsoluteToBorder(constraintLocation);

		if (constraintLocation.x == 0 || constraintLocation.y == 0) {
			return getPreferredLocation(getPreferredSideOfParent(), borderItem);
		} else {
			return ptAbsoluteLocation;
		}
	}

	/**
	 * Utility to calculate the parent bounds with consideration for the handle
	 * bounds inset.
	 * 
	 * @return <code>Rectangle</code> that is the bounds of the parent.
	 */
	protected Rectangle getParentBorder() {
		Rectangle bounds = getParentFigure().getBounds().getCopy();
		if (getParentFigure() instanceof NodeFigure) {
			bounds = ((NodeFigure) getParentFigure()).getHandleBounds()
				.getCopy();
		}
		return bounds;
	}

	/**
	 * Get an initial location based on the side. ( choose middle of the side )
	 * 
	 * @param side
	 *            the preferred side of the parent figure on which to place this
	 *            border item as defined in {@link PositionConstants}
	 * @return point
	 */
	protected Point getPreferredLocation(int side, IFigure borderItem) {
		Rectangle bounds = getParentBorder();
		int parentFigureWidth = bounds.width;
		int parentFigureHeight = bounds.height;
		int parentFigureX = bounds.x;
		int parentFigureY = bounds.y;
		int x = parentFigureX;
		int y = parentFigureY;

		Rectangle borderItemBounds = borderItem.getBounds();

		if (side == PositionConstants.WEST) {
			x = parentFigureX - borderItemBounds.width
				+ getBorderItemOffset().width;
			y += parentFigureHeight / 2;
		} else if (side == PositionConstants.EAST) {
			x = parentFigureX + parentFigureWidth - getBorderItemOffset().width;
			y += parentFigureHeight / 2;
		} else if (side == PositionConstants.NORTH) {
			y = parentFigureY - borderItemBounds.height
				+ getBorderItemOffset().height;
			x += parentFigureWidth / 2;
		} else if (side == PositionConstants.SOUTH) {
			x += parentFigureWidth / 2;
			y = parentFigureY + parentFigureHeight
				- getBorderItemOffset().height;
		}
		return new Point(x, y);
	}
	/**
	 * Ensure the suggested location actually lies on the parent boundary. The
	 * side takes precendence.
	 * 
	 * @param suggestedLocation
	 * @param suggestedSide
	 * @return point
	 */
	private Point locateOnParent(Point suggestedLocation,
			int suggestedSide, IFigure borderItem) {
		Rectangle bounds = getParentBorder();
		int parentFigureWidth = bounds.width;
		int parentFigureHeight = bounds.height;
		int parentFigureX = bounds.x;
		int parentFigureY = bounds.y;
		Rectangle borderItemBounds = borderItem.getBounds();
		int newX = suggestedLocation.x;
		int newY = suggestedLocation.y;
		int westX = parentFigureX - borderItemBounds.width
			+ getBorderItemOffset().width;
		int eastX = parentFigureX + parentFigureWidth
			- getBorderItemOffset().width;
		int southY = parentFigureY + parentFigureHeight
			- getBorderItemOffset().height;
		int northY = parentFigureY - borderItemBounds.height
			+ getBorderItemOffset().height;
		if (suggestedSide == PositionConstants.WEST) {
			if (suggestedLocation.x != westX) {
				newX = westX;
			}
			if (suggestedLocation.y < bounds.getTopLeft().y) {
				newY = northY + borderItemBounds.height;
			} else if (suggestedLocation.y > bounds.getBottomLeft().y
				- borderItemBounds.height) {
				newY = southY - borderItemBounds.height;
			}
		} else if (suggestedSide == PositionConstants.EAST) {
			if (suggestedLocation.x != eastX) {
				newX = eastX;
			}
			if (suggestedLocation.y < bounds.getTopLeft().y) {
				newY = northY + borderItemBounds.height;
			} else if (suggestedLocation.y > bounds.getBottomLeft().y
				- borderItemBounds.height) {
				newY = southY - borderItemBounds.height;
			}
		} else if (suggestedSide == PositionConstants.SOUTH) {
			if (suggestedLocation.y != southY) {
				newY = southY;
			}
			if (suggestedLocation.x < bounds.getBottomLeft().x) {
				newX = westX + borderItemBounds.width;
			} else if (suggestedLocation.x > bounds.getBottomRight().x
				- borderItemBounds.width) {
				newX = eastX - borderItemBounds.width;
			}
		} else { // NORTH
			if (suggestedLocation.y != northY) {
				newY = northY;
			}
			if (suggestedLocation.x < bounds.getBottomLeft().x) {
				newX = westX + borderItemBounds.width;
			} else if (suggestedLocation.x > bounds.getBottomRight().x
				- borderItemBounds.width) {
				newX = eastX - borderItemBounds.width;
			}
		}
		return new Point(newX, newY);
	}

	/**
	 * Determine if the the given point conflicts with the position of an
	 * existing borderItemFigure.
	 * 
	 * @param recommendedLocation
	 * @return <code>ture</code> or <code>false</code>
	 */
	private boolean conflicts(Point recommendedLocation,
			IFigure targetBorderItem) {
		Rectangle recommendedRect = new Rectangle(recommendedLocation,
			targetBorderItem.getSize());
		List borderItems = targetBorderItem.getParent().getChildren();
		ListIterator iterator = borderItems.listIterator();
		while (iterator.hasNext()) {
			IFigure borderItem = (IFigure) iterator.next();
			if (borderItem.isVisible()) {
				Rectangle rect = borderItem.getBounds().getCopy();
				if (borderItem != targetBorderItem
					&& rect.intersects(recommendedRect)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * The preferred side takes precendence.
	 * 
	 * @param suggestedLocation
	 * @param suggestedSide
	 * @param circuitCount
	 *            recursion count to avoid an infinite loop
	 * @return point
	 */
	private Point locateOnBorder(Point suggestedLocation,
			int suggestedSide, int circuitCount, IFigure borderItem) {
		Point recommendedLocation = locateOnParent(suggestedLocation,
			suggestedSide, borderItem);

		int vertical_gap = MapModeUtil.getMapMode(getParentFigure()).DPtoLP(8);
		int horizontal_gap = MapModeUtil.getMapMode(getParentFigure())
			.DPtoLP(8);
		Rectangle borderItemBounds = borderItem.getBounds();

		if (circuitCount < 4 && conflicts(recommendedLocation, borderItem)) {
			if (suggestedSide == PositionConstants.WEST) {
				do {
					recommendedLocation.y += borderItemBounds.height
						+ vertical_gap;
				} while (conflicts(recommendedLocation, borderItem));
				if (recommendedLocation.y > getParentBorder().getBottomLeft().y
					- borderItemBounds.height) { // off the bottom,
					// wrap south
					return locateOnBorder(recommendedLocation,
						PositionConstants.SOUTH, circuitCount + 1, borderItem);
				}
			} else if (suggestedSide == PositionConstants.SOUTH) {
				do {
					recommendedLocation.x += borderItemBounds.width
						+ horizontal_gap;
				} while (conflicts(recommendedLocation, borderItem));
				if (recommendedLocation.x > getParentBorder().getBottomRight().x
					- borderItemBounds.width) {
					return locateOnBorder(recommendedLocation,
						PositionConstants.EAST, circuitCount + 1, borderItem);
				}
			} else if (suggestedSide == PositionConstants.EAST) {
				// move up the east side
				do {
					recommendedLocation.y -= borderItemBounds.height
						- vertical_gap;
				} while (conflicts(recommendedLocation, borderItem));
				if (recommendedLocation.y < getParentBorder().getTopRight().y) {
					// east is full, try north.
					return locateOnBorder(recommendedLocation,
						PositionConstants.NORTH, circuitCount + 1, borderItem);
				}
			} else { // NORTH
				do {
					recommendedLocation.x -= borderItemBounds.width
						- horizontal_gap;
				} while (conflicts(recommendedLocation, borderItem));
				if (recommendedLocation.x < getParentBorder().getTopLeft().x) {
					return locateOnBorder(recommendedLocation,
						PositionConstants.WEST, circuitCount + 1, borderItem);
				}
			}
		}
		return recommendedLocation;
	}

	/**
	 * Convert the relative coords in the model to ones that are Relative to the
	 * container (absolute in respect to the main figure)
	 * 
	 * @param ptRelativeOffset
	 * @return point
	 */
	protected Point getAbsoluteToBorder(Point ptRelativeOffset) {
		Point parentOrigin = getParentBorder().getTopLeft();
		return parentOrigin.translate(ptRelativeOffset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.figures.IBorderItemLocator#getValidLocation(org.eclipse.draw2d.geometry.Rectangle,
	 *      org.eclipse.draw2d.IFigure)
	 */
	public Rectangle getValidLocation(Rectangle proposedLocation,
			IFigure borderItem) {
		Rectangle realLocation = new Rectangle(proposedLocation);
		int side = findClosestSideOfParent(proposedLocation, getParentBorder());
		Point newTopLeft = locateOnBorder(realLocation.getTopLeft(), side, 0,
			borderItem);
		realLocation.setLocation(newTopLeft);
		return realLocation;
	}

	/**
	 * Find the closest side when x,y is inside parent.
	 * 
	 * @param proposedLocation
	 * @param parentBorder
	 * @return draw constant
	 */
	public static int findClosestSideOfParent(Rectangle proposedLocation,
			Rectangle parentBorder) {
		// Rectangle parentBorder = getParentBorder();
		Point parentCenter = parentBorder.getCenter();
		Point childCenter = proposedLocation.getCenter();
		if (childCenter.x < parentCenter.x) // West, North or South.
		{
			if (childCenter.y < parentCenter.y) // west or north
			{
				// closer to west or north?
				Point parentTopLeft = parentBorder.getTopLeft();
				if ((childCenter.x - parentTopLeft.x) <= (childCenter.y - parentTopLeft.y)) {
					return PositionConstants.WEST;
				} else {
					return PositionConstants.NORTH;
				}
			} else { // west or south
				Point parentBottomLeft = parentBorder.getBottomLeft();
				if ((childCenter.x - parentBottomLeft.x) <= (parentBottomLeft.y - childCenter.y)) {
					return PositionConstants.WEST;
				} else {
					return PositionConstants.SOUTH;
				}
			}
		} else { // EAST, NORTH or SOUTH
			if (childCenter.y < parentCenter.y) // north or east
			{
				Point parentTopRight = parentBorder.getTopRight();
				if ((parentTopRight.x - childCenter.x) <= (childCenter.y - parentTopRight.y)) {
					return PositionConstants.EAST;
				} else {
					return PositionConstants.NORTH;
				}
			} else { // south or east.
				Point parentBottomRight = parentBorder.getBottomRight();
				if ((parentBottomRight.x - childCenter.x) <= (parentBottomRight.y - childCenter.y)) {
					return PositionConstants.EAST;
				} else {
					return PositionConstants.SOUTH;
				}
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
	 */
	public void relocate(IFigure borderItem) {
		Rectangle rectSuggested = new Rectangle(
			getPreferredLocation(borderItem), borderItem.getBounds().getSize());
		int closestSide = findClosestSideOfParent(rectSuggested,
			getParentBorder());
		setPreferredSideOfParent(closestSide);

		Point ptNewLocation = locateOnBorder(getPreferredLocation(borderItem),
			getPreferredSideOfParent(), 0, borderItem);
		borderItem.setLocation(ptNewLocation);

		setCurrentSideOfParent(findClosestSideOfParent(new Rectangle(ptNewLocation, borderItem
			.getBounds().getSize()), getParentBorder()));
	}

	/**
	 * getter for the parent figure
	 * 
	 * @return <code>IFigure</code>
	 */
	public IFigure getParentFigure() {
		return parentFigure;
	}

	/**
	 * @return Returns the borderItemOffset.
	 */
	public Dimension getBorderItemOffset() {
		return borderItemOffset;
	}

	/**
	 * @param borderItemOffset
	 *            The borderItemOffset to set.
	 */
	public void setBorderItemOffset(Dimension borderItemOffset) {
		this.borderItemOffset = borderItemOffset;
	}

	/**
	 * Returns the preferred side of the parent figure on which to place this
	 * border item.
	 * 
	 * @return the preferred side of the parent figure on which to place this
	 *         border item as defined in {@link PositionConstants}
	 */
	public int getPreferredSideOfParent() {
		return preferredSide;
	}

	/**
	 * Sets the preferred side of the parent figure on which to place this
	 * border item.
	 * 
	 * @param preferredSide
	 *            the preferred side of the parent figure on which to place this
	 *            border item as defined in {@link PositionConstants}
	 */
	public void setPreferredSideOfParent(int preferredSide) {
		this.preferredSide = preferredSide;
		setCurrentSideOfParent(preferredSide);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.figures.IBorderItemLocator#getSide()
	 */
	public int getCurrentSideOfParent() {
		return currentSide;
	}

	/**
	 * Sets the side of the parent figure on which the border item should
	 * appear.
	 * 
	 * @param side
	 *            the side on which this border item appears as defined in
	 *            {@link PositionConstants}
	 */
	public void setCurrentSideOfParent(int side) {
		this.currentSide = side;
	}
}
