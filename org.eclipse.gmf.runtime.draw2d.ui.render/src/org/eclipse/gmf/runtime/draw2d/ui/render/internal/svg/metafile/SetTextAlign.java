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

import java.awt.Graphics2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;

/**  
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
public class SetTextAlign implements IEmf2SvgConverter, IWmf2SvgConverter
{
	private int		m_textAlignment	= DeviceContext.TA_TOP | DeviceContext.TA_LEFT;
	
	public void readWMFRecord( Record emr ) throws IOException
	{
		m_textAlignment = emr.getShortAt( 0 );
	}

	public void readEMFRecord( Record emr ) throws IOException
	{
		m_textAlignment = emr.getIntAt( 0 );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		context.setTextAlignment( m_textAlignment );
	}

}
