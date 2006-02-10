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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;

/**
 * Represents a Brush that is used for filling shapes.  There may be multiple brushes at any one 
 * time, though only one will be used at a time to fill (the current brush is retrieved from 
 * the DeviceContext class).  
 * @author dhabib
 */
public class GdiBrush 
{
	/** A style of brush */
	public static final int BS_SOLID            = 0;
	/** A style of brush */
	public static final int BS_NULL             = 1;
	/** A style of brush */
	public static final int BS_HOLLOW           = 1;
	/** A style of brush */
	public static final int BS_HATCHED          = 2;
	/** A style of brush */
	public static final int BS_PATTERN          = 3;
	/** A style of brush */
	public static final int BS_INDEXED          = 4;
	/** A style of brush */
	public static final int BS_DIBPATTERN       = 5;
	/** A style of brush */
	public static final int BS_DIBPATTERNPT     = 6;
	/** A style of brush */
	public static final int BS_PATTERN8X8       = 7;
	/** A style of brush */
	public static final int BS_DIBPATTERN8X8    = 8;
	/** A style of brush */
	public static final int BS_MONOPATTERN      = 9;

	/** A hatch type.  Only used when they style is BS_HATCHED */
	public static final int  HS_HORIZONTAL       = 0;       /* ----- */
	/** A hatch type.  Only used when they style is BS_HATCHED */
	public static final int  HS_VERTICAL         = 1;       /* ||||| */
	/** A hatch type.  Only used when they style is BS_HATCHED */
	public static final int  HS_FDIAGONAL        = 2;       /* \\\\\ */
	/** A hatch type.  Only used when they style is BS_HATCHED */
	public static final int  HS_BDIAGONAL        = 3;       /* ///// */
	/** A hatch type.  Only used when they style is BS_HATCHED */
	public static final int  HS_CROSS            = 4;       /* +++++ */
	/** A hatch type.  Only used when they style is BS_HATCHED */
	public static final int  HS_DIAGCROSS        = 5;       /* xxxxx */
	
	private int				m_style		= BS_SOLID;
	private Color			m_color		= new Color( 0xff, 0xff, 0xff );
	private int				m_hatch		= 0;
	private BufferedImage 	m_pattern	= null;

	
	
	/**
	 * Default constructor.  Creates a default brush.
	 */
	public GdiBrush()
	{
	    // No data associated with this record.
	}

	/**
	 * Copy constructor
	 * @param brush
	 */
	GdiBrush( GdiBrush brush )
	{
		m_style 	= brush.m_style;
		
		if( brush.m_color != null )
		{	
			m_color 	= new Color( brush.m_color.getRGB() );
		}
		else
		{
			m_color = null;
		}
		
		m_hatch 	= brush.m_hatch;
		
		if( brush.m_pattern != null )
		{	
			m_pattern	= brush.m_pattern.getSubimage( 0, 0, m_pattern.getWidth(), m_pattern.getHeight() );
		}
	}
	
	/**
	 * Creates a brush with a specific style.  Should only be used with BS_SOLID, BS_NULL, 
	 * BS_HOLLOW, or BS_HATCHED. 
	 * @param style Must be one of BS_SOLID, BS_NULL, BS_HOLLOW, or BS_HATCHED.
	 * @param color Color of the brush.
	 * @param hatch Hatch style.  Only used if they style is BS_HATCHED
	 */
	public GdiBrush( int style, Color color, int hatch )
	{
		m_style = style;
		m_color = color;
		m_hatch = hatch;
	}
	
	/**
	 * Create a pattern brush.  Currently only monochrome pattern brushes are supported, so style must currently
	 * be set to BS_MONOPATTERN.  The current text color is used as the foreground, the current background color
	 * is used as the background.
	 * @param style Must be BS_MONOPATTERN
	 * @param pattern A buffered image containing the pattern.
	 */
	public GdiBrush( int style, BufferedImage pattern )
	{
		m_style 	= style;  // must be either BS_MONOPATTERN, BS_PATTERN(8x8), or BS_DIBPATTERN(8x8)
		m_pattern	= pattern;
	}

	/**
	 * Fills the specified shape with this brush.
	 * @param s Shape to fill.
	 * @param g Graphics2D object to put the shape on.
	 * @param context Current DeviceContext
	 */
	public void fill( Shape s, Graphics2D g, DeviceContext context )
	{
		g.setColor( m_color );
		
		Paint oldPaint = null;
		
		switch( m_style )
		{
			default:
			case BS_SOLID:
				// Done, nothing to do.
				break;

			case BS_NULL:
				// Same as BS_HOLLOW.  No fill is performed.
				return;
			
			case BS_HATCHED:
			{
				BufferedImage image = getHatch( context );
				Rectangle2D anchor = new Rectangle2D.Double( 0, 0, 8, 8 );
				TexturePaint tp = new TexturePaint( image, anchor );
				oldPaint = g.getPaint();
				g.setPaint( tp );
				break;
			}
				
			case BS_PATTERN:
			case BS_PATTERN8X8:
			case BS_DIBPATTERN:
			case BS_DIBPATTERN8X8:
			{
				Rectangle2D anchor = new Rectangle2D.Double( 0, 0, m_pattern.getWidth(), m_pattern.getHeight() );
				TexturePaint tp = new TexturePaint( m_pattern, anchor );
				oldPaint = g.getPaint();
				g.setPaint( tp );
				break;
			}
			
			case BS_DIBPATTERNPT:
				break;
			
			case BS_MONOPATTERN:
			{
				BufferedImage image = mapPatternToForeColor( context );
				Rectangle2D anchor = new Rectangle2D.Double( 0, 0, image.getWidth(), image.getHeight() );
				TexturePaint tp = new TexturePaint( image, anchor );
				oldPaint = g.getPaint();
				g.setPaint( tp );
				break;
			}
				
			case BS_INDEXED:
				// Can't find documentation on this, default to simply being a solid brush.
				break;
		}
		
		g.fill( s );
		
		if( oldPaint != null )
		{	
			g.setPaint( oldPaint );
		}
	}
	
