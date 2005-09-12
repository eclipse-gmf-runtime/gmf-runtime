/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
