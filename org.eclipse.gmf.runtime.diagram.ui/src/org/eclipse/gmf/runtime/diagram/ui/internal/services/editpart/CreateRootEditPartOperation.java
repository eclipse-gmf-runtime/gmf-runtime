/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * Concrete operation to create a <code>RootEditPart</code> (or
 * subclass) element.
 * 
 * @author cmahoney
 * @canBeSeenBy %level1
 */
public class CreateRootEditPartOperation extends EditPartOperation {

	/**
	 * Creates the editpart.
	 * 
	 * @param provider
	 *            the provider capable of honoring this operation.
	 * @return the created editpart instance.
	 */
	public Object execute(IProvider provider) {
		return ((IEditPartProvider) provider).createRootEditPart();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.EditPartOperation#determineCachingKey()
	 */
	protected String determineCachingKey() {
		return "RootEditPart"; //$NON-NLS-1$
	}
}