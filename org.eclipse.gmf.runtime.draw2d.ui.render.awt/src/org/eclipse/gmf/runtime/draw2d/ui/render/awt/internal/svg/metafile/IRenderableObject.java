/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.awt.Graphics2D;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;

/**
 * This class must be implemented by all metafile record handlers.  Generally, classes
 * will inherit from IEmf2SvgConverter or IWmf2SvgConverter, which extends this
 * interface.
 *   
 * @author dhabib
 */
public interface IRenderableObject 
{
	/**
	 * Draws the data from a metafile record into a Graphics2D object.  When converting to SVG, 
	 * g will actually be a SVGGraphics2D object which translates the drawing into SVG XML.
	 * @param g Graphics2D object to draw into.
	 * @param context Device context containing the current device settings.
	 * @throws TranscoderException if there was an error rendering the data.
	 */
	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException;

}
