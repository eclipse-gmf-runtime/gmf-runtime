/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.printing.render.util;

import org.eclipse.draw2d.Graphics;

import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.printing.internal.util.DiagramPrinter;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.MapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedMapModeGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedPrinterGraphics;

/**
 * A specialized <code>DiagramPrinter</code> that supports rendering of
 * images.
 * 
 * @author cmahoney
 */
public class RenderedDiagramPrinter
	extends DiagramPrinter {

	/**
	 * Creates a new instance.
	 * @param preferencesHint
	 */
	public RenderedDiagramPrinter(PreferencesHint preferencesHint) {
		super(preferencesHint);
	}

	/**
	 * Creates a mapmode graphics that supports rendering of images.
	 */
	public MapModeGraphics createMapModeGraphics(Graphics theGraphics) {
		return new RenderedMapModeGraphics(new RenderedPrinterGraphics(
			theGraphics, printer, true));
	}
}