/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.EMFRecord;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IEmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IWmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

/**
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
public class BitBlt implements IEmf2SvgConverter, IWmf2SvgConverter
{
	private static final int	EMF_XDEST_OFFSET		= 16;
	private static final int	EMF_YDEST_OFFSET		= 20;
	private static final int	EMF_WDEST_OFFSET		= 24;
	private static final int	EMF_HDEST_OFFSET		= 28;
	private static final int	EMF_XSRC_OFFSET			= 36;
	private static final int	EMF_YSRC_OFFSET			= 40;
	private static final int	EMF_BMI_OFFSET_OFFSET	= 76;
	private static final int	EMF_BMI_SIZE_OFFSET		= 80;
	private static final int	EMF_DATA_OFFSET_OFFSET	= 84;
	private static final int	EMF_DATA_SIZE_OFFSET	= 88;
	
	private static final int	WMF_XDEST_OFFSET		= 14;
	private static final int	WMF_YDEST_OFFSET		= 12;
	private static final int	WMF_WDEST_OFFSET		= 10;
	private static final int	WMF_HDEST_OFFSET		= 8;
	private static final int	WMF_XSRC_OFFSET			= 6;
	private static final int	WMF_YSRC_OFFSET			= 4;

	private static final int	WMF_BMI_OFFSET			= 16;

	protected int				m_xDest			= 0;
	protected int				m_yDest			= 0;
	protected int				m_wDest			= 0;
	protected int				m_hDest			= 0;
	protected int				m_xSrc			= 0;
	protected int				m_ySrc			= 0;
	protected BufferedImage		m_image			= null;
	
	public void readWMFRecord( Record rec ) throws IOException
	{
		// This only works for WMF_DIBBITBLT, not for WMF_BITBLT since BITBLT
		// does not contain palette entries.  
		// Need to consider supporting BITBLT in some way.
		
		// There are two formats, one where the source HDC is null and
		// one where it isn't.  We only support those where the source is non-null
		// since this is the most common to be used in the metafiles we care about.
		if( rec.getSize() > 12 )
		{	
			m_xDest		= rec.getShortAt( WMF_XDEST_OFFSET );
			m_yDest		= rec.getShortAt( WMF_YDEST_OFFSET );
			m_wDest		= rec.getShortAt( WMF_WDEST_OFFSET );
			m_hDest		= rec.getShortAt( WMF_HDEST_OFFSET );
			m_xSrc		= rec.getShortAt( WMF_XSRC_OFFSET );
			m_ySrc		= rec.getShortAt( WMF_YSRC_OFFSET );
			
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
	}
	
	public void readEMFRecord( Record rec ) throws IOException
	{
		m_xDest			= rec.getIntAt( EMF_XDEST_OFFSET );
		m_yDest			= rec.getIntAt( EMF_YDEST_OFFSET );
		m_wDest			= rec.getIntAt( EMF_WDEST_OFFSET );
		m_hDest			= rec.getIntAt( EMF_HDEST_OFFSET );
		m_xSrc			= rec.getIntAt( EMF_XSRC_OFFSET );
		m_ySrc			= rec.getIntAt( EMF_YSRC_OFFSET );
		int bmiOffset	= rec.getIntAt( EMF_BMI_OFFSET_OFFSET ) - EMFRecord.EMR_HEADER_SIZE;
		int bmiSize		= rec.getIntAt( EMF_BMI_SIZE_OFFSET );
		int dataOffset	= rec.getIntAt( EMF_DATA_OFFSET_OFFSET ) - EMFRecord.EMR_HEADER_SIZE;
		int dataSize	= rec.getIntAt( EMF_DATA_SIZE_OFFSET );

		if (bmiSize == 0) {
			return;
		}
		m_image = BitmapHelper.readBitmap( rec, bmiOffset, bmiSize, dataOffset, dataSize );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		if( m_image != null )
		{	
			int w = m_image.getWidth();
			int h = m_image.getHeight();
			
			int xSrc = context.convertXToSVGLogicalUnits( m_xSrc );
			int ySrc = context.convertYToSVGLogicalUnits( m_ySrc );
			int xDest = context.convertXToSVGLogicalUnits( m_xDest );
			int yDest = context.convertYToSVGLogicalUnits( m_yDest );
			int wDest = context.scaleX( m_wDest );
			int hDest = context.scaleY( m_hDest );
			
			if( xSrc >= w || ySrc >= h )
			{
				return;
			}
	
			// Clip the image.
			if( xSrc + wDest > w )
			{
				wDest = w - xSrc;
			}
	
			if( ySrc + hDest > h )
			{
				hDest = h - ySrc;
			}
	
			BufferedImage image = m_image.getSubimage( xSrc, ySrc, wDest, hDest );
			g.drawImage( image, new AffineTransform( 1f, 0f, 0f, 1f, xDest, yDest ), null );
		}
	}
}
