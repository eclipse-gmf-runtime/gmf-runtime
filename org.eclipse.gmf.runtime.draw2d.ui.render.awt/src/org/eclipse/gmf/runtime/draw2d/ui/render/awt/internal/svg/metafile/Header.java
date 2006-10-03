/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
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
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IEmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.ITraceMe;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IWmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.WMFTranscoder;

/**  
 * @author dhabib
 */

public class Header implements IEmf2SvgConverter, IWmf2SvgConverter, ITraceMe
{
	/** number of metafile records in the file */
	private int			m_numRecords	= 0;
	/** bounds of the metafile. */
	private Rectangle	m_bounds		= null;
	/** Dots per inch in the x direction */
	private int			m_dpiX			= 96;
	/** Dots per inch in the y direction */
	private int			m_dpiY			= 96;
	
	private static final int	EMF_BOUNDS_OFFSET			= 0;	// Device units, RECTL (4*32b; 16B)
	private static final int	EMF_FRAME_OFFSET			= 16;	// RECTL, HiMetrics, 4*32b; 16B
	private static final int	EMF_SIGNATURE_OFFSET		= 32;	// DWORD, must be ENHMETA_SIGNATURE (=0x464D4520)
//	private static final int	EMF_VERSION_OFFSET			= 36;	// DWORD, version
//	private static final int	EMF_METAFILE_SIZE_OFFSET	= 40;	// DWORD, file size
	private static final int	EMF_NUM_RECORDS_OFFSET		= 44;	// DWORD, num records in file
//	private static final int	EMF_NUM_HANDLES_OFFSET		= 48;	// WORD
//	private static final int	EMF_RESERVED_OFFSET			= 50;	// WORD
//	private static final int	EMF_DESCRIPTION_LEN_OFFSET	= 52;	// DWORD, num unicode chars in description
//	private static final int	EMF_DESCRIPTION_OFF_OFFSET	= 56;	// DWORD, offset in record to description
//	private static final int	EMF_PAL_ENTRIES_OFFSET		= 60;	// DWORD, num entries in the palette
	private static final int	EMF_DEVICE_SIZE_OFFSET		= 64;	// SIZEL, "pels", 2x32b; 8B
	private static final int	EMF_MM_SIZE_OFFSET			= 72;	// SIZEL, mm, 2x32b; 8B

	private static final int	APM_SIGNATURE_OFFSET		= 0;
	private static final int	APM_LEFT_OFFSET				= 6;
	private static final int	APM_TOP_OFFSET				= 8;
	private static final int	APM_RIGHT_OFFSET			= 10;
	private static final int	APM_BOTTOM_OFFSET			= 12;
	private static final int	APM_INCH_OFFSET				= 14;

	private static final int	WMF_FILE_TYPE_OFFSET		= 0;
	private static final int	WMF_HEADER_SIZE_OFFSET		= 2;

	private String _sizes = null;
	public String toString() {
		return _sizes;
	}

	public int getNumRecords()
	{
		return m_numRecords;
	}
	
	public Rectangle getBounds()
	{
		return m_bounds;
	}
	
	public void readEMFRecord( Record rec ) throws IOException
	{
		/* Structure of the metafile header:
		 * 
		 *	type  name				Offset		
		 *	RECTL rclBounds;		8	x8		
		 *	RECTL rclFrame;			24  x18		
		 *	DWORD dSignature;		40  x28		
		 *	DWORD nVersion;			44  x24		
		 *	DWORD nBytes;			48  x30		
		 *	DWORD nRecords;			52  x34		
		 *	WORD  nHandles;			56  x38		
		 *	WORD  sReserved;		58  x3a		
		 *	DWORD nDescription;		60  x3c		
		 *	DWORD offDescription;	64  x40		
		 *	DWORD nPalEntries;		68  x44		
		 *	SIZEL szlDevice;		72  x48		
		 *	SIZEL szlMillimeters;	80  x50		
		 *	
		 *	winver >= 4
		 *	DWORD cbPixelFormat;	88  x58		91
		 *	DWORD offPixelFormat;	92  x5a		95
		 *	DWORD bOpenGL;			96  x60		99
		 *	
		 *	winver >= 5
		 *	SIZEL szlMicrometers;	100	x64		107
		 *
		 *  Palette entries
		 *  GDI Handles???
		 * 
		 * dSignature must be the following series of bytes: 0x20, 0x45, 0x4D, 0x46 ( " EMF") 
		 * 
		 * Since the first two fields in the record are not included in the byte array returned by EMFRecord.getData, actual offsets are
		 * eight bytes less.
		 */
		 
		// get the bounding rectangle
		this.m_bounds = rec.getRectangleLAt( EMF_BOUNDS_OFFSET );
		
		// Bounds are inclusive of the bottom row and right column, need to account for this.
		this.m_bounds.width++;
		this.m_bounds.height++;
	 	
		long signature = rec.getDWORDAt( EMF_SIGNATURE_OFFSET ); 
		// Read the signature and assert that it is what we expect (should be " EMF", or 0x20, 0x45, 0x4D, 0x46
		if( signature != 0x464D4520 )
		{
			throw new IOException( "Invalid Enhanced Metafile Format:  Wrong signature" );	//$NON-NLS-1$
		}
	 	
		// Read the number of EMF records contained in the file.
		m_numRecords = (int) rec.getDWORDAt( EMF_NUM_RECORDS_OFFSET );
		
		// Read in the size, in pixels.
		Dimension deviceSize = rec.getDimensionLAt( EMF_DEVICE_SIZE_OFFSET );			
		
		// Read in the size, in millimeters.
		Dimension mmSize = rec.getDimensionLAt( EMF_MM_SIZE_OFFSET );

		m_dpiX = (int) (( deviceSize.width * 25.4 ) / mmSize.width + 0.5);
		m_dpiY = (int) (( deviceSize.height * 25.4 ) / mmSize.height + 0.5);


		Rectangle pictFrame = rec.getRectangeAt(EMF_FRAME_OFFSET);

		StringBuffer sb = new StringBuffer();
		sb.append("Bounds{device units}=" + m_bounds); //$NON-NLS-1$
		sb.append(", pictFrame{himetr}=" + pictFrame.toString());//$NON-NLS-1$
		sb.append(", deviceSize{pels}=" + deviceSize.toString());//$NON-NLS-1$
		sb.append(", mmSize{mm}=" + mmSize.toString());//$NON-NLS-1$
		sb.append(", dpiX=" + m_dpiX);//$NON-NLS-1$
		sb.append(", dpiY=" + m_dpiY);//$NON-NLS-1$
		_sizes = sb.toString();
	}

