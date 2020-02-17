/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.core;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.IDragSourceListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.IDropTargetListener;

/**
 * Interface that needs to be implemented by the client who provides
 * <code>IDragSourceListener</code> and <code>IDropTargetListener</code>
 * 
 * @author Vishy Ramaswamy
 */
public interface IDragDropListenerProvider
	extends IProvider {

	/**
	 * Returns an array of <code>IDragSourceListener</code> for the given
	 * <code>IDragDropListenerContext</code>
	 * 
	 * @param context
	 *            The context for the provider
	 * @return Returns an array of <code>IDragSourceListener</code>
	 */
	public IDragSourceListener[] getDragSourceListeners(
			IDragListenerContext context);

	/**
	 * Returns an array of <code>IDropTargetListener</code> for the given
	 * <code>IDragDropListenerContext</code>
	 * 
	 * @param context
	 *            The context for the provider
	 * @return Returns an array of <code>IDropTargetListener</code>
	 */
	public IDropTargetListener[] getDropTargetListeners(
			IDropListenerContext context);
}