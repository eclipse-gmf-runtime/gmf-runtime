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
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDragDropListenerProvider;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.IDropListenerContext;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetListener;

/**
 * An operation that gets the <code>IDropTargetListener</code> associated with
 * the <code>IDropListenerContext</code> from the
 * <code>IDragDropListenerProvider</code>
 * 
 * @author Vishy Ramaswamy
 */
public final class GetDropListenerOperation
	extends ListenerOperation {

	/**
	 * Constructor for GetDropListenerOperation.
	 * 
	 * @param context
	 *            The drop listener context
	 */
	public GetDropListenerOperation(IDropListenerContext context) {
		super(context);
	}

	/**
	 * Executes this operation on the <code>IDragDropListenerProvider</code>
	 * provider.
	 * 
	 * @param provider
	 *            The provider on which to execute this operation.
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		if (provider instanceof IDragDropListenerProvider) {
			/* Get the provider */
			IDragDropListenerProvider prov = (IDragDropListenerProvider) provider;

			/* Get the drop target listeners */
			IDropTargetListener[] listeners = prov
				.getDropTargetListeners((IDropListenerContext) getContext());

			/* Return the handler */
			return listeners;
		}

		return null;
	}
}