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
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;

/**
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 *
 */
public class Ellipse implements IEmf2SvgConverter, IWmf2SvgConverter, IRenderToPath
{
	private static final int	WMF_BOTTOM_OFFSET	= 0;
	private static final int	WMF_RIGHT_OFFSET	= 2;
	private static final int	WMF_TOP_OFFSET		= 4;
	private static final int	WMF_LEFT_OFFSET		= 6;

	private Rectangle	m_rect; 

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
		Shape shape 	= getShape( context );
		GdiBrush brush 	= context.getCurBrush();
		
		if( brush != null )
		{
			brush.fill( shape, g, context );
		}
		
		GdiPen pen = context.getCurPen();
		
		if( pen != null )
		{
			pen.apply( g, context );
			g.draw( shape );
		}
	}

	public void render( DeviceContext context ) throws TranscoderException
	{
		Shape shape 	= getShape( context );
		context.getGdiPath().appendFigure( shape );
	}
	
	private Shape getShape( DeviceContext context )
	{
		int x = context.convertXToSVGLogicalUnits( m_rect.x );
		int y = context.convertYToSVGLogicalUnits( m_rect.y );
		int w = context.scaleX( m_rect.width );
		int h = context.scaleY( m_rect.height );
		
		Ellipse2D.Double shape = new Ellipse2D.Double( x, y, w, h );
		
		return shape;
	}
}
