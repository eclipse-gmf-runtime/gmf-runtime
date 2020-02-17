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

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.IOException;

import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.AbortPath;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.AbstractTranscoder;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.AngleArc;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Arc;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.BeginPath;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.BitBlt;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.CloseFigure;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.CreateBrushIndirect;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.CreateFontIndirect;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.CreatePatternBrush;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.CreatePen;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeleteObject;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.EMFRecord;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.EOF;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Ellipse;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.EndPath;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.ExtCreatePen;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.ExtTextOut;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.FillPath;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.FlattenPath;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Header;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IEmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IRenderableObject;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.LineTo;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.ModifyWorldTransform;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.MoveTo;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.PolyBezier;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.PolyDraw;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.PolyPolyLine;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.PolyPolygon;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Polygon;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Polyline;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.PolylineTo;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.RestoreDC;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.RoundRect;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SaveDC;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SelectObject;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetArcDirection;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetBkColor;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetBkMode;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.SetMiterLimit;
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
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.StrokeAndFillPath;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.StrokePath;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.TranscoderException;


/**
 * This class implements a fairly simple transcoder for Windows format Enhanced Metafiles.  It reads the
 * metafile records and turns them into SVG.  It uses the Batik libraries for the SVG generation.
 * 
 * To use this class, create an input stream containing the contents of an EMF file.
 * Create an output stream to hold the generated SVG data.
 * Finally, create an instance of EMFTranscoder and call transcode( input, output ).  If there are no errors
 * the output stream will contain the SVG output.
 * 
 * This implementation covers a subset of the metafile records.  It is possible to extend this class by 
 * overriding the 'getConverter' method, and creating classes that implement IEmf2SvgConverter to handle the 
 * additional record types.  See 'getConverter' and the IEmf2SvgConverter interface for more details.  
 * 
 * This class should not be used externally.  The diagramming engine can support
 * rendering of EMF and WMF files through the RenderedImage infrastructure now,
 * so it is not necessary for clients to convert them to SVG before creating the
 * RenderedImage file.
 * 
 * @author dhabib
 * @author sshaw
 */
public class EMFTranscoder extends AbstractTranscoder
{
	private static final int EMR_HEADER 						= 1;
	private static final int EMR_POLYBEZIER 					= 2;
	private static final int EMR_POLYGON						= 3;
	private static final int EMR_POLYLINE						= 4;
	private static final int EMR_POLYBEZIERTO					= 5;
	private static final int EMR_POLYLINETO 					= 6;
	private static final int EMR_POLYPOLYLINE					= 7;
	private static final int EMR_POLYPOLYGON					= 8;
	private static final int EMR_SETWINDOWEXTEX 				= 9;
	private static final int EMR_SETWINDOWORGEX 				= 10;
	private static final int EMR_SETVIEWPORTEXTEX				= 11;
	private static final int EMR_SETVIEWPORTORGEX				= 12;
	private static final int EMR_SETBRUSHORGEX				= 13;
	private static final int EMR_EOF							= 14;
	private static final int EMR_SETPIXELV					= 15;
	private static final int EMR_SETMAPPERFLAGS 				= 16;
	private static final int EMR_SETMAPMODE 					= 17;
	private static final int EMR_SETBKMODE					= 18;
	private static final int EMR_SETPOLYFILLMODE				= 19;
	private static final int EMR_SETROP2						= 20;
	private static final int EMR_SETSTRETCHBLTMODE			= 21;
	private static final int EMR_SETTEXTALIGN					= 22;
	private static final int EMR_SETCOLORADJUSTMENT 			= 23;
	private static final int EMR_SETTEXTCOLOR					= 24;
	private static final int EMR_SETBKCOLOR 					= 25;
	private static final int EMR_OFFSETCLIPRGN				= 26;
	private static final int EMR_MOVETOEX						= 27;
	private static final int EMR_SETMETARGN 					= 28;
	private static final int EMR_EXCLUDECLIPRECT				= 29;
	private static final int EMR_INTERSECTCLIPRECT			= 30;
	private static final int EMR_SCALEVIEWPORTEXTEX 			= 31;
	private static final int EMR_SCALEWINDOWEXTEX				= 32;
	private static final int EMR_SAVEDC 						= 33;
	private static final int EMR_RESTOREDC					= 34;
	private static final int EMR_SETWORLDTRANSFORM			= 35;
	private static final int EMR_MODIFYWORLDTRANSFORM			= 36;
	private static final int EMR_SELECTOBJECT					= 37;
	private static final int EMR_CREATEPEN					= 38;
	private static final int EMR_CREATEBRUSHINDIRECT			= 39;
	private static final int EMR_DELETEOBJECT					= 40;
	private static final int EMR_ANGLEARC						= 41;
	private static final int EMR_ELLIPSE						= 42;
	private static final int EMR_RECTANGLE					= 43;
	private static final int EMR_ROUNDRECT					= 44;
	private static final int EMR_ARC							= 45;
	private static final int EMR_CHORD						= 46;
	private static final int EMR_PIE							= 47;
	private static final int EMR_SELECTPALETTE				= 48;
	private static final int EMR_CREATEPALETTE				= 49;
	private static final int EMR_SETPALETTEENTRIES			= 50;
	private static final int EMR_RESIZEPALETTE				= 51;
	private static final int EMR_REALIZEPALETTE 				= 52;
	private static final int EMR_EXTFLOODFILL					= 53;
	private static final int EMR_LINETO 						= 54;
	private static final int EMR_ARCTO						= 55;
	private static final int EMR_POLYDRAW						= 56;
	private static final int EMR_SETARCDIRECTION				= 57;
	private static final int EMR_SETMITERLIMIT				= 58;
	private static final int EMR_BEGINPATH					= 59;
	private static final int EMR_ENDPATH						= 60;
	private static final int EMR_CLOSEFIGURE					= 61;
	private static final int EMR_FILLPATH						= 62;
	private static final int EMR_STROKEANDFILLPATH			= 63;
	private static final int EMR_STROKEPATH 					= 64;
	private static final int EMR_FLATTENPATH					= 65;
	private static final int EMR_WIDENPATH					= 66;
	private static final int EMR_SELECTCLIPPATH 				= 67;
	private static final int EMR_ABORTPATH					= 68;

