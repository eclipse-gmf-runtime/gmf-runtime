/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.services.decorator;

import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.IDecoratorTargetBase;

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
 * 
 * @author cmahoney
 */
public interface IDecoratorTarget
	extends IDecoratorTargetBase {
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
	 *            The percentage of the connector length away from the source
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

}