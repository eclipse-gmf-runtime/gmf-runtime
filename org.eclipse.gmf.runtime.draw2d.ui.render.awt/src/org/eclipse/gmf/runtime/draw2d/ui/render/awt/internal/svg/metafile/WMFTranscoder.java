/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile;

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.IOException;

import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.AbstractTranscoder;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Arc;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.BitBlt;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.CreateBrushIndirect;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.CreateFontIndirect;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.CreatePatternBrush;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.CreatePen;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeleteObject;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Ellipse;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.ExtTextOut;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Header;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IRenderableObject;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IWmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.LineTo;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.MoveTo;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.PolyBezier;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.PolyPolygon;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Polygon;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Polyline;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.RestoreDC;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.RoundRect;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SaveDC;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SelectObject;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetBkColor;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetBkMode;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetPolyFillMode;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetROP2;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetStretchBltMode;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetTextAlign;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetTextColor;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetViewportExtent;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetViewportOrg;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetWindowExtent;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetWindowOrg;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.StretchBlt;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.StretchDIBits;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.WMFRecord;

/**
 * This class implements a fairly simple transcoder for Windows format  Metafiles.  It reads the
 * metafile records and turns them into SVG.  It uses the Batik libraries for the SVG generation.
 * 
 * To use this class, create an input stream containing the contents of an Windows Metafile (WMF) or
 * and Aldus Placable Metafile (APM) file.
 * Create an output stream to hold the generated SVG data.
 * Finally, create an instance of WMFTranscoder and call transcode( input, output ).  If there are no errors
 * the output stream will contain the SVG output.
 * 
 * This implementation covers a subset of the metafile records.  It is possible to extend this class by 
 * overriding the 'getConverter' method, and creating classes that implement IWmf2SvgConverter to handle the 
 * additional record types.  See 'getConverter' and the IWmf2SvgConverter interface for more details.  
 * 
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.awt.*
 */
public class WMFTranscoder extends AbstractTranscoder
{ 
	/** Signature of an Aldus Placable Metafile */
	protected static final int APM_HEADER_SIGNATURE			= 0x9ac6cdd7;
	/** Size of an APM header */
	protected static final int APM_HEADER_SIZE				= 22;
	/** Size of a normal windows metafile header */
	protected static final int WMF_HEADER_SIZE				= 18;

