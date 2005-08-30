/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.figures;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * A circle figure with hashed lines.
 * 
 * @author chmahone
 */
public class HashedCircle extends Ellipse {

    /**
     * An enumeration describing the hash lines inside the circle.
     */
    public static final class HashType {
        private HashType() {
        	// empty constructor
        }

        /** Draws an 'X' through the circle */
        public static final HashType X = new HashType();

        /** Draws a '\' through the circle */
        public static final HashType BACKSLASH = new HashType();
    }

    /** The type of hash lines to draw - default 'X' */
    private HashType hashType = HashType.X;

	/**
	 * Creates a <code>HashedCircle</code>.
     * @param hashType The hashType to set
     * @param radius The radius to set
	 */
	public HashedCircle(HashType hashType, int radius) {
		super();
		this.hashType = hashType;
		int width = radius * 2;
		setSize(new Dimension(width, width));
		setMaximumSize(new Dimension(width, width));
	}

    /**
     * @see #outlineShape(Graphics)
     */
    protected void outlineShape(Graphics graphics) {
        // draw the circle
        super.outlineShape(graphics);

        // calculate the x and y shift from the center to the corners of the circle
        double radius = getBounds().width / 2.0;
        int xyShift = new Double(radius / Math.sqrt(2.0)).intValue();

        Point center = getBounds().getCenter();

        if (hashType == HashType.X || hashType == HashType.BACKSLASH) {
            Point topLeft = center.getTranslated(-xyShift, -xyShift);
            Point bottomRight = center.getTranslated(xyShift, xyShift);
            graphics.drawLine(topLeft, bottomRight);
        }

        if (hashType == HashType.X) {
            Point topRight = center.getTranslated(xyShift, -xyShift);
            Point bottomLeft = center.getTranslated(-xyShift, xyShift);
            graphics.drawLine(bottomLeft, topRight);
        }
    }

}
