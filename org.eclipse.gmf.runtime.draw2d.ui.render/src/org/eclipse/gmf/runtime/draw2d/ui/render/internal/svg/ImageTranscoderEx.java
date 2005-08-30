/****************************************************************************
 Licensed Materials - Property of IBM
 (C) Copyright IBM Corp. 2002, 2004, 2005. All Rights Reserved.
 
 US Government Users Restricted Rights - Use, duplication or disclosure
 restricted by GSA ADP Schedule Contract with IBM Corp.
 *****************************************************************************/
package org.eclipse.gmf.runtime.draw2d.ui.render.internal.svg;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.batik.bridge.BaseScriptingEnvironment;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.gvt.renderer.ConcreteImageRendererFactory;
import org.apache.batik.gvt.renderer.ImageRendererFactory;
import org.apache.batik.gvt.renderer.StaticRenderer;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.keys.BooleanKey;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGSVGElement;

/**
 * @author sshaw
 *
 * Class extension for handling turning on/off anti-aliasing, replacing black and white
 * colors with choice colors and turning on/off maintain aspect ratio capability.
 */
public class ImageTranscoderEx extends ImageTranscoder {
	/**
	 * @author sshaw
	 *
	 * Utility class for returning float width and height values from a method.
	 */
	public class DimensionFloat extends Dimension2D {

		private float w;
		private float h;
		
		/**
		 * 
		 */
		public DimensionFloat(float width, float height) {
			super();
			this.w = width;
			this.h = height;
		}

		/* (non-Javadoc)
		 * @see java.awt.geom.Dimension2D#getWidth()
		 */
		public double getWidth() {
			return w;
		}

		/* (non-Javadoc)
		 * @see java.awt.geom.Dimension2D#getHeight()
		 */
		public double getHeight() {
			return h;
		}

		/* (non-Javadoc)
		 * @see java.awt.geom.Dimension2D#setSize(double, double)
		 */
		public void setSize(double width, double height) {
			this.w = (float)width;
			this.h = (float)height;

		}

	}

	/**
	 * Constructor to create an instance of SWTImageTranscoder.
	 */
	public ImageTranscoderEx() {
		// empty constructor
	}
	
	public static final TranscodingHints.Key KEY_MAINTAIN_ASPECT_RATIO
		= new BooleanKey();
	
	public static final TranscodingHints.Key KEY_FILL_COLOR
		= new ColorKey();
	
	public static final TranscodingHints.Key KEY_OUTLINE_COLOR
		= new ColorKey();
	
	public static final TranscodingHints.Key KEY_ANTI_ALIASING
		= new BooleanKey();
						
	/**
	 * initSVGDocument
	 * This is an initialization method for setting alternate stylesheet if
	 * set as a hint and also initializes the SVG document with the appropriate
	 * context.
	 * 
	 * @param svgDoc SVGODocument to initialize.
	 */
	protected void initSVGDocument(SVGOMDocument svgDoc) {
		Color fillColor = null;
		Color outlineColor = null;
		if (hints.containsKey(KEY_FILL_COLOR)) {
			fillColor = (Color)hints.get(KEY_FILL_COLOR);
		}
		
		if (hints.containsKey(KEY_OUTLINE_COLOR)) {
			outlineColor = (Color)hints.get(KEY_OUTLINE_COLOR);
		}
		
		if (fillColor == null && outlineColor == null)
			return;
		
		SVGColorConverter.getInstance().replaceDocumentColors(svgDoc, fillColor, outlineColor);
	}
	
