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
import java.awt.Shape;
import java.awt.geom.GeneralPath;

import org.apache.batik.transcoder.TranscoderException;

/**  
 * @author dhabib
 */
public class Polyline extends AbstractPoly implements IRenderToPath
{
	public Polyline( boolean b16Bits )
	{
		super( b16Bits );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		GdiPen curPen = context.getCurPen();
		
		if( curPen != null )
		{
			curPen.apply( g, context );

			Shape s = getShape( context );
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
		}
		
		return p;
	}
}
