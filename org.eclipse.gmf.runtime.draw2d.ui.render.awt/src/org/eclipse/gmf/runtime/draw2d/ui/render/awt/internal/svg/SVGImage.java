/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.util.XMLResourceDescriptor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.draw2d.ui.graphics.GCUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.Draw2dRenderPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.image.ImageConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.AbstractRenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderDebugOptions;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderStatusCodes;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.factory.RenderedImageKey;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;

/**
 * Class that represents a SVG image. This is a useful abstraction so that it
 * can be used similar to an SWT Image object.
 *  
 * @author sshaw
 */
public final class SVGImage extends AbstractRenderedImage {

	/**
	 * Constructor for SVGImage
	 * 
	 * @param buffer
	 *            byte[] array containing an cached SVG image file.
	 * @param key
	 *            ImageKey instance which is unique for the byte array.
	 */
	public SVGImage(byte[] buff, RenderedImageKey key) { 
		super(buff, key);
		
		if (key.getExtraData() == null)
			key.setExtraData(getDocument());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.AbstractRenderedImage#renderImage()
	 */
	protected Image renderImage() {
		// otherwise render the image.
		try {
			if (GCUtilities.supportsAdvancedGraphics()) {
				SVGImageConverter converter = new SVGImageConverter();
				return converter.renderSVGtoSWTImage(getDocument(), getRenderInfo());
			}
			else {
				SVGImageConverter converter = new SVGImageConverter();
				BufferedImage img = converter.renderSVGToAWTImage(getDocument(), getRenderInfo());
				return ImageConverter.convert(img);
			}
		} catch (Exception e) {
			try {
				// try rendering to awt since the SWT renderered may not support the SVG image capabilities
				SVGImageConverter converter = new SVGImageConverter();
				BufferedImage img = converter.renderSVGToAWTImage(getDocument(), getRenderInfo());
				return ImageConverter.convert(img);
			} catch (Exception e1) {
                Log.error(Draw2dRenderPlugin.getInstance(),
                    IStatus.ERROR, e.toString(), e);
				// handle failure gracefully - we can't predict all the failures
				// that
				// may occur in the 3rd party library.
				return new Image(Display.getDefault(), 8, 8);
			}
		}
	}

	/**
	 * getDocument
	 * Accessor for retrieving the SVG document for this Image
	 * 
	 * @return SVG Document that represents the image
	 */
	public Document getDocument() {

		Document document = null;
		
		// IF the document has already been created...
		if (getKey().getExtraData() != null) {
			// Return it
			return (Document)getKey().getExtraData();
		}

		// Otherwise Parse the buffer can create the document
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		InputStream in = new ByteArrayInputStream(getBuffer());

		try {
			document =
				f.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, in);

		} catch (IOException e) {
			// Log the exception to the Error Log
			Log.error(
				Draw2dRenderPlugin.getInstance(),
				Draw2dRenderStatusCodes.SVG_GENERATION_FAILURE,
				e.getMessage());
		}

		return document;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(BufferedImage.class)) {
			BufferedImage buffImg = null;
			
			// otherwise render the image.
			try {
				SVGImageConverter converter = new SVGImageConverter();
				buffImg = converter.renderSVGToAWTImage(getDocument(), getRenderInfo());
			} catch (Exception e) {
				Trace.catching(Draw2dRenderPlugin.getInstance(), Draw2dRenderDebugOptions.EXCEPTIONS_THROWING, getClass(), "getSWTImage()", //$NON-NLS-1$
				e);

				// handle failure gracefully - we can't predict all the failures
				// that
				// may occur in the 3rd party library.
				buffImg = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
			}

			return buffImg;
		}
		
		return super.getAdapter(adapter);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderedImage#getBufferedImage()
	 */
	public BufferedImage getBufferedImage() {
		return (BufferedImage)getAdapter(BufferedImage.class);
	}
}
