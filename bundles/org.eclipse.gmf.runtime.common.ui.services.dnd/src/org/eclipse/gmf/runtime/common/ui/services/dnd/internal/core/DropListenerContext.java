/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core;

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDropListenerContext;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.IListenerContext;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This class implements <code>IDropListenerContext</code> interface.
 * 
 * @author Vishy Ramaswamy
 */
public final class DropListenerContext
	implements IDropListenerContext {

	/**
	 * Attribute for the <code>IWorkbenchPart</code>
	 */
	final private IWorkbenchPart activePart;

	/**
	 * Attribute for the element type
	 */
	final private Class targetElementType;

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
	 *            active workbench part
	 * @param aTargetElementType
	 *            Class of the element to drop on
	 * @param aTransferId
	 *            String with the id of the context
	 * @param anIsCompatible
	 *            true if exact element type match required, false if compatible
	 *            matches are allowed
	 */
	public DropListenerContext(IWorkbenchPart anActivePart,
			Class aTargetElementType, String aTransferId, boolean anIsCompatible) {
		super();

		assert null != anActivePart : "anActivePart cannot be null"; //$NON-NLS-1$
		assert null != aTargetElementType  : "aTargetElementType cannot be null"; //$NON-NLS-1$
		assert null != aTransferId : "aTransferId cannot be null"; //$NON-NLS-1$

		this.activePart = anActivePart;
		this.targetElementType = aTargetElementType;
		this.transferId = aTransferId;
		this.isCompatible = anIsCompatible;
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
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDropListenerContext#isCompatible()
	 */
	public boolean isCompatible() {
		return isCompatible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDropListenerContext#getActivePart()
	 */
	public IWorkbenchPart getActivePart() {
		return activePart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDropListenerContext#getTargetElementType()
	 */
	public Class getTargetElementType() {
		return targetElementType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.IListenerContext#getOperationType()
	 */
	public String getOperationType() {
		return IListenerContext.DROP;
	}
}