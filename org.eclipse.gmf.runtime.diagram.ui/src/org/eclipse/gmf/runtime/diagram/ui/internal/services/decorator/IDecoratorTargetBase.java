/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator;

import org.eclipse.core.runtime.IAdaptable;

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
 * @canBeSeenBy %level1
 */
public interface IDecoratorTargetBase
	extends IAdaptable {

	/**
	 * Enumeration of directions for the location of shape decorations.
	 */
	public class Direction {

		private Direction() {
			// do nothing
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
	
	
}