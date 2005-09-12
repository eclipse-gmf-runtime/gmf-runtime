/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.util.XMLResourceDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Document;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.AbstractRenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderDebugOptions;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.Draw2dRenderStatusCodes;

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
	public SVGImage(byte[] buff, RenderInfo key) { 
		super(buff, key);
	}

	private Document document = null;

	/**
	 * Accessor for retrieving the default image for the rendered SVG data.
	 * This method will render the image if it doesn't exist yet. This allows
	 * for "on-demand" loading. If no-one accesses the image, then it will not
	 * be rendered.
	 * 
	 * @see com.ibm.xtools.gef.figure.svg.ResizableImage#getDefaultImage()
	 */
	public Image getSWTImage() {
		if (img != null)
			return img;

		// otherwise render the image.
		try {
			SVGImageConverter converter = new SVGImageConverter();
			img = converter.renderSVGtoSWTImage(getBuffer(), getRenderInfo());
		} catch (Exception e) {
			Trace.catching(Draw2dRenderPlugin.getInstance(), Draw2dRenderDebugOptions.EXCEPTIONS_THROWING, getClass(), "getSWTImage()", //$NON-NLS-1$
			e);

			// handle failure gracefully - we can't predict all the failures
			// that
			// may occur in the 3rd party library.
			img = new Image(Display.getDefault(), 8, 8);
		}

		return img;
	}

	/**
	 * getDocument
	 * Accessor for retrieving the SVG document for this Image
	 * 
	 * @return SVG Document that represents the image
	 */
	public Document getDocument() {

		// IF the document has already been created...
		if (document != null) {
			// Return it
			return document;
		}

		// Otherwise Parse the buffer can create the document
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		InputStream in = new ByteArrayInputStream(getBuffer());

		try {
			document =
				f.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, in);
			if (document instanceof SVGOMDocument) {
				SVGColorConverter.getInstance().replaceDocumentColors((SVGOMDocument)document, 
					getRenderInfo().getFillColor(),
					getRenderInfo().getOutlineColor());
			}

		} catch (IOException e) {
			// Log the exception to the Error Log
			Log.error(
				Draw2dRenderPlugin.getInstance(),
				Draw2dRenderStatusCodes.SVG_GENERATION_FAILURE,
				e.getMessage());
		}

		return document;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.render.RenderedImage#getBufferedImage()
	 */
	public BufferedImage getBufferedImage() {
		BufferedImage buffImg = null;
		
		// otherwise render the image.
		try {
			SVGImageConverter converter = new SVGImageConverter();
			buffImg = converter.renderSVGToAWTImage(getBuffer(), getRenderInfo());
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
}