	protected static final int WMF_FAKE_HEADER				= 0xffff;
	protected static final int WMF_ANIMATEPALETTE			= 0x0436;
	protected static final int WMF_ARC						= 0x0817;
	protected static final int WMF_BITBLT					= 0x0922;
	protected static final int WMF_CHORD					= 0x0830;
	protected static final int WMF_CREATEBRUSHINDIRECT		= 0x02FC;
	protected static final int WMF_CREATEFONTINDIRECT		= 0x02FB;
	protected static final int WMF_CREATEPALETTE			= 0x00f7;
	protected static final int WMF_CREATEPATTERNBRUSH		= 0x01F9;
	protected static final int WMF_CREATEPENINDIRECT		= 0x02FA;
	protected static final int WMF_CREATEREGION				= 0x06FF;
	protected static final int WMF_DELETEOBJECT 			= 0x01f0;
	protected static final int WMF_DIBBITBLT				= 0x0940;
	protected static final int WMF_DIBCREATEPATTERNBRUSH	= 0x0142;
	protected static final int WMF_DIBSTRETCHBLT			= 0x0b41;
	protected static final int WMF_DRAWTEXT 				= 0x062F;
	protected static final int WMF_ELLIPSE					= 0x0418;
	protected static final int WMF_ESCAPE					= 0x0626;
	protected static final int WMF_EXCLUDECLIPRECT			= 0x0415;
	protected static final int WMF_EXTFLOODFILL 			= 0x0548;
	protected static final int WMF_EXTTEXTOUT				= 0x0a32;
	protected static final int WMF_FILLREGION				= 0x0228;
	protected static final int WMF_FLOODFILL				= 0x0419;
	protected static final int WMF_FRAMEREGION				= 0x0429;
	protected static final int WMF_INTERSECTCLIPRECT		= 0x0416;
	protected static final int WMF_INVERTREGION 			= 0x012A;
	protected static final int WMF_LINETO					= 0x0213;
	protected static final int WMF_MOVETO					= 0x0214;
	protected static final int WMF_OFFSETWINDOWORG			= 0x020F;
	protected static final int WMF_OFFSETVIEWPORTORG		= 0x0211;
	protected static final int WMF_PAINTREGION				= 0x012B;
	protected static final int WMF_PATBLT					= 0x061D;
	protected static final int WMF_PIE						= 0x081A;
	protected static final int WMF_POLYBEZIER16 			= 0x1000;
	protected static final int WMF_POLYGON					= 0x0324;
	protected static final int WMF_POLYLINE 				= 0x0325;
	protected static final int WMF_POLYPOLYGON				= 0x0538;
	protected static final int WMF_REALIZEPALETTE			= 0x0035;
	protected static final int WMF_RESIZEPALETTE			= 0x0139;
	protected static final int WMF_RECTANGLE				= 0x041B;
	protected static final int WMF_RESTOREDC				= 0x0127;
	protected static final int WMF_ROUNDRECT				= 0x061C;
	protected static final int WMF_SAVEDC					= 0x001E;
	protected static final int WMF_SCALEWINDOWEXT			= 0x0410;
	protected static final int WMF_SCALEVIEWPORTEXT 		= 0x0412;
	protected static final int WMF_SELECTCLIPREGION 		= 0x012C;
	protected static final int WMF_SELECTPALETTE			= 0x0234;
	protected static final int WMF_SELECTOBJECT 			= 0x012D;
	protected static final int WMF_SETBKCOLOR				= 0x0201;
	protected static final int WMF_SETBKMODE				= 0x0102;
	protected static final int WMF_SETDIBTODEV				= 0x0d33;
	protected static final int WMF_SETLAYOUT				= 0x0149;
	protected static final int WMF_SETMAPPERFLAGS			= 0x0231;
	protected static final int WMF_SETMAPMODE				= 0x0103;
	protected static final int WMF_SETRELABS				= 0x0105;
	protected static final int WMF_SETROP2					= 0x0104;
	protected static final int WMF_SETPALENTRIES			= 0x0037;
	protected static final int WMF_SETPIXEL 				= 0x041F;
	protected static final int WMF_SETPOLYFILLMODE			= 0x0106;
	protected static final int WMF_SETSTRETCHBLTMODE		= 0x0107;
	protected static final int WMF_SETTEXTALIGN 			= 0x012E;
	protected static final int WMF_SETTEXTCHAREXTRA 		= 0x0108;
	protected static final int WMF_SETTEXTCOLOR 			= 0x0209;
	protected static final int WMF_SETTEXTJUSTIFICATION		= 0x020A;
	protected static final int WMF_SETWINDOWEXT 			= 0x020C;
	protected static final int WMF_SETWINDOWORG 			= 0x020B;
	protected static final int WMF_SETVIEWPORTEXT			= 0x020E;
	protected static final int WMF_SETVIEWPORTORG			= 0x020D;
	protected static final int WMF_STRETCHBLT				= 0x0B23;
	protected static final int WMF_STRETCHDIB				= 0x0f43;
	protected static final int WMF_TEXTOUT					= 0x0521;

