/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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