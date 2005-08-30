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

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drag.ITransferDragSourceListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.drop.ITransferDropTargetListener;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.GetTransferAdapterOperation;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core.ITransferAdapterProvider;

/**
 * The abstract parent of all concrete transfer adapter providers. A concrete
 * provider needs to override the <code>getTransferDragSourceAdapter()</code>
 * and <code>getTransferDropTargetAdapter</code> methods. The
 * <code>provides</code> method has a default implementation. The
 * <code>provides()</code> method is already handled by the proxy for the
 * provider (<code>TransferAdapterService.ProviderDescriptor</code>). The
 * proxy contains all the information necessary for the <code>provides()</code>
 * method.
 * 
 * @author Vishy Ramaswamy
 */
public class AbstractTransferAdapterProvider
	extends AbstractProvider
	implements ITransferAdapterProvider {

	/**
	 * Constructor for AbstractTransferAdapterProvider.
	 */
	public AbstractTransferAdapterProvider() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAdapterProvider#getTransferDragSourceAdapter(java.lang.String)
	 */
	public ITransferDragSourceListener getTransferDragSourceAdapter(
			String transferId) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAdapterProvider#getTransferDropTargetAdapter(java.lang.String)
	 */
	public ITransferDropTargetListener getTransferDropTargetAdapter(
			String transferId) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public final boolean provides(IOperation operation) {
		if (operation instanceof GetTransferAdapterOperation) {
			return true;
		}

		return false;
	}
}