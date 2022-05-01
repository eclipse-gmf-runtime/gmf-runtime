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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;

/**  
 * @author dhabib
 */
public class ExtTextOut implements IEmf2SvgConverter, IWmf2SvgConverter 
{
//	private static final int	EMF_BOUNDS_OFFSET				= 0;
//	private static final int	EMF_GRAPHICS_MODE_OFFSET		= 16;
//	private static final int	EMF_X_SCALE_OFFSET				= 20;
//	private static final int	EMF_Y_SCALE_OFFSET				= 24;
	private static final int	EMF_LOG_REF_POINT_OFFSET		= 28;
	private static final int	EMF_CHAR_COUNT_OFFSET			= 36;
	private static final int	EMF_STRING_OFFSET_OFFSET		= 40;
	private static final int	EMF_OPTIONS_OFFSET				= 44;
	private static final int	EMF_CLIP_RECT_OFFSET			= 48;
//	private static final int	EMF_SPACE_ARRAY_OFFSET_OFFSET	= 64;
	
	private static final int	WMF_X_OFFSET					= 2;
	private static final int	WMF_Y_OFFSET					= 0;
	private static final int	WMF_COUNT_OFFSET				= 4;
	private static final int	WMF_FLAG_OFFSET					= 6;
	private static final int	WMF_CLIPRECT_OFFSET				= 8;
	
//	private static final int	GM_COMPATIBLE			= 1;
//	private static final int	GM_ADVANCED				= 2;
	
	
	private static final int	ETO_OPAQUE				= 2;
	private static final int	ETO_CLIPPED				= 4;
	
	private boolean 	m_bWideCharVersion 	= true;
	private Point		m_refPoint			= null;
	private	int			m_options			= 0;
	private Rectangle	m_clipRect			= null;
	private String		m_text				= null;
	
	public ExtTextOut( boolean bWideCharVersion )
	{
		m_bWideCharVersion = bWideCharVersion;
	}
	
	public void readWMFRecord( Record rec ) throws IOException
	{
		int x 		= rec.getShortAt( WMF_X_OFFSET );
		int y 		= rec.getShortAt( WMF_Y_OFFSET );
		int count	= rec.getShortAt( WMF_COUNT_OFFSET );
		int flag	= rec.getShortAt( WMF_FLAG_OFFSET );
		
		m_options = flag;
		m_refPoint = new Point( x, y );
		
		int offset = WMF_CLIPRECT_OFFSET;
		if( ( flag & ETO_CLIPPED ) == ETO_CLIPPED )
		{
			// Read in the clipping rectangle.  Only exists in the record if ETO_CLIPPED is set.
			m_clipRect = rec.getRectangeAt( WMF_CLIPRECT_OFFSET );
			offset += 8; // Add 8 for the size of the cliprect. 
		}
		else
		{
			m_clipRect = new Rectangle();
		}
		
		m_text = new String( rec.getBytesAt( offset, count ) );			
	}