	private static final int EMR_GDICOMMENT 					= 70;
	private static final int EMR_FILLRGN						= 71;
	private static final int EMR_FRAMERGN						= 72;
	private static final int EMR_INVERTRGN					= 73;
	private static final int EMR_PAINTRGN						= 74;
	private static final int EMR_EXTSELECTCLIPRGN				= 75;
	private static final int EMR_BITBLT 						= 76;
	private static final int EMR_STRETCHBLT 					= 77;
	private static final int EMR_MASKBLT						= 78;
	private static final int EMR_PLGBLT 						= 79;
	private static final int EMR_SETDIBITSTODEVICE			= 80;
	private static final int EMR_STRETCHDIBITS				= 81;
	private static final int EMR_EXTCREATEFONTINDIRECTW 		= 82;
	private static final int EMR_EXTTEXTOUTA					= 83;
	private static final int EMR_EXTTEXTOUTW					= 84;
	private static final int EMR_POLYBEZIER16					= 85;
	private static final int EMR_POLYGON16					= 86;
	private static final int EMR_POLYLINE16 					= 87;
	private static final int EMR_POLYBEZIERTO16 				= 88;
	private static final int EMR_POLYLINETO16					= 89;
	private static final int EMR_POLYPOLYLINE16 				= 90;
	private static final int EMR_POLYPOLYGON16				= 91;
	private static final int EMR_POLYDRAW16 					= 92;
	private static final int EMR_CREATEMONOBRUSH				= 93;
	private static final int EMR_CREATEDIBPATTERNBRUSHPT		= 94;
	private static final int EMR_EXTCREATEPEN					= 95;
	private static final int EMR_POLYTEXTOUTA					= 96;
	private static final int EMR_POLYTEXTOUTW					= 97;

	private static final int EMR_SETICMMODE 					= 98;
	private static final int EMR_CREATECOLORSPACE				= 99;
	private static final int EMR_SETCOLORSPACE				= 100;
	private static final int EMR_DELETECOLORSPACE				= 101;
	private static final int EMR_GLSRECORD					= 102;
	private static final int EMR_GLSBOUNDEDRECORD				= 103;
	private static final int EMR_PIXELFORMAT					= 104;

	private static final int EMR_DRAWESCAPE 					= 105;
	private static final int EMR_EXTESCAPE					= 106;
	private static final int EMR_STARTDOC						= 107;
	private static final int EMR_SMALLTEXTOUT					= 108;
	private static final int EMR_FORCEUFIMAPPING				= 109;
	private static final int EMR_NAMEDESCAPE					= 110;
	private static final int EMR_COLORCORRECTPALETTE			= 111;
	private static final int EMR_SETICMPROFILEA 				= 112;
	private static final int EMR_SETICMPROFILEW 				= 113;
	private static final int EMR_ALPHABLEND 					= 114;
	private static final int EMR_ALPHADIBBLEND				= 115;
	private static final int EMR_TRANSPARENTBLT 				= 116;
	private static final int EMR_TRANSPARENTDIB 				= 117;
	private static final int EMR_GRADIENTFILL					= 118;
	private static final int EMR_SETLINKEDUFIS				= 119;
	private static final int EMR_SETTEXTJUSTIFICATION			= 120;
	
