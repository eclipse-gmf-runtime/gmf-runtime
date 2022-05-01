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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Represents the current state of the device context.  As metafile records are parsed, the data in this
 * class is updated.  This class also handles all unit conversions.  
 * 
 * @author dhabib
 */
public final class DeviceContext 
{
	/** Static list of device contexts.  Normally there will only be one (the current one, the first
		one in the vector), but the user can call 'saveDC' and 'restoreDC' to manipulate these. */
	private Vector					m_deviceContexts 	= new Vector();
	/** Current device context.  Never changes from the very first device context that was created. */
	private DeviceContext			m_curDeviceContext	= this;

	/** Specifies the dimensions, in device units, of the smallest rectangle that can be drawn around the picture stored in the metafile. This rectangle is supplied by graphics device interface (GDI). Its dimensions include the right and bottom edges. */
	private Rectangle	m_bounds		= new Rectangle( 0, 0, 100, 100 );
	/** Dots per inch in the X direction */
	private int			m_dpiX			= DPI;
	/** Dots per inch in the Y direction */
	private int			m_dpiY			= DPI;

	/** Current cursor position in the X direction */
	private int			m_curPosX 		= 0;
	/** Current cursor position in the Y direction */
	private int			m_curPosY 		= 0;

	/** Viewport origin */
	private Point		m_vpOrigin		= new Point( 0, 0 );
	/** Viewport extents */
	private Point		m_vpExtent		= new Point( 1000, 1000 );
	/** Window Origin */
	private Point		m_winOrigin		= new Point( 0, 0 );
	/** Window Extents */
	private Point		m_winExtent		= new Point( 1000, 1000 );
	/** Current mapping mode */
	private int			m_mapMode		= MM_TEXT;

	/** Alignment for text (ExtTextOut) */
	private int			m_textAlignment	= DeviceContext.TA_LEFT | DeviceContext.TA_TOP;
	/** Current fill mode */
	private int			m_fillMode		= FILL_MODE_ALTERNATE;
	/** Direction in which to draw arcs */
	private int			m_arcDirection	= AD_COUNTERCLOCKWISE;
	/** Current miter limit */
	private float		m_miterLimit	= 10.0f;
	/** Current background mode */
	private int			m_bkMode		= TRANSPARENT;
	/** Current StretchBltMode.  Possibilities are BLACKONWHITE, WHITEONBLACK, COLORONCOLOR, HALFTONE, MAXSTRETCHBLTMODE
	 *  Not currently used, just stored.
	 */
	private int			m_stretchBltMode = COLORONCOLOR;
	/** Current ROP mode.  Not currently used */
	private int			m_ROP2			 = R2_COPYPEN;
	
	
	/** Current background color */
	private Color 		m_backColor		= new Color( 0xffffff );
	/** Current text (foreground) color */
	private Color		m_textColor		= new Color( 0 );

	/** Object representing the currently selected font */
	private GdiFont		m_curFont		= new GdiFont();
	/** Object representing the currently selected brush */
	private GdiBrush	m_curBrush		= new GdiBrush();
	/** Object representing the currently selected pen */
	private GdiPen		m_curPen		= new GdiPen();
	/** Object representing the currently selected clipping region */
	private GdiRegion	m_curRegion		= new GdiRegion( (Shape) null );
	/** Set of GDI objects allocated by the user. */
	private Map			m_gdiObjectMap	= new HashMap();
	/** Extensibility mechanism so derived classes can store values.  */
	private Map			m_userProps		= new HashMap();
	
	/** Current fill/stroke path */	
	private GdiPath		m_path			= new GdiPath( this );

	/** Current world transform */
	private float[]		m_xform			= new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};


 	// Constants for indices in a transform
	private static final int _11 = 0;	// Scale X
	private static final int _12 = 1;	// Rotation not yet supported
	private static final int _21 = 2;	// Rotation not yet supported
	private static final int _22 = 3;	// Scale Y
	private static final int _31 = 4;	// Translate X
	private static final int _32 = 5;	// Translate Y

	// ModifyWorldTransform operations
	public static final int MWT_IDENTITY      = 1;
	public static final int MWT_LEFTMULTIPLY  = 2;
	public static final int MWT_RIGHTMULTIPLY = 3;

	// Contants for mapping mode.
	private static final int MM_TEXT 		= 1;
	private static final int MM_LOMETRIC 	= 2;
	private static final int MM_HIMETRIC 	= 3;
	private static final int MM_LOENGLISH 	= 4;
	private static final int MM_HIENGLISH 	= 5;
	private static final int MM_TWIPS 		= 6;
	private static final int MM_ISOTROPIC 	= 7;
	private static final int MM_ANISOTROPIC	= 8;
	
	// Constants for mapping units.
	private static final int DPI					= 72;
	private static final int HI_METRIC_PER_INCH		= 2540; 
	private static final int LO_METRIC_PER_INCH		= 254;
 	private static final int HI_ENGLISH_PER_INCH	= 1000; 
 	private static final int LO_ENGLISH_PER_INCH	= 100; 
 	private static final int TWIPS_PER_INCH			= 1440; 

 	// Constants for Stretch Blt Mode