	public void readEMFRecord( Record rec ) throws IOException
	{
		m_refPoint		= rec.getPointLAt( EMF_LOG_REF_POINT_OFFSET );
		m_options		= rec.getIntAt( EMF_OPTIONS_OFFSET );
		m_clipRect		= rec.getRectangleLAt( EMF_CLIP_RECT_OFFSET );
		
		int count 		= rec.getIntAt( EMF_CHAR_COUNT_OFFSET );
		int offset		= rec.getIntAt( EMF_STRING_OFFSET_OFFSET ) - 8;	// Subtract off the length of the header.
		//int spaceOffset = rec.getIntAt( EMF_SPACE_ARRAY_OFFSET_OFFSET ) - 8;  // Subtract off the length of the header.
		
		// Read in the string.
		if( m_bWideCharVersion )
		{	
			m_text = new String( rec.getCharsAt( offset, count ) );
		}
		else
		{
			m_text = new String( rec.getBytesAt( offset, count ) );			
		}
		
		// currently unused.
		//m_intercharSpacing = new int[ count ];
		
		//for( int index = 0; index < count; index++ )
		//{
		//	m_intercharSpacing[ index ] = rec.getIntAt( spaceOffset + ( index * 4 ) );
		//}
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		// Select the font into the graphics context.
		Font font = context.getCurFont().getFont();
		g.setFont( font );
		
		// If the drawing mode is opaque, fill the clipRect with the current background color, scaling the rect with the
		// current mapping mode.
		Rectangle 	convertedClipRect 	= context.convertRectangleToSVGLogicalUnits( m_clipRect );
		Point 		convertedPos 		= context.convertPointToSVGLogicalUnits( m_refPoint );
		
		// Use the text alignment (GetTextAlign method in windows) to adjust the position.
		int textAlignment = context.getTextAlignment();
		
		if( ( textAlignment & DeviceContext.TA_BASELINE ) == DeviceContext.TA_BASELINE )
		{
			// No action taken
		}
		else if( ( textAlignment & DeviceContext.TA_BOTTOM ) == DeviceContext.TA_BOTTOM )
		{
			// No Action taken.  
			// This is not the same as the 'baseline' though, probably need to
			// subtract off the difference between the baseline and the height of the font. 
		}
		else
		{
			// Add the height of the font since the value specified in the EMF file is to top of the font, not the baseline.
			convertedPos.y += font.getSize();
		}
		
		if( ( textAlignment & DeviceContext.TA_CENTER ) == DeviceContext.TA_CENTER )
		{
			// Get the extent of the text and subtract half of it from the x position.
            FontRenderContext fontRenderContext = g.getFontRenderContext();
            Rectangle2D rect = font.getStringBounds( m_text, fontRenderContext );

            convertedPos.x -= (int) rect.getWidth() / 2.0;
		}
		else if( ( textAlignment & DeviceContext.TA_RIGHT ) == DeviceContext.TA_RIGHT )
		{
			// Get the extent of the text and subtract it from the x position.
            FontRenderContext fontRenderContext = g.getFontRenderContext();
            Rectangle2D rect = font.getStringBounds( m_text, fontRenderContext );

            convertedPos.x -= (int) rect.getWidth();
		}
		else
		{
			// Must be TA_LEFT (0), which is the same as what Java uses so take no action.
		}
		
		// Note: Even if the text is rotated, the clipping and 'opaque' rectangles are NOT rotated.
		if( (m_options & ETO_OPAQUE) == ETO_OPAQUE )
		{
			g.setColor( context.getBackColor() );
			
			g.fillRect( convertedClipRect.x, convertedClipRect.y, convertedClipRect.width, convertedClipRect.height );			
		}
		
		// If the drawing mode is clipped, set a clipping rectangle for the text.
		Shape clip = null;
		
		if( (m_options & ETO_CLIPPED) == ETO_CLIPPED )
		{
			clip = g.getClip();

			// Append to the current clipping region.
			g.clip( convertedClipRect );
		}
		
		// Set the color to the current text color;
		g.setColor( context.getTextColor() );
		
		// If the text is angled, set an AffineTransform to rotate the text.
		//
		// Escapement is the angle (in 10ths of a degree) between the x axis and the baseline of the text.
		// Orientation is the the angle (in 10ths of a degree) between the x-axis and the baseline of a character.
		// If the mode is GM_ADVANCED, these can be different, so each character can be angled differently from the angle at which
		// the line of text is drawn.  In GM_COMPATIBLE mode only escapement is used.  Currently we only support escapement, not
		// character orientation.  I doubt that is used much in stereotype images...
		int escapement = context.getCurFont().getEscapement();
		
		if( escapement != 0 )
		{
            FontRenderContext fontRenderContext = g.getFontRenderContext();

            TextLayout	layout 	= new TextLayout( m_text, font, fontRenderContext );
            float 		height	= (float)layout.getBounds().getHeight();

            AffineTransform rotatedText = new AffineTransform();
            rotatedText.translate( convertedPos.x, convertedPos.y );
            rotatedText.rotate( Math.toRadians( 360 - (escapement / 10 ) ) );
            rotatedText.translate( 0, height );

            Shape shape = layout.getOutline( rotatedText );

            GeneralPath generalPath	= new GeneralPath( GeneralPath.WIND_NON_ZERO );
            AffineTransform transform = new AffineTransform();
            generalPath.append( transform.createTransformedShape( shape ), false );
            g.draw( shape );
		}
		else
		{
			g.drawString( m_text, convertedPos.x, convertedPos.y );
		}
		
		// Remove the transform.		

		// Reset the clip.
		if( (m_options & ETO_CLIPPED) == ETO_CLIPPED )
		{
			g.setClip( clip );
		}
	}
}
