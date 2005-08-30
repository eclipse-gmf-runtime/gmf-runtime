/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.actions.internal;

import org.eclipse.ui.PlatformUI;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.emf.ui.services.action.AbstractModelActionFilterProvider;

/**
 * An action filter provider for the Diagram UI Actions plugin.
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.actions.*
 */
public class DiagramActionFilterProvider
	extends AbstractModelActionFilterProvider {

	/**
	 * This string from XML is used to identify this provider
	 */
	private static final String CAN_DUPLICATE = "canDuplicate"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.action.AbstractModelActionFilterProvider#doTestAttribute(java.lang.Object,
	 *      java.lang.String, java.lang.String)
	 */
	protected boolean doTestAttribute(Object target, String name, String value) {
		if (CAN_DUPLICATE.equals(name)) {
			return DuplicateActionDelegate.canDuplicate(getStructuredSelection(), PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getPartService().getActivePart());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.ui.services.action.AbstractModelActionFilterProvider#doProvides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	protected boolean doProvides(IOperation operation) {
		return true;
	}

}