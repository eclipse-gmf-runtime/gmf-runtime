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

import org.eclipse.swt.dnd.TransferData;

/**
 * Concrete implementation of <code>IDropTargetEvent</code>
 * 
 * @author Vishy Ramaswamy
 */
public final class DropTargetEvent
	implements IDropTargetEvent {

	/**
	 * Attribute for the drop target event.
	 */
	private org.eclipse.swt.dnd.DropTargetEvent event = null;

	/**
	 * Constructor for DropTargetEvent.
	 */
	protected DropTargetEvent() {
		/* method not implemented */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetEvent#getCurrentDataType()
	 */
	public TransferData getCurrentDataType() {
		return event.currentDataType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetEvent#getCurrentOperation()
	 */
	public int getCurrentOperation() {
		return event.detail;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetEvent#getDataTypes()
	 */
	public TransferData[] getDataTypes() {
		return event.dataTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetEvent#getOperations()
	 */
	public int getOperations() {
		return event.operations;
	}

	/**
	 * Set the event
	 * 
	 * @param anEvent
	 *            the event
	 */
	protected void setEvent(org.eclipse.swt.dnd.DropTargetEvent anEvent) {
		assert null != anEvent : "anEvent cannot be null"; //$NON-NLS-1$
		this.event = anEvent;
	}
}