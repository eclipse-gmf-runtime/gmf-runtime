/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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