	private int m_numRecords	= 0;
	
	/**
	 * Reads the APM/WMF header from the input stream.  These records are
	 * special since they don't follow the normal WMF record format (no
	 * type and size).  This method constructs a fake header record
	 * and returns it to the caller.
	 * 
	 * @param stream Contains the data for the WMF/APM file
	 * @return <code>boolean</code> <code>true</code> if the header portion of the stream is validated
	 * to be part of an EMF file, <code>false</code> otherwise.
	 * @throws IOException
	 */
	private boolean validateHeader( BufferedInputStream stream ) throws IOException
	{
		// Mark the stream so we can reset to the current position.
		stream.mark( 1000 );
		
		// See if it's an APM
		Record rec = new Record( 0, EMFRecord.EMR_HEADER_SIZE, stream );
		
		int type = (int)rec.getDWORDAt( 0 );
		int size = (int)rec.getDWORDAt( 4 );	// Subtract 8 bytes for the header
		
		// verify type and that size is something reasonable...
		if (type == EMR_HEADER && size < 5000) {
			stream.reset();
			return true;
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.metafile2svg.AbstractTranscoder#getNextRecord(java.io.BufferedInputStream, int)
	 */
	protected Record getNextRecord(	BufferedInputStream stream,
									int curRecord ) throws IOException
	{
		EMFRecord rec = null;
		
		if( curRecord == 0 )
		{
			// Read the metafile header.  Unlike EMF, this is not a standard record type,
			// so we have to special case it here...
			if (!validateHeader( stream )) {
				throw new IOException( "Invalid Enhanced Metafile Format:  Invalid header" );//$NON-NLS-1$
			}
		}
		
		// if curRecord is 0 then we need to read the header since that
		// contains the total number of records.
		if( curRecord == 0 || curRecord < m_numRecords )
		{
			rec = new EMFRecord( stream );
		}
		
		return rec;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.metafile2svg.AbstractTranscoder#getConverter(org.eclipse.gmf.runtime.gef.ui.internal.metafile2svg.Record)
	 */
	protected IRenderableObject getConverter( Record emr ) throws TranscoderException
	{
		IRenderableObject svgConverter = null;
		
		switch( emr.getType() )
		{
			case EMR_HEADER:
				svgConverter = new Header();
				break;

			case EMR_MOVETOEX:
				svgConverter = new MoveTo();
				break;

			case EMR_LINETO:
				svgConverter = new LineTo();
				break;

			case EMR_POLYGON16:
				svgConverter = new Polygon( true );
				break;

			case EMR_POLYGON:
				svgConverter = new Polygon( false );
				break;

			case EMR_POLYBEZIER:
				svgConverter = new PolyBezier( false, false );
				break;
				
			case EMR_POLYBEZIER16:
				svgConverter = new PolyBezier( true, false );
				break;

			case EMR_POLYBEZIERTO:
				svgConverter = new PolyBezier( false, true );
				break;

			case EMR_POLYBEZIERTO16:
				svgConverter = new PolyBezier( true, true );
				break;

			case EMR_POLYLINE:
				svgConverter = new Polyline( false );
				break;

			case EMR_POLYLINE16:
				svgConverter = new Polyline( true );
				break;

			case EMR_POLYLINETO:
				svgConverter = new PolylineTo( false );
				break;

			case EMR_POLYLINETO16:
				svgConverter = new PolylineTo( true );
				break;

			case EMR_POLYPOLYLINE:
				svgConverter = new PolyPolyLine( false );
				break;	

			case EMR_POLYPOLYLINE16:
				svgConverter = new PolyPolyLine( true );
				break;	

			case EMR_POLYPOLYGON:
				svgConverter = new PolyPolygon( false );
				break;
				
			case EMR_POLYPOLYGON16:
				svgConverter = new PolyPolygon( true );
				break;

			case EMR_SETWINDOWEXTEX:
				svgConverter = new SetWindowExtent();
				break;

			case EMR_SETVIEWPORTEXTEX:
				svgConverter = new SetViewportExtent();
				break;

			case EMR_SETWINDOWORGEX:
				svgConverter = new SetWindowOrg();
				break;
				
			case EMR_SETVIEWPORTORGEX:
				svgConverter = new SetViewportOrg();
				break;
				
			case EMR_SETMAPMODE:
				svgConverter = new SetMapMode();
				break;
			
			case EMR_SETTEXTCOLOR:
				svgConverter = new SetTextColor();
				break;
			
			case EMR_SETBKCOLOR:
				svgConverter = new SetBkColor();
				break;

			case EMR_CREATEBRUSHINDIRECT:
				svgConverter = new CreateBrushIndirect();
				break;
				
			case EMR_CREATEDIBPATTERNBRUSHPT:
				svgConverter = new CreatePatternBrush( true );
				break;

			case EMR_CREATEMONOBRUSH:
				svgConverter = new CreatePatternBrush( false );
				break;

			case EMR_CREATEPEN:
				svgConverter = new CreatePen();
				break;
				
			case EMR_EXTCREATEFONTINDIRECTW:
				svgConverter = new CreateFontIndirect();
				break;
			
			case EMR_SELECTOBJECT:
				svgConverter = new SelectObject();
				break;
				
			case EMR_DELETEOBJECT:
				svgConverter = new DeleteObject();
				break;

			case EMR_SETTEXTALIGN:
				svgConverter = new SetTextAlign();
				break;

			case EMR_EXTTEXTOUTA:
				svgConverter = new ExtTextOut( false );
				break;

			case EMR_EXTTEXTOUTW:
				svgConverter = new ExtTextOut( true );
				break;

			case EMR_ELLIPSE:
				svgConverter = new Ellipse();
				break;
				
			case EMR_RECTANGLE:
				svgConverter = new Rectangle();
				break;

			case EMR_ROUNDRECT:
				svgConverter = new RoundRect();
				break;

			case EMR_SETARCDIRECTION:
				svgConverter = new SetArcDirection();
				break;
				
			case EMR_ARC:
				svgConverter = new Arc( Arc.ARC );
				break;

			case EMR_ARCTO:
				svgConverter = new Arc( Arc.ARCTO );
				break;

			case EMR_PIE:
				svgConverter = new Arc( Arc.PIE );
				break;

			case EMR_CHORD:
				svgConverter = new Arc( Arc.CHORD );
				break;

			case EMR_ANGLEARC:
				svgConverter = new AngleArc();
				break;

			case EMR_POLYDRAW:
				svgConverter = new PolyDraw( false );
				break;

			case EMR_POLYDRAW16:
				svgConverter = new PolyDraw( true );
				break;
				
			case EMR_SETPOLYFILLMODE:
				svgConverter = new SetPolyFillMode();
				break;
				
			case EMR_EXTCREATEPEN:
				svgConverter = new ExtCreatePen();
				break;

			case EMR_BEGINPATH:
				svgConverter = new BeginPath();
				break;

			case EMR_ENDPATH:
				svgConverter = new EndPath();
				break;

			case EMR_ABORTPATH:
				svgConverter = new AbortPath();
				break;

			case EMR_FILLPATH:
				svgConverter = new FillPath();
				break;

			case EMR_STROKEPATH:
				svgConverter = new StrokePath();
				break;

			case EMR_STROKEANDFILLPATH:
				svgConverter = new StrokeAndFillPath();
				break;

			case EMR_CLOSEFIGURE:
				svgConverter = new CloseFigure();
				break;

			case EMR_FLATTENPATH:
				svgConverter = new FlattenPath();
				break;
	
			case EMR_SETMITERLIMIT:
				svgConverter = new SetMiterLimit();
				break;

			case EMR_WIDENPATH:
				// Probably don't need this.
				break;
				
			case EMR_SETBKMODE:
				svgConverter = new SetBkMode();
				break;

			case EMR_EOF:
				svgConverter = new EOF();
				break;

			case EMR_BITBLT:
				svgConverter = new BitBlt();
				break;
				
			case EMR_STRETCHBLT:
				svgConverter = new StretchBlt();
				break;

			case EMR_STRETCHDIBITS:
				svgConverter = new StretchDIBits();
				break;

			case EMR_SETSTRETCHBLTMODE:
				svgConverter = new SetStretchBltMode();
				break;

			case EMR_SETROP2:
				svgConverter = new SetROP2();
				break;

			case EMR_SAVEDC:
				svgConverter = new SaveDC();
				break;

			case EMR_RESTOREDC:
				svgConverter = new RestoreDC();
				break;

			case EMR_MODIFYWORLDTRANSFORM:
				svgConverter = new ModifyWorldTransform();
				break;
				
				// To be implemented:
			case EMR_MASKBLT:
			case EMR_PLGBLT:

			// Not yet implemented:

			case EMR_SELECTPALETTE:
			case EMR_CREATEPALETTE:
			case EMR_SETPALETTEENTRIES:
			case EMR_RESIZEPALETTE:
			case EMR_REALIZEPALETTE:

			case EMR_SELECTCLIPPATH:
			case EMR_FRAMERGN:
			case EMR_INVERTRGN:
			case EMR_PAINTRGN:
			case EMR_EXTSELECTCLIPRGN:
			case EMR_EXCLUDECLIPRECT:
			case EMR_INTERSECTCLIPRECT:
			case EMR_FILLRGN:
			case EMR_OFFSETCLIPRGN:
			case EMR_SETMETARGN:
		
			case EMR_SETPIXELV:
			case EMR_EXTFLOODFILL:
				// These are not really scalable since it's pixel by pixel.  Not sure what
				// SVG will do with these.  Not easy to implement in a reasonable way.

			case EMR_SETCOLORADJUSTMENT:
			case EMR_GRADIENTFILL:
			

			case EMR_POLYTEXTOUTA:
			case EMR_POLYTEXTOUTW:

			case EMR_SETBRUSHORGEX:
			case EMR_SETMAPPERFLAGS:
			case EMR_SCALEVIEWPORTEXTEX:
			case EMR_SCALEWINDOWEXTEX:
			case EMR_SETWORLDTRANSFORM:
			case EMR_GDICOMMENT:

			case EMR_SETDIBITSTODEVICE:
			case EMR_SETICMMODE:
			case EMR_CREATECOLORSPACE:
			case EMR_SETCOLORSPACE:
			case EMR_DELETECOLORSPACE:
			case EMR_GLSRECORD:
			case EMR_GLSBOUNDEDRECORD:
			case EMR_FORCEUFIMAPPING:
			case EMR_COLORCORRECTPALETTE:
			case EMR_SETICMPROFILEA:
			case EMR_SETICMPROFILEW:
			case EMR_ALPHABLEND:
			case EMR_ALPHADIBBLEND:
			case EMR_TRANSPARENTBLT:
			case EMR_TRANSPARENTDIB:
			case EMR_SETLINKEDUFIS:
			case EMR_SETTEXTJUSTIFICATION:

			// Can't find definitions in the docs for these.
			case EMR_PIXELFORMAT:
			case EMR_DRAWESCAPE:
			case EMR_EXTESCAPE:
			case EMR_STARTDOC:
			case EMR_NAMEDESCAPE:
			case EMR_SMALLTEXTOUT:
			default:
				break;
		}
		
		if( svgConverter != null )
		{
			initializeConverter( svgConverter, emr );
		}

		return svgConverter;
	}
	
	/**
	 * Initializes the specified converter with data from the specified record.
	 * @param svgConverter
	 * @param rec
	 * @throws TranscoderException
	 */
	private void initializeConverter( IRenderableObject svgConverter, Record rec ) throws TranscoderException
	{
		try
		{
			IEmf2SvgConverter converter = (IEmf2SvgConverter) svgConverter;
			converter.readEMFRecord( rec );
			
			// Header record is special.  It contains info that the transcoder needs to function
			// such as the number of records in the metafile.
			if( converter instanceof Header )
			{
				Header header = (Header) converter;
				m_numRecords = header.getNumRecords();
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
	protected String getUserFriendlyName( int emrType )
	{
		String name = "";  //$NON-NLS-1$
		
		switch( emrType )
		{
			case EMR_HEADER:
				name = "HEADER"; //$NON-NLS-1$
				break;
			case EMR_POLYBEZIER:
				name = "POLYBEZIER"; //$NON-NLS-1$
				break;
			case EMR_POLYGON:
				name = "POLYGON"; //$NON-NLS-1$
				break;
			case EMR_POLYLINE:
				name = "POLYLINE"; //$NON-NLS-1$
				break;
			case EMR_POLYBEZIERTO:
				name = "POLYBEZIERTO"; //$NON-NLS-1$
				break;
			case EMR_POLYLINETO:
				name = "POLYLINETO"; //$NON-NLS-1$
				break;
			case EMR_POLYPOLYLINE:
				name = "POLYPOLYLINE"; //$NON-NLS-1$
				break;
			case EMR_POLYPOLYGON:
				name = "POLYPOLYGON"; //$NON-NLS-1$
				break;
			case EMR_SETWINDOWEXTEX:
				name = "SETWINDOWEXTEX"; //$NON-NLS-1$
				break;
			case EMR_SETWINDOWORGEX:
				name = "SETWINDOWORGEX"; //$NON-NLS-1$
				break;
			case EMR_SETVIEWPORTEXTEX:
				name = "SETVIEWPORTEXTEX"; //$NON-NLS-1$
				break;
			case EMR_SETVIEWPORTORGEX:
				name = "SETVIEWPORTORGEX"; //$NON-NLS-1$
				break;
			case EMR_SETBRUSHORGEX:
				name = "SETBRUSHORGEX"; //$NON-NLS-1$
				break;
			case EMR_EOF:
				name = "EOF"; //$NON-NLS-1$
				break;
			case EMR_SETPIXELV:
				name = "SETPIXELV"; //$NON-NLS-1$
				break;
			case EMR_SETMAPPERFLAGS:
				name = "SETMAPPERFLAGS"; //$NON-NLS-1$
				break;
			case EMR_SETMAPMODE:
				name = "SETMAPMODE"; //$NON-NLS-1$
				break;
			case EMR_SETBKMODE:
				name = "SETBKMODE"; //$NON-NLS-1$
				break;
			case EMR_SETPOLYFILLMODE:
				name = "SETPOLYFILLMODE"; //$NON-NLS-1$
				break;
			case EMR_SETROP2:
				name = "SETROP2"; //$NON-NLS-1$
				break;
			case EMR_SETSTRETCHBLTMODE:
				name = "SETSTRETCHBLTMODE"; //$NON-NLS-1$
				break;
			case EMR_SETTEXTALIGN:
				name = "SETTEXTALIGN"; //$NON-NLS-1$
				break;
			case EMR_SETCOLORADJUSTMENT:
				name = "SETCOLORADJUSTMENT"; //$NON-NLS-1$
				break;
			case EMR_SETTEXTCOLOR:
				name = "SETTEXTCOLOR"; //$NON-NLS-1$
				break;
			case EMR_SETBKCOLOR:
				name = "SETBKCOLOR"; //$NON-NLS-1$
				break;
			case EMR_OFFSETCLIPRGN:
				name = "OFFSETCLIPRGN"; //$NON-NLS-1$
				break;
			case EMR_MOVETOEX:
				name = "MOVETOEX"; //$NON-NLS-1$
				break;
			case EMR_SETMETARGN:
				name = "SETMETARGN"; //$NON-NLS-1$
				break;
			case EMR_EXCLUDECLIPRECT:
				name = "EXCLUDECLIPRECT"; //$NON-NLS-1$
				break;
			case EMR_INTERSECTCLIPRECT:
				name = "INTERSECTCLIPRECT"; //$NON-NLS-1$
				break;
			case EMR_SCALEVIEWPORTEXTEX:
				name = "SCALEVIEWPORTEXTEX"; //$NON-NLS-1$
				break;
			case EMR_SCALEWINDOWEXTEX:
				name = "SCALEWINDOWEXTEX"; //$NON-NLS-1$
				break;
			case EMR_SAVEDC:
				name = "SAVEDC"; //$NON-NLS-1$
				break;
			case EMR_RESTOREDC:
				name = "RESTOREDC"; //$NON-NLS-1$
				break;
			case EMR_SETWORLDTRANSFORM:
				name = "SETWORLDTRANSFORM"; //$NON-NLS-1$
				break;
			case EMR_MODIFYWORLDTRANSFORM:
				name = "MODIFYWORLDTRANSFORM"; //$NON-NLS-1$
				break;
			case EMR_SELECTOBJECT:
				name = "SELECTOBJECT"; //$NON-NLS-1$
				break;
			case EMR_CREATEPEN:
				name = "CREATEPEN"; //$NON-NLS-1$
				break;
			case EMR_CREATEBRUSHINDIRECT:
				name = "CREATEBRUSHINDIRECT"; //$NON-NLS-1$
				break;
			case EMR_DELETEOBJECT:
				name = "DELETEOBJECT"; //$NON-NLS-1$
				break;
			case EMR_ANGLEARC:
				name = "ANGLEARC"; //$NON-NLS-1$
				break;
			case EMR_ELLIPSE:
				name = "ELLIPSE"; //$NON-NLS-1$
				break;
			case EMR_RECTANGLE:
				name = "RECTANGLE"; //$NON-NLS-1$
				break;
			case EMR_ROUNDRECT:
				name = "ROUNDRECT"; //$NON-NLS-1$
				break;
			case EMR_ARC:
				name = "ARC"; //$NON-NLS-1$
				break;
			case EMR_CHORD:
				name = "CHORD"; //$NON-NLS-1$
				break;
			case EMR_PIE:
				name = "PIE"; //$NON-NLS-1$
				break;
			case EMR_SELECTPALETTE:
				name = "SELECTPALETTE"; //$NON-NLS-1$
				break;
			case EMR_CREATEPALETTE:
				name = "CREATEPALETTE"; //$NON-NLS-1$
				break;
			case EMR_SETPALETTEENTRIES:
				name = "SETPALETTEENTRIES"; //$NON-NLS-1$
				break;
			case EMR_RESIZEPALETTE:
				name = "RESIZEPALETTE"; //$NON-NLS-1$
				break;
			case EMR_REALIZEPALETTE:
				name = "REALIZEPALETTE"; //$NON-NLS-1$
				break;
			case EMR_EXTFLOODFILL:
				name = "EXTFLOODFILL"; //$NON-NLS-1$
				break;
			case EMR_LINETO:
				name = "LINETO"; //$NON-NLS-1$
				break;
			case EMR_ARCTO:
				name = "ARCTO"; //$NON-NLS-1$
				break;
			case EMR_POLYDRAW:
				name = "POLYDRAW"; //$NON-NLS-1$
				break;
			case EMR_SETARCDIRECTION:
				name = "SETARCDIRECTION"; //$NON-NLS-1$
				break;
			case EMR_SETMITERLIMIT:
				name = "SETMITERLIMIT"; //$NON-NLS-1$
				break;
			case EMR_BEGINPATH:
				name = "BEGINPATH"; //$NON-NLS-1$
				break;
			case EMR_ENDPATH:
				name = "ENDPATH"; //$NON-NLS-1$
				break;
			case EMR_CLOSEFIGURE:
				name = "CLOSEFIGURE"; //$NON-NLS-1$
				break;
			case EMR_FILLPATH:
				name = "FILLPATH"; //$NON-NLS-1$
				break;
			case EMR_STROKEANDFILLPATH:
				name = "STROKEANDFILLPATH"; //$NON-NLS-1$
				break;
			case EMR_STROKEPATH:
				name = "STROKEPATH"; //$NON-NLS-1$
				break;
			case EMR_FLATTENPATH:
				name = "FLATTENPATH"; //$NON-NLS-1$
				break;
			case EMR_WIDENPATH:
				name = "WIDENPATH"; //$NON-NLS-1$
				break;
			case EMR_SELECTCLIPPATH:
				name = "SELECTCLIPPATH"; //$NON-NLS-1$
				break;
			case EMR_ABORTPATH:
				name = "ABORTPATH"; //$NON-NLS-1$
				break;
			case EMR_GDICOMMENT:
				name = "GDICOMMENT"; //$NON-NLS-1$
				break;
			case EMR_FILLRGN:
				name = "FILLRGN"; //$NON-NLS-1$
				break;
			case EMR_FRAMERGN:
				name = "FRAMERGN"; //$NON-NLS-1$
				break;
			case EMR_INVERTRGN:
				name = "INVERTRGN"; //$NON-NLS-1$
				break;
			case EMR_PAINTRGN:
				name = "PAINTRGN"; //$NON-NLS-1$
				break;
			case EMR_EXTSELECTCLIPRGN:
				name = "EXTSELECTCLIPRGN"; //$NON-NLS-1$
				break;
			case EMR_BITBLT:
				name = "BITBLT"; //$NON-NLS-1$
				break;
			case EMR_STRETCHBLT:
				name = "STRETCHBLT"; //$NON-NLS-1$
				break;
			case EMR_MASKBLT:
				name = "MASKBLT"; //$NON-NLS-1$
				break;
			case EMR_PLGBLT:
				name = "PLGBLT"; //$NON-NLS-1$
				break;
			case EMR_SETDIBITSTODEVICE:
				name = "SETDIBITSTODEVICE"; //$NON-NLS-1$
				break;
			case EMR_STRETCHDIBITS:
				name = "STRETCHDIBITS"; //$NON-NLS-1$
				break;
			case EMR_EXTCREATEFONTINDIRECTW:
				name = "EXTCREATEFONTINDIRECTW"; //$NON-NLS-1$
				break;
			case EMR_EXTTEXTOUTA:
				name = "EXTTEXTOUTA"; //$NON-NLS-1$
				break;
			case EMR_EXTTEXTOUTW:
				name = "EXTTEXTOUTW"; //$NON-NLS-1$
				break;
			case EMR_POLYBEZIER16:
				name = "POLYBEZIER16"; //$NON-NLS-1$
				break;
			case EMR_POLYGON16:
				name = "POLYGON16"; //$NON-NLS-1$
				break;
			case EMR_POLYLINE16:
				name = "POLYLINE16"; //$NON-NLS-1$
				break;
			case EMR_POLYBEZIERTO16:
				name = "POLYBEZIERTO16"; //$NON-NLS-1$
				break;
			case EMR_POLYLINETO16:
				name = "POLYLINETO16"; //$NON-NLS-1$
				break;
			case EMR_POLYPOLYLINE16:
				name = "POLYPOLYLINE16"; //$NON-NLS-1$
				break;
			case EMR_POLYPOLYGON16:
				name = "POLYPOLYGON16"; //$NON-NLS-1$
				break;
			case EMR_POLYDRAW16:
				name = "POLYDRAW16"; //$NON-NLS-1$
				break;
			case EMR_CREATEMONOBRUSH:
				name = "CREATEMONOBRUSH"; //$NON-NLS-1$
				break;
			case EMR_CREATEDIBPATTERNBRUSHPT:
				name = "CREATEDIBPATTERNBRUSHPT"; //$NON-NLS-1$
				break;
			case EMR_EXTCREATEPEN:
				name = "EXTCREATEPEN"; //$NON-NLS-1$
				break;
			case EMR_POLYTEXTOUTA:
				name = "POLYTEXTOUTA"; //$NON-NLS-1$
				break;
			case EMR_POLYTEXTOUTW:
				name = "POLYTEXTOUTW"; //$NON-NLS-1$
				break;
			case EMR_SETICMMODE:
				name = "SETICMMODE"; //$NON-NLS-1$
				break;
			case EMR_CREATECOLORSPACE:
				name = "CREATECOLORSPACE"; //$NON-NLS-1$
				break;
			case EMR_SETCOLORSPACE:
				name = "SETCOLORSPACE"; //$NON-NLS-1$
				break;
			case EMR_DELETECOLORSPACE:
				name = "DELETECOLORSPACE"; //$NON-NLS-1$
				break;
			case EMR_GLSRECORD:
				name = "GLSRECORD"; //$NON-NLS-1$
				break;
			case EMR_GLSBOUNDEDRECORD:
				name = "GLSBOUNDEDRECORD"; //$NON-NLS-1$
				break;
			case EMR_PIXELFORMAT:
				name = "PIXELFORMAT"; //$NON-NLS-1$
				break;
			case EMR_DRAWESCAPE:
				name = "DRAWESCAPE"; //$NON-NLS-1$
				break;
			case EMR_EXTESCAPE:
				name = "EXTESCAPE"; //$NON-NLS-1$
				break;
			case EMR_STARTDOC:
				name = "STARTDOC"; //$NON-NLS-1$
				break;
			case EMR_SMALLTEXTOUT:
				name = "SMALLTEXTOUT"; //$NON-NLS-1$
				break;
			case EMR_FORCEUFIMAPPING:
				name = "FORCEUFIMAPPING"; //$NON-NLS-1$
				break;
			case EMR_NAMEDESCAPE:
				name = "NAMEDESCAPE"; //$NON-NLS-1$
				break;
			case EMR_COLORCORRECTPALETTE:
				name = "COLORCORRECTPALETTE"; //$NON-NLS-1$
				break;
			case EMR_SETICMPROFILEA:
				name = "SETICMPROFILEA"; //$NON-NLS-1$
				break;
			case EMR_SETICMPROFILEW:
				name = "SETICMPROFILEW"; //$NON-NLS-1$
				break;
			case EMR_ALPHABLEND:
				name = "ALPHABLEND"; //$NON-NLS-1$
				break;
			case EMR_ALPHADIBBLEND:
				name = "ALPHADIBBLEND"; //$NON-NLS-1$
				break;
			case EMR_TRANSPARENTBLT:
				name = "TRANSPARENTBLT"; //$NON-NLS-1$
				break;
			case EMR_TRANSPARENTDIB:
				name = "TRANSPARENTDIB"; //$NON-NLS-1$
				break;
			case EMR_GRADIENTFILL:
				name = "GRADIENTFILL"; //$NON-NLS-1$
				break;
			case EMR_SETLINKEDUFIS:
				name = "SETLINKEDUFIS"; //$NON-NLS-1$
				break;
			case EMR_SETTEXTJUSTIFICATION:
				name = "SETTEXTJUSTIFICATION"; //$NON-NLS-1$
				break;
			default:
				name = "<unknown>"; //$NON-NLS-1$
				break;				
		}
		
		return name;
	}
}

