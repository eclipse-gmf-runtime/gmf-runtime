/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile;

import java.awt.Graphics2D;

import org.apache.batik.transcoder.TranscoderException;

/**
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
class SelectClipRgn extends ObjectBase
{
	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		if( getId() == 0 )
		{
			g.setClip( null );
		}
		else
		{
			GdiRegion clip = (GdiRegion) context.getGDI( getId() );
			
			if( clip != null )
			{	
				clip.apply( g );
			}
			else
			{
				g.setClip( null );
			}
		}
	}	
}
