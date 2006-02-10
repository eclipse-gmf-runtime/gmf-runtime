/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.RGB;

/**
 * @author dhabib
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.render.*
 */
class BitmapHelper 
{
	private static final int		COMPRESSION_TYPE_OFFSET 	= 16;
	private static final int		BMI_BITCOUNT_OFFSET			= 14;
	private static final int		BMI_COLORS_OFFSET 			= 32;
	private static final int		BCH_BITCOUNT_OFFSET			= 10;

//	private static final int		BI_RGB						= 0;
//	private static final int		BI_RLE8						= 1;
//	private static final int		BI_RLE4						= 2;
	private static final int		BI_BITFIELDS				= 3;

	private static final int		BASE_BMI_SIZE				= 40;
	private static final int		BASE_BCH_SIZE				= 12;
	
	private static final int		BLUE_MASK					= 0x1f;
	private static final int		GREEN_MASK					= 0x3e0;
	private static final int		RED_MASK					= 0x7C00;

	static BufferedImage readBitmap(	Record rec, 
										int bmiOffset, 
										int bmiSize, 
										int bitOffset, 
										int bitSize ) throws IOException
	{
		int compressionType = rec.getIntAt( bmiOffset + COMPRESSION_TYPE_OFFSET );
		int bitCount		= rec.getShortAt( bmiOffset + BMI_BITCOUNT_OFFSET );
		
		// Read the data in using SWT's image code.

		// First, create an input stream that looks like a file on the disk (header + bitmapinfo + palette + data)
		int headerSize = 14;
		int size = bmiSize + bitSize + headerSize;
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out.write( (byte) 0x42 );				// signature1
		out.write( (byte) 0x4D );				// signature2
		writeInt( out, size );					// File size.
		writeInt( out, 0 );						// 2 reserved words, always 0

		int dataOffset = headerSize + (bitOffset - bmiOffset);

		// The SWT image loader always assumes that there is a palette if there are <= 8 bits per 
		// pixel.  In some cases (ie: CreatePatternBrush) there is either no palette (monochrome brush)
		// or the palette is not as large as it *should* be.  So, we have to pad it.
		int numEntries = getNumberOfPaletteEntries( rec, bmiOffset );
		
		int requiredPaletteSize = numEntries * 4;	// 4 bytes per entry
		int actualPaletteSize = bitOffset - bmiOffset - BASE_BMI_SIZE;
		int numFakePaletteEntriesToWrite = 0;
		
		if( bitCount <= 8 && requiredPaletteSize > actualPaletteSize )
		{
			numFakePaletteEntriesToWrite = ( requiredPaletteSize - actualPaletteSize ) / 4;
		}

		if( numFakePaletteEntriesToWrite > 0 )
		{
			dataOffset += numFakePaletteEntriesToWrite * 4;	
		}
 
		writeInt( out, dataOffset ); 	// offset to the data.		
		
		// write the bitmap info.
		byte[] bmi = rec.getBytesAt( bmiOffset, bmiSize );
		
		if( compressionType == BI_BITFIELDS )
		{
			// Hack, change compression type to RGB for now since SWT doesn't currently
			// support BI_BITFIELDS.  Only difference is the order of the color, which
			// we can correct.
			bmi[ COMPRESSION_TYPE_OFFSET  ] = 0;
		}
		
		out.write( bmi );
		
		// Write the fake palette.
		for( int index = 0; index < numFakePaletteEntriesToWrite; index++ )
		{
			writeInt( out, index );
		}

		// Write the data.
		byte[] bits = rec.getBytesAt( bitOffset, bitSize );
		out.write( bits );
		
		// Load the image using SWT's image loading functionality.
		byte[] bmpData = out.toByteArray();
		
		ByteArrayInputStream stream = new ByteArrayInputStream( bmpData );
		
		ImageLoader imageLoader = new ImageLoader();
		ImageData imageData[] = imageLoader.load( stream );
		
		// now we should have the image data for the bitmap, decompressed in imageData[0].data.
		// Convert that to a Buffered Image.
		BufferedImage image = new BufferedImage( imageData[0].width, imageData[0].height, BufferedImage.TYPE_3BYTE_BGR );

		// loop over the imagedata and set each pixel in the BufferedImage to the appropriate color.
		for( int y = 0; y < imageData[0].height; y++ )
		{
			for( int x = 0; x < imageData[0].width; x++ )
			{
				int color = imageData[0].getPixel( x, y );
				
				color = translateColor( rec, bmiOffset, bitCount, compressionType, imageData[ 0 ], color );
			
				image.setRGB( x, y, color );
			}
		}

		return image;
	}
	