// 	private static final int BLACKONWHITE			= 1;
//	private static final int WHITEONBLACK			= 2;
 	private static final int COLORONCOLOR			= 3;
// 	private static final int HALFTONE				= 4;

 	// Constants for the ROP mode
//	private static final int R2_BLACK            = 1;
//	private static final int R2_NOTMERGEPEN      = 2;
//	private static final int R2_MASKNOTPEN       = 3;
//	private static final int R2_NOTCOPYPEN       = 4;
//	private static final int R2_MASKPENNOT       = 5;
//	private static final int R2_NOT              = 6;
//	private static final int R2_XORPEN           = 7;
//	private static final int R2_NOTMASKPEN       = 8;
//	private static final int R2_MASKPEN          = 9;
//	private static final int R2_NOTXORPEN        = 10;
//	private static final int R2_NOP              = 11;
//	private static final int R2_MERGENOTPEN      = 12;
	private static final int R2_COPYPEN          = 13;
//	private static final int R2_MERGEPENNOT      = 14;
//	private static final int R2_MERGEPEN         = 15;
//	private static final int R2_WHITE            = 16;
//	private static final int SRCCOPY             = 0x00CC0020;
//	private static final int SRCPAINT            = 0x00EE0086;
//	private static final int SRCAND              = 0x008800C6;
//	private static final int SRCINVERT           = 0x00660046;
//	private static final int SRCERASE            = 0x00440328;
//	private static final int NOTSRCCOPY          = 0x00330008;
//	private static final int NOTSRCERASE         = 0x001100A6;
//	private static final int MERGECOPY           = 0x00C000CA;
//	private static final int MERGEPAINT          = 0x00BB0226;
//	private static final int PATCOPY             = 0x00F00021;
//	private static final int PATPAINT            = 0x00FB0A09;
//	private static final int PATINVERT           = 0x005A0049;
//	private static final int DSTINVERT           = 0x00550009;
//	private static final int BLACKNESS           = 0x00000042;
//	private static final int WHITENESS           = 0x00FF0062;
 	
 	/** Palette usage constant */
	public static final int		DIB_RGB_COLORS		= 0;
 	/** Palette usage constant */
	public static final int		DIB_PAL_COLORS		= 1;

	/** Background mode constant */
	public static final int TRANSPARENT				= 1;
 	/** Background mode constant */
 	public static final int OPAQUE					= 2;
 	
 	/** Poly fill mode constant */
 	public static final int FILL_MODE_ALTERNATE		= 1;
 	/** Poly fill mode constant */
 	public static final int FILL_MODE_WINDING		= 2;

 	/** Arc Direction constant */
 	public static final int AD_COUNTERCLOCKWISE		= 1;
 	/** Arc Direction constant */
 	public static final int AD_CLOCKWISE			= 2;

	/** Text alignment constant */
	public static final int TA_TOP	                  	= 0;
	/** Text alignment constant */
	public static final int TA_LEFT						= 0;
	/** Text alignment constant */
	public static final int TA_UPDATECP                 = 1;
	/** Text alignment constant */
	public static final int TA_RIGHT                    = 2;
	/** Text alignment constant */
	public static final int TA_CENTER                   = 6;
	/** Text alignment constant */
	public static final int TA_BOTTOM                   = 8;
	/** Text alignment constant */
	public static final int TA_BASELINE                 = 24;
	/** Text alignment constant */
	public static final int TA_RTLREADING               = 256;
	
	/**
	 * Initializes a new instance of the class and sets all the variables to their defaults.
	 */
	public DeviceContext()
	{
	    // No initialization required
	}
	
	/**
	 * Copy constructor.
	 * @param context
	 */
	private DeviceContext( DeviceContext context )
	{
		copy( context );
	}
	
	/**
	 * Copies the contents of the specified device context.
	 * @param context
	 */
	private void copy( DeviceContext context )
	{
		m_bounds		= new Rectangle( context.m_bounds );

		m_dpiX			= context.m_dpiX;
		m_dpiY			= context.m_dpiY;

		m_curPosX 		= context.m_curPosX;
		m_curPosY 		= context.m_curPosY;

		m_vpOrigin		= new Point( context.m_vpOrigin );
		m_vpExtent		= new Point( context.m_vpExtent );
		m_winOrigin		= new Point( context.m_winOrigin );
		m_winExtent		= new Point( context.m_winExtent );
		m_mapMode		= context.m_mapMode;

		m_textAlignment	= context.m_textAlignment;
		m_fillMode		= context.m_fillMode;
		m_arcDirection	= context.m_arcDirection;
		m_miterLimit	= context.m_miterLimit;
		m_bkMode		= context.m_bkMode;
	
		m_backColor		= new Color( context.m_backColor.getRGB() );
		m_textColor		= new Color( context.m_textColor.getRGB() );

		m_curFont		= new GdiFont( context.m_curFont );
		m_curBrush		= new GdiBrush( context.m_curBrush );
		m_curPen		= new GdiPen( context.m_curPen );
		m_curRegion		= new GdiRegion( context.m_curRegion );
		m_path			= new GdiPath( this, context.m_path );
	}
	
	/**
	 * Saves the current device context settings and pushes them on a stack.
	 * Can be restored by calling 'RestoreDC'
	 */
	public void saveDC()
	{
		// put a copy of this DC at the end of the device contexts vector.
		m_deviceContexts.add( new DeviceContext( this ) );
	}
	
	/**
	 * Restores a DC that was previously saved with a call to 'saveDC'
	 */
	public void restoreDC( int dcIndex )
	{
		// Figure out what the index into the vector really is.
		int realIndex = 0;
		if( dcIndex < 0 )
		{
			realIndex = -dcIndex - 1;
		}
		else
		{
			realIndex = m_deviceContexts.size() - dcIndex;
		}

		// Retrieve and restore the specified DeviceContext
		if( realIndex < m_deviceContexts.size() )
		{
			DeviceContext context = (DeviceContext) m_deviceContexts.get( realIndex );
			m_curDeviceContext.copy( context );
			
			// Remove all the device contexts from 0 to realIndex
			for( int index = 0; index <= realIndex; index++ )
			{
				m_deviceContexts.remove( 0 );
			}
		}
	}

	/**
	 * Sets the current cursor position in the horizontal direction.
	 * This is always in window units, not converted to device units.
 	 * @param x
 	 */
 	public void setCurPosX( int x )
	{
		m_curPosX = x;
	}

 	/**
	 * Gets the current cursor position in the horizontal direction.
	 * This is always in window units, not converted to device units.
 	 * @return Current cursor position.
 	 */
	public int getCurPosX()
	{
		return m_curPosX;
	}

 	/**
	 * Sets the current cursor position in the vertical direction.
	 * This is always in window units, not converted to device units.
 	 * @param x
 	 */
	public void setCurPosY( int y )
	{
		m_curPosY = y;
	}

 	/**
	 * Gets the current cursor position in the vertical direction.
	 * This is always in window units, not converted to device units.
 	 * @return Current cursor position.
 	 */
	public int getCurPosY()
	{
		return m_curPosY;
	}

	/**
	 * Sets the viewport origin.
	 * @param origin
	 */
	public void setViewportOrigin( Point origin )
	{
		m_vpOrigin = new Point( origin );
	}

	/**
	 * Retrieves the viewport origin.
	 * @return
	 */
	public Point getViewportOrigin()
	{
		return new Point( m_vpOrigin );
	}

	/**
	 * Sets the viewport extents.
	 * @param ext
	 */
	public void setViewportExtent( Point ext )
	{
		m_vpExtent = new Point( ext );
	}

	/**
	 * @return the viewport extents.
	 */
	public Point getViewportExtent()
	{
		return new Point( m_vpExtent );
	}

	/**
	 * Sets the window origin.
	 * @param origin
	 */
	public void setWindowOrigin( Point origin )
	{
		m_winOrigin = new Point( origin );
	}

	/**
	 * @return The current window origin.
	 */
	public Point getWindowOrigin()
	{
		return new Point( m_winOrigin );
	}

	/**
	 * Sets the window extent.
	 * @param extent
	 */
	public void setWindowExtent( Point extent )
	{
		m_winExtent = new Point( extent );
	}

	/**
	 * @return The current window extent.
	 */
	public Point getWindowExtent()
	{
		return new Point( m_winExtent );
	}
	
	/**
	 * Sets the current mapping mode (MM_TEXT, MM_ISOTROPIC, etc).
	 * @param mode
	 */
	public void setMapMode( int mode )
	{
		m_mapMode = mode;
	}
	
	/**
	 * @return The current mapping mode.
	 */
	public int getMapMode()
	{
		return m_mapMode;
	}

	/**
	 * Apply changes to the world transform. Note that support for rotation
	 * isn't actually implemented...
	 * 
	 * @param operation
	 *            One of these:
	 * <pre>
	 *   MWT_IDENTITY
	 *      The transform will be reset to identity. The given transform is ignored.
	 * </pre>
	 * <pre>
	 *   MWT_LEFTMULTIPLY
	 *      new = given X current
	 * </pre>
	 * <pre>
	 *   MWT_RIGHTMULTIPLY
	 *      new = current X given
	 * </pre>
	 * @param transform
	 *            Given transform. See parameter operation for reference.
	 */
	public void modifyWorldTransform(int operation, float[] transform) {
		switch (operation) {
			case MWT_IDENTITY: {
				m_xform = new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
				break;
			}
			case MWT_LEFTMULTIPLY: {
				m_xform = crossMultiply(transform, m_xform);
				break;
			}
			case MWT_RIGHTMULTIPLY: {
				m_xform = crossMultiply(m_xform, transform);
				break;
			}
		}
	}

	/**
	 * Each matrix M has this format, expressed as a float array of length 6:
	 * <pre>
	 *     M[0]  M[1]  0
	 *     M[2]  M[3]  0
	 *     M[4]  M[5]  1
	 * </pre>
	 */
	private float[] crossMultiply(float[] L, float[] R) {
		float[] P = new float[6];
		P[_11]  =  L[_11] * R[_11]  +  L[_12] * R[_21]; // + 0*R[3,1]
		P[_21]  =  L[_21] * R[_11]  +  L[_22] * R[_21]; // + 0*R[3,1]
		P[_31]  =  L[_31] * R[_11]  +  L[_32] * R[_21]  +  1 * R[_31];
		P[_12]  =  L[_11] * R[_12]  +  L[_12] * R[_22]; // + 0*R[3,2]
		P[_22]  =  L[_21] * R[_12]  +  L[_22] * R[_22]; // + 0*R[3,2]
		P[_32]  =  L[_31] * R[_12]  +  L[_32] * R[_22]  +  1 * R[_32];
		return P;
	}

	/**
	 * Opposite of convertXToSVGLogicalUnits, converts a coordinate in the SVG coordinate system back to the
	 * windows coordinate system. 
	 * @param x
	 * @return The converted coordinate.
	 */
	public int convertXToWindowsLogicalUnits( int x )
	{
		x += m_bounds.x;
		x -= m_vpOrigin.x;

		switch( m_mapMode )
		{
			case MM_TEXT:
				// Maps logical units to device units, 1 logical unit = 1 pixel.
				break;
				
			case MM_LOENGLISH:
				x = (x * LO_ENGLISH_PER_INCH) / m_dpiX;
				break;
			
			case MM_HIENGLISH:
				x = (x * HI_ENGLISH_PER_INCH) / m_dpiX;
				break;
			
			case MM_LOMETRIC:
				x = (x * LO_METRIC_PER_INCH) / m_dpiX;
				break;
				
			case MM_HIMETRIC:  
				x = (x * HI_METRIC_PER_INCH) / m_dpiX;
				break;
			
			case MM_TWIPS:
				x = (x * TWIPS_PER_INCH) / m_dpiX;
				break;
			
			case MM_ANISOTROPIC:
				x = (x * m_winExtent.x) / m_vpExtent.x;
				break;
				 
			case MM_ISOTROPIC:
				x = x * m_winExtent.x / m_vpExtent.x;
				break;
		}

		float f = x / m_xform[_11];
		return (int) f + m_winOrigin.x;
	}

	/**
	 * Converts the specified window coordinate to the SVG coordinate system.  Uses the current mapping mode,
	 * the window origin, and the window and viewport extents.
	 * @param x
	 * @return The converted coordinate.
	 */
	public int convertXToSVGLogicalUnits( int x )
	{
		// SVG's SVGGraphics object uses AWT User Space logical units, which appears to be about 72 dpi according to the documentation.
		// So, depending upon the current mapping mode, convert the specified coordinate to logical units.
		
		// Subtract off the origin.  Only care about the window extents, not the VP extents at this point.
		x -= m_winOrigin.x;

		x = scaleX( x );
		x += (int) m_xform[_31];
		x += m_vpOrigin.x;
		x -= m_bounds.x;
		
		return x;
	}
	
	/**
	 * Scales the specified cooridinate using the current mapping mode (etc).  Does not use the window origin
	 * or bounding rectangle (does no translation, just scales the coordinate).
	 * @param x
	 * @return  The scaled coordinate.
	 */
	public int scaleX( int x )
	{		
		switch( m_mapMode )
		{
			case MM_TEXT:
				// Maps logical units to device units, 1 logical unit = 1 pixel.
				break;
				
			case MM_LOENGLISH:
				// x is in 100 units/inch
				x = (x * m_dpiX) / LO_ENGLISH_PER_INCH;
				break;
			
			case MM_HIENGLISH:
				// x is in 1000 units/inch
				x = (x * m_dpiX) / HI_ENGLISH_PER_INCH;
				break;
			
			case MM_LOMETRIC:
				// x is in 100 units/mm
				x = (x * m_dpiX) / LO_METRIC_PER_INCH;
				break;
				
			case MM_HIMETRIC:  
				// x is in 1000 units/mm
				x = (x * m_dpiX) / HI_METRIC_PER_INCH;
				break;
			
			case MM_TWIPS:
				// x is in 1440 units/inch
				x = (x * m_dpiX) / TWIPS_PER_INCH;
				break;
			
			case MM_ANISOTROPIC:
				// Logical units are mapped to arbitrary units with arbitrarily scaled axes. Use the SetWindowExtEx and 
				// SetViewportExtEx functions to specify the units, orientation, and scaling.
				
				// Convert to device units.
				x = (x * m_vpExtent.x) / m_winExtent.x;
				break;
				 
			case MM_ISOTROPIC:
				// Logical units are mapped to arbitrary units with equally scaled axes; that is, one unit along the 
				// x-axis is equal to one unit along the y-axis. Use the SetWindowExtEx and SetViewportExtEx functions 
				// to specify the units and the orientation of the axes. Graphics device interface (GDI) makes adjustments 
				// as necessary to ensure the x and y units remain the same size (When the window extent is set, the viewport 
				// will be adjusted to keep the units isotropic).

				// Convert to device units.
				x = x * m_vpExtent.x / m_winExtent.x;

				break;
		}
		float f = x * m_xform[_11];
		return (int) f;
	}
	
	/**
	 * Opposite of convertYToSVGLogicalUnits, converts a coordinate in the SVG coordinate system back to the
	 * windows coordinate system. 
	 * @param y
	 * @return
	 */
	public int convertYToWindowsLogicalUnits( int y )
	{
		y += m_bounds.y;
		y -= m_vpOrigin.y;

		switch( m_mapMode )
		{
			case MM_TEXT:
				// Maps logical units to device units, 1 logical unit = 1 pixel.
				break;
				
			case MM_LOENGLISH:
				y = (y * LO_ENGLISH_PER_INCH) / m_dpiY;
				break;
			
			case MM_HIENGLISH:
				y = (y * HI_ENGLISH_PER_INCH) / m_dpiY;
				break;
			
			case MM_LOMETRIC:
				y = (y * LO_METRIC_PER_INCH) / m_dpiY;
				break;
				
			case MM_HIMETRIC:  
				y = (y * HI_METRIC_PER_INCH) / m_dpiY;
				break;
			
			case MM_TWIPS:
				y = (y * TWIPS_PER_INCH) / m_dpiY;
				break;
			
			case MM_ANISOTROPIC:
				y = (y * m_winExtent.y) / m_vpExtent.y;
				break;
				 
			case MM_ISOTROPIC:
				y = y * m_winExtent.x / m_vpExtent.x;
				break;
		}

		float f = y / m_xform[_22];
		return (int) f +  m_winOrigin.y;
	}

	/**
	 * Converts the specified window coordinate to the SVG coordinate system.  Uses the current mapping mode,
	 * the window origin, and the window and viewport extents.
	 * @param y
	 * @return The converted coordinate.
	 */
	public int convertYToSVGLogicalUnits( int y )
	{
		// Subtract off the origin.  Only care about the window extents, not the VP extents at this point.
		y -= m_winOrigin.y;

		y = scaleY( y );
		y += (int) m_xform[_32];
		y += m_vpOrigin.y;
		y -= m_bounds.y;
		
		return y;
	}

	/**
	 * Scales the specified cooridinate using the current mapping mode (etc).  Does not use the window origin
	 * or bounding rectangle (does no translation, just scales the coordinate).
	 * @param y
	 * @return  The scaled coordinate.
	 */
	public int scaleY( int y )
	{
		switch( m_mapMode )
		{
			case MM_TEXT:
				// Maps logical units to device units, 1 logical unit = 1 pixel.
				break;

			case MM_LOENGLISH:
				// y is in 100 units/inch
				y = -(y * m_dpiY) / LO_ENGLISH_PER_INCH;
				break;
			
			case MM_HIENGLISH:
				// y is in 1000 units/inch
				y = -(y * m_dpiY) / HI_ENGLISH_PER_INCH;
				break;
			
			case MM_LOMETRIC:
				// y is in 100 units/mm
				y = -(y * m_dpiY) / LO_METRIC_PER_INCH;
				break;
				
			case MM_HIMETRIC:  
				// y is in 1000 units/mm
				y = -(y * m_dpiY) / HI_METRIC_PER_INCH;
				break;
			
			case MM_TWIPS:
				// y is in 1440 units/inch
				y = -(y * m_dpiY) / TWIPS_PER_INCH;
				break;
				
			case MM_ANISOTROPIC:
				// Logical units are mapped to arbitrary units with arbitrarily scaled axes. Use the SetWindowExtEx and 
				// SetViewportExtEx functions to specify the units, orientation, and scaling.

				// Convert to device units.
				y = y * m_vpExtent.y / m_winExtent.y;
				break;
				
			case MM_ISOTROPIC:
				// Logical units are mapped to arbitrary units with equally scaled axes; that is, one unit along the 
				// x-axis is equal to one unit along the y-axis. Use the SetWindowExtEx and SetViewportExtEx functions 
				// to specify the units and the orientation of the axes. Graphics device interface (GDI) makes adjustments 
				// as necessary to ensure the x and y units remain the same size (When the window extent is set, the viewport 
				// will be adjusted to keep the units isotropic).

				// Convert to device units.
				y = y * m_vpExtent.x / m_winExtent.x;
				break;
		}

		float f = y * m_xform[_22];
		return (int) f;
	}

	/**
	 * Converts an array of Windows x-coordinates into SVG coordinates. 
	 * @param array
	 * @return Array of converted coordinates.
	 */
	public int[] convertXArrayToSVGLogicalUnits( int[] array )
	{
		int[] out = new int[ array.length ];
		
		for( int index = 0; index < array.length; index++ )
		{
			out[ index ] = convertXToSVGLogicalUnits( array[ index ] );
		}
		
		return out;
	}
	
	/**
	 * Converts an array of Windows y-coordinates into SVG coordinates. 
	 * @param array
	 * @return Array of converted coordinates.
	 */
	public int[] convertYArrayToSVGLogicalUnits( int[] array )
	{
		int[] out = new int[ array.length ];
		
		for( int index = 0; index < array.length; index++ )
		{
			out[ index ] = convertYToSVGLogicalUnits( array[ index ] );
		}
		
		return out;
	}

	/**
	 * Converts a rectange from windows coordinates to SVG coordinates.
	 * @param rect
	 * @return Converted rectangle.
	 */
	public Rectangle convertRectangleToSVGLogicalUnits( Rectangle rect )
	{
		Rectangle converted = new Rectangle( rect );
		
		converted.x 		= convertXToSVGLogicalUnits( converted.x );
		converted.width 	= scaleX( converted.width );
		converted.y			= convertYToSVGLogicalUnits( converted.y );
		converted.height	= scaleY( converted.height );
		
		return converted;		
	}

	/**
	 * Converts a dimension from windows coordinates to SVG coordinates.
	 * @param dim
	 * @return The converted dimension
	 */
	public Dimension convertDimensionToSVGLogicalUnits( Dimension dim )
	{
		Dimension converted = new Dimension( dim );
		
		converted.width 		= scaleX( converted.width );
		converted.height		= scaleY( converted.height );
		
		return converted;		
	}

	/**
	 * Converts a point from windows coordinates to SVG coordinates.
	 * @param pt
	 * @return The converted point.
	 */
	public Point convertPointToSVGLogicalUnits( Point pt )
	{
		Point converted = new Point( pt );
		
		converted.x		= convertXToSVGLogicalUnits( converted.x );
		converted.y		= convertYToSVGLogicalUnits( converted.y );
		
		return converted;		
	}

	/**
	 * Sets the current background color.
	 * @param color
	 */
	public void setBackColor( Color color ) 
	{
		m_backColor = color;
	}

	/**
	 * @return The current background color.
	 */
	public Color getBackColor() 
	{
		return m_backColor;
	}

	/**
	 * Sets the current text color.
	 * @param color
	 */
	public void setTextColor( Color color ) 
	{
		m_textColor = color;
	}

	/**
	 * @return The current text color.
	 */
	public Color getTextColor() 
	{
		return m_textColor;
	}

	/**
	 * @return Retrieves the currently selected font.
	 */
	public GdiFont getCurFont() 
	{
		return m_curFont;
	}

	/**
	 * Sets the currently selected font.
	 * @param font
	 */
	public void setCurFont( GdiFont font ) 
	{
		m_curFont = font;
	}

	/**
	 * @return The currently selected brush.
	 */
	public GdiBrush getCurBrush() 
	{
		return m_curBrush;
	}

	/**
	 * Sets the currently selected brush.
	 * @param brush
	 */
	public void setCurBrush( GdiBrush brush ) 
	{
		m_curBrush = brush;
	}

	/**
	 * @return The currently selected pen.
	 */
	public GdiPen getCurPen() 
	{
		return m_curPen;
	}

	/**
	 * Sets the current pen.
	 * @param pen
	 */
	public void setCurPen( GdiPen pen ) 
	{
		m_curPen = pen;
	}

	/**
	 * Retreives the next object id for use with WMF created objects.
	 */ 
	public int getNextObjectId()
	{
		// Find the next unique key.  This appears to be what the WMF files expect.
		int objId = 0;

		while( m_curDeviceContext.m_gdiObjectMap.containsKey( Integer.valueOf( objId ) ) )
		{
			objId++;
		}

		return objId;
	}
	
	/**
	 * Adds a new Gdi object to the set of objects that the user has allocated.
	 * @param id - Identifier used to find this object.
	 * @param gdiObject - object to add.
	 */
	public void addGDI( int id, Object gdiObject )
	{
		m_gdiObjectMap.put( Integer.valueOf( id ), gdiObject );
	}

	/**
	 * Retrieves a GDI object based on it's id.
	 * @param id  Identifier of the object to retrieve
	 * @return  Gdi object associated with the specified identifier.
	 */
	public Object getGDI( int id )
	{
		return m_gdiObjectMap.get( Integer.valueOf( id ) );
	}
	
	/**
	 * Removes the specified Gdi object.
	 * @param id ID of the object to remove.
	 */
	public void deleteGDI( int id )
	{
		m_gdiObjectMap.remove( Integer.valueOf( id ) );
	}

	/**
	 * @return the dimensions, in device units, of the smallest rectangle that can be drawn around the picture stored in the metafile.  
	 */
	public Rectangle getBounds()
	{
		return new Rectangle( m_bounds );
	}
	
	/**
	 * Sets the bounds of the metafile.  Set when the metafile header is parsed.
	 * @param rect
	 */
	public void setBounds( Rectangle rect )
	{
		m_bounds = rect;
	}

	/**
	 * @return The number of dots per inch in the horizontal direction.
	 */
	public int getDpiX() 
	{
		return m_dpiX;
	}

	/**
	 * Sets the number of dots per inch of the original display device.  This is set when the header record is parsed
	 * and is used for unit conversion.
	 * @param i
	 */
	public void setDpiX( int dpi ) 
	{
		if( dpi >= 60 )
		{
			m_dpiX = dpi;
		}
	}

	/**
	 * @return The number of dots per inch in the vertical direction.
	 */
	public int getDpiY() 
	{
		return m_dpiY;
	}

	/**
	 * Sets the number of dots per inch of the original display device.  This is set when the header record is parsed
	 * and is used for unit conversion.
	 * @param i
	 */
	public void setDpiY( int dpi ) 
	{
		if( dpi >= 60 )
		{
			m_dpiY = dpi;
		}
	}

	/**
	 * @return The current text alignment.
	 */
	public int getTextAlignment() 
	{
		return m_textAlignment;
	}

	/**
	 * Sets the current text alignment constant (TA_TOP, etc).
	 * @param i
	 */
	public void setTextAlignment( int i ) 
	{
		m_textAlignment = i;
	}

	/**
	 * @return Returns the fillMode.
	 */
	public int getPolyFillMode()
	{
		return m_fillMode;
	}

	/**
	 * Sets the current polygon fill mode (FILL_MODE_ALTERNATE, FILL_MODE_WINDING)
	 * @param fillMode The fillMode to set.
	 */
	public void setPolyFillMode( int fillMode )
	{
		m_fillMode = fillMode;
	}

	/**
	 * @return Returns the arcDirection.
	 */
	public int getArcDirection()
	{
		return m_arcDirection;
	}

	/**
	 * @param arcDirection The arcDirection to set.
	 */
	public void setArcDirection( int arcDirection )
	{
		m_arcDirection = arcDirection;
	}
	
	/**
	 * @return The current GdiPath object.
	 */
	public GdiPath getGdiPath()
	{
		return m_path;
	}

	/**
	 * @return Returns the miterLimit.
	 */
	public float getMiterLimit()
	{
		return m_miterLimit;
	}

	/**
	 * @param miterLimit The miterLimit to set.
	 */
	public void setMiterLimit( float miterLimit )
	{
		if( miterLimit < 1.0f )
		{
			miterLimit = 1.0f;
		}
		m_miterLimit = miterLimit;
	}

	/**
	 * @return Returns the current background mode (OPAQUE, TRANSPARENT).
	 */
	public int getBkMode()
	{
		return m_bkMode;
	}

	/**
	 * Sets the current background mode (OPAQUE, TRANSPARENT)
	 * @param bkMode The bkMode to set.
	 */
	public void setBkMode(int bkMode)
	{
		m_bkMode = bkMode;
	}

	/**
	 * Returns the currently selected region.
	 * @return Returns the curClip.
	 */
	public GdiRegion getCurRegion() 
	{
		return m_curRegion;
	}

	/**
	 * Sets the currently selected region.
	 * @param curClip The curClip to set.
	 */
	public void setCurRegion( GdiRegion curRegion ) 
	{
		m_curRegion = curRegion;
	}

	/**
	 * @return Returns the stretchBltMode.
	 */
	public int getStretchBltMode()
	{
		return m_stretchBltMode;
	}

	/**
	 * @param stretchBltMode The stretchBltMode to set.
	 */
	public void setStretchBltMode( int stretchBltMode )
	{
		m_stretchBltMode = stretchBltMode;
	}

	/**
	 * @return Returns the rOP2.
	 */
	public int getROP2()
	{
		return m_ROP2;
	}

	/**
	 * @param rop2 The rOP2 to set.
	 */
	public void setROP2(int rop2)
	{
		m_ROP2 = rop2;
	}
	
	/**
	 * Sets a user defined property.  Not used by the base code, this is for
	 * extensibility.  3rd parties may create their own transcoders or implement
	 * support for different records.  When this happens they may use this
	 * generic property map to dynamically add properties to the 
	 * DeviceContext.
	 * @param key
	 * @param value
	 */
	public void setProperty( Object key, Object value )
	{
		m_userProps.put( key, value );
	}

	/**
	 * Gets a user defined property.  Not used by the base code, this is for
	 * extensibility.  3rd parties may create their own transcoders or implement
	 * support for different records.  When this happens they may use this
	 * generic property map to dynamically add properties to the 
	 * DeviceContext.
	 * @param key
	 * @return The properties associated with the specified key.
	 */
	public Object getProperty( Object key )	
	{
		return m_userProps.get( key );
	}

	/**
	 * Removes a user defined property.  Not used by the base code, this is for
	 * extensibility.  3rd parties may create their own transcoders or implement
	 * support for different records.  When this happens they may use this
	 * generic property map to dynamically add properties to the 
	 * DeviceContext.
	 * @param key Property to remove
	 */
	public void removeProperty( Object key )
	{
		m_userProps.remove( key );
	}
}
