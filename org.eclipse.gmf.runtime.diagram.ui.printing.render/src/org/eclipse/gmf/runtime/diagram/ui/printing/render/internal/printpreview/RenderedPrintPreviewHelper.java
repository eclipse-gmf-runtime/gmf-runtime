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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.printing.internal.printpreview.PrintPreviewHelper#createMapModeGraphics(org.eclipse.gmf.runtime.draw2d.ui.graphics.ScaledGraphics)
	 */
	protected MapModeGraphics createMapModeGraphics(
			ScaledGraphics scaledGraphics) {
		return new RenderedMapModeGraphics(scaledGraphics, getMapMode());
	}

}