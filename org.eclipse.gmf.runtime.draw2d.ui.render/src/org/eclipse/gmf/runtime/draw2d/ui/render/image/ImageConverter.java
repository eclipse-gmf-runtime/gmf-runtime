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


package org.eclipse.gmf.runtime.draw2d.ui.render.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.PlatformUI;

/**
 * This is a helper class used to convert an SWT Image into an AWT
 * BufferedImage.
 * 
 * @author Jody Schofield / sshaw
 * @deprecated Will be removed from public api.  GMF shouldn't have AWT based classes exposed in it's public api.
 * This class will move to internal package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.image.
 * Clients of <code>RenderedImage</code> can use the <code>IAdapter</code> interface
 */
public class ImageConverter {

	private static final PaletteData PALETTE_DATA = new PaletteData(0xFF0000, 0xFF00, 0xFF);
	
	/**
	 * Converts an AWT based buffered image into an SWT <code>Image</code>.  This will always return an
	 * <code>Image</code> that has 24 bit depth regardless of the type of AWT buffered image that is 
	 * passed into the method.
	 * 
	 * @param srcImage the {@link java.awt.image.BufferedImage} to be converted to an <code>Image</code>
	 * @return an <code>Image</code> that represents the same image data as the AWT 
	 * <code>BufferedImage</code> type.
	 */
	public static Image convert( BufferedImage srcImage) {
		// We can force bitdepth to be 24 bit because BufferedImage getRGB allows us to always
		// retrieve 24 bit data regardless of source color depth.
		ImageData swtImageData =
			new ImageData(srcImage.getWidth(), srcImage.getHeight(), 24, PALETTE_DATA);

		// ensure scansize is aligned on 32 bit.
		int scansize = (((srcImage.getWidth() * 3) + 3) * 4) / 4;
		
		WritableRaster alphaRaster = srcImage.getAlphaRaster();
		byte[] alphaBytes = new byte[srcImage.getWidth()];
			
		for (int y=0; y<srcImage.getHeight(); y++) {
			int[] buff = srcImage.getRGB(0, y, srcImage.getWidth(), 1, null, 0, scansize);
			swtImageData.setPixels(0, y, srcImage.getWidth(), buff, 0);
			
			// check for alpha channel
			if (alphaRaster != null) {
				int[] alpha = alphaRaster.getPixels(0, y, srcImage.getWidth(), 1, (int[])null);
				for (int i=0; i<srcImage.getWidth(); i++)
					alphaBytes[i] = (byte)alpha[i];
				swtImageData.setAlphas(0, y, srcImage.getWidth(), alphaBytes, 0);
			}
		}
	
		return new Image(PlatformUI.getWorkbench().getDisplay(), swtImageData);
	}
	
	/**
	 * Converts an swt based image into an AWT <code>BufferedImage</code>.  This will always return a
	 * <code>BufferedImage</code> that is of type <code>BufferedImage.TYPE_INT_ARGB</code> regardless of
	 * the type of swt image that is passed into the method.
	 * 
	 * @param srcImage the {@link org.eclipse.swt.graphics.Image} to be converted to a <code>BufferedImage</code>
	 * @return a <code>BufferedImage</code> that represents the same image data as the swt <code>Image</code>
	 */
	public static BufferedImage convert( Image srcImage ) {
		
		ImageData imageData = srcImage.getImageData();
		int width = imageData.width;
		int height = imageData.height;
		ImageData maskData = null;
		int alpha[] = new int[1];
		
		if (imageData.alphaData == null)
			maskData = imageData.getTransparencyMask();
		
		// now we should have the image data for the bitmap, decompressed in imageData[0].data.
		// Convert that to a Buffered Image.
		BufferedImage image = new BufferedImage( imageData.width, imageData.height, BufferedImage.TYPE_INT_ARGB );
	    
	    WritableRaster alphaRaster = image.getAlphaRaster();

		// loop over the imagedata and set each pixel in the BufferedImage to the appropriate color.
		for( int y = 0; y < height; y++ )
		{
			for( int x = 0; x < width; x++ )
			{
				int color = imageData.getPixel( x, y );

				color = translateColor( imageData, color );
				image.setRGB( x, y, color );
		
				// check for alpha channel
				if (alphaRaster != null) {
					if( imageData.alphaData != null) {
						alpha[0] = imageData.getAlpha( x, y );
						alphaRaster.setPixel( x, y, alpha );
					}
					else {
						// check for transparency mask
						if( maskData != null) {
							alpha[0] = maskData.getPixel( x, y ) == 0 ? 0 : 255;
							alphaRaster.setPixel( x, y, alpha );
						}
					}
				}
			}
		}

		return image;
	}


	private static int translateColor( ImageData imageData, int color ) {

		int bitCount = imageData.depth;
		RGB[] rgb = imageData.getRGBs();

		if( bitCount == 1 || bitCount == 4 || bitCount == 8 )
		{
			// Look up actual rgb value in the rgb array.
			if( rgb != null )
			{
				java.awt.Color foo = new java.awt.Color( rgb[color].red, rgb[color].green, rgb[color].blue );
				color = foo.getRGB();
			}
			else
			{
				color = 0;
			}
		}
		else if( bitCount == 16 )
		{
			int		BLUE_MASK					= 0x1f;
			int		GREEN_MASK					= 0x3e0;
			int		RED_MASK					= 0x7C00;
			
			// Each word in the bitmap array represents a single pixels, 5 bits for each
			// red, green and blue.
			color = applyRGBMask( color, RED_MASK, GREEN_MASK, BLUE_MASK );
		}
		else if( bitCount == 24 )
		{
			// 3 8 bit color values.
			int blue = (color & 0x00ff0000) >> 16;
			int green = (color & 0x0000ff00) >> 8;
			int red = (color & 0x000000ff);

			java.awt.Color foo = new java.awt.Color( red, green, blue );
			color = foo.getRGB();
		}
		else if( bitCount == 32 )
		{	
			int blue = (color & 0xff000000) >>> 24;
			int green = (color & 0x00ff0000) >> 16;
			int red = (color & 0x0000ff00) >> 8;

			java.awt.Color foo = new java.awt.Color( red, green, blue );
			color = foo.getRGB();
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
		
		java.awt.Color foo = new java.awt.Color( red, green, blue );
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

}
