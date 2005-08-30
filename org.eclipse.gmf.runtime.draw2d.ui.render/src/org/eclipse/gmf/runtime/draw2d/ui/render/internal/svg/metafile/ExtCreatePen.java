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

/**  
 * @author dhabib
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;


/**  
 * @author dhabib
 */

public class ExtCreatePen implements IEmf2SvgConverter, ITraceMe
{
	private static final int	ID_OFFSET			= 0;
	private static final int	STYLE_OFFSET		= 20;
	private static final int	WIDTH_OFFSET		= 24;
	private static final int	COLOR_OFFSET		= 32;

	private int		m_id 		= 0;
	private int		m_style		= 0;
	private int		m_width		= 1;
	private Color	m_color		= new Color( 0 );
	
	private GdiPen m_pen		= null;

	public void readEMFRecord( Record rec ) throws IOException
	{
		m_id 	= (int) rec.getDWORDAt( ID_OFFSET );
		m_style	= rec.getIntAt( STYLE_OFFSET );
		m_width	= rec.getIntAt( WIDTH_OFFSET );
		m_color	= rec.getColorAt( COLOR_OFFSET );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		m_pen = new GdiPen( m_style, m_width, m_color );
		context.addGDI( m_id, m_pen );
	}

	public String toString() {
		return m_pen.toString() + ", id=" + m_id; //$NON-NLS-1$
	}
}


