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

package org.eclipse.gmf.runtime.draw2d.ui.render.factory;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.zip.Adler32;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderDebugOptions;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.factory.RenderInfoImpl;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.factory.RenderedImageKey;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.image.ImageRenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.SVGImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile.EMFTranscoder;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg.metafile.WMFTranscoder;


/**
 * @author sshaw
 *
 * Factory class for generating RenderedImage objects
 */
public class RenderedImageFactory {
	static private Map instanceMap = new WeakHashMap();
 
	/**
	 * createInfo static Utility to create a RenderInfo object.
	 * 
	 * @param width
	 *            the width of the rendered image to set
	 * @param height
	 *            the height of the rendered image to set
	 * @param fill the <code>Color</code> of the fill that could instrumented into image formats that
	 * support dynamic color replacement.  Typically, this would replace colors in the image which are "white"
	 * i.e. RGB(255,255,255)
	 * @param outline the <code>Color</code> of the outline that could instrumented into image formats that
	 * support dynamic color replacement.  Typically, this would replace colors in the image which are "black"
	 * i.e. RGB(0,0,0)
	 * @param maintainAspectRatio
	 *            <code>boolean</code> <code>true</code> if aspect ratio of original vector file is
	 *            maintained, <code>false</code> otherwise
	 * @param antialias
	 * 			  <code>boolean</code> <code>true</code> if the image is to be rendered using anti-aliasing
	 * 			 (removing "jaggies" producing smoother lines), <code>false</code> otherwise
	 * @return <code>RenderInfo</code> object that contains information about the rendered
	 *         image.
	 */
	static public RenderInfo createInfo(
		int width,
		int height,
		Color fill,
		Color outline,
		boolean maintainAspectRatio,
		boolean antialias) {
		RenderInfoImpl svgInfo = new RenderInfoImpl();
		svgInfo.setValues(
			width,
			height,
			fill,
			outline,
			maintainAspectRatio,
			antialias);
		return svgInfo;
	}

	/**
	 * getInstance static constructor method for retrieving the appropriate
	 * instance of the immutable class <code>RenderedImage</code>. This method is used to read
	 * svg images from JARs.
	 * 
	 * @param theURL
	 *            URL of the SVG image. Normally in a JAR
	 * @return <code>RenderedImage</code> instance with the size dimensions requested.
	 */
	static public RenderedImage getInstance(URL theURL) {
		return getInstance(theURL, new RenderInfoImpl());
	}

	/**
	 * getInstance static constructor method for retrieving the appropriate
	 * instance of the immutable class <code>RenderedImage</code>. This method is used to read
	 * svg images from JARs.
	 * 
	 * @param theURL
	 *            URL of the SVG image.
	 * @param info
	 *            object containing information about the size and general data
	 *            regarding how the image will be rendered.
	 * 
	 * @return <code>RenderedImage</code> instance with the size dimensions requested.
	 */
	static public RenderedImage getInstance(URL theURL, RenderInfo info) {

		try {
			InputStream is = theURL.openStream();

			int size = is.available();
			byte[] buffer = new byte[size];
			
			is.read(buffer);
			is.close();

			return getInstance(buffer, info);

		} catch (Exception e) {
			Trace.throwing(Draw2dRenderPlugin.getInstance(), Draw2dRenderDebugOptions.EXCEPTIONS_THROWING, RenderedImage.class, "RenderedImageFactory.getInstance()", //$NON-NLS-1$
			e);
		}

		return null;
	}

	/**
	 * getInstance static constructor method for retrieving the appropriate
	 * instance of the immutable class <code>RenderedImage</code>.
	 * 
	 * @param szFilePath
	 *            <code>String</code> file path of svg file
	 * @return <code>RenderedImage</code> instance with the size dimensions requested.
	 */
	static public RenderedImage getInstance(String szFilePath) {
		return getInstance(szFilePath, new RenderInfoImpl());
	}

