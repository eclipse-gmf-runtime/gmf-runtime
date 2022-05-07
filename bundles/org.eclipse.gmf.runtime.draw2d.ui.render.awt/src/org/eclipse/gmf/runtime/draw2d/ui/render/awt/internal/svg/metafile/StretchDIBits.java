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

/**
 * Very similar to StretchBlt, but the offsets are completely different.
 * @author dhabib
 */
public class StretchDIBits implements IWmf2SvgConverter, IEmf2SvgConverter
{
	private static final int	EMF_XDEST_OFFSET		= 16;
	private static final int	EMF_YDEST_OFFSET		= 20;
	private static final int	EMF_XSRC_OFFSET			= 24;
	private static final int	EMF_YSRC_OFFSET			= 28;
	private static final int	EMF_WSRC_OFFSET			= 32;
	private static final int	EMF_HSRC_OFFSET			= 36;
	private static final int	EMF_BMI_OFFSET_OFFSET	= 40;
	private static final int	EMF_BMI_SIZE_OFFSET		= 44;
	private static final int	EMF_DATA_OFFSET_OFFSET	= 48;
	private static final int	EMF_DATA_SIZE_OFFSET	= 52;
//	private static final int	EMF_USAGE_OFFSET		= 56;
//	private static final int	EMF_ROP_OFFSET			= 60;
	private static final int	EMF_WDEST_OFFSET		= 64;
	private static final int	EMF_HDEST_OFFSET		= 68;
	

	private static final int	WMF_XDEST_OFFSET		= 18;
	private static final int	WMF_YDEST_OFFSET		= 16;
	private static final int	WMF_WDEST_OFFSET		= 14;
	private static final int	WMF_HDEST_OFFSET		= 12;
	private static final int	WMF_XSRC_OFFSET			= 10;
	private static final int	WMF_YSRC_OFFSET			= 8;
	private static final int	WMF_WSRC_OFFSET			= 6;
	private static final int	WMF_HSRC_OFFSET			= 4;

	private static final int	WMF_BMI_OFFSET			= 20;
	
	protected int				m_xDest			= 0;	// Dest coords are in logical units
	protected int				m_yDest			= 0;
	protected int				m_wDest			= 0;
	protected int				m_hDest			= 0;
	protected int				m_xSrc			= 0;	// Src coords are in pixels
	protected int				m_ySrc			= 0;
	protected int				m_wSrc			= 0;
	protected int				m_hSrc			= 0;
	protected BufferedImage		m_image			= null;

	public void readWMFRecord( Record rec ) throws IOException
	{
		m_xDest		= rec.getShortAt( WMF_XDEST_OFFSET );
		m_yDest		= rec.getShortAt( WMF_YDEST_OFFSET );
		m_wDest		= rec.getShortAt( WMF_WDEST_OFFSET );
		m_hDest		= rec.getShortAt( WMF_HDEST_OFFSET );
		m_xSrc		= rec.getShortAt( WMF_XSRC_OFFSET );
		m_ySrc		= rec.getShortAt( WMF_YSRC_OFFSET );
		m_wSrc		= rec.getShortAt( WMF_WSRC_OFFSET );
		m_hSrc		= rec.getShortAt( WMF_HSRC_OFFSET );
		
		int bmiSize = BitmapHelper.getHeaderSize( 	rec, 
													WMF_BMI_OFFSET, 
													DeviceContext.DIB_RGB_COLORS );
		
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
		m_xDest			= rec.getIntAt( EMF_XDEST_OFFSET );
		m_yDest			= rec.getIntAt( EMF_YDEST_OFFSET );
		m_wDest			= rec.getIntAt( EMF_WDEST_OFFSET );
		m_hDest			= rec.getIntAt( EMF_HDEST_OFFSET );
		m_xSrc			= rec.getIntAt( EMF_XSRC_OFFSET );
		m_ySrc			= rec.getIntAt( EMF_YSRC_OFFSET );
		m_wSrc			= rec.getIntAt( EMF_WSRC_OFFSET );
		m_hSrc			= rec.getIntAt( EMF_HSRC_OFFSET );
		int bmiOffset	= rec.getIntAt( EMF_BMI_OFFSET_OFFSET ) - EMFRecord.EMR_HEADER_SIZE;
		int bmiSize		= rec.getIntAt( EMF_BMI_SIZE_OFFSET );
		int dataOffset	= rec.getIntAt( EMF_DATA_OFFSET_OFFSET ) - EMFRecord.EMR_HEADER_SIZE;
		int dataSize	= rec.getIntAt( EMF_DATA_SIZE_OFFSET );

		m_image = BitmapHelper.readBitmap( rec, bmiOffset, bmiSize, dataOffset, dataSize );
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
