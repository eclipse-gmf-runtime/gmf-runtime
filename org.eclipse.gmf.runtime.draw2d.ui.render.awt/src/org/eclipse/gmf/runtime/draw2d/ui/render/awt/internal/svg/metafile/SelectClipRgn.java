/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
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

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.GdiRegion;

/**
 * @author dhabib
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
