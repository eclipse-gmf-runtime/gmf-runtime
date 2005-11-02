/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.handles;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.tools.ConnectionHandleTool;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;
import org.eclipse.swt.graphics.Image;

/**
 * This is the handle figure used to represent a connection handle.
 * 
 * @author cmahoney
 */
public class ConnectionHandle extends AbstractHandle {

	/**
	 * An enumeration of connection directions.
	 * OUTGOING = source to target
	 * INCOMING = target to source
	 */
	public static final class HandleDirection {
		private HandleDirection() {
		    // empty
		}

		/** source to target */
		public static final HandleDirection OUTGOING = new HandleDirection();

		/** target to source */
		public static final HandleDirection INCOMING = new HandleDirection();
	}

	/** the error icon that can be superimposed on the connection handle image */
	private static final ImageFigure ERROR_IMAGE = new ImageFigure(
		DiagramResourceManager.getInstance().getImage(
			DiagramResourceManager.IMAGE_ERROR));

	static {
		ERROR_IMAGE.setSize(DiagramResourceManager.getInstance().getImage(
			DiagramResourceManager.IMAGE_ERROR).getBounds().width,
			DiagramResourceManager.getInstance().getImage(
				DiagramResourceManager.IMAGE_ERROR).getBounds().height);
	}

	/** direction that the relationship is to be created */
	private HandleDirection handleDirection;

	/**
	 * Creates a new <code>ConnectionHandle</code>.
	 * @param ownerEditPart the editpart for which the handle belongs
	 * @param relationshipDirection direction that the relationship is to be created
	 * @param tooltip the tooltip
	 */
	public ConnectionHandle(
		IGraphicalEditPart ownerEditPart,
		HandleDirection relationshipDirection,
		String tooltip) {

		setOwner(ownerEditPart);
		setRelationshipDirection(relationshipDirection);
		setToolTip(new Label(tooltip));

		// A stack layout is used so that the error icon can be overlayed on top.
		setLayoutManager(new StackLayout());
	}
	
	/**
	 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
	 */
	protected DragTracker createDragTracker() {
		return new ConnectionHandleTool(this);
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#findFigureAt(int, int, org.eclipse.draw2d.TreeSearch)
	 */
	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		// return the ConnectionHandle and not the children figures
		if (containsPoint(x, y)) {
			return this;
		}
		return super.findFigureAt(x, y, search);
	}

	/**
	 * Make public.
	 * @see org.eclipse.gef.handles.AbstractHandle#setLocator(org.eclipse.draw2d.Locator)
	 */
	public void setLocator(Locator locator) {
		super.setLocator(locator);
	}

	/**
	 * Make public.
	 * @see org.eclipse.gef.handles.AbstractHandle#getOwner()
	 */
	public GraphicalEditPart getOwner() {
		return super.getOwner();
	}

	/**
	 * Sets the direction that the relationship is to be created.
	 * @param direction the <code>HandleDirection</code> that the relationship is to be created
	 */
	protected void setRelationshipDirection(HandleDirection direction) {
		handleDirection = direction;
	}

	/**
	 * Is this for incoming relationships?
	 * @return true if this is for incoming relationships, false otherwise
	 */
	public boolean isIncoming() {
		return handleDirection == HandleDirection.INCOMING;
	}

	/**
	 * Superimposes an error icon on this connection handle.
	 */
	public void addErrorIcon() {
		add(ERROR_IMAGE);
	}

	/**
	 * Removes the error icon if it is being displayed.
	 */
	public void removeErrorIcon() {
		if (getChildren().contains(ERROR_IMAGE)) {
			remove(ERROR_IMAGE);
		}
	}

	/**
	 * Updates the images used for the handles, based on the side they will
	 * appear on.  Sets the location of the handles using the locator.
	 * @see org.eclipse.draw2d.IFigure#validate()
	 */
	public void validate() {
		if (isValid())
			return;

		removeAll();
		int side = ((ConnectionHandleLocator) getLocator())
			.getBorderSide();
		Image image = null;
		if (side == PositionConstants.WEST) {
			image = isIncoming() ? DiagramResourceManager.getInstance()
				.getImage(
					DiagramResourceManager.IMAGE_HANDLE_INCOMING_WEST)
				: DiagramResourceManager.getInstance().getImage(
					DiagramResourceManager.IMAGE_HANDLE_OUTGOING_WEST);
		} else if (side == PositionConstants.EAST) {
			image = isIncoming() ? DiagramResourceManager.getInstance()
				.getImage(
					DiagramResourceManager.IMAGE_HANDLE_INCOMING_EAST)
				: DiagramResourceManager.getInstance().getImage(
					DiagramResourceManager.IMAGE_HANDLE_OUTGOING_EAST);
		} else if (side == PositionConstants.SOUTH){
			image = isIncoming() ? DiagramResourceManager.getInstance()
				.getImage(
					DiagramResourceManager.IMAGE_HANDLE_INCOMING_SOUTH)
				: DiagramResourceManager.getInstance().getImage(
					DiagramResourceManager.IMAGE_HANDLE_OUTGOING_SOUTH);
		} else {
		image = isIncoming() ? DiagramResourceManager.getInstance()
			.getImage(
				DiagramResourceManager.IMAGE_HANDLE_INCOMING_NORTH)
			: DiagramResourceManager.getInstance().getImage(
				DiagramResourceManager.IMAGE_HANDLE_OUTGOING_NORTH);
		}

		ImageFigure imageFigure = new ImageFigure(image);
		imageFigure.setSize(image.getBounds().width, image.getBounds().height);
		add(imageFigure);

		setSize(imageFigure.getSize().getUnioned(ERROR_IMAGE.getSize()));

		super.validate();
	}

}
