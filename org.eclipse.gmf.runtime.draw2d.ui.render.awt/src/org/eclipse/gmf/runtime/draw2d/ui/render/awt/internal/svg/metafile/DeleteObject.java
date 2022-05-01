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

import org.apache.batik.transcoder.TranscoderException;


/**  
 * @author dhabib
 */
public class DeleteObject extends ObjectBase implements ITraceMe
{
	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		Object obj = context.getGDI( getId() );
		
		if( obj != null )
		{
			if( obj instanceof GdiFont )
			{
				GdiFont gdiFont = (GdiFont) obj;

				if( gdiFont == context.getCurFont() )
				{
					context.setCurFont( new GdiFont() );
				}
			}
			else if( obj instanceof GdiBrush )
			{
				GdiBrush gdiBrush = (GdiBrush) obj;
				
				if( gdiBrush == context.getCurBrush() )
				{
					context.setCurBrush( new GdiBrush() );
				}
			}
			else if( obj instanceof GdiPen )
			{
				GdiPen pen = (GdiPen) obj;
				
				if( pen == context.getCurPen() )
				{
					context.setCurPen( new GdiPen() );
				}
			}
			else if( obj instanceof GdiRegion )
			{
				GdiRegion rgn = (GdiRegion) obj;

				if( rgn == context.getCurRegion() )
				{
					context.setCurRegion( new GdiRegion( (Shape) null ) );
				}
			}
			else
			{
				AbstractTranscoder.logMessage( getClass().getName() + "Invalid object type in delete object, object #" + getId() );	//$NON-NLS-1$
				return;
			}

			context.deleteGDI( getId() );
		}
		else
		{
			AbstractTranscoder.logMessage( getClass().getName() + ": DeleteObject failed to delete object #" + getId() );	//$NON-NLS-1$
		}
	}

	public String toString() {
		return Integer.toString(getId());
	}
}
