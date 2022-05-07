/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.tests.runtime.draw2d.ui.render.internal.svg;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.image.ImageConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.factory.RenderedImageFactory;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;



/**
 * @author sshaw
 */
public class RenderMultipleSVGTest extends TestCase
{
	private Bundle bundle;
	
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
        bundle = Platform.getBundle("org.eclipse.gmf.tests.runtime.draw2d.ui.render" );//$NON-NLS-1$
	}
	
	protected void tearDown() throws Exception 
	{
		super.tearDown();
	}
	
	public void testRenderFromSVG() throws Exception
	{
		// Enumerate all the *.emf files in the source directory.
        String rootDir = "resources/metafiles/"; //$NON-NLS-1$
		Enumeration enumFiles = bundle.getEntryPaths(rootDir);
        
        long startTime = System.currentTimeMillis();
        Vector failures = new Vector();
        
        while (enumFiles.hasMoreElements()) {    
		    String metafileName = (String)enumFiles.nextElement();
            IPath filePath = new Path(metafileName);
			if (filePath != null && (metafileName.endsWith("svg") ||    //$NON-NLS-1$
                                    metafileName.endsWith("emf")  ||    //$NON-NLS-1$
                                    metafileName.endsWith("wmf"))) {     //$NON-NLS-1$
                try
    			{
                    URL url = FileLocator.find(bundle, filePath, null);
                    RenderedImage directSWTRender = RenderedImageFactory.getInstance(url);	
    					
    				BufferedImage bufImg = (BufferedImage)directSWTRender.getAdapter(BufferedImage.class);
    				Image swtImgOriginal = ImageConverter.convert(bufImg);
                     if (swtImgOriginal == null)
                         failures.add( metafileName );
//    				ImageLoader imageLoader = new ImageLoader();
//    				imageLoader.data = new ImageData[] {swtImgOriginal.getImageData()};
//    				imageLoader.logicalScreenHeight = swtImgOriginal.getBounds().width;
//    				imageLoader.logicalScreenHeight = swtImgOriginal.getBounds().height;
//    					
//    				String svgOrigFilename = metafileName.substring( 0, metafileName.length() - 4 ) + "ORIG" + ".bmp"; //$NON-NLS-1$ //$NON-NLS-2$
//    					
//    				imageLoader.save(svgOrigFilename, SWT.IMAGE_BMP);
//    				swtImgOriginal.dispose();
    			}
    			catch( Exception e )
    			{
    				String errorMessage = "Caught exception while processing file " + metafileName +	//$NON-NLS-1$
    										"\n" + e.toString();	//$NON-NLS-1$
    					
    				failures.add( errorMessage );
    			}
            }
		}
		
		enumFiles = bundle.getEntryPaths(rootDir);
     
		long awtTime = System.currentTimeMillis() - startTime;
		startTime = System.currentTimeMillis();
			
        while (enumFiles.hasMoreElements()) {
            String metafileName = (String)enumFiles.nextElement();
            IPath filePath = new Path(metafileName);
            if (filePath != null && (metafileName.endsWith("svg") ||    //$NON-NLS-1$
                    metafileName.endsWith("emf")  ||    //$NON-NLS-1$
                    metafileName.endsWith("wmf"))) {     //$NON-NLS-1$
    			try
    			{
                    URL url = FileLocator.find(bundle, filePath, null);
                    RenderedImage directSWTRender = RenderedImageFactory.getInstance(url);	
    				Image swtImgNew = directSWTRender.getSWTImage();
                    
                    if (swtImgNew == null)
                        failures.add( metafileName );
                    //assertTrue(swtImgNew != null);
                    
//    				ImageLoader imageLoader = new ImageLoader();
//    				imageLoader.data = new ImageData[] {swtImgNew.getImageData()};
//    				imageLoader.logicalScreenHeight = swtImgNew.getBounds().width;
//    				imageLoader.logicalScreenHeight = swtImgNew.getBounds().height;
//    
//    				String svgNewFilename = metafileName.substring( 0, metafileName.length() - 4 ) + "NEW" + ".bmp"; //$NON-NLS-1$ //$NON-NLS-2$
//    					
//    				imageLoader.save(svgNewFilename, SWT.IMAGE_BMP);
    			}
    			catch( Exception e )
    			{
    				String errorMessage = "Caught exception while processing file " + metafileName +	//$NON-NLS-1$
    										"\n" + e.toString();	//$NON-NLS-1$
    					
    				failures.add( errorMessage );
    			}
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
