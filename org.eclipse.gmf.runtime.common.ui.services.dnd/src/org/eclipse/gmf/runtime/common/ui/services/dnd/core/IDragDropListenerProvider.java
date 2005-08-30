/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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