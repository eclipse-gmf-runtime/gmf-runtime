/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.gef.ui.internal.handles;

import org.eclipse.gef.ConnectionEditPart;

import org.eclipse.gmf.runtime.gef.ui.internal.l10n.Cursors;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.gef.ui.*
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class LineSegMoveInvisibleHandle extends BendpointCreationInvisibleHandle {

	/**
	 * Creates a new BendpointCreationHandle, sets its owner to <code>owner</code>
	 * and its index to <code>index</code>, and sets its locator to a new
	 * {@link org.eclipse.draw2d.MidpointLocator}.
	 */
	public LineSegMoveInvisibleHandle(
		ConnectionEditPart owner,
		int index) {
		super(owner, index);
		
		setCursor(Cursors.CURSOR_SEG_MOVE);
	}

}
