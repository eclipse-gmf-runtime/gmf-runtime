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
