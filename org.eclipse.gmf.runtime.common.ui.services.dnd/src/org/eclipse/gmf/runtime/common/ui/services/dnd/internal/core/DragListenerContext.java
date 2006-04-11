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

package org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core;

import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDragListenerContext;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.IListenerContext;

/**
 * This class implements <code>IDragListenerContext</code> interface.
 * 
 * @author Vishy Ramaswamy
 */
public final class DragListenerContext
	implements IDragListenerContext {

	/**
	 * Attribute for the <code>IWorkbenchPart</code>
	 */
	final private IWorkbenchPart activePart;

	/**
	 * Attribute for the element type
	 */
	final private Class selectedElementType;

	/**
	 * Attribute for the transfer id
	 */
	final private String transferId;

	/**
	 * Attribute for compatability flag
	 */
	final private boolean isCompatible;

	/**
	 * Create a DragListenerContext.
	 * 
	 * @param anActivePart
	 *            active IWorkbenchPart
	 * @param aSelectedElementType
	 *            Class of element to drag
	 * @param aTransferId
	 *            String with the id of the context
	 * @param anIsCompatible
	 *            true if exact element type match required, false if compatible
	 *            matches are allowed
	 */
	public DragListenerContext(IWorkbenchPart anActivePart,
			Class aSelectedElementType, String aTransferId,
			boolean anIsCompatible) {
		super();

		assert null != anActivePart : "anActivePart cannot be null"; //$NON-NLS-1$
		assert null != aSelectedElementType : "aSelectedElementType cannot be null"; //$NON-NLS-1$
		assert null != aTransferId : "aTransferId cannot be null"; //$NON-NLS-1$

		this.activePart = anActivePart;
		this.selectedElementType = aSelectedElementType;
		this.transferId = aTransferId;
		this.isCompatible = anIsCompatible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDragListenerContext#getSelectedElementType()
	 */
	public Class getSelectedElementType() {
		return selectedElementType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IListenerContext#getTransferId()
	 */
	public String getTransferId() {
		return transferId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDragListenerContext#isCompatible()
	 */
	public boolean isCompatible() {
		return isCompatible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDragListenerContext#getActivePart()
	 */
	public IWorkbenchPart getActivePart() {
		return activePart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IListenerContext#getOperationType()
	 */
	public String getOperationType() {
		return IListenerContext.DRAG;
	}
}