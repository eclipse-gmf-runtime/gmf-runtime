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
public class AngleArc implements IEmf2SvgConverter, IRenderToPath
{
	private static final int	CENTER_OFFSET		= 0;
	private static final int	RADIUS_OFFSET		= 8;
	private static final int	START_ANGLE_OFFSET	= 12;
	private static final int	END_ANGLE_OFFSET	= 16;

	private Point	m_center		= new Point( 0, 0 );
	private int		m_radius		= 0;
	private float	m_startAngle	= 0.0f;
	private float	m_sweepAngle	= 0.0f;
	
	public void readEMFRecord( Record emr ) throws IOException
	{
		m_center 		= emr.getPointLAt( CENTER_OFFSET );
		m_radius 		= emr.getIntAt( RADIUS_OFFSET );
		m_startAngle	= emr.getFloatAt( START_ANGLE_OFFSET );
		m_sweepAngle	= emr.getFloatAt( END_ANGLE_OFFSET );
	}
	
	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		GdiPen curPen = context.getCurPen();
		
		if( curPen != null )
		{	
			curPen.apply( g, context );
			Shape shape = getShape( context );

			// Draw a line from the current position to the beginning of the arc.
			Arc2D.Double arc = (Arc2D.Double) shape;
			Point2D point = arc.getStartPoint();
			
			g.drawLine(	context.convertXToSVGLogicalUnits( context.getCurPosX() ), 
						context.convertYToSVGLogicalUnits( context.getCurPosY() ), 
						(int) point.getX(), 
						(int) point.getY() );
			g.draw( shape );

			// Update the endpoint.
			point = arc.getEndPoint();
			context.setCurPosX( context.convertXToWindowsLogicalUnits( (int) point.getX() ) );
			context.setCurPosY( context.convertYToWindowsLogicalUnits( (int) point.getY() ) );
		}		
	}
	
	public void render( DeviceContext context ) throws TranscoderException
	{
		Shape shape = getShape( context );
		GeneralPath p = context.getGdiPath().getCurrentFigure();
		p.append( shape, true );

		Arc2D.Double arc = (Arc2D.Double) shape;
		Point2D point = arc.getEndPoint();
		context.setCurPosX( context.convertXToWindowsLogicalUnits( (int) point.getX() ) );
		context.setCurPosY( context.convertYToWindowsLogicalUnits( (int) point.getY() ) );
	}

	private Shape getShape( DeviceContext context )
	{
		int centerX = context.convertXToSVGLogicalUnits( m_center.x );
		int centerY = context.convertYToSVGLogicalUnits( m_center.y );
		int radius = context.scaleX( m_radius );

		// find the 'bounding rectangle'.  Since this is a arc on a circle, the rectangle is actually
		// a square, width and height are radius*2...
		Rectangle 	rect = new Rectangle( centerX - radius, centerY - radius, radius * 2, radius * 2 );

		Arc2D.Double arc = new Arc2D.Double();
		arc.setArc( rect, m_startAngle, m_sweepAngle, Arc2D.OPEN );

		return arc;
	}
}
