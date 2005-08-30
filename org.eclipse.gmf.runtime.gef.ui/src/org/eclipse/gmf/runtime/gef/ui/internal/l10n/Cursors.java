/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.gef.ui.internal.l10n;

import org.eclipse.swt.graphics.Cursor;


/**
 * This is class that stores a series of globally accessible cursors.
 * 
 * @author sshaw
 * @canBeSeenBy %partners
 *
 */
public class Cursors {

    /**
     * Constant define for a cursor used by adding line segments to an
     * existing line.
     */
    public static final Cursor CURSOR_SEG_ADD;
    
    /**
     * Constant define for a cusor used to move an existing line segment
     */
    public static final Cursor CURSOR_SEG_MOVE;

    static {
		CURSOR_SEG_ADD = new Cursor(null, GefResourceManager.getInstance()
			.getImageDescriptor(GefResourceManager.SEG_ADD_MASK_IMAGE)
			.getImageData(), GefResourceManager.getInstance()
			.getImageDescriptor(GefResourceManager.SEG_ADD_IMAGE)
			.getImageData(), 0, 0);

		CURSOR_SEG_MOVE = new Cursor(null, GefResourceManager.getInstance()
			.getImageDescriptor(GefResourceManager.SEG_MOVE_MASK_IMAGE)
			.getImageData(), GefResourceManager.getInstance()
			.getImageDescriptor(GefResourceManager.SEG_MOVE_IMAGE)
			.getImageData(), 0, 0);
    }

}
