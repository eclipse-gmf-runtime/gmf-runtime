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

package org.eclipse.gmf.tests.runtime.draw2d.ui.render.internal.svg;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.factory.RenderedImageFactory;
import org.eclipse.gmf.runtime.draw2d.ui.render.image.ImageConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;



/**
 * @author sshaw
 */
public class RenderMultipleSVGTest extends TestCase
{
	private String m_srcPath;
	
	public RenderMultipleSVGTest( String name )
	{
		super( name );
	}

	public static void main( String[] args ) 
	{
		TestRunner.run( suite() );
	}

	public static Test suite() 
	{
		return new TestSuite( RenderMultipleSVGTest.class );
	}

	protected void setUp() throws Exception 
	{
		super.setUp();
		
		// Initialize the path the the resources.
		URL pluginURL = Platform.getBundle("org.eclipse.gmf.tests.runtime.draw2d.ui.render" ).getEntry("/"); //$NON-NLS-1$ //$NON-NLS-2$
		String pluginPath = Platform.resolve( pluginURL ).getPath().substring(1);
		m_srcPath = pluginPath + "resources/metafiles/";  //$NON-NLS-1$
	}
	
	protected void tearDown() throws Exception 
	{
		super.tearDown();
	}
	
	public void testRenderFromSVG() throws Exception
	{
		// Enumerate all the *.emf files in the source directory.
		File dir = new File( m_srcPath );
		
		Assert.assertTrue( "Directory doesn't exist: " + m_srcPath, dir.exists() );	//$NON-NLS-1$
		Assert.assertTrue( "Expected a directory: " + m_srcPath, dir.isDirectory() );	//$NON-NLS-1$
		
		Vector failures = new Vector();
		
		if( dir.exists() && dir.isDirectory() )
		{	
			String[] files = dir.list( new SVGFileFilter() );
			Assert.assertTrue( "No files in input directory: " + m_srcPath, files.length > 0 );	//$NON-NLS-1$
			
			long startTime = System.currentTimeMillis();
			
			for( int index = 0; index < files.length; index++ )
			{
				// Get the emf file to translate.
				String 			metafileName		= m_srcPath + files[ index ];					
				try
				{
					RenderedImage directSWTRender = RenderedImageFactory.getInstance(metafileName);	
					
					BufferedImage bufImg = (BufferedImage)directSWTRender.getAdapter(BufferedImage.class);
					Image swtImgOriginal = ImageConverter.convert(bufImg);
					
					ImageLoader imageLoader = new ImageLoader();
					imageLoader.data = new ImageData[] {swtImgOriginal.getImageData()};
					imageLoader.logicalScreenHeight = swtImgOriginal.getBounds().width;
					imageLoader.logicalScreenHeight = swtImgOriginal.getBounds().height;
					
					String svgOrigFilename = metafileName.substring( 0, metafileName.length() - 4 ) + "ORIG" + ".bmp"; //$NON-NLS-1$ //$NON-NLS-2$
					
					imageLoader.save(svgOrigFilename, SWT.IMAGE_BMP);
					swtImgOriginal.dispose();
				}
				catch( Exception e )
				{
					String errorMessage = "Caught exception while processing file " + metafileName +	//$NON-NLS-1$
											"\n" + e.toString();	//$NON-NLS-1$
					
					failures.add( errorMessage );
				}
			}
			
			long awtTime = System.currentTimeMillis() - startTime;
			startTime = System.currentTimeMillis();
			
			for( int index = 0; index < files.length; index++ )
			{
				// Get the emf file to translate.
				String 			metafileName		= m_srcPath + files[ index ];
				try
				{
					RenderedImage directSWTRender = RenderedImageFactory.getInstance(metafileName);	
					Image swtImgNew = directSWTRender.getSWTImage();
					
					ImageLoader imageLoader = new ImageLoader();
					imageLoader.data = new ImageData[] {swtImgNew.getImageData()};
					imageLoader.logicalScreenHeight = swtImgNew.getBounds().width;
					imageLoader.logicalScreenHeight = swtImgNew.getBounds().height;

					String svgNewFilename = metafileName.substring( 0, metafileName.length() - 4 ) + "NEW" + ".bmp"; //$NON-NLS-1$ //$NON-NLS-2$
					
					imageLoader.save(svgNewFilename, SWT.IMAGE_BMP);
				}
				catch( Exception e )
				{
					String errorMessage = "Caught exception while processing file " + metafileName +	//$NON-NLS-1$
											"\n" + e.toString();	//$NON-NLS-1$
					
					failures.add( errorMessage );
				}
			}
			
			long swtTime = System.currentTimeMillis() - startTime;
			System.out.println("AWT rendering time was: " + awtTime); //$NON-NLS-1$
			System.out.println("SWT rendering time was: " + swtTime); //$NON-NLS-1$
			System.out
				.println("Percentage difference: " + (swtTime - awtTime) / (float) awtTime * 100 + "%"); //$NON-NLS-1$ //$NON-NLS-2$

			assertTrue(swtTime < awtTime);

			
			if( failures.size() > 0 )
			{	
				String failureString = "";	//$NON-NLS-1$
				for( int index = 0; index < failures.size(); index++ )
				{
					failureString = failureString + (String) failures.get( index );
				}
				Assert.assertEquals( "Found failures:\n" + failureString, 0, failures.size() );	//$NON-NLS-1$
			}
		}
	}

	private class SVGFileFilter implements FilenameFilter
	{
		public boolean accept( File file, String name )
		{
			String lowerCaseName = name.toLowerCase();
			return lowerCaseName.endsWith( ".svg" );		//$NON-NLS-1$
		}
	}
}
