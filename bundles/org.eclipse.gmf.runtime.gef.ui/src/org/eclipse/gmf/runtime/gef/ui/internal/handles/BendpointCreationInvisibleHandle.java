/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.handles;

import org.eclipse.draw2d.Graphics;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.handles.BendpointCreationHandle;
import org.eclipse.gmf.runtime.gef.ui.internal.l10n.Cursors;

/**
 * @author sshaw
 */
public class BendpointCreationInvisibleHandle extends BendpointCreationHandle {

	/**
	 * Creates a new BendpointCreationHandle, sets its owner to <code>owner</code>
	 * and its index to <code>index</code>, and sets its locator to a new
	 * {@link org.eclipse.draw2d.MidpointLocator}.
	 */
	public BendpointCreationInvisibleHandle(
		ConnectionEditPart owner,
		int index) {
		super(owner, index);
		
		setCursor(Cursors.CURSOR_SEG_ADD);
	}

	/**
	 * Draws the handle with fill color and outline color dependent 
	 * on the primary selection status of the owner editpart.
	 *
	 * @param g The graphics used to paint the figure.
	 */
	public void paintFigure(Graphics g) {
		// do nothing - this handle is invisible
	}
}
