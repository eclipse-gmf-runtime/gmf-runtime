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

import org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDragListenerContext;

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