	public void readWMFRecord( Record rec ) throws IOException
	{
		// Two kinds of WMF's that we support: Standard and APM.
		// APM is a standard metafile with an additional header tacked on for good measure.
		/*

			Standard header:
			typedef struct _WindowsMetaHeader
			{
			  WORD  FileType;       // Type of metafile (0=memory, 1=disk)
			  WORD  HeaderSize;     // Size of header in WORDS (always 9) 
			  WORD  Version;        // Version of Microsoft Windows used
			  DWORD FileSize;       // Total size of the metafile in WORDs
			  WORD  NumOfObjects;   // Number of objects in the file
			  DWORD MaxRecordSize;  // The size of largest record in WORDs
			  WORD  NumOfParams;    // Not Used (always 0)
			} WMFHEAD;

			// APM header:
			typedef struct _PlaceableMetaHeader
			{
			  DWORD Key;           // Magic number (always 9AC6CDD7h)
			  WORD  Handle;        // Metafile HANDLE number (always 0)
			  SHORT Left;          // Left coordinate in metafile units
			  SHORT Top;           // Top coordinate in metafile units
			  SHORT Right;         // Right coordinate in metafile units
			  SHORT Bottom;        // Bottom coordinate in metafile units
			  WORD  Inch;          // Number of metafile units per inch
			  DWORD Reserved;      // Reserved (always 0)
			  WORD  Checksum;      // Checksum value for previous 10 WORDs
			} PLACEABLEMETAHEADER;
		 */

		long signature = rec.getDWORDAt( APM_SIGNATURE_OFFSET );
		int offset = 0;
		if( signature == WMFTranscoder.APM_HEADER_SIGNATURE )
		{
			// Read the APM header.
			int left 	= rec.getShortAt( APM_LEFT_OFFSET );
			int top 	= rec.getShortAt( APM_TOP_OFFSET );
			int right 	= rec.getShortAt( APM_RIGHT_OFFSET );
			int bottom 	= rec.getShortAt( APM_BOTTOM_OFFSET );
			int inch	= rec.getShortAt( APM_INCH_OFFSET );

			// Appears that the x,y position is ignored in the boundaries.  This is probably
			// just used for placement, not for offsetting the x,y positions in the metafile.
			//m_bounds = new Rectangle( left, top, right - left, bottom - top );
			m_bounds = new Rectangle( 0, 0, right - left, bottom - top );
			
			m_dpiX = inch;
			m_dpiY = inch;
			offset = WMFTranscoder.APM_HEADER_SIZE;
		}
		else
		{
			m_bounds = new Rectangle( 0, 0, 1000, 1000 );
		}

		// Read and verify the WMF Header.
		int type 		= rec.getShortAt( offset + WMF_FILE_TYPE_OFFSET );
		int size 		= rec.getShortAt( offset + WMF_HEADER_SIZE_OFFSET );
			
		if( type != 1 || size != 9 )
		{
			// Not a supported type.  We only support disk metafiles, the
			// header size is always 9, and the version must be either 1 or 3.
			throw new IOException( "Not a valid WMF file" ); 	//$NON-NLS-1$
		}
		
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		context.setBounds( m_bounds );

		// Set the DPI, calculated from the size of the reference device in pixels and in mm.
		// pixels / mm * mm / inch = pixels / inch
		
		context.setDpiX( m_dpiX );
		context.setDpiY( m_dpiY );

		// Set the default viewport extents.  This doesn't seem necessary for EMFs,
		// but for WMF's there appears to be many cases where they use the MM_ISOTROPIC/MMANISOTROPIC
		// mapping mode without first setting the viewport extent.  So, calculate and set a default
		// here.
		context.setViewportExtent( new Point( m_bounds.width, m_bounds.height ) );
		
		// Set the background color to white;
		g.setColor( new Color( 0xff, 0xff, 0xff ) );
		g.setBackground( new Color( 0xff, 0xff, 0xff ) );
	}
}
