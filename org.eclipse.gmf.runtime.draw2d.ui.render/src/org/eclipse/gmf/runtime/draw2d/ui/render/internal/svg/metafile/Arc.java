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

package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;

/**
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 *
 */
public class Arc implements IEmf2SvgConverter, IWmf2SvgConverter, IRenderToPath
{
	public static final int			ARC		= 1;
	public static final int			ARCTO	= 2;
	public static final int			PIE		= 3;
	public static final int			CHORD	= 4;

	private static final int	EMF_BOX_OFFSET		= 0;
	private static final int	EMF_START_OFFSET	= 16;
	private static final int	EMF_END_OFFSET		= 24;

	private static final int	WMF_Y_END_OFFSET	= 0;
	private static final int	WMF_X_END_OFFSET	= 2;
	private static final int	WMF_Y_START_OFFSET	= 4;
	private static final int	WMF_X_START_OFFSET	= 6;
	private static final int	WMF_BOTTOM_OFFSET	= 8;
	private static final int	WMF_RIGHT_OFFSET	= 10;
	private static final int	WMF_TOP_OFFSET		= 12;
	private static final int	WMF_LEFT_OFFSET		= 14;
	
	private int			m_type			= ARC;
	
	private Rectangle	m_box 			= new Rectangle();
	private Point		m_start			= new Point( 0, 0 );
	private Point		m_end 			= new Point( 0, 0 );
	
	
	public Arc( int type )
	{
		m_type = type;
	}
	
	public void readWMFRecord( Record rec ) throws IOException
	{
		int xStart 	= rec.getShortAt( WMF_X_START_OFFSET );
		int yStart 	= rec.getShortAt( WMF_Y_START_OFFSET );
		int xEnd	= rec.getShortAt( WMF_X_END_OFFSET );
		int yEnd	= rec.getShortAt( WMF_Y_END_OFFSET );
		
		int xBound	= rec.getShortAt( WMF_LEFT_OFFSET );
		int yBound	= rec.getShortAt( WMF_TOP_OFFSET );
		int x1Bound	= rec.getShortAt( WMF_RIGHT_OFFSET );
		int y1Bound	= rec.getShortAt( WMF_BOTTOM_OFFSET );
		
		m_box 	= new Rectangle( xBound, yBound, x1Bound - xBound, y1Bound - yBound );
		m_start	= new Point( xStart, yStart );
		m_end	= new Point( xEnd, yEnd );
	}

	public void readEMFRecord( Record emr ) throws IOException
	{
		m_box 	= emr.getRectangleLAt( EMF_BOX_OFFSET );
		m_start	= emr.getPointLAt( EMF_START_OFFSET );
		m_end	= emr.getPointLAt( EMF_END_OFFSET );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		Shape shape = getShape( context );

		if( m_type == PIE || m_type == CHORD )
		{
			GdiBrush brush = context.getCurBrush();
			
			if( brush != null )
			{
				brush.fill( shape, g, context );
			}
		}	

		GdiPen curPen = context.getCurPen();
		
		if( curPen != null )
		{	
			curPen.apply( g, context );

			if( m_type == ARCTO )
			{
				Arc2D.Double arc = (Arc2D.Double) shape;
				Point2D point = arc.getStartPoint();
				
				g.drawLine(	context.convertXToSVGLogicalUnits( context.getCurPosX() ), 
							context.convertYToSVGLogicalUnits( context.getCurPosY() ), 
							(int) point.getX(), 
							(int) point.getY() );
			}

			g.draw( shape );
		}
		
		if( m_type == ARCTO )
		{
			Arc2D.Double arc = (Arc2D.Double) shape;
			Point2D point = arc.getEndPoint();
			context.setCurPosX( context.convertXToWindowsLogicalUnits( (int) point.getX() ) );
			context.setCurPosY( context.convertYToWindowsLogicalUnits( (int) point.getY() ) );
		}
	}
	
	public void render( DeviceContext context ) throws TranscoderException
	{
		Shape shape = getShape( context );
		
		if( m_type == ARCTO )
		{
			GeneralPath p = context.getGdiPath().getCurrentFigure();
			p.append( shape, true );
			Arc2D.Double arc = (Arc2D.Double) shape;
			Point2D point = arc.getEndPoint();
			context.setCurPosX( context.convertXToWindowsLogicalUnits( (int) point.getX() ) );
			context.setCurPosY( context.convertYToWindowsLogicalUnits( (int) point.getY() ) );
		}
		else
		{
			// Do not update the cursor position on the 'main' path.
			context.getGdiPath().appendFigure( shape );
		}
	}
	
	private int getAngle( int triWidth, int triHeight )
	{
		double tan = 0.0;
		if( triWidth != 0 )
		{	
			tan = (double) triHeight / (double) triWidth;
		}
		
		// The 'tangent' of this is triHeight/triWidth.  We want the angle so compute the arcTan of
		// this value.
		int angle = (int)Math.toDegrees( Math.atan( tan ) );
		
		if( triWidth < 0 )
		{
			// 2nd or 3rd quadrant. 
			angle += 180;
		}
		else if( triHeight < 0 )
		{
			// 4th quadrant.  angle will be negative.  Convert to a positive angle.
			angle += 360;
		}
		
		return angle;
	}

	private Shape getShape( DeviceContext context )
	{
		int x = context.convertXToSVGLogicalUnits( m_box.x );
		int y = context.convertYToSVGLogicalUnits( m_box.y );
		int w = context.scaleX( m_box.width );
		int h = context.scaleY( m_box.height );
		
		int centerArcX = x + ( w / 2 );
		int centerArcY = y + ( h / 2 );
		
		// Get the 'start angle', which is the angle from the center point of the containing rectangle
		// to the intersection of the arc with the containing rectangle.
		
		// Get the two 'legs' of the right triangle formed by the bounding box, the x/y axis and the line
		// from the centerpoint to the start point.
		int startPointX = context.convertXToSVGLogicalUnits( m_start.x );
		int startPointY = context.convertYToSVGLogicalUnits( m_start.y );
		int triWidth	= startPointX - centerArcX;
		int triHeight 	= centerArcY - startPointY ;
		
		// The 'tangent' of this is triHeight/triWidth.  We want the angle so compute the arcTan of
		// this value.
		int startAngle = getAngle( triWidth, triHeight );
		
		// Do the same for the 'end point);
		int endPointX 	= context.convertXToSVGLogicalUnits( m_end.x );
		int endPointY 	= context.convertYToSVGLogicalUnits( m_end.y );
		triWidth		= endPointX - centerArcX;
		triHeight 		= centerArcY - endPointY;
		
		int endAngle = getAngle( triWidth, triHeight );
		int arcAngle;
		
		if( context.getArcDirection() == DeviceContext.AD_COUNTERCLOCKWISE )
		{
			arcAngle = endAngle - startAngle;
		}
		else
		{
			arcAngle = startAngle - endAngle;
		}

		int arcType;

		if( m_type == ARC || m_type == ARCTO )
		{
			arcType = Arc2D.OPEN;
		}
		else if( m_type == CHORD )
		{
			arcType = Arc2D.CHORD;
		}
		else
		{
			arcType = Arc2D.PIE;
		}

		Rectangle bounds = new Rectangle( x, y, w, h );
		Arc2D.Double arc = new Arc2D.Double();

		arc.setArc( bounds, startAngle, arcAngle, arcType );

		return arc;
	}
}
