/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.printing.render.providers;

import org.eclipse.gmf.runtime.common.ui.action.actions.IPrintActionHelper;
import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.actions.EnhancedPrintActionHelper;

/**
 * A specialized <code>DiagramWithPrintGlobalActionHandler</code> that
 * supports printing of images.
 * 
 * @author cmahoney
 */
public class DiagramWithPrintGlobalActionHandler
	extends org.eclipse.gmf.runtime.diagram.ui.printing.providers.DiagramWithPrintGlobalActionHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.printing.providers.DiagramWithPrintGlobalActionHandler#doPrint(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	protected void doPrint(IGlobalActionContext cntxt) {
		IPrintActionHelper helper = new EnhancedPrintActionHelper();
		helper.doPrint(cntxt.getActivePart());
	}
}