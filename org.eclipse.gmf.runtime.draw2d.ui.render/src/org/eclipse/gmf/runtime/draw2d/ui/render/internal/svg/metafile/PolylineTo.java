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
public class PolylineTo extends AbstractPoly implements IRenderToPath 
{
	public PolylineTo( boolean b16Bits )
	{
		super( b16Bits );
	}
	
	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		if( getCount() > 0 )
		{
			GdiPen curPen = context.getCurPen();
			
			if( curPen != null )
			{
				curPen.apply( g, context );
				g.drawLine( context.convertXToSVGLogicalUnits( context.getCurPosX() ), 
							context.convertYToSVGLogicalUnits( context.getCurPosY() ), 
							context.convertXToSVGLogicalUnits( getXPoints()[0] ), 
							context.convertYToSVGLogicalUnits( getYPoints()[0] ) );
	
				g.drawPolygon( 	context.convertXArrayToSVGLogicalUnits( getXPoints() ), 
								context.convertYArrayToSVGLogicalUnits( getYPoints() ), 
								getCount() );
			}
						
			// Update the current position.
			context.setCurPosX( getXPoints()[ getCount() - 1 ] );
			context.setCurPosY( getYPoints()[ getCount() - 1 ] );
		}
	}

	public void render( DeviceContext context ) throws TranscoderException
	{
		Shape s = getShape( context );
		context.getGdiPath().getCurrentFigure().append( s, true );

		// Update the current position.
		context.setCurPosX( getXPoints()[ getCount() - 1 ] );
		context.setCurPosY( getYPoints()[ getCount() - 1 ] );
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
		}
		
		return p;
	}
}
