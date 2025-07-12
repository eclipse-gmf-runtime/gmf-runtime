/******************************************************************************
 * Copyright (c) 2002, 2025 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.l10n;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.ImageDataProvider;

/**
 * Stores a series of globally accessible cursors.
 * 
 * @author sshaw
 */
public class Cursors {

    /**
     * Constant define for a cursor used by adding line segments to an
     * existing line.
     */
    public static final Cursor CURSOR_SEG_ADD;
    
    /**
     * Constant define for a cursor used to move an existing line segment
     */
    public static final Cursor CURSOR_SEG_MOVE;

    static {
		CURSOR_SEG_ADD = createCursor(GefUIPluginImages.DESC_SEG_ADD, 0, 0);
		CURSOR_SEG_MOVE = createCursor(GefUIPluginImages.DESC_SEG_MOVE, 0, 0);
	}
    
	// Taken from org.eclipse.gef.internal.InternalGEFPlugin.java
    // https://github.com/eclipse-gef/gef-classic/blob/467992a631d37b3a5268765ad87bcae8425a9d21/org.eclipse.gef/src/org/eclipse/gef/internal/InternalGEFPlugin.java#L127
	private static Cursor createCursor(ImageDescriptor source, int hotspotX, int hotspotY) {
		try {
			Constructor<Cursor> ctor = Cursor.class.getConstructor(Device.class, ImageDataProvider.class, int.class,
					int.class);
			return ctor.newInstance(null, (ImageDataProvider) source::getImageData, hotspotX, hotspotY);
		} catch (NoSuchMethodException e) {
			// SWT version < 3.131.0 (no ImageDataProvider-based constructor)
			return new Cursor(null, source.getImageData(100), hotspotX, hotspotY); // older constructor
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to instantiate Cursor", e); //$NON-NLS-1$
		}
	}
}
