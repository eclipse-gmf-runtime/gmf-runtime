/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
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
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.util.DrawConstant;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.jface.util.Assert;

/**
 * Border Items are figures that sit within BorderItemContainerFigures and can
 * be painted outside the client area of the main figure. They work in
 * conjunction with BorderedFigures. The BorderItemLocator determines placement
 * of borderItemFigure.
 * 
 * @author tisrar
 * @author jbruck
 * 
 */
public class BorderItemFigure
	extends NodeFigure {

	/**
	 * The borderItemFigure locator is responsible for positioning
	 * borderItemFigure on the shape.
	 */
	private Locator locator = null;

	private Dimension borderItemOffset = new Dimension(1, 1);

	private DrawConstant preferredSide = DrawConstant.WEST;

	private DrawConstant currentSide = DrawConstant.WEST;

	/**
	 * The default constructor which sets the size of the figure.
	 * 
	 * @param preferredSide
	 *            The preferredSide to set on this figure
	 */
	public BorderItemFigure(DrawConstant preferredSide) {
		super();
		setPreferredSide(preferredSide);
		this.setOpaque(true);
	}

	/**
	 * BorderItemLocator works closely with border items in determining
	 * position. BorderItemLocators use a figure as the bounds around which the
	 * borderItemFigure can be placed. This figure may not necessarily be the
	 * main figure.
	 * 
	 * BorderItemLocator allows intial placement ( N/W/S/E ) and is used in
	 * conjunction with BorderItemSelectionEditPolicy for feedback.
	 * 
	 * Collision detection is provided so that border items cannot overlap and
	 * therfore be hidden.
	 *  ( this class re-written from orginal by Tauseef ).
	 * 
	 * @author tisrar
	 * @author jbruck
	 */
	public static class BorderItemLocator
		implements Locator {

		private final static int VERTICAL_GAP = MapMode.DPtoLP(8);

		private final static int HORIZONTAL_GAP = MapMode.DPtoLP(8);

		private IFigure boundaryFigure = null;

		private BorderItemFigure borderItemFigure = null;

		private Point constraintLocation = new Point(0, 0); // port is relative
															// to border fig.

		/**
		 * Accessor to return the constraint location of the borderItemFigure.
		 * 
		 * @return <code>Point</code> that is the location constraint.
		 */
		protected Point getConstraintLocation() {
			return constraintLocation;
		}

		/**
		 * The preferred side determines placement of figure.
		 * 
		 * @param borderItemFigure
		 * @param parentFigure
		 */
		public BorderItemLocator(BorderItemFigure borderItem,
				IFigure parentFigure) {
			Assert.isNotNull(parentFigure);
			Assert.isNotNull(borderItem);
			this.boundaryFigure = parentFigure;
			this.borderItemFigure = borderItem;
		}

		/**
		 * The preferred location overrides the preferred side.
		 * 
		 * @param borderItemFigure
		 * @param parentFigure
		 * @param rectProposed
		 */
		public BorderItemLocator(BorderItemFigure borderItem,
				IFigure parentFigure, Rectangle rectProposed) {
			this(borderItem, parentFigure);
			resetPosition(rectProposed);
		}

		/**
		 * Reset the position information to be filled in during the next
		 * "relocate" call.
		 * 
		 * @param rectProposed
		 */
		public void resetPosition(Rectangle rectProposed) {
			constraintLocation = rectProposed.getTopLeft();

			if (rectProposed.getTopLeft().x == 0
				|| rectProposed.getTopLeft().y == 0) {
				borderItemFigure.setPreferredSide(borderItemFigure
					.getPreferredSide());
				borderItemFigure.setCurrentSide(borderItemFigure
					.getCurrentSide());
			}
			borderItemFigure.getParent().revalidate();
			borderItemFigure.getParent().repaint();
		}

		/**
		 * Get the preferred location. If none has been previously set, use the
		 * preferred side to take an initial guess.
		 * 
		 * @return point
		 */
		public Point getPreferredLocation() {
			Point ptAbsoluteLocation = this
				.getAbsoluteToBorder(constraintLocation);

			if (constraintLocation.x == 0 || constraintLocation.y == 0) {
				return getStartLocation(borderItemFigure.getPreferredSide());
			} else {
				return ptAbsoluteLocation;
			}
		}

		private Rectangle getBorderItemBounds() {
			return ((NodeFigure) borderItemFigure).getHandleBounds().getCopy();
		}

		/**
		 * Utility to calculate the parent bounds with consideration for the
		 * handle bounds inset.
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
		 * Get an initial location based on the side. ( choose middle of the
		 * side )
		 * 
		 * @param side
		 * @return point
		 */
		protected Point getStartLocation(DrawConstant side) {
			Rectangle bounds = getParentBorder();
			int parentFigureWidth = bounds.width;
			int parentFigureHeight = bounds.height;
			int parentFigureX = bounds.x;
			int parentFigureY = bounds.y;
			int x = parentFigureX;
			int y = parentFigureY;
			Rectangle borderItemBounds = borderItemFigure.getHandleBounds()
				.getCopy();
			if (side == DrawConstant.WEST) {
				x = parentFigureX - borderItemBounds.width
					+ borderItemFigure.getBorderItemOffset().width;
				y += parentFigureHeight / 2;
			} else if (side == DrawConstant.EAST) {
				x = parentFigureX + parentFigureWidth
					- borderItemFigure.getBorderItemOffset().width;
				y += parentFigureHeight / 2;
			} else if (side == DrawConstant.NORTH) {
				y = parentFigureY - borderItemBounds.height
					+ borderItemFigure.getBorderItemOffset().height;
				x += parentFigureWidth / 2;
			} else if (side == DrawConstant.SOUTH) {
				x += parentFigureWidth / 2;
				y = parentFigureY + parentFigureHeight
					- borderItemFigure.getBorderItemOffset().height;
			}
			return new Point(x, y);
		}

		/**
		 * Ensure the suggested location actually lies on the parent boundary.
		 * The side takes precendence.
		 * 
		 * @param suggestedLocation
		 * @param suggestedSide
		 * @return point
		 */
		protected Point locateOnParent(Point suggestedLocation,
				DrawConstant suggestedSide) {
			Rectangle bounds = getParentBorder();
			int parentFigureWidth = bounds.width;
			int parentFigureHeight = bounds.height;
			int parentFigureX = bounds.x;
			int parentFigureY = bounds.y;
			Rectangle borderItemBounds = getBorderItemBounds();
			int newX = suggestedLocation.x;
			int newY = suggestedLocation.y;
			int westX = parentFigureX - borderItemBounds.width
				+ borderItemFigure.getBorderItemOffset().width;
			int eastX = parentFigureX + parentFigureWidth
				- borderItemFigure.getBorderItemOffset().width;
			int southY = parentFigureY + parentFigureHeight
				- borderItemFigure.getBorderItemOffset().height;
			int northY = parentFigureY - borderItemBounds.height
				+ borderItemFigure.getBorderItemOffset().height;
			if (suggestedSide == DrawConstant.WEST) {
				if (suggestedLocation.x != westX) {
					newX = westX;
				}
				if (suggestedLocation.y < bounds.getTopLeft().y) {
					newY = northY + borderItemBounds.height;
				} else if (suggestedLocation.y > bounds.getBottomLeft().y
					- borderItemBounds.height) {
					newY = southY - borderItemBounds.height;
				}
			} else if (suggestedSide == DrawConstant.EAST) {
				if (suggestedLocation.x != eastX) {
					newX = eastX;
				}
				if (suggestedLocation.y < bounds.getTopLeft().y) {
					newY = northY + borderItemBounds.height;
				} else if (suggestedLocation.y > bounds.getBottomLeft().y
					- borderItemBounds.height) {
					newY = southY - borderItemBounds.height;
				}
			} else if (suggestedSide == DrawConstant.SOUTH) {
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
		protected boolean conflicts(Point recommendedLocation) {
			Rectangle recommendedRect = new Rectangle(recommendedLocation,
				borderItemFigure.getSize());
			List borderItems = borderItemFigure.getParent().getChildren();
			ListIterator iterator = borderItems.listIterator();
			while (iterator.hasNext()) {
				IFigure borderItem = (IFigure) iterator.next();
				if (borderItem.isVisible()) {
					Rectangle rect = borderItem.getBounds().getCopy();
					if (borderItem != this.borderItemFigure
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
		 * @return point
		 */
		protected Point locateOnBorder(Point suggestedLocation,
				DrawConstant suggestedSide, int circuitCount) {
			Point recommendedLocation = locateOnParent(suggestedLocation,
				suggestedSide);
			if (circuitCount < 4 && conflicts(recommendedLocation)) {
				if (suggestedSide == DrawConstant.WEST) {
					do {
						recommendedLocation.y += getBorderItemBounds().height
							+ VERTICAL_GAP;
					} while (conflicts(recommendedLocation));
					if (recommendedLocation.y > getParentBorder()
						.getBottomLeft().y
						- getBorderItemBounds().height) { // off the bottom,
															// wrap south
						return locateOnBorder(recommendedLocation,
							DrawConstant.SOUTH, circuitCount + 1);
					}
				} else if (suggestedSide == DrawConstant.SOUTH) {
					do {
						recommendedLocation.x += getBorderItemBounds().width
							+ HORIZONTAL_GAP;
					} while (conflicts(recommendedLocation));
					if (recommendedLocation.x > getParentBorder()
						.getBottomRight().x
						- getBorderItemBounds().width) {
						return locateOnBorder(recommendedLocation,
							DrawConstant.EAST, circuitCount + 1);
					}
				} else if (suggestedSide == DrawConstant.EAST) { // move up
																	// the east
																	// side
					do {
						recommendedLocation.y -= getBorderItemBounds().height
							- VERTICAL_GAP;
					} while (conflicts(recommendedLocation));
					if (recommendedLocation.y < getParentBorder().getTopRight().y) { // east
																						// is
																						// full,
																						// try
																						// north.
						return locateOnBorder(recommendedLocation,
							DrawConstant.NORTH, circuitCount + 1);
					}
				} else { // NORTH
					do {
						recommendedLocation.x -= getBorderItemBounds().width
							- HORIZONTAL_GAP;
					} while (conflicts(recommendedLocation));
					if (recommendedLocation.x < getParentBorder().getTopLeft().x) {
						return locateOnBorder(recommendedLocation,
							DrawConstant.WEST, circuitCount + 1);
					}
				}
			}
			return recommendedLocation;
		}

		/**
		 * Convert the relative coords in the model to ones that are Relative to
		 * the container (absolute in respect to the main figure)
		 * 
		 * @param ptRelativeOffset
		 * @return point
		 */
		public Point getAbsoluteToBorder(Point ptRelativeOffset) {
			Point parentOrigin = getParentBorder().getTopLeft();
			return parentOrigin.translate(ptRelativeOffset);
		}

		/**
		 * Make saved constraint in the model a point that is relative to the
		 * origin of the border fig.
		 * 
		 * @param pt
		 * @return point
		 */
		public Point getRelativeToBorder(Point pt) {
			Point parentOrigin = getParentBorder().getTopLeft();
			Dimension d = pt.getDifference(parentOrigin);
			return new Point(d.width, d.height);
		}

		/**
		 * For a given proposed location, find a location suitable on the
		 * border.
		 * 
		 * @param proposedLocation
		 * @return rectangle
		 */
		public Rectangle locateOnBorder(Rectangle proposedLocation) {
			Rectangle realLocation = new Rectangle(proposedLocation);
			DrawConstant side = findClosestSide(proposedLocation,
				getParentBorder());
			Point newTopLeft = locateOnBorder(realLocation.getTopLeft(), side,
				0);
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
		public static DrawConstant findClosestSide(Rectangle proposedLocation,
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
						return DrawConstant.WEST;
					} else {
						return DrawConstant.NORTH;
					}
				} else { // west or south
					Point parentBottomLeft = parentBorder.getBottomLeft();
					if ((childCenter.x - parentBottomLeft.x) <= (parentBottomLeft.y - childCenter.y)) {
						return DrawConstant.WEST;
					} else {
						return DrawConstant.SOUTH;
					}
				}
			} else { // EAST, NORTH or SOUTH
				if (childCenter.y < parentCenter.y) // north or east
				{
					Point parentTopRight = parentBorder.getTopRight();
					if ((parentTopRight.x - childCenter.x) <= (childCenter.y - parentTopRight.y)) {
						return DrawConstant.EAST;
					} else {
						return DrawConstant.NORTH;
					}
				} else { // south or east.
					Point parentBottomRight = parentBorder.getBottomRight();
					if ((parentBottomRight.x - childCenter.x) <= (parentBottomRight.y - childCenter.y)) {
						return DrawConstant.EAST;
					} else {
						return DrawConstant.SOUTH;
					}
				}
			}
		}

		/**
		 * called when parent resized.
		 * 
		 * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
		 */
		public void relocate(IFigure target) {
			Rectangle rectSuggested = new Rectangle(getPreferredLocation(),
				getBorderItemBounds().getSize());
			DrawConstant currentSide = findClosestSide(rectSuggested,
				getParentBorder());
			borderItemFigure.setPreferredSide(currentSide);

			Point ptNewLocation = locateOnBorder(getPreferredLocation(),
				borderItemFigure.getPreferredSide(), 0);
			borderItemFigure.setLocation(ptNewLocation);

			borderItemFigure.setCurrentSide(findClosestSide(new Rectangle(
				ptNewLocation, getBorderItemBounds().getSize()),
				getParentBorder()));
		}

		/**
		 * getter for the parent figure
		 * 
		 * @return <code>IFigure</code>
		 */
		public IFigure getParentFigure() {
			return boundaryFigure;
		}

		/**
		 * setter for the parent Figure
		 * 
		 * @param figure
		 */
		public void setParentFigure(BorderItemContainerFigure figure) {
			boundaryFigure = figure;
		}

		/**
		 * getter for the <code>BorderItemFigure</code>
		 * 
		 * @return <code>BorderItemFigure</code>
		 * @deprecated To be changed to private (and renamed)..
		 */
		public BorderItemFigure getGate() {
			return borderItemFigure;
		}

		/**
		 * @param figure
		 * @deprecated To be changed to private (and renamed).
		 */
		public void setGate(BorderItemFigure figure) {
			borderItemFigure = figure;
		}
	} // BorderItemLocator

	/**
	 * Getter method for the locator.
	 * 
	 * @return Locator. The locator which is reposible for positioning the
	 *         borderItemFigure on a shape.
	 */
	public Locator getLocator() {
		return locator;
	}

	/**
	 * The setter method for a locator. The locator is responsible for
	 * positioning the borderItemFigure on the shape.
	 * 
	 * @param locator
	 *            The BorderItemLocator.
	 */
	public void setLocator(Locator locator) {
		this.locator = locator;
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
	 * @return Returns the preferredSide.
	 */
	public DrawConstant getPreferredSide() {
		return preferredSide;
	}

	/**
	 * @param preferredSide
	 *            The preferredSide to set.
	 */
	public void setPreferredSide(DrawConstant preferredSide) {
		this.preferredSide = preferredSide;
		setCurrentSide(preferredSide);
	}

	/**
	 * @return Returns the currentSide.
	 */
	public DrawConstant getCurrentSide() {
		return currentSide;
	}

	/**
	 * @param currentSide
	 *            The currentSide to set.
	 */
	public void setCurrentSide(DrawConstant currentSide) {
		this.currentSide = currentSide;
	}

	/**
	 * gets the boundary figure
	 * 
	 * @return <code>IFigure</code>
	 */
	public IFigure getBoundaryFigure() {
		if (getLocator() instanceof BorderItemLocator) {
			return ((BorderItemLocator) getLocator()).getParentFigure();
		}
		return null;
	}
}