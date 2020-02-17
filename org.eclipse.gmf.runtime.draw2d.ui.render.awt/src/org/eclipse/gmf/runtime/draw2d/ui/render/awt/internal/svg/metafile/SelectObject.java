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

import java.awt.Color;
import java.awt.Graphics2D;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.AbstractTranscoder;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.GdiBrush;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.GdiFont;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.GdiPen;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.GdiRegion;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.ITraceMe;


/**  
 * @author dhabib
 */
public class SelectObject extends ObjectBase implements ITraceMe
{
	private String m_trace = null;

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		Object obj = context.getGDI( getId() );
		
		if( obj != null )
		{
			m_trace = Integer.toString(getId());

			if( obj instanceof GdiFont )
			{
				GdiFont gdiFont = (GdiFont) obj;
				
				context.setCurFont( gdiFont );
			}
			else if( obj instanceof GdiBrush )
			{
				GdiBrush gdiBrush = (GdiBrush) obj;
				context.setCurBrush( gdiBrush );
			}
			else if( obj instanceof GdiPen )
			{
				GdiPen pen = (GdiPen) obj;
				context.setCurPen( pen );
			}
			else if( obj instanceof GdiRegion )
			{
				GdiRegion rgn = (GdiRegion) obj;
				context.setCurRegion( rgn );
			}
			else
			{
				AbstractTranscoder.logMessage( getClass().getName() + "Invalid object type in select object, object #" + getId() );	//$NON-NLS-1$
				return;
			}
		}
		else
		{
			if( (getId() & ENHMETA_STOCK_OBJECT) == ENHMETA_STOCK_OBJECT )
			{
				// Stock object.  Figure out which kind.
				int objectType = getId() & ~ENHMETA_STOCK_OBJECT;
				
				switch( objectType )
				{
					case WHITE_BRUSH:
						m_trace = "WHITE_BRUSH"; //$NON-NLS-1$
						context.setCurBrush( new GdiBrush( GdiBrush.BS_SOLID, Color.white, 0 ) );
						break;
					case LTGRAY_BRUSH:
						m_trace = "LTGRAY_BRUSH"; //$NON-NLS-1$
						context.setCurBrush( new GdiBrush( GdiBrush.BS_SOLID, Color.lightGray, 0 ) );
						break;
					case GRAY_BRUSH:
						m_trace = "GRAY_BRUSH"; //$NON-NLS-1$
						context.setCurBrush( new GdiBrush( GdiBrush.BS_SOLID, Color.gray, 0 ) );
						break;
					case DKGRAY_BRUSH:
						m_trace = "DKGRAY_BRUSH"; //$NON-NLS-1$
						context.setCurBrush( new GdiBrush( GdiBrush.BS_SOLID, Color.darkGray, 0 ) );
						break;
					case BLACK_BRUSH:
						m_trace = "BLACK_BRUSH"; //$NON-NLS-1$
						context.setCurBrush( new GdiBrush( GdiBrush.BS_SOLID, Color.black, 0 ) );
						break;
					case NULL_BRUSH:
						m_trace = "NULL_BRUSH"; //$NON-NLS-1$
						context.setCurBrush( null );
						break;
					case WHITE_PEN:
						m_trace = "WHITE_PEN"; //$NON-NLS-1$
						context.setCurPen( new GdiPen( GdiPen.PS_SOLID, 1, Color.white ) );
						break;
					case BLACK_PEN:
						m_trace = "BLACK_PEN"; //$NON-NLS-1$
						context.setCurPen( new GdiPen( GdiPen.PS_SOLID, 1, Color.black ) );
						break;
					case NULL_PEN:
						m_trace = "NULL_PEN"; //$NON-NLS-1$
						context.setCurPen( null );
						break;
					case OEM_FIXED_FONT:
						m_trace = "OEM_FIXED_FONT"; //$NON-NLS-1$
						context.setCurFont( new GdiFont( 10, false, false, false, false, "Monospaced", 0 ) );	//$NON-NLS-1$
						break;
					case ANSI_FIXED_FONT:
						m_trace = "ANSI_FIXED_FONT"; //$NON-NLS-1$
						context.setCurFont( new GdiFont( 10, false, false, false, false, "Monospaced", 0 ) );	//$NON-NLS-1$
						break;
					case ANSI_VAR_FONT:
						m_trace = "ANSI_VAR_FONT"; //$NON-NLS-1$
						context.setCurFont( new GdiFont( 10, false, false, false, false, "SanSerif", 0 ) );		//$NON-NLS-1$
						break;
					case SYSTEM_FONT:
						m_trace = "SYSTEM_FONT"; //$NON-NLS-1$
						context.setCurFont( new GdiFont( 12, false, false, false, true, "System", 0 ) );		//$NON-NLS-1$
						break;
					case DEVICE_DEFAULT_FONT:
						m_trace = "DEVICE_DEFAULT_FONT"; //$NON-NLS-1$
						context.setCurFont( new GdiFont( 10, false, false, false, false, "Dialog", 0 ) );		//$NON-NLS-1$
						break;
					case SYSTEM_FIXED_FONT:
						m_trace = "SYSTEM_FIXED_FONT"; //$NON-NLS-1$
						context.setCurFont( new GdiFont( 10, false, false, false, false, "Monospaced", 0 ) );	//$NON-NLS-1$
						break;
					case DEFAULT_PALETTE:
						m_trace = "DEFAULT_PALETTE"; //$NON-NLS-1$
					default:
						break;
				}
			}
			else
			{
				AbstractTranscoder.logMessage( getClass().getName() + ": SelectObject failed to select object #" + getId() );	//$NON-NLS-1$
			}
		}
	}

	public String toString() {
		return m_trace;
	}
}
