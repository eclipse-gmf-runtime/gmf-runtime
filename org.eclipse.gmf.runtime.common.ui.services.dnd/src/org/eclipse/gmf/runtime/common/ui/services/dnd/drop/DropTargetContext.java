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

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Concrete implementation of <code>IDropTargetContext</code>
 * 
 * @author Vishy Ramaswamy
 */
public class DropTargetContext
	implements IDropTargetContext {

	/**
	 * The threshold used to determine if the mouse is before or after a target
	 * item.
	 */
	protected static final int LOCATION_EPSILON = 5;

	/**
	 * Attribute for the active part.
	 */
	private final IWorkbenchPart activePart;

	/**
	 * Attribute for the target.
	 */
	protected Object target = null;

	/**
	 * Attribute for the drop target viewer control.
	 */
	private final Control viewerControl;

	/**
	 * Attribute for the current location.
	 */
	protected Point location = null;

	/**
	 * Attribute for the relative location.
	 */
	protected int relativeLocation = IDropTargetContext.LOCATION_NONE;

	/**
	 * Constructor for DropTargetContext.
	 * 
	 * @param anActivePart
	 *            the active IWorkbenchPart
	 * @param aViewerControl
	 *            Control of the drop target
	 */
	public DropTargetContext(IWorkbenchPart anActivePart, Control aViewerControl) {
		super();

		assert null != anActivePart : "anActivePart cannot be null"; //$NON-NLS-1$
		assert null != aViewerControl : "aViewerControl cannot be null"; //$NON-NLS-1$

		this.activePart = anActivePart;
		this.viewerControl = aViewerControl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetContext#getActivePart()
	 */
	public final IWorkbenchPart getActivePart() {
		return activePart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetContext#getCurrentTarget()
	 */
	public final Object getCurrentTarget() {
		return target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetContext#getCurrentLocation()
	 */
	public final Point getCurrentLocation() {
		return location;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetContext#getViewerControl()
	 */
	public final Control getViewerControl() {
		return viewerControl;
	}

	/**
	 * Sets the target item of the given drop event as the current target. Also
	 * sets the current coordinates of the current location of the mouse.
	 * 
	 * @param event
	 *            the event
	 */
	protected void setCurrentTargetAndLocation(DropTargetEvent event) {
		/* Set the target */
		target = event.item == null ? null
			: event.item.getData();

		/* Set the location coordinates */
		location = getViewerControl().toControl(new Point(event.x, event.y));

		/* Set the relative location */
		if (!(event.item instanceof Item)) {
			relativeLocation = IDropTargetContext.LOCATION_NONE;
		} else {
			/* Get the item */
			Item item = (Item) event.item;

			/* Get the bounds */
			Rectangle bounds = null;
			if (item instanceof TreeItem) {
				bounds = ((TreeItem) item).getBounds();
			} else if (item instanceof TableItem) {
				bounds = ((TableItem) item).getBounds(0);
			}

			/* Set the relative location */
			if (bounds == null) {
				relativeLocation = IDropTargetContext.LOCATION_NONE;
			} else if ((location.y - bounds.y) < LOCATION_EPSILON) {
				relativeLocation = IDropTargetContext.LOCATION_BEFORE;
			} else if ((bounds.y + bounds.height - location.y) < LOCATION_EPSILON) {
				relativeLocation = IDropTargetContext.LOCATION_AFTER;
			} else {
				relativeLocation = IDropTargetContext.LOCATION_ON;
			}
		}
	}

	/**
	 * Set details of the event upon entering the drop target
	 * 
	 * @param event
	 *            DropTargetEvent to have its detail field reset
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	protected void dragEnter(DropTargetEvent event) {
		event.detail = DND.DROP_NONE;
	}

	/**
	 * Called when the drag operation has changed
	 * 
	 * @param event
	 *            DropTargetEvent to update
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	protected void dragOperationChanged(DropTargetEvent event) {
		event.detail = DND.DROP_NONE;
	}

	/**
	 * Called when over the drop target
	 * 
	 * @param event
	 *            DropTargetEvent to update
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	protected void dragOver(DropTargetEvent event) {
		event.detail = DND.DROP_NONE;
	}

	/**
	 * Called upon a drop
	 * 
	 * @param event
	 *            DropTargetEvent to update
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	protected void drop(DropTargetEvent event) {
		event.detail = DND.DROP_NONE;
	}

	/**
	 * Called immediately before a drop
	 * 
	 * @param event
	 *            DropTargetEvent to have its detail field reset
	 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	protected void dropAccept(DropTargetEvent event) {
		event.detail = DND.DROP_NONE;
	}

	/**
	 * Called when leaving the drop target or cancelling
	 * 
	 * @param event
	 *            DropTargetEvent to update
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	protected void dragLeave(DropTargetEvent event) {
		/* method not implemented */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetContext#getRelativeLocation()
	 */
	public final int getRelativeLocation() {
		return relativeLocation;
	}
}