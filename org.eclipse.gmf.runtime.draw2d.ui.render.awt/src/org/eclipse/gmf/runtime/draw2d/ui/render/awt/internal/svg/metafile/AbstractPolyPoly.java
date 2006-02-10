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
import java.awt.geom.GeneralPath;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IEmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IRenderToPath;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IWmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

/**  
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */

abstract class AbstractPolyPoly implements IEmf2SvgConverter, IWmf2SvgConverter, IRenderToPath
{
	private static final int	EMF_N_POLYS_OFFSET		= 16;
	private static final int	EMF_COUNT_POINTS_OFFSET	= 20;
	private static final int	EMF_COUNTS_OFFSET		= 24;

	private static final int	WMF_N_POLYS_OFFSET		= 0;
	private static final int	WMF_COUNTS_OFFSET		= 2;

	/** number of separate polys */
	private int		m_nPolys		= 0;
	/** total number of points in the array */
	private int		m_nPoints		= 0;
	/** array containing the number of points in each specific poly */
	private int[]	m_nPointsInPoly	= null; 
	/** array integers representing the x position of all points on all polys */
	private int[]	m_xPointList		= null;
	/** array integers representing the y position of all points on all polys */
	private int[]	m_yPointList		= null;
	/** Indicates whether or not the point array is stored as 16 or 32 bit integers. */
	private boolean m_b16Bit			= true;
	
	public AbstractPolyPoly( boolean b16Bit )
	{
		m_b16Bit = b16Bit;
	}

	public void readWMFRecord( Record emr ) throws IOException
	{
		m_nPolys = emr.getShortAt( WMF_N_POLYS_OFFSET );
		m_nPointsInPoly = new int[ m_nPolys ];
		m_nPoints = 0;
		
		
		// Read the size of each polygon.
		for( int i = 0; i < m_nPolys; i++ )
		{
			m_nPointsInPoly[ i ] = emr.getShortAt( WMF_COUNTS_OFFSET + ( i * 2 ) );
			m_nPoints += m_nPointsInPoly[ i ];
		}

		m_xPointList 	= new int[ m_nPoints ];
		m_yPointList 	= new int[ m_nPoints ];

		int curOffset = WMF_COUNTS_OFFSET + (m_nPolys * 2);
		
		for( int i = 0; i < m_nPoints; i++ )
		{
			// Short based points.
			m_xPointList[ i ] = emr.getShortAt( curOffset );
			m_yPointList[ i ] = emr.getShortAt( curOffset + 2 );
			curOffset += 4;
		}
	}

	public void readEMFRecord( Record emr ) throws IOException
	{
		m_nPolys = emr.getIntAt( EMF_N_POLYS_OFFSET );
		m_nPoints = emr.getIntAt( EMF_COUNT_POINTS_OFFSET );
		
		m_nPointsInPoly	= new int[ m_nPolys ];
		m_xPointList 	= new int[ m_nPoints ];
		m_yPointList 	= new int[ m_nPoints ];
		
		// Read the size of each polygon.
		for( int i = 0; i < m_nPolys; i++ )
		{
			m_nPointsInPoly[ i ] = emr.getIntAt( EMF_COUNTS_OFFSET + ( i * 4 ) );	
		}
		
		int curOffset = EMF_COUNTS_OFFSET + (m_nPolys * 4);
		
		for( int i = 0; i < m_nPoints; i++ )
		{
			if( m_b16Bit )
			{
				// Short based points.
				m_xPointList[ i ] = emr.getShortAt( curOffset );
				m_yPointList[ i ] = emr.getShortAt( curOffset + 2 );
				curOffset += 4;
			}
			else
			{
				// Int based points.
				m_xPointList[ i ] = emr.getIntAt( curOffset );
				m_yPointList[ i ] = emr.getIntAt( curOffset + 4 );
				curOffset += 8;
			}
		}
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		int curOffset = 0;
		
		for( int i = 0; i < m_nPolys; i++ )
		{
			int 	numPoints 	= m_nPointsInPoly[ i ];
			int[] 	xPoints 	= new int[ numPoints ];			
			int[] 	yPoints 	= new int[ numPoints ];
			
			for( int j = 0; j < numPoints; j++ )
			{
				xPoints[ j ] = context.convertXToSVGLogicalUnits( m_xPointList[ curOffset ] );
				yPoints[ j ] = context.convertYToSVGLogicalUnits( m_yPointList[ curOffset ] );
				curOffset++;
			}
			
			renderPoly( g, context, xPoints, yPoints, numPoints );
		}
	}
	
	public void render( DeviceContext context ) throws TranscoderException
	{
		int curOffset = 0;

		GeneralPath p = new GeneralPath();
		
		for( int i = 0; i < m_nPolys; i++ )
		{
			int 	numPoints 	= m_nPointsInPoly[ i ];
			int[] 	xPoints 	= new int[ numPoints ];			
			int[] 	yPoints 	= new int[ numPoints ];
			
			for( int j = 0; j < numPoints; j++ )
			{
				xPoints[ j ] = context.convertXToSVGLogicalUnits( m_xPointList[ curOffset ] );
				yPoints[ j ] = context.convertYToSVGLogicalUnits( m_yPointList[ curOffset ] );
				curOffset++;
			}
			
			renderPoly( p, xPoints, yPoints, numPoints );
		}
		
		context.getGdiPath().appendFigure( p );
	}

	protected abstract boolean renderPoly( Graphics2D g, DeviceContext context, int[] xPoints, int[] yPoints, int nPoints );
	protected abstract boolean renderPoly( GeneralPath path, int[] xPoints, int[] yPoints, int nPoints );
}