	static int getNumberOfPaletteEntries( Record rec, int bmiOffset ) throws IOException
	{
		// Fortunately we are always using 'packed' bitmap info structures.  This is supposed
		// to guarantee that biClrUsed is either the actual number of entries in the palette
		// or it is 0.  If it is 0, the number of palette entries is either the number of 
		// 2 ^ bitCount or it is actually 0.  I don't think the palette is ever more than 256
		// in length, so only 8 bit entries matter.
		
		int bmiSize = rec.getIntAt( bmiOffset );
		int numEntries = 0;
		
		if( bmiSize == BASE_BCH_SIZE )
		{
			// This is a BITMAPCOREHEADER
			int bitCount = rec.getShortAt( bmiOffset + BCH_BITCOUNT_OFFSET );
			
			if( bitCount <= 8 )
			{
				numEntries = 1 << bitCount;
			}
		}
		else
		{
			// This is a BITMAPINFOHEADER
			int bitCount = rec.getShortAt( bmiOffset + BMI_BITCOUNT_OFFSET );

			numEntries = rec.getIntAt( bmiOffset + BMI_COLORS_OFFSET );
			
			if( numEntries == 0 && bitCount <= 8 )
			{	
				numEntries = 1 << bitCount;
			}
		}

		return numEntries;
	}
	
	static int getHeaderSize( Record rec, int bmiOffset, int usage ) throws IOException
	{
		int numColorEntries = getNumberOfPaletteEntries( rec, bmiOffset );
		int bmiSize = rec.getIntAt( bmiOffset );
		int multiplier = 4; // size of rgb quad
		int size = 0;
		
		if( usage == DeviceContext.DIB_PAL_COLORS )
		{
			multiplier = 2; // size of palette entry
		}
		
		if( bmiSize == BASE_BCH_SIZE )
		{
			size = BASE_BCH_SIZE;
		}
		else
		{
			size = BASE_BMI_SIZE;
		}
		
		size += numColorEntries * multiplier;
		
		return size;
	}
	
	private static void writeInt( ByteArrayOutputStream out, int val ) throws IOException 
	{
		// Little endian write
		out.write( (byte) ( val & 0xff ) );
		out.write( (byte) ( ( val >> 8 ) & 0xff ) );
		out.write( (byte) ( ( val >> 16 ) & 0xff ) );
		out.write( (byte) ( ( val >> 24 ) & 0xff ) );
	}

