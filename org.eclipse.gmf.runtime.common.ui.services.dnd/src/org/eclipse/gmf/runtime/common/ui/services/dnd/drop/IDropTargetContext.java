/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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