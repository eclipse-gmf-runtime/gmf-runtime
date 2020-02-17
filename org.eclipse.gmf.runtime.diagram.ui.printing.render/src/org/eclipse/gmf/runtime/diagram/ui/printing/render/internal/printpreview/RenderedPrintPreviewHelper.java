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