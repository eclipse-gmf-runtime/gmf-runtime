/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;

/**
 * This locator places the figure relative to the reference figure's edge 
 * (where the reference figure's edge is the same as where the resize/move
 * handles would be placed).  
 * There are three options available for controlling where the figure is 
 * placed:
 * <ol>
 * <li>direction - A direction to place the figure relative to the reference
 * figure as defined in {@link PositionConstants}
 * <li>margin - The margin is the space between the reference figure's edge 
 * and the figure.  A positive margin will place the figure outside the
 * reference figure, a negative margin will place the figure inside the 
 * reference figure.
 * </ol>
 * 
 * NOTE: This locator uses the size of the figure to calculate where it should
 * be placed.  Therefore it may be necessary to explicitly set this figure's
 * size.
 * 
 * @author cmahoney
 */
public class RelativeToBorderLocator implements Locator {

	/** the margin to leave by the edge of the parent figure */
	private int margin;

	/** the direction */
	protected int direction;

	/** the parent figure */
	protected IFigure reference;
	
	/**
	 * Constructor for <code>RelativeToBorderLocator</code>.
	 * 
	 * @param reference the parent figure
	 * @param direction A direction to place the figure relative to the reference
	 * figure as defined in {@link PositionConstants}
	 * @param margin The margin is the space between the reference figure's edge 
	 * and the figure.  A positive margin will place the figure outside the
	 * reference figure, a negative margin will place the figure inside the 
	 * reference figure.
	 */
	public RelativeToBorderLocator(
		IFigure reference,
		int direction,
		int margin) {
			
		this.reference = reference;
		this.direction = direction;
		this.margin = margin;
	}

	/**
	 * Puts the figure either inside or outside the parent edge (where the edge 
	 * is the same as where the resize/move handles would be placed),
	 * identified by the direction, with a margin.
	 * 
	 * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
	 */
	public void relocate(IFigure target) {
		Rectangle bounds =
			reference instanceof HandleBounds
				? new PrecisionRectangle(((HandleBounds) reference).getHandleBounds())
				: new PrecisionRectangle(reference.getBounds());	

		reference.translateToAbsolute(bounds);
		target.translateToRelative(bounds);

		int width = target.getBounds().width;
		int halfWidth = width / 2;

		int height = target.getBounds().height;
		int halfHeight = height / 2;

		if (direction == PositionConstants.CENTER) {

			Dimension shift = new Dimension(-halfWidth, -halfHeight);
			target.setLocation(bounds.getCenter().getTranslated(shift));

		} else if (margin < 0) {

			if (direction == PositionConstants.NORTH_WEST) {

				Dimension shift = new Dimension(-margin, -margin);
				target.setLocation(bounds.getTopLeft().getTranslated(shift));

			} else if (direction == PositionConstants.NORTH) {

				Dimension shift = new Dimension(-halfWidth, -margin);
				target.setLocation(bounds.getTop().getTranslated(shift));

			} else if (direction == PositionConstants.NORTH_EAST) {

				Dimension shift = new Dimension(-(width + -margin), -margin);
				target.setLocation(bounds.getTopRight().getTranslated(shift));

			} else if (direction == PositionConstants.SOUTH_WEST) {

				Dimension shift = new Dimension(-margin, -(height + -margin));
				target.setLocation(bounds.getBottomLeft().getTranslated(shift));

			} else if (direction == PositionConstants.SOUTH) {

				Dimension shift = new Dimension(-halfWidth, -(height + -margin));
				target.setLocation(bounds.getBottom().getTranslated(shift));

			} else if (direction == PositionConstants.SOUTH_EAST) {

				Dimension shift = new Dimension(-(width + -margin),
					-(height + -margin));
				target
					.setLocation(bounds.getBottomRight().getTranslated(shift));

			} else if (direction == PositionConstants.WEST) {

				Dimension shift = new Dimension(-margin, -halfHeight);
				target.setLocation(bounds.getLeft().getTranslated(shift));

			} else if (direction == PositionConstants.EAST) {

				Dimension shift = new Dimension(-(width + -margin), -halfHeight);
				target.setLocation(bounds.getRight().getTranslated(shift));

			}
		} else {

			if (direction == PositionConstants.NORTH_WEST) {

				Dimension shift =
					new Dimension(- (width + margin), - (height + margin));
				target.setLocation(bounds.getTopLeft().getTranslated(shift));

			} else if (direction == PositionConstants.NORTH) {

				Dimension shift =
					new Dimension(-halfWidth, - (height + margin));
				target.setLocation(bounds.getTop().getTranslated(shift));

			} else if (direction == PositionConstants.NORTH_EAST) {

				Dimension shift = new Dimension(margin, - (height + margin));
				target.setLocation(bounds.getTopRight().getTranslated(shift));

			} else if (direction == PositionConstants.SOUTH_WEST) {

				Dimension shift = new Dimension(- (width + margin), margin);
				target.setLocation(bounds.getBottomLeft().getTranslated(shift));

			} else if (direction == PositionConstants.SOUTH) {

				Dimension shift = new Dimension(-halfWidth, margin);
				target.setLocation(bounds.getBottom().getTranslated(shift));

			} else if (direction == PositionConstants.SOUTH_EAST) {

				Dimension shift = new Dimension(margin, margin);
				target.setLocation(
					bounds.getBottomRight().getTranslated(shift));

			} else if (direction == PositionConstants.WEST) {

				Dimension shift =
					new Dimension(- (width + margin), -halfHeight);
				target.setLocation(bounds.getLeft().getTranslated(shift));

			} else if (direction == PositionConstants.EAST) {

				Dimension shift = new Dimension(margin, -halfHeight);
				target.setLocation(bounds.getRight().getTranslated(shift));

			}
		}
	}

}
