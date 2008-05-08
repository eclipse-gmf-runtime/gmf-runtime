/******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
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
import java.awt.Graphics2D;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.Draw2dRenderPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderDebugOptions;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.DeviceContext;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IRenderToPath;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.IRenderableObject;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.ITraceMe;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.Record;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.TranscoderException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;


/**
 * Abstract base class for both the WMFTranscoder and the EMFTranscoder.  Implements the common
 * functionality.  May be extended to provide a new type of transcoder.
 * 
 * @author dhabib
 */
public abstract class AbstractTranscoder 
{
	private static boolean 	m_gDebugMode						= false;
	private Dimension 		m_size								= new Dimension( 100, 100 );

	/**
	 * Puts the transcoder in 'debug' mode so messages are written out using System.out
	 * instead of using the Trace/Debug stuff.  This is so we can operate without
	 * eclipse.
	 */
	static void setDebugMode()
	{
		m_gDebugMode = true;
	}
	
	/**
	 * Logs the specified message.  If we are in 'debug' mode it will log the message to
	 * the output console.  If we are running as a plugin, it will log the
	 * message using the core 
	 * @param message
	 */
	static void logMessage( String message )
	{
		if( m_gDebugMode )
		{	
			System.out.println( message );
		}
		else
		{	
			// We may not handle all types of selections.
			Trace.trace(	Draw2dRenderPlugin.getInstance(), 
							Draw2dRenderDebugOptions.DEBUG, 
							message );
		}
	}
	
	/**
	 * Sets the size, in pixels, of the metafile output.  In the case of the WMF/APM and the EMF
	 * transcoder, this information is contained in the header record, so after initializing that
	 * record they will call this method.
	 * @param size
	 */
	public void setSize( Dimension size )
	{
		m_size = size;
	}
	
	/**
	 * Retrieves the current size, in pixels, of the metafile output.
	 * @return
	 */
	public Dimension getSize()
	{
		return new Dimension( m_size );
	}

	/**
	 * Translate an input stream containing a metafile to an output stream containing SVG.
	 * @param input - Contains Metafile Data
	 * @param output - After successful transcoding, contains generated SVG output.
	 * @throws TranscoderException
	 */
	public void transcode( InputStream input, OutputStream output) throws TranscoderException
	{
		transcode(input, output, SVGDOMImplementation.SVG_NAMESPACE_URI);
	}
	
	/**
	 * Translate an input stream containing a metafile to an output stream containing SVG.
	 * @param input - Contains Metafile Data
	 * @param output - After successful transcoding, contains generated SVG output.
	 * @param url - document namespace url
	 * @throws TranscoderException
	 */
	public void transcode( InputStream input, OutputStream output, String url ) throws TranscoderException
	{
		if( input == null )
		{
			throw new IllegalArgumentException( "Parameter 'input' may not be null" );	//$NON-NLS-1$
		}
		else if( output == null )
		{
			throw new IllegalArgumentException( "Parameter 'output' may not be null" );	//$NON-NLS-1$
		}

		try
		{
			BufferedInputStream stream = new BufferedInputStream( input );
			
			// Create the DOM
			DOMImplementation	impl 	= SVGDOMImplementation.getDOMImplementation();
			String 				svgNS 	= url;
			SVGDocument 		doc 	= (SVGDocument)impl.createDocument( svgNS, "svg", null );	//$NON-NLS-1$

			// Create the SVG converter.
			SVGGraphics2D svg = new SVGGraphics2D( doc );
			
			// There seems to be a bug in the batik libraries.  It appears that
			// there are some variables that are not set properly at initialization
			// time.  This sets the variable so it works.
			svg.getGeneratorContext().setPrecision( 3 );

			// Iterate over the input stream and paint into the svg graphics object.
			iterateRecords( stream, svg );
	
			// Get the size from the header.
			svg.setSVGCanvasSize( m_size );
	
			Writer writer = new OutputStreamWriter( output, "UTF-8" );//$NON-NLS-1$
			svg.stream( writer );
		}
		catch( IOException e )
		{
				throw new TranscoderException( e );
		}
	}
	