	/**
	 * Reads the APM/WMF header from the input stream.  These records are
	 * special since they don't follow the normal WMF record format (no
	 * type and size).  This method constructs a fake header record
	 * and returns it to the caller.
	 * @param stream Contains the data for the WMF/APM file
	 * @return
	 * @throws IOException
	 */
	private Record readHeader( BufferedInputStream stream ) throws IOException
	{
		// Two kinds of WMF's that we support: Standard and APM.
		// APM is a standard metafile with an additional header tacked on for good measure.
	
		// Mark the stream so we can reset to the current position.
		stream.mark( 1000 );
		
		// See if it's an APM
		Record rec = new Record( 0, APM_HEADER_SIZE, stream );
		
		int signature = (int) rec.getDWORDAt( 0 );
		if( signature == APM_HEADER_SIGNATURE )
		{
			// APM header detected, read both headers into the record.
			stream.reset();
			rec = new Record( WMF_FAKE_HEADER, APM_HEADER_SIZE + WMF_HEADER_SIZE, stream );
		}
		else
		{
			// No APM header, must be a standard WMF file.
			stream.reset();
			rec = new Record( WMF_FAKE_HEADER, WMF_HEADER_SIZE, stream );
		}
		
		return rec;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.metafile2svg.AbstractTranscoder#getNextRecord(java.io.BufferedInputStream, int)
	 */
	protected Record getNextRecord(	BufferedInputStream stream,
									int curRecord ) throws IOException
	{
		if( curRecord == 0 )
		{
			// Read the metafile header.  Unlike EMF, this is not a standard record type,
			// so we have to special case it here...
			return readHeader( stream );
		}
		else
		{	
			WMFRecord rec = new WMFRecord( stream );
			
			if( rec.getType() == 0 )
			{
				// Record type of 0 means there are no more records.
				return null;
			}
			else
			{
				return rec;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.metafile2svg.AbstractTranscoder#getConverter(org.eclipse.gmf.runtime.gef.ui.internal.metafile2svg.Record)
	 */
	protected IRenderableObject getConverter( Record rec ) throws TranscoderException
	{
		IWmf2SvgConverter svgConverter = null;
		
		switch( rec.getType() )
		{
			case WMF_FAKE_HEADER:
				svgConverter = new Header();
				break;
				
			case WMF_SETMAPMODE:
				svgConverter = new SetMapMode();
				break;

			case WMF_SETWINDOWEXT:
				svgConverter = new SetWindowExtent();
				break;
				
			case WMF_SETWINDOWORG:
				svgConverter = new SetWindowOrg();
				break;
				
			case WMF_SETVIEWPORTEXT:
				svgConverter = new SetViewportExtent();
				break;
				
			case WMF_SETVIEWPORTORG:
				svgConverter = new SetViewportOrg();
				break;

			case WMF_SETPOLYFILLMODE:
				svgConverter = new SetPolyFillMode();
				break;

			case WMF_SETBKMODE:
				svgConverter = new SetBkMode();
				break;

			case WMF_SETBKCOLOR:
				svgConverter = new SetBkColor();
				break;

			case WMF_SAVEDC:
				svgConverter = new SaveDC();
				break;

			case WMF_RESTOREDC:
				svgConverter = new RestoreDC();
				break;

			case WMF_SELECTOBJECT:
				svgConverter = new SelectObject();
				break;
				
			case WMF_DELETEOBJECT:
				svgConverter = new DeleteObject();
				break;

			case WMF_CREATEBRUSHINDIRECT:
				svgConverter = new CreateBrushIndirect();
				break;

			case WMF_CREATEPENINDIRECT:
				svgConverter = new CreatePen();
				break;

			case WMF_CREATEFONTINDIRECT:
				svgConverter = new CreateFontIndirect();
				break;
				
			case WMF_SETTEXTCOLOR:
				svgConverter = new SetTextColor();
				break;

			case WMF_SETTEXTALIGN:
				svgConverter = new SetTextAlign();
				break;

			case WMF_POLYGON:
				svgConverter = new Polygon( true );
				break;
				
			case WMF_POLYLINE:
				svgConverter = new Polyline( true );
				break;

			case WMF_POLYBEZIER16:
				svgConverter = new PolyBezier( true, false );
				break;

			case WMF_RECTANGLE:
				svgConverter = new Rectangle();
				break;
				
			case WMF_ROUNDRECT:
				svgConverter = new RoundRect();
				break;

			case WMF_LINETO:
				svgConverter = new LineTo();
				break;
				
			case WMF_MOVETO:
				svgConverter = new MoveTo();
				break;

			case WMF_ELLIPSE:
				svgConverter = new Ellipse();
				break;
				
			case WMF_EXTTEXTOUT:
				svgConverter = new ExtTextOut( false );
				break;

			case WMF_SELECTCLIPREGION:
				svgConverter = new SelectClipRgn();
				break;

			case WMF_INTERSECTCLIPRECT:
				svgConverter = new IntersectClipRect();
				break;

			case WMF_POLYPOLYGON:
				svgConverter = new PolyPolygon( true );
				break;
				
			case WMF_ARC:
				svgConverter = new Arc( Arc.ARC );
				break;
				
			case WMF_CHORD:
				svgConverter = new Arc( Arc.CHORD );
				break;
				
			case WMF_PIE:
				svgConverter = new Arc( Arc.PIE );
				break;

			case WMF_SETSTRETCHBLTMODE:
				svgConverter = new SetStretchBltMode();
				break;

			case WMF_SETROP2:
				svgConverter = new SetROP2();
				break;

			case WMF_CREATEPATTERNBRUSH:
				svgConverter = new CreatePatternBrush( false );
				break;

			case WMF_DIBCREATEPATTERNBRUSH:
				svgConverter = new CreatePatternBrush( true );
				break;

			case WMF_STRETCHDIB:
				svgConverter = new StretchBlt();
				break;

			case WMF_DIBSTRETCHBLT:
				svgConverter = new StretchDIBits();
				break;

			case WMF_DIBBITBLT:
				svgConverter = new BitBlt();
				break;

			// Consume but do nothing
			case WMF_ESCAPE:
			case WMF_ANIMATEPALETTE:
			case WMF_CREATEPALETTE:
			case WMF_SELECTPALETTE:
			case WMF_REALIZEPALETTE:
			case WMF_RESIZEPALETTE:
				// These opcodes don't seem to matter at all, so 'No-op' them for now.
				// It may be that we will need to at least handle the palette creation
				// since this will create a new Gdi object in the system and potentially
				// mess up the object ids.
				svgConverter = new Noop();
				break;


			case WMF_STRETCHBLT:
				// Not currently supported.  Not normally seen in metafiles.  This is a device
				// dependent stretch blt.  I don't think we can support this unless we support
				// palettes properly since the bitmap does not contain any palette entries.
				break;
			case WMF_BITBLT:
				// Not currently supported.  Not normally seen in metafiles.  This is a device
				// dependent stretch blt.  I don't think we can support this unless we support
				// palettes properly since the bitmap does not contain any palette entries.
				break;
			
			case WMF_SETPIXEL:
			case WMF_TEXTOUT:


			// Region stuff.  Haven't seen any of this, which is good since parsing 'createregion'
			// could be quite difficult.
			case WMF_CREATEREGION:
			case WMF_EXCLUDECLIPRECT:
			case WMF_FILLREGION:
			case WMF_FRAMEREGION:
			case WMF_INVERTREGION:
			case WMF_PAINTREGION:


			// Unlikely we will need to implement any of this.
			case WMF_EXTFLOODFILL:
			case WMF_FLOODFILL:
			case WMF_OFFSETWINDOWORG:
			case WMF_OFFSETVIEWPORTORG:
			case WMF_PATBLT:
			case WMF_SCALEWINDOWEXT:
			case WMF_SCALEVIEWPORTEXT:
			case WMF_SETDIBTODEV:
			case WMF_SETLAYOUT:
			case WMF_SETMAPPERFLAGS:
				
			case WMF_SETTEXTCHAREXTRA:
			case WMF_SETRELABS:
			case WMF_SETPALENTRIES:
				
			case WMF_SETTEXTJUSTIFICATION:
				break;
				
			default:
				break;
		}
		
		if( svgConverter != null )
		{
			initializeConverter( svgConverter, rec );
		}

		return svgConverter;
	}

	/**
	 * Initializes the specified converter with data from the specified record.
	 * @param svgConverter
	 * @param rec
	 * @throws TranscoderException
	 */
	private void initializeConverter(	IRenderableObject svgConverter, 
										Record rec ) throws TranscoderException
	{
		try
		{
			IWmf2SvgConverter converter = (IWmf2SvgConverter) svgConverter;
			converter.readWMFRecord( rec );
	
			// Header record is special.  It contains info that the transcoder needs to function
			// such as the number of records in the metafile.
			if( converter instanceof Header )
			{
				Header header = (Header) converter;
				setSize( new Dimension( header.getBounds().width, header.getBounds().height ) );
			}
		}
		catch( IOException e )
		{
			throw new TranscoderException( e, false ); // don't log this exception since it will be used for autosense failure
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.metafile2svg.AbstractTranscoder#getUserFriendlyName(int)
	 */
	protected String getUserFriendlyName( int recordType )
	{
		String name = "";  //$NON-NLS-1$
		
		switch( recordType )
		{
			case WMF_FAKE_HEADER:
				name = "FAKE_HEADER"; //$NON-NLS-1$
				break;
			case WMF_ANIMATEPALETTE:
				name = "ANIMATEPALETTE"; //$NON-NLS-1$
				break;
			case WMF_ARC:
				name = "ARC"; //$NON-NLS-1$
				break;
			case WMF_BITBLT:
				name = "BITBLT"; //$NON-NLS-1$
				break;
			case WMF_CHORD:
				name = "CHORD"; //$NON-NLS-1$
				break;
			case WMF_CREATEBRUSHINDIRECT:
				name = "CREATEBRUSHINDIRECT"; //$NON-NLS-1$
				break;
			case WMF_CREATEFONTINDIRECT:
				name = "CREATEFONTINDIRECT"; //$NON-NLS-1$
				break;
			case WMF_CREATEPALETTE:
				name = "CREATEPALETTE"; //$NON-NLS-1$
				break;
			case WMF_CREATEPATTERNBRUSH:
				name = "CREATEPATTERNBRUSH"; //$NON-NLS-1$
				break;
			case WMF_CREATEPENINDIRECT:
				name = "CREATEPENINDIRECT"; //$NON-NLS-1$
				break;
			case WMF_CREATEREGION:
				name = "CREATEREGION"; //$NON-NLS-1$
				break;
			case WMF_DELETEOBJECT:
				name = "DELETEOBJECT"; //$NON-NLS-1$
				break;
			case WMF_DIBBITBLT:
				name = "DIBBITBLT"; //$NON-NLS-1$
				break;
			case WMF_DIBCREATEPATTERNBRUSH:
				name = "DIBCREATEPATTERNBRUSH"; //$NON-NLS-1$
				break;
			case WMF_DIBSTRETCHBLT:
				name = "DIBSTRETCHBLT"; //$NON-NLS-1$
				break;
			case WMF_DRAWTEXT:
				name = "DRAWTEXT"; //$NON-NLS-1$
				break;
			case WMF_ELLIPSE:
				name = "ELLIPSE"; //$NON-NLS-1$
				break;
			case WMF_ESCAPE:
				name = "ESCAPE"; //$NON-NLS-1$
				break;
			case WMF_EXCLUDECLIPRECT:
				name = "EXCLUDECLIPRECT"; //$NON-NLS-1$
				break;
			case WMF_EXTFLOODFILL:
				name = "EXTFLOODFILL"; //$NON-NLS-1$
				break;
			case WMF_EXTTEXTOUT:
				name = "EXTTEXTOUT"; //$NON-NLS-1$
				break;
			case WMF_FILLREGION:
				name = "FILLREGION"; //$NON-NLS-1$
				break;
			case WMF_FLOODFILL:
				name = "FLOODFILL"; //$NON-NLS-1$
				break;
			case WMF_FRAMEREGION:
				name = "FRAMEREGION"; //$NON-NLS-1$
				break;
			case WMF_INTERSECTCLIPRECT:
				name = "INTERSECTCLIPRECT"; //$NON-NLS-1$
				break;
			case WMF_INVERTREGION:
				name = "INVERTREGION"; //$NON-NLS-1$
				break;
			case WMF_LINETO:
				name = "LINETO"; //$NON-NLS-1$
				break;
			case WMF_MOVETO:
				name = "MOVETO"; //$NON-NLS-1$
				break;
			case WMF_OFFSETWINDOWORG:
				name = "OFFSETWINDOWORG"; //$NON-NLS-1$
				break;
			case WMF_OFFSETVIEWPORTORG:
				name = "OFFSETVIEWPORTORG"; //$NON-NLS-1$
				break;
			case WMF_PAINTREGION:
				name = "PAINTREGION"; //$NON-NLS-1$
				break;
			case WMF_PATBLT:
				name = "PATBLT"; //$NON-NLS-1$
				break;
			case WMF_PIE:
				name = "PIE"; //$NON-NLS-1$
				break;
			case WMF_POLYBEZIER16:
				name = "POLYBEZIER16"; //$NON-NLS-1$
				break;
			case WMF_POLYGON:
				name = "POLYGON"; //$NON-NLS-1$
				break;
			case WMF_POLYLINE:
				name = "POLYLINE"; //$NON-NLS-1$
				break;
			case WMF_POLYPOLYGON:
				name = "POLYPOLYGON"; //$NON-NLS-1$
				break;
			case WMF_REALIZEPALETTE:
				name = "REALIZEPALETTE"; //$NON-NLS-1$
				break;
			case WMF_RESIZEPALETTE:
				name = "RESIZEPALETTE"; //$NON-NLS-1$
				break;
			case WMF_RECTANGLE:
				name = "RECTANGLE"; //$NON-NLS-1$
				break;
			case WMF_RESTOREDC:
				name = "RESTOREDC"; //$NON-NLS-1$
				break;
			case WMF_ROUNDRECT:
				name = "ROUNDRECT"; //$NON-NLS-1$
				break;
			case WMF_SAVEDC:
				name = "SAVEDC"; //$NON-NLS-1$
				break;
			case WMF_SCALEWINDOWEXT:
				name = "SCALEWINDOWEXT"; //$NON-NLS-1$
				break;
			case WMF_SCALEVIEWPORTEXT:
				name = "SCALEVIEWPORTEXT"; //$NON-NLS-1$
				break;
			case WMF_SELECTCLIPREGION:
				name = "SELECTCLIPREGION"; //$NON-NLS-1$
				break;
			case WMF_SELECTPALETTE:
				name = "SELECTPALETTE"; //$NON-NLS-1$
				break;
			case WMF_SELECTOBJECT:
				name = "SELECTOBJECT"; //$NON-NLS-1$
				break;
			case WMF_SETBKCOLOR:
				name = "SETBKCOLOR"; //$NON-NLS-1$
				break;
			case WMF_SETBKMODE:
				name = "SETBKMODE"; //$NON-NLS-1$
				break;
			case WMF_SETDIBTODEV:
				name = "SETDIBTODEV"; //$NON-NLS-1$
				break;
			case WMF_SETLAYOUT:
				name = "SETLAYOUT"; //$NON-NLS-1$
				break;
			case WMF_SETMAPPERFLAGS:
				name = "SETMAPPERFLAGS"; //$NON-NLS-1$
				break;
			case WMF_SETMAPMODE:
				name = "SETMAPMODE"; //$NON-NLS-1$
				break;
			case WMF_SETRELABS:
				name = "SETRELABS"; //$NON-NLS-1$
				break;
			case WMF_SETROP2:
				name = "SETROP2"; //$NON-NLS-1$
				break;
			case WMF_SETPALENTRIES:
				name = "SETPALENTRIES"; //$NON-NLS-1$
				break;
			case WMF_SETPIXEL:
				name = "SETPIXEL"; //$NON-NLS-1$
				break;
			case WMF_SETPOLYFILLMODE:
				name = "SETPOLYFILLMODE"; //$NON-NLS-1$
				break;
			case WMF_SETSTRETCHBLTMODE:
				name = "SETSTRETCHBLTMODE"; //$NON-NLS-1$
				break;
			case WMF_SETTEXTALIGN:
				name = "SETTEXTALIGN"; //$NON-NLS-1$
				break;
			case WMF_SETTEXTCHAREXTRA:
				name = "SETTEXTCHAREXTRA"; //$NON-NLS-1$
				break;
			case WMF_SETTEXTCOLOR:
				name = "SETTEXTCOLOR"; //$NON-NLS-1$
				break;
			case WMF_SETTEXTJUSTIFICATION:
				name = "SETTEXTJUSTIFICATION"; //$NON-NLS-1$
				break;
			case WMF_SETWINDOWEXT:
				name = "SETWINDOWEXT"; //$NON-NLS-1$
				break;
			case WMF_SETWINDOWORG:
				name = "SETWINDOWORG"; //$NON-NLS-1$
				break;
			case WMF_SETVIEWPORTEXT:
				name = "SETVIEWPORTEXT"; //$NON-NLS-1$
				break;
			case WMF_SETVIEWPORTORG:
				name = "SETVIEWPORTORG"; //$NON-NLS-1$
				break;
			case WMF_STRETCHBLT:
				name = "STRETCHBLT"; //$NON-NLS-1$
				break;
			case WMF_STRETCHDIB:
				name = "STRETCHDIB"; //$NON-NLS-1$
				break;
			case WMF_TEXTOUT:
				name = "TEXTOUT"; //$NON-NLS-1$
				break;
			default:
				name = "<unknown>"; //$NON-NLS-1$
				break;				
		}
		
		return name;
	}
}
