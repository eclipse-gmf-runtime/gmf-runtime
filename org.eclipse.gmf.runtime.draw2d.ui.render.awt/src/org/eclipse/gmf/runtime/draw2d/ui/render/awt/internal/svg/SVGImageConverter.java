/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.svg;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.fop.svg.PDFTranscoder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderInfo;
import org.eclipse.gmf.runtime.draw2d.ui.render.awt.internal.Draw2dRenderPlugin;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Document;


/**
 * @author sshaw
 *
 * Class for conversion of SVG to different Image formats
 */
public class SVGImageConverter {
	/**
	 * Consructor to create a new instance of SVGtoBufferedImageConverter
	 */
	public SVGImageConverter() {
		// empty constructor
	}

	/**
	 * renderSVGToAWTImage
	 * Given a filename, will render the SVG file into an SWT Image
	 * 
	 * @param document Document of svg file
	 * @param RenderInfo object containing information about the size and 
	 * general data regarding how the image will be rendered.
	 * @return BufferedImage AWT image containing the rendered SVG file.
	 * @throws Exception
	 */
	public BufferedImage renderSVGToAWTImage(Document document, RenderInfo info)
		throws Exception {
		ImageTranscoderEx transcoder = new ImageTranscoderEx();
		setUpTranscoders(document, transcoder, info);
		
		return transcoder.getBufferedImage();
	}
	
	/**
	 * renderSVGtoSWTImage
	 * Given an InputStream, will render the SVG file into an SWT Image
	 * 
	 * @param document Document of svg file
	 * @param RenderInfo object containing information about the size and 
	 * general data regarding how the image will be rendered.
	 * @return Image SWT image containing the rendered SVG file.
	 * @throws Exception
	 */
	public Image renderSVGtoSWTImage(Document document, RenderInfo info)
		throws Exception {
		
		SWTImageTranscoder transcoder = new SWTImageTranscoder();
		setUpTranscoders(document, transcoder, info);
		
		return transcoder.getSWTImage();
	}
	
	/**
	 * renderSVGToAWTImage
	 * Given a filename, will render the SVG file into an SWT Image
	 * 
	 * @param strFileName String file path of svg file
	 * @param RenderInfo object containing information about the size and 
	 * general data regarding how the image will be rendered.
	 * @return BufferedImage AWT image containing the rendered SVG file.
	 * @throws Exception
	 */
	public BufferedImage renderSVGToAWTImage(String strFileName, RenderInfo info)
		throws Exception {
		InputStream in = new FileInputStream(strFileName);
		return renderSVGToAWTImage(in, info);
	}

	/**
	 * renderSVGToAWTImage
	 * Given a buffer, will render the SVG file into an SWT Image
	 * 
	 * @param buffer byte[] array containing an cached SVG image file.
	 * @param RenderInfo object containing information about the size and 
	 * general data regarding how the image will be rendered.
	 * @return BufferedImage AWT iimage containing the rendered SVG file.
	 * @throws Exception
	 */
	public BufferedImage renderSVGToAWTImage(byte[] buffer, RenderInfo info)
		throws Exception {
		InputStream in = new ByteArrayInputStream(buffer);
		return renderSVGToAWTImage(in, info);
	}
	
	/**
	 * setUpTranscoders
	 * sets up the transcoders with the hints based on the RenderInfo structure.
	 * 
	 * @param in
	 * @param transcoder
	 * @param info
	 */
	private void setUpTranscoders(InputStream in, Transcoder transcoder, RenderInfo info)
		throws Exception {
		initializeTranscoderFromInfo(transcoder, info);
		
		TranscoderInput input = null;
		TranscoderOutput output = null;

		input = new TranscoderInput(in);
		output = new ImageTranscoderOutput(); 
		transcoder.transcode(input, output);
	}
	
	/**
	 * setUpTranscoders
	 * sets up the transcoders with the hints based on the RenderInfo structure.
	 * 
	 * @param document
	 * @param transcoder
	 * @param info
	 */
	private void setUpTranscoders(Document document, Transcoder transcoder, RenderInfo info)
		throws Exception {
		initializeTranscoderFromInfo(transcoder, info);
		
		TranscoderInput input = null;
		TranscoderOutput output = null;

		input = new TranscoderInput(document);
		output = new ImageTranscoderOutput(); 
		transcoder.transcode(input, output);
	}

