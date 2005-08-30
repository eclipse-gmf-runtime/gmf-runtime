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
 */
public class SetMiterLimit implements IEmf2SvgConverter, ITraceMe
{
	private float m_miterLimit	= 10.0f;
	
	public void readEMFRecord( Record rec ) throws IOException
	{
		// In all Microsoft documentation for SetMiterLimit this is defined as a
		// "float". However, in the actual file it is persisted as an integer!
		// So read an integer but then treat it as a float...
		m_miterLimit = rec.getIntAt( 0 );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		context.setMiterLimit( m_miterLimit );
	}

	public String toString() {
		return Float.toString(m_miterLimit);
	}
}
