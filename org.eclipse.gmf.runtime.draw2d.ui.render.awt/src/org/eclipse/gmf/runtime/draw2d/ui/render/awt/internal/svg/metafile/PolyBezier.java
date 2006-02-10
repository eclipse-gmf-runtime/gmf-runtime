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

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.GdiPen;

/**
 * @author dhabib
 *
 */
public class PolyBezier extends AbstractPoly
{
	private boolean m_bTo	= false;
	
	/**
	 * @param b16Bits
	 */
	public PolyBezier( boolean b16Bits, boolean bTo )
	{
		super( b16Bits );
		m_bTo = bTo;
	}

	public void render(Graphics2D g, DeviceContext context)
		throws TranscoderException
	{
		GdiPen pen = context.getCurPen();
		
		if( pen != null )
		{
			pen.apply( g, context );

			int count = getCount();
			
			if( count > 0 )
			{	
				Shape shape = getShape( context );
				g.draw( shape );
				
				if( m_bTo )
				{
					// Update the current position.
					context.setCurPosX( getXPoints()[ count - 1 ] );
					context.setCurPosY( getYPoints()[ count - 1 ] );
				}
			}
		}
	}
	
	public void render( DeviceContext context )
		throws TranscoderException
	{
		int count = getCount();
		
		if( count > 0 )
		{	
			Shape shape = getShape( context );
			
			if( m_bTo )
			{
				context.getGdiPath().getCurrentFigure().append( shape, true );
				// Update the current position.
				context.setCurPosX( getXPoints()[ count - 1 ] );
				context.setCurPosY( getYPoints()[ count - 1 ] );
			}
			else
			{
				context.getGdiPath().appendFigure( shape );
			}
		}
	}
	
	private Shape getShape( DeviceContext context )
	{
		GeneralPath genPath = new GeneralPath( GeneralPath.WIND_NON_ZERO );

		int count = getCount();
	
		if( count > 0 )
		{	
			m_xPointsConv = context.convertXArrayToSVGLogicalUnits( getXPoints() );
			m_yPointsConv = context.convertYArrayToSVGLogicalUnits( getYPoints() );

			// The PolyBezier and PolyBezierTo are similar in that they each use 4 points to define
			// the curve: first point is the 'start' point, next two are 'control' points, and the fourth
			// is the 'end' point.  The end point of each curve is used as the start point of the next curve.  
			// PolyBezier uses the first point from the array as the initial start
			// point and does not update the current position in the device context.  The 'To' version
			// uses the current position in the device context as the starting point and updates that position after drawing
			// the last curve in the array.
			
			int startIndex;

			if( m_bTo )
			{
				startIndex	= 0;
				
				if( context.getGdiPath().isOpen() == false )
				{
					// No path is open, use the current position in the context
					genPath.moveTo( context.convertXToSVGLogicalUnits( context.getCurPosX() ), 
									context.convertYToSVGLogicalUnits( context.getCurPosY() ) );
				}
				else
				{
					// Must have the initial 'moveTo'.  Get the current coordinates from the currently open path.
					Point2D curPos = context.getGdiPath().getCurrentFigure().getCurrentPoint();
					genPath.moveTo( (float)curPos.getX(), (float)curPos.getY() );
				}
			}
			else
			{
				genPath.moveTo( m_xPointsConv[ 0 ], m_yPointsConv[ 0 ] );
				startIndex = 1;
			}

			for ( int j = startIndex; j <= count - 3; j = j + 3 ) 
			{
				int firstControlPointX 	= m_xPointsConv[ j ];
				int firstControlPointY 	= m_yPointsConv[ j ];
				int secondControlPointX	= m_xPointsConv[ j + 1 ];
				int secondControlPointY	= m_yPointsConv[ j + 1 ];
				int endPointX			= m_xPointsConv[ j + 2 ];
				int endPointY			= m_yPointsConv[ j + 2 ];

				genPath.curveTo(	firstControlPointX, firstControlPointY, 
								 	secondControlPointX, secondControlPointY, 
									endPointX, endPointY );
			}
		}
		
		return genPath;
	}
}
