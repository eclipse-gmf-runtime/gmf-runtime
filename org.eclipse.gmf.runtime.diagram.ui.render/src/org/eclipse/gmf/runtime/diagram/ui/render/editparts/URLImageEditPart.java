/******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.editparts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.gmf.runtime.common.ui.util.FileUtil;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.factory.RenderedImageFactory;
import org.eclipse.gmf.runtime.notation.View;


/**
 * @author sshaw
 *
 * Class for handling display of an image whose source is a URI.
 */
abstract public class URLImageEditPart
	extends AbstractImageEditPart {

	/**
	 * Default constructor
	 * @param view
	 */
	public URLImageEditPart(View view) {
		super(view);
	}
	
	/**
	 * getImagePath
	 * Transient accessor to retrieve the file path representing the image
	 * file to be rendered.  
	 * 
	 * @return String if valid, null otherwise.
	 */
	abstract protected String getImagePath();
	
	/**
	 * getPathImagePathIsRelativeTo
	 * getImagePath could return a relative path.  If so, this transient
	 * accessor method allows calculation of an absolute path from
	 * the image file and the return path of this method.
	 * 
	 * @return String that is an absolute path that can be used
	 * to calculate the absolute path of a relative path URL.
	 */
	abstract protected String getPathImagePathIsRelativeTo();
	
	/**
	 * getURL
	 * Accessor method that calculates the URL expression based on the string returned
	 * by the method getImagePath.
	 * 
	 * @return URL that can be streamed to retrieve the image data.
	 */
	protected URL getURL() {
		String urlExpression = getImagePath();
		if (urlExpression==null || urlExpression.length()==0)
			return null;
		URL url = null;		
		String launchPath = null;
		
		try {
			url = new URL(urlExpression);
		}

		// URL expression is invalid so convert the URL into a OS specific file
		// path.
		catch (MalformedURLException malformedUrl) {
			launchPath = calculateLaunchPath(urlExpression);
			try {
				url = new URL("file:" + launchPath);//$NON-NLS-1$
			}
			catch (MalformedURLException malformedUrl2) {
				// do nothing
			}
		}
	
		return url;
	}
	
	/**
	 * @param urlExpression
	 * @return
	 */
	private String calculateLaunchPath(String urlExp) {
		String launchPath = null;
		String urlExpression = urlExp;
		String pathImageIsRelativeTo = getPathImagePathIsRelativeTo();
		Path path = new Path(urlExpression);
		if (path != null) {
			if(path.isAbsolute()) {
				urlExpression = path.toOSString();
			} else {
				if (pathImageIsRelativeTo != null && pathImageIsRelativeTo.length() > 0)
					urlExpression = FileUtil.getAbsolutePath(path.toOSString(), getPathImagePathIsRelativeTo());
			}
		}
		
		// Attempt to launch the default program that handles the URL
		// expression.
		final String urlPath = urlExpression;
		if (pathImageIsRelativeTo != null && pathImageIsRelativeTo.length() > 0)
			launchPath = FileUtil.getAbsolutePath(urlPath, getPathImagePathIsRelativeTo());
		
		return launchPath;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.AbstractImageEditPart#regenerateImageFromSource()
	 */
	final protected RenderedImage regenerateImageFromSource() {
		URL url = getURL();
		if (url != null) {			
			try {
				URI uri = URI.createURI(url.toString());
				
				InputStream is = getEditingDomain().getResourceSet().getURIConverter().createInputStream(uri);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				int b = is.read();
				while (b != -1) {
					baos.write(b);
					b = is.read();
				}
				
				// read in the file source specified by the URI, otherwise return null;
				return RenderedImageFactory.getInstance(baos.toByteArray());
			} catch (IOException e) {
				// Ignore and return null;
			} catch (IllegalArgumentException e) {
				// Ignore and return null;
			}
		}
		
		return null;
	}


}
