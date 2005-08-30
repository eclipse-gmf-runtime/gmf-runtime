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
 *
 */
public class SetPolyFillMode implements IEmf2SvgConverter, IWmf2SvgConverter, ITraceMe
{
	private int 	m_mode = 0;

	public void readWMFRecord( Record rec ) throws IOException
	{
		m_mode = rec.getShortAt( 0 );
	}

	public void readEMFRecord( Record rec ) throws IOException
	{
		m_mode = rec.getIntAt( 0 );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		context.setPolyFillMode( m_mode );
	}

	public String toString() {
		switch (m_mode) {
			case 1:
				return "Alternate mode"; //$NON-NLS-1$
			case 2:
				return "Winding mode"; //$NON-NLS-1$
			default:
				return "bad mode..."; //$NON-NLS-1$
		}
	}
}
