/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
