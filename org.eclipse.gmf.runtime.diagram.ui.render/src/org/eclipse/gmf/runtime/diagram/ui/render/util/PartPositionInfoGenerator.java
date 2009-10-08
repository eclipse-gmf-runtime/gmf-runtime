/******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.render.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.LabelEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.image.PartPositionInfo;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PrecisionPointList;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg.Sign;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A Utility class to generate the info for images of diagrams
 * 
 * @author aboyko
 *
 */
public final class PartPositionInfoGenerator {

	/**
	 * Margin around the connection. <code>Double</code> value is expected. The
	 * generator picks the maximum between this value and the line width of the
	 * connection. The value must be in logical units.
	 * <p>Default value of 0 is taken if options is not provided</p>
	 */
	public static final String CONNECTION_MARGIN = "connectionMargin"; //$NON-NLS-1$
	/**
	 * Point of the origin of the diagram, this is expected to be a
	 * {@link org.eclipse.draw2d.geometry.Point} in logical units, relative to
	 * printable layer.
	 * <p>Default value of (0,0) is taken if this option is not provided</p>
	 */
	public static final String DIAGRAM_ORIGIN = "diagramOrigin"; //$NON-NLS-1$
	/**
	 * Scaling factor for generating parts info for scaled down or up diagram.
	 * Double is expected.
	 * <p>Default value of 1.0 will be taken if this option is
	 * not provided</p>
	 */
	public static final String SCALE_FACTOR = "scaleFactor";  //$NON-NLS-1$

	/**
	 * Generates the info for a diagram
	 * 
	 * @param diagramEditPart the diagram
	 * @param options options affecting positional info
	 * @return a list of <code>PartPositionInfo</code>
	 */
	public static final List<PartPositionInfo> getDiagramPartInfo(
			DiagramEditPart diagramEditPart, Map<String, Object> options) {
		List<PartPositionInfo> result = new ArrayList<PartPositionInfo>();
		List<IGraphicalEditPart> editParts = new ArrayList<IGraphicalEditPart>();

		List<IGraphicalEditPart> children = (List<IGraphicalEditPart>) diagramEditPart.getPrimaryEditParts();
		IMapMode mm = MapModeUtil.getMapMode(diagramEditPart.getFigure());
		
		double connectionMargin = options.get(PartPositionInfoGenerator.CONNECTION_MARGIN) != null ?
				((Double)options.get(PartPositionInfoGenerator.CONNECTION_MARGIN)).doubleValue() : 0;
		Point origin = options.get(PartPositionInfoGenerator.DIAGRAM_ORIGIN) != null ?
				(Point)options.get(PartPositionInfoGenerator.DIAGRAM_ORIGIN) : new Point();
		double scale = options.get(PartPositionInfoGenerator.SCALE_FACTOR) != null ?
				((Double)options.get(PartPositionInfoGenerator.CONNECTION_MARGIN)).doubleValue() : 1.0;
		if (scale <= 0) {
			throw new IllegalArgumentException();
		}

		for (IGraphicalEditPart part : children) {
			editParts.add(part);
			getNestedEditParts(part, editParts);
		}
		
		IFigure printableLayer = LayerManager.Helper.find(diagramEditPart)
				.getLayer(LayerConstants.PRINTABLE_LAYERS);

		for (IGraphicalEditPart part : editParts) {
			IFigure figure = part.getFigure();

			// Need to support any kind of shape edit part
			// and shape compartments, too, because these sometimes
			// correspond to distinct semantic elements
			View view = part.getNotationView();
			if (part instanceof ConnectionEditPart
					&& figure instanceof PolylineConnection) {
				// find a way to get (P1, P2, ... PN) for connection edit part
				// add MARGIN and calculate "stripe" for the polyline instead of
				// bounding box.
				PartPositionInfo position = new PartPositionInfo();

				position.setView(view);
				position.setSemanticElement(ViewUtil
						.resolveSemanticElement(view));

				PolylineConnection mainPoly = (PolylineConnection) figure;
				PointList mainPts = mainPoly.getPoints().getCopy();

				DiagramImageUtils.translateTo(mainPts, figure, printableLayer);
				PointList envelopingPts = calculateEnvelopingPolyline(mainPts,
						(int) Math.max(connectionMargin, mainPoly
								.getLineWidth() >> 1));
				envelopingPts.translate(new PrecisionPoint(-origin.preciseX(),
						-origin.preciseY()));
				mm.LPtoDP(envelopingPts);
				envelopingPts.performScale(scale);

				List<Point> pts = new ArrayList(envelopingPts.size());
				for (int i = 0; i < envelopingPts.size(); i++) {
					pts.add(envelopingPts.getPoint(i));
				}

				position.setPolyline(pts);
				result.add(0, position);
			} else if ((view != null && view.isSetElement())
					|| (part instanceof ShapeEditPart
							|| part instanceof ShapeCompartmentEditPart || part instanceof LabelEditPart)) {
				PartPositionInfo position = new PartPositionInfo();

				position.setView(view);
				position.setSemanticElement(ViewUtil
						.resolveSemanticElement(view));

				PrecisionRectangle bounds = new PrecisionRectangle(figure
						.getBounds());
				DiagramImageUtils.translateTo(bounds, figure, printableLayer);
				bounds.translate(new PrecisionPoint(-origin.preciseX(), -origin
						.preciseY()));
				mm.LPtoDP(bounds);

				bounds.performScale(scale);

				position.setPartHeight(bounds.height);
				position.setPartWidth(bounds.width);
				position.setPartX(bounds.x);
				position.setPartY(bounds.y);
				result.add(0, position);
			}
		}
		return result;
	}

