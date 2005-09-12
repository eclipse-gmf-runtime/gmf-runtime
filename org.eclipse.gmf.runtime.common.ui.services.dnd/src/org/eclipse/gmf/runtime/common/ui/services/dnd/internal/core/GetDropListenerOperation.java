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