	/**
	 * buildGVTTree
	 * This method builds the GVT tree that is used to render the SVG data.
	 * 
	 * @param svgDoc SVGOMDocument representing the physical SVG document.
	 * @param context BridgeContext containing information of the render to occur.
	 * @return GraphicsNode object.
	 * @throws TranscoderException thrown if a BridgeException occurs when building the root object.
	 */
	protected GraphicsNode buildGVTTree(SVGOMDocument svgDoc, BridgeContext context)  
										throws TranscoderException {
		GVTBuilder gvtBuilder = new GVTBuilder();
		GraphicsNode gvtRoot = null;
		try {
			gvtRoot = gvtBuilder.build(context, svgDoc);

		} catch (BridgeException ex) {
			throw new TranscoderException(ex);
		}
		
		return gvtRoot;
	}
	
	
	/**
	 * calculateSizeTransform
	 * Calculates the transformation matrix that is applied during the render operation.  Specifically,
	 * the transformation for changing the size of the rendered data.
	 * 
	 * @param svgRoot SVGSVGElement root element of the SVG tree.
	 * @param gvtRoot GraphicsNode graphic node root
	 * @param uri String of the SVG document URI
	 * @param docWidth float width values of the original vector.
	 * @param docHeight float height values of the original vector.
	 * @param newWidth float width values of the rendered image.
	 * @param newHeight float height values of the rendered image.
	 * @return AffineTransform object that represents the size transformation to take place.
	 * @throws TranscoderException thrown if a BridgeException occurs when building the root object.
	 */
	protected AffineTransform calculateSizeTransform(SVGSVGElement svgRoot, GraphicsNode gvtRoot, String uri, 
			float docWidth, float docHeight, 
			float newWidth, float newHeight) 
	throws TranscoderException {
		AffineTransform Px;
		String ref = null;
		try {
			ref = new URL(uri == null ? new String(""): uri).getRef(); //$NON-NLS-1$
		} catch (MalformedURLException ex) {
			// nothing to do, catched previously
		}
		
		boolean maintainAspectRatio = true;
		if (hints.containsKey(KEY_MAINTAIN_ASPECT_RATIO)) {
			maintainAspectRatio = ((Boolean)hints.get(KEY_MAINTAIN_ASPECT_RATIO)).booleanValue();
		}
		
		if (maintainAspectRatio) {
			try {
				Px = ViewBox.getViewTransform(ref, svgRoot, newWidth, newHeight);
			} catch (BridgeException ex) {
				throw new TranscoderException(ex);
			}
			
			if (Px.isIdentity() && (newWidth != docWidth || newHeight != docHeight)) {
				// The document has no viewBox, we need to resize it by hand.
				// we want to keep the document size ratio
				float d = Math.max(docWidth, docHeight);
				float dd = Math.max(newWidth, newHeight);
				float scale = dd/d;
				Px = AffineTransform.getScaleInstance(scale, scale);
			}
		}
		else {
			float xscale = newWidth / docWidth;
			float yscale = newHeight / docHeight; 
			Px = AffineTransform.getScaleInstance(xscale, yscale);
		}
		
		// take the AOI into account if any
		if (hints.containsKey(KEY_AOI)) {
			Rectangle2D aoi = (Rectangle2D)hints.get(KEY_AOI);
			// transform the AOI into the image's coordinate system
			aoi = Px.createTransformedShape(aoi).getBounds2D();
			AffineTransform Mx = new AffineTransform();
			double sx = newWidth / aoi.getWidth();
			double sy = newHeight / aoi.getHeight();
			Mx.scale(sx, sy);
			double tx = -aoi.getX();
			double ty = -aoi.getY();
			Mx.translate(tx, ty);

			// take the AOI transformation matrix into account
			// we apply first the preserveAspectRatio matrix
			Px.preConcatenate(Mx);
		}
		
		CanvasGraphicsNode cgn = getCanvasGraphicsNode(gvtRoot);
        if (cgn != null) {
            cgn.setViewingTransform(Px);
            curTxf = new AffineTransform();
        } else {
            curTxf = Px;
        }
        
		return curTxf;
	}
		
	/**
	 * Transcodes the specified Document as an image in the specified output.
	 *
	 * @param document the document to transcode
	 * @param uri the uri of the document or null if any
	 * @param output the ouput where to transcode
	 * @exception TranscoderException if an error occured while transcoding
	 */
	protected void transcode(Document document,
			String uri,
			TranscoderOutput output)
	throws TranscoderException {

		if ((document != null) &&
				!(document.getImplementation() instanceof SVGDOMImplementation)) {
			DOMImplementation impl;
			impl = SVGDOMImplementation.getDOMImplementation();
			document = DOMUtilities.deepCloneDocument(document, impl);
			if (uri != null) {
				try { 
					URL url = new URL(uri);
					((SVGOMDocument)document).setURLObject(url);
				} catch (MalformedURLException mue) {

					//TODO: Implement error handling
				}
			}
		}

		ctx = new BridgeContext(userAgent);
		SVGOMDocument svgDoc = (SVGOMDocument)document;
		SVGSVGElement svgRoot = svgDoc.getRootElement();

		// build the GVT tree
		builder = new GVTBuilder();
		// flag that indicates if the document is dynamic
		boolean isDynamic = 
			(hints.containsKey(KEY_EXECUTE_ONLOAD) &&
					((Boolean)hints.get(KEY_EXECUTE_ONLOAD)).booleanValue() &&
					BaseScriptingEnvironment.isDynamicDocument(ctx, svgDoc));

		if (isDynamic)
			ctx.setDynamicState(BridgeContext.DYNAMIC);

		initSVGDocument(svgDoc);
		
		GraphicsNode gvtRoot;
		try {
			gvtRoot = builder.build(ctx, svgDoc);
		} catch (BridgeException ex) {
			throw new TranscoderException(ex);
		}
		
		// get the 'width' and 'height' attributes of the SVG document
		float docWidth = (float)ctx.getDocumentSize().getWidth();
		float docHeight = (float)ctx.getDocumentSize().getHeight();

		setImageSize(docWidth, docHeight);

		//compute the transformation matrix
		AffineTransform Px = calculateSizeTransform(svgRoot, gvtRoot, uri, docWidth, docHeight, width, height);

		gvtRoot = renderImage(output, gvtRoot, Px, (int)width, (int)height);

		this.root = gvtRoot;
	}
	