	/**
	 * Parses the metafile records in the stream , rendering them into
	 * the provided Graphics object.
	 * @param stream
	 * @param g
	 * @throws TranscoderException
	 */
	private void iterateRecords(	BufferedInputStream stream, 
									Graphics2D g ) throws TranscoderException, IOException
	{
		DeviceContext context = new DeviceContext();

		int 	index = 0;
		Record 	rec;
		
		while( (rec = getNextRecord( stream, index )) != null )
		{
			index++;
			
			try
			{
				String output = null;
				IRenderableObject svgConverter = getConverter( rec );
				
				if( svgConverter != null )
				{
					if( context.getGdiPath().isOpen() &&
						svgConverter instanceof IRenderToPath )
					{
						// Rendering to a path and this converter handles paths.
						IRenderToPath pathRender = (IRenderToPath) svgConverter;
						pathRender.render( context );
						if (m_gDebugMode) {
							output = buildOutput(rec, "IRenderToPath", index); //$NON-NLS-1$
						}
					}
					else
					{
						svgConverter.render( g, context );
						if (m_gDebugMode) {
							output = buildOutput(rec, "IRenderableObject", index); //$NON-NLS-1$
						}
					}
					if (m_gDebugMode && svgConverter instanceof ITraceMe) {
						output += ' ' + svgConverter.toString();
					}
				}
				else
				{
					// Unconditionally trace this
					output = buildOutput(rec, "Unhandled", index); //$NON-NLS-1$
				}
 				logMessage( output );
 			}
			catch (org.apache.batik.transcoder.TranscoderException e)
			{
				throw new TranscoderException(e);
			}
			catch (TranscoderException e)
			{
				throw e;
			}
 			catch( Exception e )
 			{
 				String output = buildOutput(
					rec,
					"Exception while processing",//$NON-NLS-1$
					index) + ":\n" + e.toString(); //$NON-NLS-1$
				logMessage( output );
				e.printStackTrace();
			}
		}
	}

	private String _thisClassName = null;
	private String buildOutput(Record rec, String msg, int ix) {
		if (_thisClassName == null) {
			_thisClassName = getClass().getName();
			_thisClassName = _thisClassName.substring(_thisClassName.lastIndexOf('.') + 1);
		}
		StringBuffer sb = new StringBuffer();
		sb.append(_thisClassName);
		sb.append(", record " + ix + ' ');//$NON-NLS-1$
		sb.append(msg + "  metafile record type "); //$NON-NLS-1$
		sb.append(rec.getType() + " = "+ getUserFriendlyName( rec.getType() ));//$NON-NLS-1$

		return sb.toString();
	}

	/**
	 * Reads the next metafile record from the provided stream.  Returns 'null' 
	 * if at the end of the set of records.
	 * @param stream Contains the data to read the record from.
	 * @param curRecord Contains the current record number.
	 * @return Next metafile record in the stream, or null if the last record has been read.
	 * @throws IOException
	 */
	protected abstract Record getNextRecord(	BufferedInputStream stream,
												int curRecord ) throws IOException;

	/**
	 * This method takes a metafile record and instantiates a class 
	 * implementing IRenderableObject to handle that type of record.
	 * This method will initialize the newly created converter
	 * with the data contained in the record.
	 * @param stream
	 * @return A handler for the specified record, or null if no handler exists.
	 * @throws TranscoderException
	 */
	protected abstract IRenderableObject getConverter( Record rec ) throws TranscoderException;
	

	/**
	 * Returns the user friendly name for the specified record type.
	 * @param recordType
	 * @return The user friendly name for the specified record type.
	 */
	protected abstract String getUserFriendlyName( int recordType );
}
