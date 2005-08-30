/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;


/**  
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
public class SetTextColor implements IEmf2SvgConverter, IWmf2SvgConverter
{
	private Color m_color = new Color( 0x00ffffff );	// Default to white.
	
	public void readWMFRecord( Record rec ) throws IOException
	{
		m_color = rec.getColorAt( 0 );
	}

	public void readEMFRecord( Record rec ) throws IOException
	{
		m_color = rec.getColorAt( 0 );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		context.setTextColor( m_color );
	}
}
