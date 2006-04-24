/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.decorator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.swt.graphics.Image;

/**
 * An object that can be decorated. The decorator target is an adaptable.
 * Minimally, it adapts to an <code>EditPart</code> and
 * <code>org.eclipse.uml2.Element</code> if the shape/connection has an
 * underlying element.
 * 
 * <p>
 * Here is an example:
 * 
 * <pre>
 * theDecoratorTarget.getAdapter(EditPart.class)
 * </pre>
 * 
 * </p>
 * <p>
 * This interface is <EM>not</EM> intended to be implemented by clients as new
 * methods may be added in the future. 
 * </p>
 * 
 * @author cmahoney
 */
public interface IDecoratorTarget

	extends IAdaptable {
    
  /**
   * Enumeration of directions for the location of shape decorations.
   */
    public class Direction {

        private Direction() {
            super();
        }
        
        /** Center */
        public static final Direction CENTER = new Direction();

        /** North */
        public static final Direction NORTH = new Direction();

        /** South */
        public static final Direction SOUTH = new Direction();

        /** West */
        public static final Direction WEST = new Direction();

        /** East */
        public static final Direction EAST = new Direction();

        /** North-East */
        public static final Direction NORTH_EAST = new Direction();

        /** North-West */
        public static final Direction NORTH_WEST = new Direction();

        /** South-East */
        public static final Direction SOUTH_EAST = new Direction();

        /** South-West */
        public static final Direction SOUTH_WEST = new Direction();
    
    }
    
	/**
	 * Installs a decorator on this decorator target using a key (a String
	 * identifier). If another decorator is installed on the same target with
	 * the same key then it will override the previous one installed.
	 * 
	 * @param key
	 *            the key for the decorator, used to override a decorator
	 *            previously installed on this decoratorTarget object
	 * @param decorator
	 *            the decorator to install
	 */
	public void installDecorator(Object key, IDecorator decorator);

	/**
	 * Adds an image as a decoration on a shape.
	 * 
	 * @param image
	 *            The image to be used as the decoration.
	 * @param direction
	 *            The direction relative to the shape to place the decoration.
	 * @param margin
	 *            The margin is the space, in himetric units, between the
	 *            shape's edge and the decoration. A positive margin will place
	 *            the figure outside the shape, a negative margin will place the
	 *            decoration inside the shape.
	 * @param isVolatile
	 *            True if this decoration is volatile (i.e. not to be included
	 *            in the printed output of a diagram); false otherwise.
	 * @return The decoration object, which will be needed to remove the
	 *         decoration from the shape.
	 */
	public IDecoration addShapeDecoration(Image image, Direction direction,
			int margin, boolean isVolatile);

	/**
	 * Adds an image as a decoration on a connection.
	 * 
	 * @param image
	 *            The image to be used as the decoration.
	 * @param percentageFromSource
	 *            The percentage of the connection length away from the source
	 *            end (range is from 0 to 100) where the decoration should be
	 *            positioned.
	 * @param isVolatile
	 *            True if this decoration is volatile (i.e. not to be included
	 *            in the printed output of a diagram); false otherwise.
	 * @return The decoration object, which will be needed to remove the
	 *         decoration from the connection.
	 */
	public IDecoration addConnectionDecoration(Image image,
			int percentageFromSource, boolean isVolatile);

	/**
	 * Removes the decoration from the shape or connection it has been added to.
	 * 
	 * @param decoration
	 *            The decoration to be removed.
	 */
	public void removeDecoration(IDecoration decoration);
    
    /**
     * Adds a figure as a decoration on a shape.
     * 
     * @param figure
     *            the figure to be used as the decoration
     * @param direction
     *            The direction relative to the shape to place the
     *            decoration.
     * @param margin
     *            The margin is the space, in himetric units, between the
     *            shape's edge and the decoration. A positive margin will
     *            place the figure outside the shape, a negative margin will
     *            place the decoration inside the shape.
     * @param isVolatile
     *            True if this decoration is volatile (i.e. not to be
     *            included in the printed output of a diagram); false
     *            otherwise.
     * @return The decoration object, which is needed to later remove the
     *         decoration.
     */
    public IDecoration addShapeDecoration(IFigure figure,
            Direction direction, int margin, boolean isVolatile);

    /**
     * Adds a figure as a decoration on a connection.
     * 
     * @param figure
     *            the figure to be used as the decoration
     * @param percentageFromSource
     *            The percentage of the connection length away from the
     *            source end (range is from 0 to 100) where the decoration
     *            should be positioned.
     * @param isVolatile
     *            True if this decoration is volatile (i.e. not to be
     *            included in the printed output of a diagram); false
     *            otherwise.
     * @return The decoration object, which is needed to later remove the
     *         decoration.
     */
    public IDecoration addConnectionDecoration(IFigure figure,
            int percentageFromSource, boolean isVolatile);
    /**
     * Adds a figure as a decoration on a shape or connection.
     * 
     * @param figure
     *            the figure to be used as the decoration
     * @param locator
     *            The locator to be used to position the decoration
     * @param isVolatile
     *            True if this decoration is volatile (i.e. not to be
     *            included in the printed output of a diagram); false
     *            otherwise.
     * @return The decoration object, which is needed to later remove the
     *         decoration.
     */
    public IDecoration addDecoration(IFigure figure, Locator locator,
            boolean isVolatile);
}