	/**
	 * getInstance static constructor method for retrieving the appropriate
	 * instance of the immutable class <code>RenderedImage</code>.
	 * 
	 * @param szFilePath
	 *            <code>String</code> file path of svg file
	 * @param info
	 * 			  <code>RenderInfo</code> object containing information about the size and general data
	 *            regarding how the image will be rendered.
	 * @return <code>RenderedImage</code> instance with the size dimensions requested.
	 */
	static public RenderedImage getInstance(String szFilePath, RenderInfo info) {
		try {
			FileInputStream fis = new FileInputStream(szFilePath);
			int size = fis.available();
			byte[] buffer = new byte[size];
			
			fis.read(buffer);
			fis.close();

			return getInstance(buffer, info);

		} catch (Exception e) {
			Trace.throwing(Draw2dRenderPlugin.getInstance(), Draw2dRenderDebugOptions.EXCEPTIONS_THROWING, RenderedImageFactory.class, "RenderedImageFactory.getInstance()", //$NON-NLS-1$
			e);
		}

		return null;
	}

	/**
	 * getInstance static constructor method for retrieving the appropriate
	 * instance of the immutable class <code>RenderedImage</code>.
	 * 
	 * @param buffer
	 *            <code>byte[]</code> array containing an cached SVG image file.
	 * @return the <code>RenderedImage</code> that encapsulates the contents of the given byte buffer.
	 */
	static public RenderedImage getInstance(byte[] buffer) {
		return getInstance(buffer, new RenderInfoImpl());
	}

	/**
	 * getInstance static constructor method for retrieving the appropriate
	 * instance of the immutable class <code>RenderedImage</code>.
	 * 
	 * @param buffer
	 *            byte[] array containing an cached SVG image file.
	 * @param info
	 *            object containing information about the size and general data
	 *            regarding how the image will be rendered.
	 * @return <code>RenderedImage</code> instance with the size dimensions requested.
	 */
	static public RenderedImage getInstance(byte[] buffer, RenderInfo info) {
		if (buffer == null)
			throw new InvalidParameterException();
	
		Adler32 checksum = new Adler32();
		checksum.update(buffer);
		final RenderedImageKey key = new RenderedImageKey(checksum.getValue(), info);
		WeakReference ref = (WeakReference)instanceMap.get(key);
		RenderedImage image = null;
		if (ref != null)
			image = (RenderedImage)(((WeakReference) instanceMap.get(key)).get());
		
		if (image == null) {
			if (isSVG(buffer))
				image = new SVGImage(buffer, key);
			else {
				// not a recognizable image format so assume it's an EMF file
			    try {
		    	   	WMFTranscoder imageTransformer = new WMFTranscoder();
	    		   	ByteArrayInputStream input = new ByteArrayInputStream(buffer);
		    	   	ByteArrayOutputStream output = new ByteArrayOutputStream();
		    	   	imageTransformer.transcode(input, output);
		    	   	image = new SVGImage(output.toByteArray(), key);
		    	} catch (Exception e2) {
		    		try {
		    			EMFTranscoder imageTransformer = new EMFTranscoder();
	    	    	   	ByteArrayInputStream input = new ByteArrayInputStream(buffer);
		    	       	ByteArrayOutputStream output = new ByteArrayOutputStream();
		    	       	imageTransformer.transcode(input, output);
		    	       	image = new SVGImage(output.toByteArray(), key);
		    	    } catch (Exception e3) {
		    	    	image = new ImageRenderedImage(buffer, key);
		    	    }
			    }
			}
			 
			if (image != null)
				instanceMap.put(key, new WeakReference(image));
		}
	
		return image;
	}
	

	private static boolean isSVG(byte[] buffer) {
		ByteArrayInputStream bIS = new ByteArrayInputStream(buffer);
		String parserName = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory svgFactory = new SAXSVGDocumentFactory(parserName);
		
		try {
			svgFactory.createDocument(null,bIS); //$NON-NLS-1$
			return true;
		} catch (IOException e) {
			Log.error(Draw2dRenderPlugin.getInstance(), IStatus.ERROR, e.getMessage(), e);
		}
		return false;
	}

	
	
}
