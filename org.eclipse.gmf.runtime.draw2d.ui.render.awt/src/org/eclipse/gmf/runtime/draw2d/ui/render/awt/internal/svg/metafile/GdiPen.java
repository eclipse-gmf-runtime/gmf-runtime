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

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;


/**
 * Represents a Pen that is used for drawing lines.  There may be multiple pens at any one 
 * time, though only one of the pens will be used at a time to draw.  The currently selected
 * pen may be retrieved from the DeviceContext.  
 *
 * @author dhabib
 */
public class GdiPen 
{
	/** Pen drawing style */
	public static final int 	PS_SOLID            = 0;
	/** Pen drawing style */
	public static final int 	PS_DASH             = 1;       /* -------  */
	/** Pen drawing style */
	public static final int 	PS_DOT              = 2;       /* .......  */
	/** Pen drawing style */
	public static final int 	PS_DASHDOT          = 3;       /* _._._._  */
	/** Pen drawing style */
	public static final int 	PS_DASHDOTDOT       = 4;       /* _.._.._  */
	/** Pen drawing style */
	public static final int 	PS_NULL             = 5;
	/** Pen drawing style */
	public static final int 	PS_INSIDEFRAME      = 6;
	/** Pen drawing style */
	public static final int 	PS_USERSTYLE        = 7;
	/** Pen drawing style */
	public static final int 	PS_ALTERNATE        = 8;
	/** Pen drawing style */
	public static final int		PS_STYLE_MASK       = 0x0000000F;

	/** Pen cap style */
	public static final int		PS_ENDCAP_ROUND     = 0x00000000;
	/** Pen cap style */
	public static final int		PS_ENDCAP_SQUARE    = 0x00000100;
	/** Pen cap style */
	public static final int		PS_ENDCAP_FLAT      = 0x00000200;
	/** Pen cap style */
	public static final int		PS_ENDCAP_MASK      = 0x00000F00;

	/** Pen join style */
	public static final int		PS_JOIN_ROUND       = 0x00000000;
	/** Pen join style */
	public static final int		PS_JOIN_BEVEL       = 0x00001000;
	/** Pen join style */
	public static final int		PS_JOIN_MITER       = 0x00002000;
	/** Pen join style */
	public static final int		PS_JOIN_MASK        = 0x0000F000;

	private int		m_style		= 0;
	private int		m_width		= 1;
	private Color	m_color		= new Color( 0 );

	private static final float[]	DASH			= { (float) 5.0, (float) 3.0};
	private static final float[]	DOT				= { (float) 1.0, (float) 3.0 };
	private static final float[]	DASH_DOT		= { (float) 5.0, (float) 3.0, (float) 1.0, (float) 3.0 };
	private static final float[]	DASH_DOT_DOT	= { (float) 5.0, (float) 3.0, (float) 1.0, (float) 3.0, (float) 1.0, (float) 3.0 };

	/**
	 * Creates a default pen.
	 */
	public GdiPen()
	{
	    // NOthing to initialize
	}

	/**
	 * Creates a pen with specific attributes.
	 * @param style - A combination of the PS_* for drawing styles, end caps, and joins.
	 * @param width - Width of the pen, in pixels.
	 * @param color - color of the pen.
	 */
	public GdiPen( int style, int width, Color color )
	{
		m_style 	= style;
		m_width 	= width;
		m_color		= color;
	}
	
	/**
	 * Copy constructor
	 * @param pen
	 */
	GdiPen( GdiPen pen )
	{
		m_style 	= pen.m_style;
		m_width 	= pen.m_width;
		m_color		= new Color( pen.m_color.getRGB() );
	}

	/**
	 * Applies the pen to the specified graphics object.
	 * @param g Graphics object on which to apply the pen
	 * @param context Current device context.
	 */
	public void apply( Graphics2D g, DeviceContext context )
	{
		g.setColor( m_color );
		
		int width = context.scaleX( m_width );
		if( width <= 0 )
		{
			width = 1;
		}
		
		boolean bNullStroke = false;
		float[] dotPattern = null;	
		switch( m_style & PS_STYLE_MASK )
		{
			case PS_NULL:
				bNullStroke = true;
				break;
			case PS_INSIDEFRAME:
			case PS_SOLID:
			default:
				break;
			case PS_DASH:
				dotPattern = DASH; 
				break;
			case PS_DOT:
				dotPattern = DOT;
				break;
			case PS_DASHDOT:
				dotPattern = DASH_DOT;
				break;
			case PS_DASHDOTDOT:
				dotPattern = DASH_DOT_DOT;
				break;
		}

		int endcap;
		switch( m_style & PS_ENDCAP_MASK )
		{
			default:
			case PS_ENDCAP_ROUND:
				endcap = BasicStroke.CAP_ROUND;
				break;
			case PS_ENDCAP_SQUARE:
				endcap = BasicStroke.CAP_SQUARE;
				break;
			case PS_ENDCAP_FLAT:
				endcap = BasicStroke.CAP_BUTT;
				break;
		}
	
		int miter;
		switch( m_style & PS_JOIN_MASK )
		{
			case PS_JOIN_ROUND:
				miter = BasicStroke.JOIN_ROUND;
				break;
			case PS_JOIN_BEVEL:
				miter = BasicStroke.JOIN_BEVEL;
				break;
			default:
			case PS_JOIN_MITER:
				miter = BasicStroke.JOIN_MITER;
				break;
		}

		if( !bNullStroke )
		{
			BasicStroke	stroke;
			if( dotPattern != null )
			{
				stroke = new BasicStroke( width, endcap, miter, context.getMiterLimit(), dotPattern, (float) 0.0 );
			}
			else
			{
				stroke = new BasicStroke( width, endcap, miter, context.getMiterLimit() );
			}
	
			g.setStroke( stroke );
		}
		else
		{
			g.setStroke( new NullStroke() );
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Pen: width=" + m_width); //$NON-NLS-1$
		sb.append(", color=" + m_color.toString()); //$NON-NLS-1$
		sb.append(", style: "); //$NON-NLS-1$
		switch( m_style & PS_STYLE_MASK )
		{
			case PS_NULL:
				sb.append("nullStroke"); //$NON-NLS-1$
				break;
			case PS_INSIDEFRAME:
			case PS_SOLID:
			default:
				sb.append("solid"); //$NON-NLS-1$
				break;
			case PS_DASH:
				sb.append("dash"); //$NON-NLS-1$
				break;
			case PS_DOT:
				sb.append("dot"); //$NON-NLS-1$
				break;
			case PS_DASHDOT:
				sb.append("dashDot"); //$NON-NLS-1$
				break;
			case PS_DASHDOTDOT:
				sb.append("dashDotDot"); //$NON-NLS-1$
				break;
		}
		switch( m_style & PS_ENDCAP_MASK )
		{
			default:
			case PS_ENDCAP_ROUND:
				sb.append(" roundCap"); //$NON-NLS-1$
				break;
			case PS_ENDCAP_SQUARE:
				sb.append(" squareCap"); //$NON-NLS-1$
				break;
			case PS_ENDCAP_FLAT:
				sb.append(" buttCap"); //$NON-NLS-1$
				break;
		}
		switch( m_style & PS_JOIN_MASK )
		{
			case PS_JOIN_ROUND:
				sb.append(" roundJoin"); //$NON-NLS-1$
				break;
			case PS_JOIN_BEVEL:
				sb.append(" bevelJoin"); //$NON-NLS-1$
				break;
			default:
			case PS_JOIN_MITER:
				sb.append(" miterJoin"); //$NON-NLS-1$
				break;
		}

		return sb.toString();
	}
}
