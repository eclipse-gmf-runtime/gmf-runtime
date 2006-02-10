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

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.zip.Adler32;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.AbstractRenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderDebugOptions;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.factory.RenderedImageKey;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.image.ImageRenderedImage;
import org.eclipse.swt.graphics.RGB;

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
	 * @param maintainAspectRatio
	 *            <code>boolean</code> <code>true</code> if aspect ratio of
	 *            original vector file is maintained, <code>false</code>
	 *            otherwise
	 * @param antialias
	 *            <code>boolean</code> <code>true</code> if the image is to
	 *            be rendered using anti-aliasing (removing "jaggies" producing
	 *            smoother lines), <code>false</code> otherwise
	 * @param fill
	 *            the <code>RGB</code> of the fill that could instrumented
	 *            into image formats that support dynamic color replacement.
	 *            Typically, this would replace colors in the image which are
	 *            "white" i.e. RGB(255,255,255)
	 * @param outline
	 *            the <code>RGB</code> of the outline that could
	 *            instrumented into image formats that support dynamic color
	 *            replacement. Typically, this would replace colors in the image
	 *            which are "black" i.e. RGB(0,0,0)
	 * @return <code>RenderInfo</code> object that contains information about
	 *         the rendered image.
	 */
	static public RenderInfo createInfo(int width, int height, boolean maintainAspectRatio, boolean antialias,
			RGB fill, RGB outline ) {
		RenderedImageKey svgInfo = new RenderedImageKey();
		svgInfo.setValues(width, height, maintainAspectRatio,
			antialias, fill, outline);
		return svgInfo;
	}
	
	/**
	 * getInstance static constructor method for retrieving the appropriate
	 * instance of the immutable class <code>RenderedImage</code>. This
	 * method is used to read svg images from JARs.
	 * 
	 * @param theURL
	 *            URL of the SVG image. Normally in a JAR
	 * @return <code>RenderedImage</code> instance with the size dimensions
	 *         requested.
	 */
	static public RenderedImage getInstance(URL theURL) {
		return getInstance(theURL, new RenderedImageKey());
	}

	/**
	 * getInstance static constructor method for retrieving the appropriate
	 * instance of the immutable class <code>RenderedImage</code>. This
	 * method is used to read svg images from JARs.
	 * 
	 * @param theURL
	 *            URL of the SVG image.
	 * @param info
	 *            object containing information about the size and general data
	 *            regarding how the image will be rendered.
	 * 
	 * @return <code>RenderedImage</code> instance with the size dimensions
	 *         requested.
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
			Trace.throwing(Draw2dRenderPlugin.getInstance(),
				Draw2dRenderDebugOptions.EXCEPTIONS_THROWING,
				RenderedImage.class, "RenderedImageFactory.getInstance()", //$NON-NLS-1$
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
	 * @return <code>RenderedImage</code> instance with the size dimensions
	 *         requested.
	 */
	static public RenderedImage getInstance(String szFilePath) {
		return getInstance(szFilePath, new RenderedImageKey());
	}

	/**
	 * getInstance static constructor method for retrieving the appropriate
	 * instance of the immutable class <code>RenderedImage</code>.
	 * 
	 * @param szFilePath
	 *            <code>String</code> file path of svg file
	 * @param info
	 *            <code>RenderInfo</code> object containing information about
	 *            the size and general data regarding how the image will be
	 *            rendered.
	 * @return <code>RenderedImage</code> instance with the size dimensions
	 *         requested.
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
			Trace.throwing(Draw2dRenderPlugin.getInstance(),
				Draw2dRenderDebugOptions.EXCEPTIONS_THROWING,
				RenderedImageFactory.class,
				"RenderedImageFactory.getInstance()", //$NON-NLS-1$
				e);
		}

		return null;
	}

	/**
	 * getInstance static constructor method for retrieving the appropriate
	 * instance of the immutable class <code>RenderedImage</code>.
	 * 
	 * @param buffer
	 *            <code>byte[]</code> array containing an cached SVG image
	 *            file.
	 * @return the <code>RenderedImage</code> that encapsulates the contents
	 *         of the given byte buffer.
	 */
	static public RenderedImage getInstance(byte[] buffer) {
		return getInstance(buffer, new RenderedImageKey());
	}

	/**
	 * Returns a related instance of the given <code>RenderedImage</code> that
	 * is based on the same byte stream or file info but instrumented for the
	 * passed in <code>RenderInfo</code> object
	 * 
	 * @param image
	 *            <code>RenderedImage</code> that is used as a base to
	 *            retrieve the related instance.
	 * @param info
	 *            <code>RenderInfo</code> object containing information about
	 *            the size and general data regarding how the image will be
	 *            rendered.
	 * @return <code>RenderedImage</code> instance with the size dimensions
	 *         requested. May return <code>null</code> if no related instance
	 *         can be found or if the original buffer cannot be retrieved.
	 */
	static public RenderedImage getRelatedInstance(RenderedImage image,
			RenderInfo info) {
		if (image instanceof AbstractRenderedImage) {
			RenderedImageKey oldKey = ((AbstractRenderedImage) image).getKey();
			RenderedImageKey key = new RenderedImageKey(info, oldKey.getChecksum(), oldKey.getExtraData());
			WeakReference ref = (WeakReference) instanceMap.get(key);
			if (ref != null) {
				return (RenderedImage) ref.get();
			} else {
				return autodetectImage(((AbstractRenderedImage) image)
					.getBuffer(), key);
			}
		}

		return null;
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
	 * @return <code>RenderedImage</code> instance with the size dimensions
	 *         requested.
	 */
	static public RenderedImage getInstance(byte[] buffer, RenderInfo info) {
		if (buffer == null)
			throw new InvalidParameterException();

		Adler32 checksum = new Adler32();
		checksum.update(buffer);
		final RenderedImageKey key = new RenderedImageKey(info, checksum.getValue(), null);
		WeakReference ref = (WeakReference) instanceMap.get(key);
		RenderedImage image = null;
		if (ref != null)
			image = (RenderedImage) (((WeakReference) instanceMap.get(key))
				.get());
		else
			image = autodetectImage(buffer, key);

		return image;
	}

	private static final String E_MODIFIER_FACTORY = "factory"; //$NON-NLS-1$
	private static final String A_CLASS = "class"; //$NON-NLS-1$

	static private List imageTypes = null;
	
	static private RenderedImage autodetectImage(byte[] buffer,
			final RenderedImageKey key) {
		
		if (imageTypes == null) {
			imageTypes = new ArrayList();
			
			IExtensionPoint riExtensionPt = Platform.getExtensionRegistry().getExtensionPoint("org.eclipse.gmf.runtime.draw2d.ui.render", //$NON-NLS-1$
															"renderedImageFactory");  //$NON-NLS-1$
			IConfigurationElement[] configEls = riExtensionPt.getConfigurationElements();
			for (int i = 0; i < configEls.length; i++) {
				IConfigurationElement element = configEls[i];
	
				if (element.getName().equals(E_MODIFIER_FACTORY)) {
					RenderedImageType imageType = null;
					try {
						imageType = (RenderedImageType)element.createExecutableExtension(A_CLASS);
						if (imageType != null)
							imageTypes.add(imageType);
					} catch (CoreException e) {
						continue;
					}
				}
			}
		}
		
		RenderedImage image = null;
		ListIterator li = imageTypes.listIterator();
		while (li.hasNext()) {
			RenderedImageType imageType = (RenderedImageType)li.next();
			image = imageType.autoDetect(buffer, key);
			if (image != null)
				return image;
		}

		// can't create a RenderedImageType for image files until bugzilla 116227 is resolved.  Until then,
		// assume, the fall through type is ImageRenderedImage.
		return new ImageRenderedImage(buffer, key);
	}
}
