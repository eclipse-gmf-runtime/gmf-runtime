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

package org.eclipse.gmf.runtime.gef.ui.figures;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Ray;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;

/**
 * @author oboyko
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SlidableImageAnchor
	extends SlidableAnchor {

	static private class ImageAnchorLocation {

		static private Map imageAnchorLocationMap = new WeakHashMap();

		/**
		 * getInstance Static method for returning an instance of the
		 * ImageAnchorLocation object corresponding to the given Image.
		 * 
		 * @param image
		 *            Image to determine anchor location for
		 * @return ImageAnchorLocation containing cached information about
		 *         anchor
		 */
		static ImageAnchorLocation getInstance(Image image) {
			ImageAnchorLocation imgAnchorLoc = (ImageAnchorLocation) imageAnchorLocationMap
				.get(image);
			if (imgAnchorLoc == null) {
				imgAnchorLoc = new ImageAnchorLocation(image);
				imageAnchorLocationMap.put(image, imgAnchorLoc);
			}

			return imgAnchorLoc;
		}

		private Map locationMap = new HashMap();

		private ImageData imgData = null;

		private ImageData transMaskData = null;

		private ImageAnchorLocation(Image img) {
			imgData = img.getImageData();
			transMaskData = imgData.getTransparencyMask();
		}

		/**
		 * @return Returns the imgData.
		 */
		protected ImageData getImageData() {
			return imgData;
		}

		/**
		 * @return Returns the transMaskData.
		 */
		protected ImageData getTransparencyMaskData() {
			return transMaskData;
		}

		/**
		 * isTransparentAt Accessor to determine if the image is transparent at
		 * a given point.
		 * 
		 * @param x
		 *            int location into the image
		 * @param y
		 *            int location into the image
		 * @param checkAdjacent
		 *            check adjacent pixels for transparency as well.
		 * @return boolean true if transparent, false otherwise.
		 */
		protected boolean isTransparentAt(int x, int y, boolean checkAdjacent) {
			// boundary checking
			if (x < 0 || x >= getImageData().width || y < 0
				|| y >= getImageData().height)
				return true;

			// check for alpha channel
			int transValue = 255;
			// check for transparency mask
			if (getTransparencyMaskData() != null) {
				transValue = getTransparencyMaskData().getPixel(x, y) == 0 ? 0
					: 255;
			}

			if (transValue != 0) {
				if (getImageData().alphaData != null) {
					transValue = getImageData().getAlpha(x, y);
				}
			}

			// use a tolerance
			boolean trans = false;
			if (transValue < 10) {
				trans = true;

				if (checkAdjacent) {
					trans &= isTransparentAt(x + 1, y, false);
					trans &= isTransparentAt(x + 1, y + 1, false);
					trans &= isTransparentAt(x + 1, y - 1, false);
					trans &= isTransparentAt(x - 1, y + 1, false);
					trans &= isTransparentAt(x - 1, y, false);
					trans &= isTransparentAt(x - 1, y - 1, false);
					trans &= isTransparentAt(x, y + 1, false);
					trans &= isTransparentAt(x, y - 1, false);
				}
			}

			return trans;
		}

		/**
		 * getLocation Delegation function used by the ConnectionAnchor
		 * getLocation
		 * 
		 * @param start the <code>Point</code> that is the beginning of a line segment used to 
		 * calculate the anchor location inside the image.
		 * @param edge the <code>Point</code> that is the end of a line segment used to 
		 * calculate the anchor location inside the image.
		 * @param isDefaultAnchor - true if location for the default anchor should be calculated
		 * @return Point representing the location inside the image to anchor
		 *         to.
		 */
		private Point getLocation(Point start, Point edge, Rectangle containerRect, boolean isDefaultAnchor) {

			int angle = calculateAngleOfEntry(start, edge);
			Point top = containerRect.getTopLeft();
			
			Point ptIntersect = null;
			
			// Default anchors are cached
			if (isDefaultAnchor) {
				// determine if a cached value exists
				ptIntersect = (Point) locationMap.get(new Integer(angle));
			}
			if (ptIntersect == null) {
				// if no cached value exists return the calculated value and add to
				// the map
				Dimension dim = edge.getDifference(top);
				Point edgeImg = new Point(Math.max(0, Math.min(dim.width,
					getImageData().width - 1)), Math.max(0, Math.min(dim.height,
					getImageData().height - 1)));
				Dimension startDim = start.getDifference(top);
				Point startImg = new Point(Math.max(0, Math.min(startDim.width,
					getImageData().width - 1)), Math.max(0, Math.min(
					startDim.height, getImageData().height - 1)));
				ptIntersect = calculateIntersection(startImg, edgeImg);
				if (ptIntersect == null)
					return null;
				if (isDefaultAnchor) {
					locationMap.put(new Integer(angle), ptIntersect);
				}
			}
			return ptIntersect.getTranslated(top.x, top.y);
		}

		/**
		 * calculateAngleOfEntry Utility method to calculate the angle of entry
		 * 
		 * @param start
		 * @param edge
		 * @return int angle in degrees rounded to 15% for use as a key to a
		 *         map.
		 */
		private int calculateAngleOfEntry(Point start, Point edge) {
			LineSeg lineSeg = new LineSeg(start, edge);
			Ray ray = new Ray(lineSeg.getOrigin(), new Point(lineSeg
				.getOrigin().x + 1, lineSeg.getOrigin().y));

			double angle = 0.0;
			LineSeg.TrigValues trig = lineSeg.getTrigValues(ray);
			if (trig != null)
				angle = Math.atan2(-trig.sinTheta, -trig.cosTheta) + Math.PI;

			int keyAngle = (int) Math.round(angle * 360 / (Math.PI * 2));
			return keyAngle - (keyAngle % 10);
		}

		/**
		 * calculateIntersection Utility method to calculate the intersection
		 * point of a given point at an angle into the image to find the first
		 * opaque pixel.
		 * 
		 * @param start
		 *            Point that is in the center of the Image.
		 * @param edge
		 *            Point that is on the edge of the Image.
		 * @return Point that is the intersection with the first opaque pixel.
		 */
		private Point calculateIntersection(Point start, Point edge) {
			Point opaque = new Point(edge);

			LineSeg line = new LineSeg(start, edge);
			long distance = Math.round(line.length());

			// otherwise calculate value
			while (opaque.x >= 0 && opaque.x < getImageData().width
				&& opaque.y >= 0 && opaque.y < getImageData().height) {

				if (!isTransparentAt(opaque.x, opaque.y, true)) {
					return opaque;
				}

				line.pointOn(distance, LineSeg.KeyPoint.ORIGIN, opaque);
				distance--;
			}

			// default is to fall through and return the chopbox point
			return null;
		}
	}

	private ImageFigure imageFig;

	/**
	 * Empty constructor
	 */
	public SlidableImageAnchor() {
		super();
	}

	/**
	 * Dumb default constructor, for which reference point is at the center of the figure
	 * @param f the <code>IFigure</code> bounding figure
	 */
	public SlidableImageAnchor(IFigure f) {
		super(f);
	}

	/**
	 * Default constructor, for which reference point is at the cneter of the figure
	 * 
	 * @param container the <code>IFigure</code> bounding figure
	 * @param imageFig the <code>ImageFigure</code> inside the bounding figure
	 */
	public SlidableImageAnchor(IFigure container, ImageFigure imageFig) {
		super(container);
		this.imageFig = imageFig;
	}

	/**
	 * Constructor, for which reference point is specified
	 * 
	 * @param f the <code>IFigure</code> bounding figure
	 * @param imageFig the <code>ImageFigure</code> inside the bounding figure
	 * @param p the <code>PrecisionPoint</code> relative reference
	 */
	public SlidableImageAnchor(IFigure f, ImageFigure imageFig, PrecisionPoint p) {
		super(f, p);
		this.imageFig = imageFig;
	}

	/**
	 * Returns the image.
	 * 
	 * @return the <code>Image</code> object
	 */
	protected Image getImage() {
		return imageFig.getImage();
	}
	
	/**
	 * Returns bounds of the figure.
	 * 
	 * @return the owner figure
	 */
	protected IFigure getContainer() {
		return getOwner();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.figures.SlidableAnchor#getLocation(org.eclipse.draw2d.geometry.Point, org.eclipse.draw2d.geometry.Point)
	 */
	protected Point getLocation(Point ownReference, Point foreignReference) {
		Image image = getImage();
		if (image==null)
			return super.getLocation(ownReference,foreignReference);
		Rectangle ownerRect = new Rectangle(getBox());
		PointList intersections = getIntersectionPoints(ownReference, foreignReference);
		if (intersections!=null && intersections.size()!=0) {
			Point ptRef = PointListUtilities.pickFarestPoint(intersections, foreignReference);
			Point ptEdge = PointListUtilities.pickClosestPoint(intersections,foreignReference);
			Point loc = ImageAnchorLocation.getInstance(getImage()).getLocation(ptRef, ptEdge, ownerRect, isDefaultAnchor());
			if (loc != null)
				loc = normalizeToStraightlineTolerance(foreignReference, loc, 3);
			return loc;
		}
		return null;
	}
}
