/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
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
import java.awt.geom.GeneralPath;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.GdiPen;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IEmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IRenderToPath;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;


/**
 * @author dhabib
 *
 */
public class PolyDraw implements IEmf2SvgConverter, IRenderToPath
{
	private static final int		COUNT_OFFSET	= 16;
	private static final int		POINT_OFFSET	= 20;

	private static final int		PT_CLOSEFIGURE	= 1;
	private static final int		PT_LINETO		= 2;
	private static final int		PT_BEZIERTO		= 4;
	private static final int		PT_MOVETO		= 6;
	
	private int		m_count			= 0;
	private int[]	m_xPoints		= null;
	private int[]	m_yPoints		= null;
	private byte[]	m_flags			= null;
	
	private boolean	m_b16Bits		= false;

	public PolyDraw( boolean b16Bits )
	{
		m_b16Bits = b16Bits;
	}
	
	public void readEMFRecord( Record emr ) throws IOException
	{
		m_count 	= (int) emr.getDWORDAt( COUNT_OFFSET );
		
		m_xPoints 	= new int[ m_count ];
		m_yPoints 	= new int[ m_count ];
		m_flags 	= new byte[ m_count ];
		
		int curOffset = POINT_OFFSET;
		
		for( int index = 0; index < m_count; index++ )
		{
			if( m_b16Bits )
			{	
				m_xPoints[ index ] = emr.getShortAt( curOffset );
				m_yPoints[ index ] = emr.getShortAt( curOffset + 2 );
				curOffset += 4;
			}
			else
			{
				m_xPoints[ index ] = emr.getIntAt( curOffset );
				m_yPoints[ index ] = emr.getIntAt( curOffset + 4 );
				curOffset += 8;
			}
		}

		m_flags = emr.getBytesAt( curOffset, m_count );
	}
	
	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		GdiPen curPen = context.getCurPen();
		
		if( curPen != null )
		{	
			curPen.apply( g, context );

			GeneralPath p = new GeneralPath( GeneralPath.WIND_NON_ZERO );
			p.moveTo( 	context.convertXToSVGLogicalUnits( context.getCurPosX() ),
						context.convertYToSVGLogicalUnits( context.getCurPosY() ) );

			getShape( context, p );
			g.draw( p );
		}		
	}

	public void render( DeviceContext context ) throws TranscoderException
	{
		getShape( context, context.getGdiPath().getCurrentFigure() );
	}

	private void getShape( DeviceContext context, GeneralPath p )
	{
		int		lastMoveToX		= context.getCurPosX();
		int		lastMoveToY		= context.getCurPosY();

		for( int index = 0; index < m_count; index++ )
		{
			boolean bCloseFigure 	= (m_flags[ index ] & PT_CLOSEFIGURE) == PT_CLOSEFIGURE;
			int		type			= m_flags[ index ] & ~PT_CLOSEFIGURE;
			int		x				= m_xPoints[ index ];
			int		y				= m_yPoints[ index ];
			
			switch( type )
			{
				case PT_MOVETO:
					context.setCurPosX( x );
					context.setCurPosY( y );
					p.moveTo( x, y );
					lastMoveToX = x;
					lastMoveToY = y;
					break;

				case PT_LINETO:
					p.lineTo(	context.convertXToSVGLogicalUnits( x ), 
								context.convertYToSVGLogicalUnits( y ) );
					
					if( bCloseFigure )
					{
						p.closePath();
						
						context.setCurPosX( lastMoveToX );
						context.setCurPosY( lastMoveToY );
					}
					else
					{	
						context.setCurPosX( x );
						context.setCurPosY( y );
					}
					break;

				case PT_BEZIERTO:
					// Always in groups of 3 points, current position is the first point,
					// next two points are the control points, last point is the endpoint.
					
					int cp1X = context.convertXToSVGLogicalUnits( x );
					int cp1Y = context.convertYToSVGLogicalUnits( y );
					int cp2X = context.convertXToSVGLogicalUnits( m_xPoints[ index + 1 ] );
					int cp2Y = context.convertYToSVGLogicalUnits( m_yPoints[ index + 1 ] );
					bCloseFigure |= (m_flags[ index + 1 ] & PT_CLOSEFIGURE) == PT_CLOSEFIGURE;
					int endX = context.convertXToSVGLogicalUnits( m_xPoints[ index + 2 ] );
					int endY = context.convertYToSVGLogicalUnits( m_yPoints[ index + 2 ] );
					bCloseFigure |= (m_flags[ index + 2 ] & PT_CLOSEFIGURE) == PT_CLOSEFIGURE;
					p.curveTo( cp1X, cp1Y, cp2X, cp2Y, endX, endY );
					
					if( bCloseFigure )
					{
						p.closePath();

						context.setCurPosX( lastMoveToX );
						context.setCurPosY( lastMoveToY );
					}
					else
					{	
						context.setCurPosX( m_xPoints[ index + 2 ] );
						context.setCurPosY( m_yPoints[ index + 2 ] );
					}
					index += 2;
					break;

				default:
					// Assert
					break;
			}
		}
	}
}
