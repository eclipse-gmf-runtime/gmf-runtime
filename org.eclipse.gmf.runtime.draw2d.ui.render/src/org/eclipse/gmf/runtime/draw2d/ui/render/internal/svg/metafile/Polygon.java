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
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import org.apache.batik.transcoder.TranscoderException;

/**  
 * @author dhabib
 */
public class Polygon extends AbstractPoly implements IRenderToPath 
{
	public Polygon( boolean b16Bits )
	{
		super( b16Bits );
	}
	
	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		GdiBrush brush = context.getCurBrush();
		Shape s = getShape( context );
		
		if( brush != null )
		{	
			brush.fill( s, g, context );
		}

		GdiPen curPen = context.getCurPen();
		
		if( curPen != null )
		{
			curPen.apply( g, context );
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
		GeneralPath p = new GeneralPath();

		int count = getCount();
		
		if( count > 0 )
		{
			m_xPointsConv = context.convertXArrayToSVGLogicalUnits( getXPoints() );
			m_yPointsConv = context.convertYArrayToSVGLogicalUnits( getYPoints() );
			
			p.moveTo( m_xPointsConv[ 0 ], m_yPointsConv[ 0 ] );
			
			for( int index = 1; index < count; index++ )
			{
				p.lineTo( m_xPointsConv[ index ], m_yPointsConv[ index ] );
			}
			
			p.closePath();
		}
		
		return p;
	}
}
