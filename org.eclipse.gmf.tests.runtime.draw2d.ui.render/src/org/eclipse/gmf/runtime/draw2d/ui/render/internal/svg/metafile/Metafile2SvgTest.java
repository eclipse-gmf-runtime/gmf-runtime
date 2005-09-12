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

package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.core.runtime.Platform;



/**
 * @author dhabib
 */
public class Metafile2SvgTest extends TestCase
{
	/** Indicates whether or not we are re-generating the SVG files from the EMF files 
	 * Check out .svg files to be updated so they are writable, flip this bit to 'true' 
	 * and run the test to regenerate the svg files.  If a .svg file is not writable, it
	 * is skipped.  */
	private static final boolean	GENERATE_SVG_FILES	= false;
	
	private String m_srcPath;
	
	public Metafile2SvgTest( String name )
	{
		super( name );
	}

	public static void main( String[] args ) 
	{
		TestRunner.run( suite() );
	}

	public static Test suite() 
	{
		return new TestSuite( Metafile2SvgTest.class );
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
	
	public void testEmf2Svg() throws Exception
	{
		// Enumerate all the *.emf files in the source directory.
		File dir = new File( m_srcPath );
		
		Assert.assertTrue( "Directory doesn't exist: " + m_srcPath, dir.exists() );	//$NON-NLS-1$
		Assert.assertTrue( "Expected a directory: " + m_srcPath, dir.isDirectory() );	//$NON-NLS-1$
		
		Vector failures = new Vector();
		
		if( dir.exists() && dir.isDirectory() )
		{	
			String[] files = dir.list( new EMFFileFilter() );
			Assert.assertTrue( "No files in input directory: " + m_srcPath, files.length > 0 );	//$NON-NLS-1$
			
			for( int index = 0; index < files.length; index++ )
			{
				// Get the emf file to translate.
				String 			metafileName		= m_srcPath + files[ index ];
				File 			metafile			= new File( metafileName );
				FileInputStream	metafileInputStream	= new FileInputStream( metafile );						

				try
				{
					// Get the svg file to write/compare with.
					String svgFilename = metafileName.substring( 0, metafileName.length() - 3 ) + "svg"; //$NON-NLS-1$
	
					if( GENERATE_SVG_FILES )
					{
						File svgFile = new File( svgFilename );
						
						if( svgFile.exists() )
						{
							if( !svgFile.canWrite() )
							{
								// File is read only, skip it (probably not checked out)
								continue;
							}
							svgFile.delete();
						}
						
						svgFile.createNewFile();
	
						FileOutputStream svgOutputStream = new FileOutputStream( svgFile );
	
						// Translate the metafile to SVG.
						AbstractTranscoder tc;
						
						if( metafileName.endsWith( ".emf" ) )	//$NON-NLS-1$
						{	
							tc = new EMFTranscoder();
						}
						else
						{
							tc = new WMFTranscoder();
						}
						
						tc.transcode( metafileInputStream, svgOutputStream );
	
						// Close the output stream.
						svgOutputStream.flush();
						svgOutputStream.close();
					}
					else
					{
						// Create an outputstream to hold the generated svg data.
						ByteArrayOutputStream svgOutputStream = new ByteArrayOutputStream( 10000 );
	
						// Translate the metafile to SVG.
						AbstractTranscoder tc;
						
						if( metafileName.endsWith( ".emf" ) )	//$NON-NLS-1$
						{	
							tc = new EMFTranscoder();
						}
						else
						{
							tc = new WMFTranscoder();
						}
	
						tc.transcode( metafileInputStream, svgOutputStream );
	
						// Compare the generated SVG data to the file
						
						// read the svg file on the disk.
						File svgFile = new File( svgFilename );
						Assert.assertTrue( "Missing generated SVG file: " + svgFilename, svgFile.exists() );	//$NON-NLS-1$
						
						long 		fileSize 		= svgFile.length();
						byte[] 		fileBytes 		= new byte[ (int)fileSize ];
						InputStream	svgInputStream	= new FileInputStream( svgFile );
	
						svgInputStream.read( fileBytes );
						svgInputStream.close();
						
						// Read the output stream.
						svgOutputStream.flush();
						svgOutputStream.close();
						byte[] generatedBytes = svgOutputStream.toByteArray();
						
						if( fileBytes.length != generatedBytes.length )
						{
							String errorMessage = 	"Data sizes are not equal: " + svgFilename +	//$NON-NLS-1$
													" Expected: " + fileBytes.length + 				//$NON-NLS-1$
													" Received: " + generatedBytes.length + "\n";	//$NON-NLS-2$//$NON-NLS-1$
							failures.add( errorMessage );
							// Write the generated data out.
							writeErrorFile( svgFilename, generatedBytes );
							continue;
						}
						
						for( int i = 0; i < fileBytes.length; i++ )
						{
							if( fileBytes[ i ] != generatedBytes[ i ] )
							{
								String errorMessage = "Data is different in file \'" + svgFilename + "\' at byte " + i + "\n";	//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
								failures.add( errorMessage );
								// Write the generated data out.
								writeErrorFile( svgFilename, generatedBytes );
								break;
							}
						}
					}
					
					metafileInputStream.close();
				}
				catch( Exception e )
				{
					String errorMessage = "Caught exception while processing file " + metafileName +	//$NON-NLS-1$
											"\n" + e.toString();	//$NON-NLS-1$
					
					failures.add( errorMessage );
				}
			}
			
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
	
	/**
	 * Writes out a svg error file based on the specified filename.  Filename is 
	 * appended with _failed.svg and the specified data is written to it.
	 * 
	 * @param svgFilename
	 * @param data
	 * @throws IOException
	 */
	private void writeErrorFile( String svgFilename, byte[] data ) throws IOException
	{
		String filename = svgFilename.substring( 0, svgFilename.length() - 4 ) + "_failed.svg"; //$NON-NLS-1$

		File errorFile = new File( filename );
		
		if( errorFile.exists() )
		{
			if( !errorFile.canWrite() )
			{
				return;
			}
			errorFile.delete();
		}
		
		errorFile.createNewFile();
		
		FileOutputStream svgOutputStream = new FileOutputStream( errorFile );
		svgOutputStream.write( data );
		svgOutputStream.flush();
		svgOutputStream.close();
	}

	private class EMFFileFilter implements FilenameFilter
	{
		public boolean accept( File file, String name )
		{
			String lowerCaseName = name.toLowerCase();
			return lowerCaseName.endsWith( ".emf" ) ||		//$NON-NLS-1$
					lowerCaseName.endsWith( ".wmf" );		//$NON-NLS-1$
		}
	}
}