	/**
	 * @param output
	 * @param gvtRoot
	 * @param Px
	 * @param w
	 * @param h
	 * @return
	 * @throws TranscoderException
	 */
	private GraphicsNode renderImage(TranscoderOutput output, GraphicsNode gvtRoot, AffineTransform Px, int w, int h)
		throws TranscoderException {
		
		// paint the SVG document using the bridge package
		// create the appropriate renderer
		ImageRendererFactory rendFactory = new ConcreteImageRendererFactory();
		StaticRenderer renderer = (StaticRenderer)rendFactory.createStaticImageRenderer();
		
		boolean antialias = true;
		if (hints.containsKey(KEY_ANTI_ALIASING)) {
			antialias = ((Boolean)hints.get(KEY_ANTI_ALIASING)).booleanValue();
		}
		
		RenderingHints renderHints = renderer.getRenderingHints();
		renderHints.put(RenderingHints.KEY_ANTIALIASING,
				antialias ? RenderingHints.VALUE_ANTIALIAS_ON : 
					RenderingHints.VALUE_ANTIALIAS_OFF);
		renderer.setRenderingHints(renderHints);
		
		renderer.updateOffScreen(w, h);
		renderer.setTransform(Px);
		renderer.setTree(gvtRoot);
		
		gvtRoot = null; // We're done with it...

		try {
			//TODO there should be some optimization opportunities here - to many buffer copies?
			// now we are sure that the aoi is the image size
			Shape raoi = new Rectangle2D.Float(0, 0, width, height);
			// Warning: the renderer's AOI must be in user space
			renderer.repaint(Px.createInverse().createTransformedShape(raoi));
			BufferedImage rend = renderer.getOffScreen();
			renderer = null; // We're done with it...

			BufferedImage dest = createImage(w, h);

			Graphics2D g2d = GraphicsUtil.createGraphics(dest);
			if (hints.containsKey(KEY_BACKGROUND_COLOR)) {
				Paint bgcolor = (Paint)hints.get(KEY_BACKGROUND_COLOR);
				g2d.setComposite(AlphaComposite.SrcOver);
				g2d.setPaint(bgcolor);
				g2d.fillRect(0, 0, w, h);
			}
			if (rend != null) { // might be null if the svg document is empty
				g2d.drawRenderedImage(rend, new AffineTransform());
			}
			g2d.dispose();
			rend = null; // We're done with it...
			writeImage(dest, output);
		} catch (Exception ex) {
			throw new TranscoderException(ex);
		}
		return gvtRoot;
	}





	private BufferedImage bufferedImage = null;
	
	/**
	 * getBufferedImage
	 * Accessor to return the buffered image used for rendering.
	 * 
	 * @return BufferedImage
	 */
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}
	
	/**
	 * Override to create a BufferedImage type that support an alpha channel for
	 * transparency.
	 * 
	 * @see org.apache.batik.transcoder.image.ImageTranscoder#createImage(int, int)
	 */
	public BufferedImage createImage(int w, int h) {
		return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	}
	
	/** 
	 * Override to support the translation of the BufferedImage type into the SWT Image
	 * format.
	 * 
	 * @see org.apache.batik.transcoder.image.ImageTranscoder#writeImage(java.awt.image.BufferedImage, org.apache.batik.transcoder.TranscoderOutput)
	 */
	public void writeImage(BufferedImage img, TranscoderOutput arg1)
		throws TranscoderException {

		bufferedImage = img;
	}
}
