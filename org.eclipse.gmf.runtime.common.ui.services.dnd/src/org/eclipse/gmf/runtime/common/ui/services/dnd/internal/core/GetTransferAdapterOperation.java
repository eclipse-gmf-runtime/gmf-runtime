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

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * An operation that gets the transfer adapter listener
 * 
 * @author Vishy Ramaswamy
 */
public final class GetTransferAdapterOperation
	extends ListenerOperation {

	/**
	 * Constructor for GetTransferAdapterOperation.
	 * 
	 * @param context
	 *            The listener context
	 */
	public GetTransferAdapterOperation(IListenerContext context) {
		super(context);
	}

	/**
	 * Executes this operation on the <code>ITransferAdapterProvider</code>
	 * provider.
	 * 
	 * @param provider
	 *            The provider on which to execute this operation.
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		if (provider instanceof ITransferAdapterProvider
			&& getContext().getOperationType() != null) {
			/* Get the adapter */
			if (getContext().getOperationType().equals(IListenerContext.DRAG)) {
				return ((ITransferAdapterProvider) provider)
					.getTransferDragSourceAdapter(getContext().getTransferId());
			} else if (getContext().getOperationType().equals(
				IListenerContext.DROP)) {
				return ((ITransferAdapterProvider) provider)
					.getTransferDropTargetAdapter(getContext().getTransferId());
			}
		}

		return null;
	}
}