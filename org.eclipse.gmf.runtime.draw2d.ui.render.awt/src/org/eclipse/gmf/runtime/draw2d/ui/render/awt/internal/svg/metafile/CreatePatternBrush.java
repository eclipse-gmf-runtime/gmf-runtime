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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.AbstractTranscoder;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.EMFRecord;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.GdiBrush;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IEmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IWmf2SvgConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;

/**
 * @author dhabib
 *
 */ 
public class CreatePatternBrush implements IEmf2SvgConverter, IWmf2SvgConverter
{
	private static final int	EMF_ID_OFFSET			= 0;
//	private static final int	EMF_USAGE_OFFSET		= 4;
	private static final int	EMF_BMI_OFFSET_OFFSET	= 8;
	private static final int	EMF_BMI_SIZE_OFFSET		= 12;
	private static final int	EMF_BITS_OFFSET_OFFSET	= 16;
	private static final int	EMF_BITS_SIZE_OFFSET	= 20;

	private static final int	WMF_BMI_OFFSET			= 4;
//	private static final int	WMF_DEPTH_OFFSET		= 18;

	private boolean			m_bDibPattern	= false;
	private int				m_id 			= -1;
	private BufferedImage	m_image			= null;
	private int				m_type 			= GdiBrush.BS_MONOPATTERN;
	
	public CreatePatternBrush( boolean bDibPattern )
	{
		m_bDibPattern = bDibPattern;
	}
	
	public void readWMFRecord( Record rec ) throws IOException
	{
		// First 2 bytes are the 'type', which could be BS_PATTERN or BS_DIBPATTERN
		m_type = rec.getShortAt( 0 );
		
		int bmiOffset = 0;
		int bmiSize = 0;
		int bitOffset = 0;
		int bitSize = 0;
		
		switch( m_type )
		{
			case GdiBrush.BS_MONOPATTERN:
			case GdiBrush.BS_PATTERN:
			case GdiBrush.BS_PATTERN8X8:
			case GdiBrush.BS_DIBPATTERN:
			case GdiBrush.BS_DIBPATTERN8X8:
			{
				bmiOffset = WMF_BMI_OFFSET;
				
				int tableSize = BitmapHelper.getNumberOfPaletteEntries( rec, WMF_BMI_OFFSET );
				tableSize *= 4;	// Each entry is 4 bytes long.
				bmiSize = (int) rec.getDWORDAt( bmiOffset ) + tableSize;
				bitOffset = bmiOffset + bmiSize;
				bitSize = rec.getSize() - bitOffset;
				break;
			}
			default:
				AbstractTranscoder.logMessage( "Unknown/Unsupported pattern type encountered: " + m_type );//$NON-NLS-1$
				return;
		}
		
		m_image = BitmapHelper.readBitmap( rec, bmiOffset, bmiSize, bitOffset, bitSize );
	}
	
	public void readEMFRecord( Record rec ) throws IOException
	{
		if( m_bDibPattern )
		{
			m_type = GdiBrush.BS_DIBPATTERN;
		}
		
		m_id 	= (int) rec.getDWORDAt( EMF_ID_OFFSET );
		
		// Header is not included in record, so subtract off the size of the header.
		int bmiOffset	= (int) rec.getDWORDAt( EMF_BMI_OFFSET_OFFSET ) - EMFRecord.EMR_HEADER_SIZE;	
		int bmiSize		= (int) rec.getDWORDAt( EMF_BMI_SIZE_OFFSET );
		int bitOffset	= (int) rec.getDWORDAt( EMF_BITS_OFFSET_OFFSET ) - EMFRecord.EMR_HEADER_SIZE;		
		int bitSize		= (int) rec.getDWORDAt( EMF_BITS_SIZE_OFFSET );
		
		m_image = BitmapHelper.readBitmap( rec, bmiOffset, bmiSize, bitOffset, bitSize );
	}

	public void render( Graphics2D g, DeviceContext context ) throws TranscoderException
	{
		if( m_id == -1 )
		{	
			m_id 	= context.getNextObjectId();
		}

		context.addGDI( m_id, new GdiBrush( m_type, m_image ) );
	}

}



