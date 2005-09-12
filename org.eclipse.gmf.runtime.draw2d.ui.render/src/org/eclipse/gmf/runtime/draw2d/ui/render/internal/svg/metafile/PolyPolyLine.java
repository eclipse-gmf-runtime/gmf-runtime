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
import java.awt.geom.GeneralPath;

/**  
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
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