	private static void getNestedEditParts(IGraphicalEditPart childEditPart,
			Collection editParts) {

		for (Iterator iter = childEditPart.getChildren().iterator(); iter
				.hasNext();) {

			IGraphicalEditPart child = (IGraphicalEditPart) iter.next();
			editParts.add(child);
			getNestedEditParts(child, editParts);
		}
	}

	/**
	 * Calculates enveloping polyline for a given polyline with margin MARGIN
	 * 
	 *   E1                  E2
	 *     +----------------+
	 *     |                |<------- MARGIN
	 *   A *----------------* B
	 *     |                |
	 *     +----------------+
	 *   E4                  E3
	 * 
	 * On the figure above: AB is a given polyline. E1E2E3E4 is enveloping
	 * polyline built around AB perimeter using margin MARGIN.
	 * 
	 * 
	 * @param polyPts
	 * @param origin
	 *            location of the main diagram bounding box used to shift
	 *            coordinates to be relative against diagram
	 * 
	 * @return List of Point type objects (that carry X and Y coordinate pair)
	 *         representing the polyline
	 */
	private static PointList calculateEnvelopingPolyline(PointList polyPts, int margin) {
		PointList result = new PrecisionPointList(polyPts.size() << 1);
		List<LineSeg> mainSegs = (List<LineSeg>) PointListUtilities.getLineSegments(polyPts);		
		if (mainSegs.size() > 0) {		
			result = calculateParallelPolyline(mainSegs, margin);
			PointList pts = calculateParallelPolyline(mainSegs, -margin);
			for (int i = pts.size() - 1; i >= 0; i--) {
				result.addPoint(pts.getPoint(i));
			}
			result.addPoint(result.getFirstPoint());
		}
		return result;
	}
	
	/**
	 * Calculates polyline offset from the given polyline by the margin value
	 * 
	 *  ResultA          ResultB
	 *     +----------------+
	 *     |                |<------- MARGIN
	 *   A *----------------* B
	 * 
	 * On the figure above: AB is a given polyline. ResultA-ResultB is the result
	 * @param polySegs given polyline
	 * @param margin offset from given poly-line, can be negative.
	 * @return offset or parallel polyline.
	 */
	private static PointList calculateParallelPolyline(List<LineSeg> polySegs, int margin) {
		PointList result = new PrecisionPointList(polySegs.size() << 2);
		int index = 0;
		int absMargin = Math.abs(margin);
		Sign sign = margin < 0 ? Sign.NEGATIVE : Sign.POSITIVE;
		LineSeg parallel_1, parallel_2;
		result.addPoint(polySegs.get(index++).locatePoint(0, absMargin, sign));
		parallel_1 = polySegs.get(index - 1).getParallelLineSegThroughPoint(result.getLastPoint());
		for (; index < polySegs.size(); index++) {
			parallel_2 = polySegs.get(index).getParallelLineSegThroughPoint(
					polySegs.get(index).locatePoint(0, absMargin, sign));
			PointList intersections = parallel_1.getLinesIntersections(parallel_2);
			if (intersections.size() > 0) {
				result.addPoint(intersections.getFirstPoint());
			} else {
				result.addPoint(parallel_1.getTerminus());
				result.addPoint(parallel_2.getOrigin());
			}
			parallel_1 = parallel_2;
		}
		result.addPoint(polySegs.get(index - 1).locatePoint(1.0, absMargin, sign));
		return result;
	}
	
}

