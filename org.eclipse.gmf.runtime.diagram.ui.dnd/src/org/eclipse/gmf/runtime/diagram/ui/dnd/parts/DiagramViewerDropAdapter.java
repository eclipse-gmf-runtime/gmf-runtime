/******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.dnd.parts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.gef.dnd.DelegatingDropAdapter;
import org.eclipse.gef.dnd.TransferDropTargetListener;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.DelegatingDropTargetAdapter;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.DropTargetContext;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetContext;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;

/**
 * This class extends the GEF drop target adapter. It delgates all the drop
 * events to the
 * <code>org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.DelegatingDropTargetAdapter</code>
 * If there are no drop target listeners registered with the common
 * infrastructure, then the drop events are delegated to the parent adpater
 * 
 * @author Vishy Ramaswamy
 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.DelegatingDropTargetAdapter
 */
public class DiagramViewerDropAdapter
	extends DelegatingDropAdapter {

	/**
	 * Inner class that extends the drop target context to provide graphical
	 * viewer specific behaviour
	 * 
	 * @author Vishy Ramaswamy
	 */
	private final class DiagramViewerDropTargetContext
		extends DropTargetContext {

		/**
		 * Constructor for DiagramViewerDropTargetContext
		 * 
		 * @param activePart
		 * @param viewerControl
		 */
		public DiagramViewerDropTargetContext(IWorkbenchPart activePart,
				Control viewerControl) {
			super(activePart, viewerControl);
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.DropTargetContext#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
		 */
		protected void dragEnter(DropTargetEvent event) {
			DiagramViewerDropAdapter.super.dragEnter(event);
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.DropTargetContext#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
		 */
		protected void dragLeave(DropTargetEvent event) {
			DiagramViewerDropAdapter.super.dragLeave(event);
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.DropTargetContext#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
		 */
		protected void dragOperationChanged(DropTargetEvent event) {
			DiagramViewerDropAdapter.super.dragOperationChanged(event);
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.DropTargetContext#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
		 */
		protected void dragOver(DropTargetEvent event) {
			DiagramViewerDropAdapter.super.dragOver(event);
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.DropTargetContext#drop(org.eclipse.swt.dnd.DropTargetEvent)
		 */
		protected void drop(DropTargetEvent event) {
			DiagramViewerDropAdapter.super.drop(event);
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.DropTargetContext#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
		 */
		protected void dropAccept(DropTargetEvent event) {
			DiagramViewerDropAdapter.super.dropAccept(event);
		}

		/*
		 * @see org.eclipse.gmf.runtime.common.ui.internal.dnd.drop.DropTargetContext#setCurrentTargetAndLocation(org.eclipse.swt.dnd.DropTargetEvent)
		 */
		protected void setCurrentTargetAndLocation(DropTargetEvent event) {
			/* Set the location */
			location = getViewerControl()
				.toControl(new Point(event.x, event.y));

			/* Set the target */
			org.eclipse.draw2d.geometry.Point gefPoint = new org.eclipse.draw2d.geometry.Point(
				location.x, location.y);

			target = DiagramViewerDropAdapter.this.getWorkbenchPart()
				.getDiagramGraphicalViewer().findObjectAt(gefPoint);

			/* Set the relative location */
			relativeLocation = IDropTargetContext.LOCATION_NONE;
		}
	}

	/**
	 * Attribute for the diagram workbench part.
	 */
	private final IDiagramWorkbenchPart workbenchPart;

	/**
	 * Attribute for the delegating drop target adapter
	 */
	private DelegatingDropTargetAdapter internalDropAdapter;

	/**
	 * Constructor
	 * 
	 * @param workbenchPart
	 *            the diagram workbench part
	 */
	public DiagramViewerDropAdapter(IDiagramWorkbenchPart workbenchPart) {
		Assert.isNotNull(workbenchPart);

		/* Set the part */
		this.workbenchPart = workbenchPart;
	}

	/**
	 * Adds the given TransferDropTargetListener.
	 * @param listener the listener
	 * @deprecated 
	 */
	public void addDropTargetListener(TransferDropTargetListener listener) {
		super.addDropTargetListener(listener);
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.util.DelegatingDropAdapter#getTransfers()
	 */
	public Transfer[] getTransfers() {
		if (getInternalDropAdapter() == null) {
			return super.getTransfers();
		}

		/* Get all the transfers */
		Transfer[] allTransferAgents = getInternalDropAdapter()
			.getAllTransferAgents();
		Transfer[] superTransferTypes = super.getTransfers();

		/* Combine the transfers */
		List myTransfers = null;
		List superTransfers = null;

		if (allTransferAgents != null)
			myTransfers = Arrays.asList(allTransferAgents);

		if (superTransferTypes != null)
			superTransfers = Arrays.asList(superTransferTypes);

		ArrayList allTransfers = new ArrayList();
		if (superTransfers != null)
			allTransfers.addAll(superTransfers);
		if (myTransfers != null)
			allTransfers.addAll(myTransfers);

		Transfer[] arrTransfers = new Transfer[allTransfers.size()];
		arrTransfers = (Transfer[]) allTransfers.toArray(arrTransfers);
		return arrTransfers;
	}

	/**
	 * Return the delegating drop adapter
	 * 
	 * @return DelegatingDropTargetAdapter
	 */
	private DelegatingDropTargetAdapter getInternalDropAdapter() {
		return internalDropAdapter;
	}

	/**
	 * Initializes the delegating drop adapter
	 * 
	 * @param control
	 *            the viewer's control
	 */
	protected final void initialize(Control control) {
		if (this.internalDropAdapter == null) {
			Assert.isNotNull(control);
			/* Create the drop adapter */
			DiagramViewerDropTargetContext context = new DiagramViewerDropTargetContext(
				getWorkbenchPart(), control);
			this.internalDropAdapter = new DelegatingDropTargetAdapter(context);
		}
	}

	/*
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragEnter(DropTargetEvent event) {
		getInternalDropAdapter().dragEnter(event);
	}

	/*
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragLeave(DropTargetEvent event) {
		getInternalDropAdapter().dragLeave(event);
	}

	/*
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOperationChanged(DropTargetEvent event) {
		getInternalDropAdapter().dragOperationChanged(event);
	}

	/*
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOver(DropTargetEvent event) {
		getInternalDropAdapter().dragOver(event);
	}

	/*
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop(DropTargetEvent event) {
		getInternalDropAdapter().drop(event);
	}

	/*
	 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dropAccept(DropTargetEvent event) {
		getInternalDropAdapter().dropAccept(event);
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.dnd.DelegatingDropAdapter#getTransferTypes()
	 */
	public Transfer[] getTransferTypes() {
		return getTransfers();
	}

	/**
	 * Returns the diagram workbenchPart.
	 * 
	 * @return the workbenchPart.
	 */
	private IDiagramWorkbenchPart getWorkbenchPart() {
		return this.workbenchPart;
	}

	/*
	 * @see org.eclipse.jface.util.DelegatingDropAdapter#isEmpty()
	 */
	public boolean isEmpty() {
		Transfer[] transfers = getTransfers();
		if (transfers != null && transfers.length > 0) {
			return false;
		}

		return true;
	}

	/**
	 * Returns the isInitialized.
	 * 
	 * @return the isInitialized.
	 */
	protected final boolean isInitialized() {
		return getInternalDropAdapter() != null;
	}
}