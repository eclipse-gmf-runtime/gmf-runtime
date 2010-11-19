/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
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
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.GdiBrush;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.GdiPen;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IEmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IRenderToPath;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IWmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

/**
 * @author dhabib
 */
public class Rectangle implements IEmf2SvgConverter, IWmf2SvgConverter, IRenderToPath
{
	private static final int	WMF_BOTTOM_OFFSET	= 0;
	private static final int	WMF_RIGHT_OFFSET	= 2;
	private static final int	WMF_TOP_OFFSET		= 4;
	private static final int	WMF_LEFT_OFFSET		= 6;
	
	private java.awt.Rectangle	m_rect; 

	public void readWMFRecord( Record rec ) throws IOException
	{
		int bottom 	= rec.getShortAt( WMF_BOTTOM_OFFSET );
		int right 	= rec.getShortAt( WMF_RIGHT_OFFSET );
		int top 	= rec.getShortAt( WMF_TOP_OFFSET );
		int left 	= rec.getShortAt( WMF_LEFT_OFFSET );
		
		m_rect = new java.awt.Rectangle( left, top, right - left, bottom - top );
	}

	public void readEMFRecord( Record rec ) throws IOException
	{
		m_rect = rec.getRectangleLAt( 0 );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		Shape s = getShape( context );
		
		GdiBrush brush = context.getCurBrush();
		
		if( brush != null )
		{
			brush.fill( s, g, context );
		}
		
		GdiPen pen = context.getCurPen();
		
		if( pen != null )
		{
			pen.apply( g, context );
			g.draw( s );
		}
	}
	
	public void render( DeviceContext context ) throws TranscoderException
	{
		Shape s = getShape( context );
		context.getGdiPath().appendFigure( s );
	}
	
	private Shape getShape( DeviceContext context )
	{
		int x = context.convertXToSVGLogicalUnits( m_rect.x );
		int y = context.convertYToSVGLogicalUnits( m_rect.y );
		int w = context.scaleX( m_rect.width );
		int h = context.scaleY( m_rect.height );
		
		return new Rectangle2D.Float( x, y, w, h );
	}
}
