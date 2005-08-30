/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.printing.render.internal.printpreview;

import org.eclipse.gmf.runtime.diagram.ui.printing.internal.printpreview.PrintPreviewHelper;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedMapModeGraphics;

/**
 * A specialized <code>PrintPreviewHelper</code> that supports printing of
 * images.
 * 
 * @author cmahoney
 */
public class RenderedPrintPreviewHelper
	extends PrintPreviewHelper {

	/**
	 * Creates a new instance.
	 */
	public RenderedPrintPreviewHelper() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.printing.internal.printpreview.PrintPreviewHelper#createMapModeGraphics(org.eclipse.gmf.runtime.draw2d.ui.graphics.ScaledGraphics)
	 */
	protected MapModeGraphics createMapModeGraphics(
			ScaledGraphics scaledGraphics) {
		return new RenderedMapModeGraphics(scaledGraphics);
	}

}