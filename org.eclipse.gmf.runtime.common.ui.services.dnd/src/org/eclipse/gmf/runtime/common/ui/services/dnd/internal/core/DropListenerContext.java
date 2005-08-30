/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core;

import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDropListenerContext;

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