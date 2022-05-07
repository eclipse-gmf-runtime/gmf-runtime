/******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.EMFTranscoder;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg.metafile.WMFTranscoder;
import org.eclipse.gmf.runtime.draw2d.ui.render.factory.RenderedImageType;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.factory.RenderedImageKey;


public class SVGImageType
	implements RenderedImageType {

	public RenderedImage autoDetect(byte[] buffer, RenderedImageKey key) {
		RenderedImage image = null;

		if (isSVG(buffer))
			image = new SVGImage(buffer, key);
		else {
			// not a recognizable image format so assume it's an EMF file
			try {
				WMFTranscoder imageTransformer = new WMFTranscoder();
				ByteArrayInputStream input = new ByteArrayInputStream(buffer);
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				imageTransformer.transcode(input, output, key.getURLString());
				image = new SVGImage(output.toByteArray(), key);
			} catch (Exception e2) {
				try {
					EMFTranscoder imageTransformer = new EMFTranscoder();
					ByteArrayInputStream input = new ByteArrayInputStream(
						buffer);
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					imageTransformer.transcode(input, output, key.getURLString());
					image = new SVGImage(output.toByteArray(), key);
				} catch (Exception e3) {
					return null;
				}
			}
		}
		
		return image;
	}
	
	private static boolean isSVG(byte[] buffer) {
		ByteArrayInputStream bIS = new ByteArrayInputStream(buffer);
		String parserName = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory svgFactory = new SAXSVGDocumentFactory(parserName);
		
		try {
			svgFactory.createDocument(null,bIS);
		} catch (Exception e) {
			return false;
		}
        
		return true;
	}

}