	private void initializeTranscoderFromInfo(Transcoder transcoder, RenderInfo info) {
		if (info.getWidth() > 0)
			transcoder.addTranscodingHint(
				ImageTranscoder.KEY_WIDTH,
				new Float(info.getWidth()));
		if (info.getHeight() > 0)
			transcoder.addTranscodingHint(
				ImageTranscoder.KEY_HEIGHT,
				new Float(info.getHeight()));
		
		transcoder.addTranscodingHint(
			ImageTranscoderEx.KEY_MAINTAIN_ASPECT_RATIO,
				Boolean.valueOf(info.shouldMaintainAspectRatio()));
	
		transcoder.addTranscodingHint(
			ImageTranscoderEx.KEY_ANTI_ALIASING,
				Boolean.valueOf(info.shouldAntiAlias()));
				
		if (info.getBackgroundColor() != null) {
			transcoder.addTranscodingHint(
				ImageTranscoderEx.KEY_FILL_COLOR,
				new Color(info.getBackgroundColor().red, 
						  info.getBackgroundColor().green,
						  info.getBackgroundColor().blue));
		}
		
		if (info.getForegroundColor() != null) {
					transcoder.addTranscodingHint(
						ImageTranscoderEx.KEY_OUTLINE_COLOR,
						new Color(info.getForegroundColor().red, 
								  info.getForegroundColor().green,
								  info.getForegroundColor().blue));
		}

	}
	
	/**
	 * renderSVG
	 * Given an InputStream, will render the SVG file into an SWT Image
	 * 
	 * @param in InputSteam which contains the SVG file data
	 * @param RenderInfo object containing information about the size and 
	 * general data regarding how the image will be rendered.
	 * @return BufferedImage AWT iimage containing the rendered SVG file.
	 * @throws Exception
	 */
	public BufferedImage renderSVGToAWTImage(InputStream in, RenderInfo info)
		throws Exception {
		ImageTranscoderEx transcoder = new ImageTranscoderEx();
		setUpTranscoders(in, transcoder, info);

		return transcoder.getBufferedImage();
	}
	
	/**
	 * renderSVGtoSWTImage
	 * Given a filename, will render the SVG file into an SWT Image
	 * 
	 * @param strFileName String file path of svg file
	 * @param RenderInfo object containing information about the size and 
	 * general data regarding how the image will be rendered.
	 * @return Image SWT image containing the rendered SVG file.
	 * @throws Exception
	 */
	public Image renderSVGtoSWTImage(String strFileName, RenderInfo info)
		throws Exception {
		InputStream in = new FileInputStream(strFileName);
		return renderSVGtoSWTImage(in, info);
	}

	/**
	 * renderSVGtoSWTImage
	 * Given a buffer, will render the SVG file into an SWT Image
	 * 
	 * @param buffer byte[] array containing an cached SVG image file.
	 * @param RenderInfo object containing information about the size and 
	 * general data regarding how the image will be rendered.
	 * @return Image SWT image containing the rendered SVG file.
	 * @throws Exception
	 */
	public Image renderSVGtoSWTImage(byte[] buffer, RenderInfo info)
		throws Exception {
		InputStream in = new ByteArrayInputStream(buffer);
		return renderSVGtoSWTImage(in, info);
	}

	/**
	 * renderSVGtoSWTImage
	 * Given an InputStream, will render the SVG file into an SWT Image
	 * 
	 * @param in InputSteam which contains the SVG file data
	 * @param RenderInfo object containing information about the size and 
	 * general data regarding how the image will be rendered.
	 * @return Image SWT image containing the rendered SVG file.
	 * @throws Exception
	 */
	public Image renderSVGtoSWTImage(InputStream in, RenderInfo info)
		throws Exception {
		
		SWTImageTranscoder transcoder = new SWTImageTranscoder();
		setUpTranscoders(in, transcoder, info);
		
		return transcoder.getSWTImage();
	}
	
	 /**
	 * Export SVG image to PDF file format.
	 * 
	 * @param SVGImage The input SVG image.
	 * @param fileOutputStream The output stream to write the PDF to.
	 * @throws CoreException
	 */
    public static void exportToPDF(SVGImage svgImage,
			FileOutputStream fileOutputStream)
			throws CoreException {
    	
		try {
			TranscoderOutput transcoderOutput = new TranscoderOutput(fileOutputStream);
			TranscoderInput transcoderInput = new TranscoderInput(svgImage
					.getDocument());

			PDFTranscoder pdfTranscoder = new PDFTranscoder();
			pdfTranscoder.transcode(transcoderInput, transcoderOutput);

		} catch (TranscoderException e) {
			Log.error(Draw2dRenderPlugin.getInstance(), IStatus.ERROR, e
					.getMessage(), e);
			IStatus status = new Status(IStatus.ERROR,
					"exportToPDF", IStatus.OK, //$NON-NLS-1$
					e.getMessage(), null);
			throw new CoreException(status);
		} 
	}
}
