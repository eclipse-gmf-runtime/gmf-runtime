/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.tools.ConnectorHandleTool;
import org.eclipse.gmf.runtime.diagram.ui.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;

/**
 * This is the handle figure used to represent a connector handle.
 * 
 * @author cmahoney
 */
public class ConnectorHandle extends AbstractHandle {

	/**
	 * An enumeration of connector directions.
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

	/** the error icon that can be superimposed on the connector handle image */
	private static final ImageFigure ERROR_IMAGE =
		new ImageFigure(Images.ICON_ERROR);

	static {
		ERROR_IMAGE.setSize(
			Images.ICON_ERROR.getBounds().width,
			Images.ICON_ERROR.getBounds().height);
	}

	/** direction that the relationship is to be created */
	private HandleDirection handleDirection;

	/**
	 * Creates a new <code>ConnectorHandle</code>.
	 * @param ownerEditPart the editpart for which the handle belongs
	 * @param relationshipDirection direction that the relationship is to be created
	 * @param tooltip the tooltip
	 */
	public ConnectorHandle(
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
		return new ConnectorHandleTool(this);
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#findFigureAt(int, int, org.eclipse.draw2d.TreeSearch)
	 */
	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		// return the ConnectorHandle and not the children figures
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
	 * Superimposes an error icon on this connector handle.
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
		int side = ((ConnectorHandleLocator) getLocator())
			.getBorderSide();
		Image image = null;
		if (side == PositionConstants.WEST) {
			image = isIncoming() ? PresentationResourceManager.getInstance()
				.getImage(
					PresentationResourceManager.IMAGE_HANDLE_INCOMING_WEST)
				: PresentationResourceManager.getInstance().getImage(
					PresentationResourceManager.IMAGE_HANDLE_OUTGOING_WEST);
		} else if (side == PositionConstants.EAST) {
			image = isIncoming() ? PresentationResourceManager.getInstance()
				.getImage(
					PresentationResourceManager.IMAGE_HANDLE_INCOMING_EAST)
				: PresentationResourceManager.getInstance().getImage(
					PresentationResourceManager.IMAGE_HANDLE_OUTGOING_EAST);
		} else if (side == PositionConstants.SOUTH){
			image = isIncoming() ? PresentationResourceManager.getInstance()
				.getImage(
					PresentationResourceManager.IMAGE_HANDLE_INCOMING_SOUTH)
				: PresentationResourceManager.getInstance().getImage(
					PresentationResourceManager.IMAGE_HANDLE_OUTGOING_SOUTH);
		} else {
		image = isIncoming() ? PresentationResourceManager.getInstance()
			.getImage(
				PresentationResourceManager.IMAGE_HANDLE_INCOMING_NORTH)
			: PresentationResourceManager.getInstance().getImage(
				PresentationResourceManager.IMAGE_HANDLE_OUTGOING_NORTH);
		}

		ImageFigure imageFigure = new ImageFigure(image);
		imageFigure.setSize(image.getBounds().width, image.getBounds().height);
		add(imageFigure);

		setSize(imageFigure.getSize().getUnioned(ERROR_IMAGE.getSize()));

		super.validate();
	}

}
