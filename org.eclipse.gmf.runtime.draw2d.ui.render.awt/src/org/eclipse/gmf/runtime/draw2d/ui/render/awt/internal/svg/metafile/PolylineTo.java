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
import java.awt.geom.GeneralPath;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.GdiPen;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IRenderToPath;

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