	private static int translateColor( Record rec, 
									   int bmiOffset, 
									   int bitCount, 
									   int compressionType, 
									   ImageData imageData, 
									   int color ) throws IOException
	{
		RGB[] rgb = imageData.getRGBs();
				
		if( bitCount == 1 || bitCount == 4 || bitCount == 8 )
		{
			// Look up actual rgb value in the rgb array.
			if( rgb != null )
			{
				Color foo = new Color( rgb[color].red, rgb[color].green, rgb[color].blue );
				color = foo.getRGB();
			}
			else
			{
				color = 0;
			}
		}
		else if( bitCount == 16 )
		{
			if( compressionType == BI_BITFIELDS )
			{
				// Color mask is being used, stored in the first 3 entries in the palette.
				//Get the color mask
				int redMask 	= (int) rec.getDWORDAt( bmiOffset + BASE_BMI_SIZE );
				int greenMask 	= (int) rec.getDWORDAt( bmiOffset + BASE_BMI_SIZE + 4 );
				int blueMask 	= (int) rec.getDWORDAt( bmiOffset + BASE_BMI_SIZE + 8 );
				
				// Bytes are in the wrong order.
				color = applyRGBMask( color, redMask, greenMask, blueMask );
			}
			else
			{
				// Each word in the bitmap array represents a single pixels, 5 bits for each
				// red, green and blue.
				color = applyRGBMask( color, RED_MASK, GREEN_MASK, BLUE_MASK );
			}
		}
		else if( bitCount == 24 )
		{
			// 3 8 bit color values.
			int blue = (color & 0x00ff0000) >> 16;
			int green = (color & 0x0000ff00) >> 8;
			int red = (color & 0x000000ff);

			Color foo = new Color( red, green, blue );
			color = foo.getRGB();
		}
		else if( bitCount == 32 )
		{	
			if( compressionType == BI_BITFIELDS )
			{
				// Mask the color
				int redMask 	= (int) rec.getDWORDAt( bmiOffset + BASE_BMI_SIZE );
				int greenMask 	= (int) rec.getDWORDAt( bmiOffset + BASE_BMI_SIZE + 4 );
				int blueMask 	= (int) rec.getDWORDAt( bmiOffset + BASE_BMI_SIZE + 8 );
				
				// Bytes are in the wrong order.
				color = flipBytes( color );
				color = applyRGBMask( color, redMask, greenMask, blueMask );
			}
			else
			{
				int blue = (color & 0xff000000) >>> 24;
				int green = (color & 0x00ff0000) >> 16;
				int red = (color & 0x0000ff00) >> 8;

				Color foo = new Color( red, green, blue );
				color = foo.getRGB();
			}
		}

		return color;
	}

	private static int applyRGBMask( int color, int redMask, int greenMask, int blueMask )
	{
		int shiftCount;
		int maskSize;
		int red;
		int green;
		int blue;
		
		shiftCount = getShiftCount( redMask );
		maskSize = countBits( redMask );
		red = ( color & redMask ) >>> shiftCount;
		// Scale the color value to something between 0 and 255.
		red = red * 255 / ( (int) Math.pow( 2, maskSize ) - 1 );
		
		shiftCount = getShiftCount( greenMask );
		maskSize = countBits( greenMask );
		green = ( color & greenMask ) >>> shiftCount;
		// Scale the color value to something between 0 and 255.
		green = green * 255 / ( (int) Math.pow( 2, maskSize ) - 1 );

		shiftCount = getShiftCount( blueMask );
		maskSize = countBits( blueMask );
		blue = ( color & blueMask ) >>> shiftCount;
		// Scale the color value to something between 0 and 255.
		blue = blue * 255 / ( (int) Math.pow( 2, maskSize ) - 1 );
		
		Color foo = new Color( red, green, blue );
		color = foo.getRGB();

		return color;
	}
	
	private static int getShiftCount( int mask )
	{
		int count = 0;
		
		while( mask != 0 && ( ( mask & 0x1 ) == 0 ) )
		{
			mask = mask >>> 1;
			count++;
		}
		
		return count;
	}
	
	private static int countBits( int mask )
	{
		int count = 0;
		for( int index = 0; index < 32; index++ )
		{
			if( ( mask & 0x1 ) != 0 )
			{
				count++;
			}
			mask = mask >>> 1;
		}
		
		return count;
	}

	private static int flipBytes( int data )
	{
		int byte1 = data & 0xff;
		int byte2 = (data & 0xff00) >>> 8;
		int byte3 = (data & 0xff0000) >>> 16;
		int byte4 = (data & 0xff000000) >>> 24;
		
		data = byte1 << 24;
		data += byte2 << 16;
		data += byte3 << 8;
		data += byte4;
		
		return data;
	}

}

