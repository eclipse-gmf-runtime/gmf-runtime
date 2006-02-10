/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

/**  
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.GdiPen;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IEmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IWmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;


/**  
 * @author dhabib
 */

public class CreatePen implements IEmf2SvgConverter, IWmf2SvgConverter
{
	private static final int	EMF_ID_OFFSET			= 0;
	private static final int	EMF_STYLE_OFFSET		= 4;
	private static final int	EMF_WIDTH_OFFSET		= 8;
	private static final int	EMF_COLOR_OFFSET		= 16;

	private static final int	WMF_STYLE_OFFSET		= 0;
	private static final int	WMF_WIDTH_OFFSET		= 2;
	private static final int	WMF_COLOR_OFFSET		= 6;

	private int		m_id 		= -1;
	private int		m_style		= 0;
	private int		m_width		= 1;
	private Color	m_color		= new Color( 0 );
	
	public void readWMFRecord( Record rec ) throws IOException
	{
		m_style	= rec.getShortAt( WMF_STYLE_OFFSET );
		m_width	= rec.getShortAt( WMF_WIDTH_OFFSET );
		m_color	= rec.getColorAt( WMF_COLOR_OFFSET );
	}

	public void readEMFRecord( Record rec ) throws IOException
	{
		m_id 	= (int) rec.getDWORDAt( EMF_ID_OFFSET );
		m_style	= rec.getIntAt( EMF_STYLE_OFFSET );
		m_width	= rec.getIntAt( EMF_WIDTH_OFFSET );
		m_color	= rec.getColorAt( EMF_COLOR_OFFSET );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		if( m_id == -1 )
		{	
			m_id 	= context.getNextObjectId();
		}

		context.addGDI( m_id, new GdiPen( m_style, m_width, m_color ) );
	}
}

