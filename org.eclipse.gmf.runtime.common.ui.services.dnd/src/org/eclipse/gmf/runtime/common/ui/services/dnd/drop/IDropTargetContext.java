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

package org.eclipse.gmf.runtime.common.ui.services.dnd.drop;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Interface that contains the context information relevant to the drop target.
 * 
 * @author Vishy Ramaswamy
 */
public interface IDropTargetContext {

	/**
	 * Constant describing the position of the cursor relative to the target
	 * object. This means the mouse is positioned slightly before the target.
	 * 
	 * @see #getRelativeLocation
	 */
	public static final int LOCATION_BEFORE = 1;

	/**
	 * Constant describing the position of the cursor relative to the target
	 * object. This means the mouse is positioned slightly after the target.
	 * 
	 * @see #getRelativeLocation
	 */
	public static final int LOCATION_AFTER = 2;

	/**
	 * Constant describing the position of the cursor relative to the target
	 * object. This means the mouse is positioned directly on the target.
	 * 
	 * @see #getRelativeLocation
	 */
	public static final int LOCATION_ON = 3;

	/**
	 * Constant describing the position of the cursor relative to the target
	 * object. This means the mouse is not positioned over or near any valid
	 * target.
	 * 
	 * @see #getRelativeLocation
	 */
	public static final int LOCATION_NONE = 4;

	/**
	 * Returns the control associated with the drop target viewer.
	 * 
	 * @return Control
	 */
	public Control getViewerControl();

	/**
	 * Returns the active <code>IWorkbenchPart</code> associated with the drop
	 * target viewer.
	 * 
	 * @return Returns the active <code>IWorkbenchPart</code>
	 */
	public IWorkbenchPart getActivePart();

	/**
	 * Returns the current target on the drop target viewer
	 * 
	 * @return Returns the current target on the drop target viewer
	 */
	public Object getCurrentTarget();

	/**
	 * Returns the coordinates of the current location of the mouse.
	 * 
	 * @return Point the location
	 */
	public Point getCurrentLocation();

	/**
	 * Constant describing the position of the cursor relative to the target
	 * object
	 * 
	 * @return int the location
	 */
	public int getRelativeLocation();
}