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

/**  
 * @author dhabib
 */
public class PolyPolyLine extends AbstractPolyPoly
{
	public PolyPolyLine( boolean b16Bits )
	{
		super( b16Bits );
	}
	
	public boolean renderPoly( Graphics2D g, DeviceContext context, int[] xPoints, int[] yPoints, int nPoints )
	{
		GdiPen curPen = context.getCurPen();
		
		if( curPen != null )
		{
			curPen.apply( g, context );
			g.drawPolyline( xPoints, yPoints, nPoints );
		}
		
		return true;
	}
	
	protected boolean renderPoly( 	GeneralPath		p, 
									int[] 			xPoints, 
									int[] 			yPoints, 
									int 			nPoints )
	{
		if( nPoints > 0 )
		{	
			p.moveTo( xPoints[ 0 ], yPoints[ 0 ] );
			
			for( int index = 1; index < nPoints; index++ )
			{
				p.lineTo( xPoints[ index ], yPoints[ index ] );
			}
		}
		return true;
	}
}
