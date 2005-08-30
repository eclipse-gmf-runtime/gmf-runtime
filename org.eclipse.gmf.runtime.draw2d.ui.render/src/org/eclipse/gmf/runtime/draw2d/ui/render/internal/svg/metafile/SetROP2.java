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
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;

/**
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
public class SetROP2 implements IEmf2SvgConverter, IWmf2SvgConverter
{
	private int 	m_mode = 0;

	public void readWMFRecord( Record rec ) throws IOException
	{
		m_mode = rec.getShortAt( 0 );
	}

	public void readEMFRecord( Record rec ) throws IOException
	{
		m_mode = (int) rec.getDWORDAt( 0 );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		context.setROP2( m_mode );
	}
}
