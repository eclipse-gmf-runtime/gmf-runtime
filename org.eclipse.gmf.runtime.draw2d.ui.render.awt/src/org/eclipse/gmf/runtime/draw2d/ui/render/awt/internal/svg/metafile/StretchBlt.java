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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.BitBlt;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

/**
 * @author dhabib
 */
public class StretchBlt extends BitBlt
{
	private static final int	EMF_WSRC_OFFSET		= 92;
	private static final int	EMF_HSRC_OFFSET		= 96;
	
	private static final int	WMF_XDEST_OFFSET		= 20;
	private static final int	WMF_YDEST_OFFSET		= 18;
	private static final int	WMF_WDEST_OFFSET		= 16;
	private static final int	WMF_HDEST_OFFSET		= 14;
	private static final int	WMF_XSRC_OFFSET			= 12;
	private static final int	WMF_YSRC_OFFSET			= 10;
	private static final int	WMF_WSRC_OFFSET			= 8;
	private static final int	WMF_HSRC_OFFSET			= 6;
	private static final int	WMF_USAGE_OFFSET		= 4;

	private static final int	WMF_BMI_OFFSET			= 22;

	protected int		m_wSrc	= 0;
	protected int		m_hSrc	= 0;
	
	public void readWMFRecord( Record rec ) throws IOException
	{
		// Do not call base class implementation since the WMF offsets
		// are different for StretchDIB vs DibBitBlt

		// This only works for WMF_STRETCHDIB, not for WMF_STRETCHBLT since STRETCHBLT
		// does not contain palette entries.  
		// Need to consider supporting STRETCHBLT in some way.
		m_xDest		= rec.getShortAt( WMF_XDEST_OFFSET );
		m_yDest		= rec.getShortAt( WMF_YDEST_OFFSET );
		m_wDest		= rec.getShortAt( WMF_WDEST_OFFSET );
		m_hDest		= rec.getShortAt( WMF_HDEST_OFFSET );
		m_xSrc		= rec.getShortAt( WMF_XSRC_OFFSET );
		m_ySrc		= rec.getShortAt( WMF_YSRC_OFFSET );
		m_wSrc		= rec.getShortAt( WMF_WSRC_OFFSET );
		m_hSrc		= rec.getShortAt( WMF_HSRC_OFFSET );
		int usage	= rec.getShortAt( WMF_USAGE_OFFSET );
		
		int bmiSize = BitmapHelper.getHeaderSize( 	rec, 
													WMF_BMI_OFFSET, 
													usage );
		int dataOffset = WMF_BMI_OFFSET + bmiSize;
		int dataSize = rec.getSize() - dataOffset;
		
		m_image = BitmapHelper.readBitmap( 	rec, 
											WMF_BMI_OFFSET, 
											bmiSize, 
											dataOffset, 
											dataSize );
		
	}
	
	public void readEMFRecord( Record rec ) throws IOException
	{
		super.readEMFRecord( rec );
		
		// Read in the cx and cy values.
		m_wSrc			= rec.getIntAt( EMF_WSRC_OFFSET );
		m_hSrc			= rec.getIntAt( EMF_HSRC_OFFSET );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		int w = m_image.getWidth();
		int h = m_image.getHeight();
		
		int xDest = context.convertXToSVGLogicalUnits( m_xDest );
		int yDest = context.convertYToSVGLogicalUnits( m_yDest );
		int wDest = context.scaleX( m_wDest );
		int hDest = context.scaleY( m_hDest );
		
		if( m_xSrc >= w || m_ySrc >= h )
		{
			return;
		}
		
		// Clip the image.
		if( m_xSrc + m_wSrc > w )
		{
			m_wSrc = w - m_xSrc;
		}

		if( m_ySrc + m_hSrc > h )
		{
			m_hSrc = h - m_ySrc;
		}

		BufferedImage image = m_image.getSubimage( m_xSrc, m_ySrc, m_wSrc, m_hSrc );
		
		double scaleFactorX = (double) wDest / (double) m_wSrc;
		double scaleFactorY = (double) hDest / (double) m_hSrc;

		AffineTransform xform = new AffineTransform();
		xform.translate( xDest, yDest );
		xform.scale( scaleFactorX, scaleFactorY );
		
		g.drawImage( image, xform, null );
	}
}
