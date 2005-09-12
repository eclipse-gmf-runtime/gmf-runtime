/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.tools;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ZoomableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.Images;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 * 
 * Implementation for the ZoomTool that supports marquee selection of zoom area
 * and zoom-in / zoom-out capability.
 */
public class ZoomTool
	extends RubberbandSelectionTool {

	static private final int ZOOM_IN_MODE = 1;
	static private final int ZOOM_OUT_MODE = 2;
	static private final int ZOOM_PAN_MODE = 3;

	static final Cursor zoom_out_cursor = new Cursor(null,
		Images.DESC_ACTION_ZOOM_OUT.getImageData(), 0, 0); //$NON-NLS-1$
	static final Cursor zoom_in_cursor = new Cursor(null,
		Images.DESC_ACTION_ZOOM_IN.getImageData(), 0, 0); //$NON-NLS-1$
	static final Cursor zoom_pan_cursor = Cursors.HAND;

	private int zoommode = 1;
	
	/**
	 * Creates a new MarqueeSelectionTool.
	 */
	public ZoomTool() {
		setUnloadWhenFinished(false);
	}

	protected Cursor getDefaultCursor() {
		if (getCurrentViewer() instanceof GraphicalViewer) {
			if (getZoomMode() == ZOOM_IN_MODE)
				return zoom_in_cursor;
			else if (getZoomMode() == ZOOM_OUT_MODE)
				return zoom_out_cursor;
			else if (getZoomMode() == ZOOM_PAN_MODE)
				return zoom_pan_cursor;
		}
		return Cursors.NO;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
	 */
	protected String getCommandName() {
		return REQ_SELECTION;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
	 */
	protected String getDebugName() {
		return "Marquee Tool";//$NON-NLS-1$
	}

	private int getZoomMode() {
		return zoommode;
	}

	/**
	 * Handles high-level processing of a key down event. KeyEvents are
	 * forwarded to the current viewer's {@link KeyHandler}, via
	 * {@link KeyHandler#keyPressed(KeyEvent)}.
	 * 
	 * @see AbstractTool#handleKeyDown(KeyEvent)
	 */
	protected boolean handleKeyDown(KeyEvent e) {
		if (super.handleKeyDown(e))
			return true;
		if (getCurrentViewer().getKeyHandler() != null
			&& getCurrentViewer().getKeyHandler().keyPressed(e))
			return true;
		if (e.keyCode == SWT.SHIFT)
			setZoomMode(ZOOM_OUT_MODE);
		if (e.keyCode == SWT.ALT)
			setZoomMode(ZOOM_PAN_MODE);
		return false;
	}

	/**
	 * Handles high-level processing of a key down event. KeyEvents are
	 * forwarded to the current viewer's {@link KeyHandler}, via
	 * {@link KeyHandler#keyPressed(KeyEvent)}.
	 * 
	 * @see AbstractTool#handleKeyDown(KeyEvent)
	 */
	protected boolean handleKeyUp(KeyEvent e) {
		if (super.handleKeyUp(e))
			return true;
		if (e.keyCode == SWT.SHIFT ||
			e.keyCode == SWT.ALT)
			setZoomMode(ZOOM_IN_MODE);
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.tools.RubberbandSelectionTool#performMarqueeSelect()
	 */
	protected void performMarqueeSelect() {
		EditPartViewer viewer = getCurrentViewer();
		ZoomableEditPart zoomableEditPart = null;
		if (viewer.getRootEditPart() instanceof ZoomableEditPart)
			zoomableEditPart = (ZoomableEditPart)viewer.getRootEditPart();
		
		Rectangle zoomRect = getMarqueeSelectionRectangle();
		if (zoomRect.width < 2 || zoomRect.height < 2) {
			if (getZoomMode() == ZOOM_OUT_MODE) {
				zoomableEditPart.zoomOut(getLocation());
			} else if (getZoomMode() == ZOOM_IN_MODE) {
				zoomableEditPart.zoomIn(getLocation());
			}
		}
		else {
			zoomableEditPart.zoomTo(zoomRect);
		}
	}

	/**
	 * @see org.eclipse.gef.Tool#setViewer(org.eclipse.gef.EditPartViewer)
	 */
	public void setViewer(EditPartViewer viewer) {
		if (viewer == getCurrentViewer())
			return;
		super.setViewer(viewer);
	}

	private void setZoomMode(int zoommode) {
		this.zoommode = zoommode;
		setCursor(getDefaultCursor());
	}
}