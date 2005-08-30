/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile;

import java.awt.Graphics2D;

import org.apache.batik.transcoder.TranscoderException;

/**
 * This class must be implemented by all metafile record handlers.  Generally, classes
 * will inherit from IEmf2SvgConverter or IWmf2SvgConverter, which extends this
 * interface.
 *   
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
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
