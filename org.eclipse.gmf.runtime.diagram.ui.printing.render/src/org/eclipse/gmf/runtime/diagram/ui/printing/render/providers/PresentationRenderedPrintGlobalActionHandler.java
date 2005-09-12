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

import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext;
import org.eclipse.gmf.runtime.diagram.ui.printing.actions.DefaultPrintActionHelper;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.providers.PresentationWithPrintGlobalActionHandler;
import org.eclipse.gmf.runtime.diagram.ui.printing.render.util.RenderedDiagramPrinter;

/**
 * A specialized <code>PresentationWithPrintGlobalActionHandler</code> that
 * supports printing of images.
 * 
 * @author cmahoney
 */
public class PresentationRenderedPrintGlobalActionHandler
	extends PresentationWithPrintGlobalActionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.printing.internal.providers.PresentationWithPrintGlobalActionHandler#doPrint(org.eclipse.gmf.runtime.common.ui.services.action.global.IGlobalActionContext)
	 */
	protected void doPrint(IGlobalActionContext cntxt) {
		DefaultPrintActionHelper.doRun((IEditorPart) cntxt.getActivePart(),
			new RenderedDiagramPrinter(getPreferencesHint((IEditorPart) cntxt
				.getActivePart())));
	}
}