	private BufferedImage getHatch( DeviceContext context )
	{
		// Create a buffered image to contain the texture to apply.
		BufferedImage image = new BufferedImage( 8, 8, BufferedImage.TYPE_3BYTE_BGR );
		Graphics2D textureGraphics = image.createGraphics();

		textureGraphics.setColor( m_color );
		textureGraphics.setBackground( context.getBackColor() );
		textureGraphics.clearRect( 0, 0, 8, 8 );
		
		switch( m_hatch )
		{
			case HS_HORIZONTAL:		/* ----- */
				textureGraphics.drawLine( 0, 4, 7, 4 );
				break;
			case HS_VERTICAL:		/* ||||| */
				textureGraphics.drawLine( 4, 0, 4, 7 );
				break;
			case HS_FDIAGONAL:		/* \\\\\ */
				textureGraphics.drawLine( 0, 0, 7, 7 );
				break;
			case HS_BDIAGONAL:		/* ///// */
				textureGraphics.drawLine( 7, 0, 0, 7 );
				break;
			case HS_CROSS:			/* +++++ */
				textureGraphics.drawLine( 0, 4, 7, 4 );
				textureGraphics.drawLine( 4, 0, 4, 7 );
				break;
			default:
			case HS_DIAGCROSS:		/* xxxxx */
				textureGraphics.drawLine( 0, 0, 7, 7 );
				textureGraphics.drawLine( 7, 0, 0, 7 );
				break;
		}
		
		return image;
	}

	private BufferedImage mapPatternToForeColor( DeviceContext context )
	{
		// Image is monochrome, should only have one bit per pixel.  According to the docs, a
		// value of '0' is the foreground color, '1' is the background color.
		BufferedImage image = m_pattern.getSubimage( 0, 0, m_pattern.getWidth(), m_pattern.getHeight() );
		
		int height = image.getHeight();
		for( int y = 0; y < height; y++ )
		{
			int width = image.getWidth();
			for( int x = 0; x < width; x++ )
			{
				int rgb = image.getRGB( x, y ) & 0x00ffffff;
				if( rgb == 0 )
				{
					// Set to the foreground color
					image.setRGB( x, y, context.getTextColor().getRGB() );
				}
				else
				{
					// Set to the background color
					image.setRGB( x, y, context.getBackColor().getRGB() );
				}
			}
 		}

		return image;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Brush: color=" + m_color.toString()); //$NON-NLS-1$
		sb.append(", style="); //$NON-NLS-1$
		switch( m_style )
		{
			default:
			case BS_SOLID:
				sb.append("solid"); //$NON-NLS-1$
				break;
			case BS_NULL:
				sb.append("null"); //$NON-NLS-1$
				break;
			case BS_HATCHED:
				sb.append("hatch "); //$NON-NLS-1$
				switch(m_hatch)
				{
					case HS_HORIZONTAL:
						sb.append("-----"); //$NON-NLS-1$
						break;
					case HS_VERTICAL:
						sb.append("|||||"); //$NON-NLS-1$
						break;
					case HS_FDIAGONAL:
						sb.append("\\\\\\\\\\"); //$NON-NLS-1$
						break;
					case HS_BDIAGONAL:
						sb.append("/////"); //$NON-NLS-1$
						break;
					case HS_CROSS:
						sb.append("+++++"); //$NON-NLS-1$
						break;
					default:
					case HS_DIAGCROSS:
						sb.append("XXXXX"); //$NON-NLS-1$
						break;
				}
				break;
			case BS_PATTERN:
				sb.append("pattern"); //$NON-NLS-1$
				break;
			case BS_PATTERN8X8:
				sb.append("pattern8x8"); //$NON-NLS-1$
				break;
			case BS_DIBPATTERN:
				sb.append("dibPattern"); //$NON-NLS-1$
				break;
			case BS_DIBPATTERN8X8:
				sb.append("dibPattern8x8"); //$NON-NLS-1$
				break;
			case BS_DIBPATTERNPT:
				sb.append("dibPatternPT"); //$NON-NLS-1$
				break;
			case BS_MONOPATTERN:
				sb.append("monoPattern"); //$NON-NLS-1$
				break;
			case BS_INDEXED:
				sb.append("indexed"); //$NON-NLS-1$
				break;
		}

		return sb.toString();
	}
}
