/******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.tools;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.ZoomableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.l10n.DiagramUIPluginImages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;

/**
 * @author sshaw
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
		DiagramUIPluginImages.DESC_ZOOM_OUT.getImageData(), 0, 0); 

	static final Cursor zoom_in_cursor = new Cursor(null,
		DiagramUIPluginImages.DESC_ZOOM_IN.getImageData(), 0, 0);

	static final Cursor zoom_pan_cursor = Cursors.HAND;

    /**
     * true if this is a zoom in tool; false if this is a zoom out tool
     */
    private boolean zoomIn = true;
    
	private int zoommode = 1;
	
	/**
	 * Creates a new ZoomTool.
	 * @param zoomIn true if this is a zoom in tool; false if this is a zoom out tool
	 */
	public ZoomTool(boolean zoomIn) {
	    this.zoomIn = zoomIn;
	    zoommode = getInitialZoomMode();
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
		return "Zoom Tool";//$NON-NLS-1$
	}

	private int getZoomMode() {
		return zoommode;
	}

    /* (non-Javadoc)
     * @see org.eclipse.gef.tools.AbstractTool#handleViewerEntered()
     */
    protected boolean handleViewerEntered() {
        boolean handled = super.handleViewerEntered();
        if (getCurrentViewer() != null) {
        	getCurrentViewer().getControl().forceFocus();
       		handled = true;
       	}
       	
       	return handled;
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
			setZoomMode(zoomIn ? ZOOM_OUT_MODE : ZOOM_IN_MODE);
		if (e.keyCode == SWT.ALT && zoomIn)
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
			setZoomMode(getInitialZoomMode());
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
	
	private int getInitialZoomMode() {
	    return zoomIn ? ZOOM_IN_MODE : ZOOM_OUT_MODE;
	}
}
