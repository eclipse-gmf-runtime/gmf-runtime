/******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.clipboard;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.image.PartPositionInfo;
import org.eclipse.gmf.runtime.diagram.ui.internal.figures.IExpandableFigure;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.Decoration;
import org.eclipse.gmf.runtime.diagram.ui.l10n.SharedImages;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.LineSeg.Sign;
import org.eclipse.gmf.runtime.draw2d.ui.internal.graphics.ScaledGraphics;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedMapModeGraphics;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;

/**
 * Provides the framework to generate SWT and AWT images of a diagram or a
 * subset of editparts on a diagram.
 * 
 * @author sshaw
 * @author Barys Dubauski
 */
abstract public class DiagramGenerator {

	private int image_margin = 0;

	private DiagramEditPart _dgrmEP;

	/**
	 * Creates a new instance.
	 * 
	 * @param dgrmEP
	 *            the diagram editpart
	 */
	public DiagramGenerator(DiagramEditPart dgrmEP) {
		this._dgrmEP = dgrmEP;
		image_margin = getMapMode().DPtoLP(10);
	}

	/**
	 * @return DiagramEditPart
	 */
	protected DiagramEditPart getDiagramEditPart() {
		return this._dgrmEP;
	}

	/**
	 * Allows hook for the creation of a <code>Graphics</code> object that is
	 * used for the rendering of the diagram.
	 * 
	 * @param width
	 *            of the clipping area
	 * @param height
	 *            of the clipping area
	 * @return Graphics element that is the target for rendering.
	 */
	abstract protected Graphics setUpGraphics(int width, int height);

	/**
	 * Allows hook to dispose of any artifacts around the creation of the
	 * <code>Graphics</code> object used for rendering.
	 * 
	 * @param g
	 *            Graphics element that is to be disposed.
	 */
	protected void disposeGraphics(Graphics g) {
		g.dispose();
	}

	/**
	 * Creates an image descriptor representing the image rendered from the
	 * diagram.
	 * 
	 * @param g
	 *            Graphics object where information to form the image descriptor
	 *            can be retrieved from.
	 * @return ImageDescriptor representing the image rendered from the diagram.
	 */
	abstract protected ImageDescriptor getImageDescriptor(Graphics g);

	/**
	 * Creates an AWT image for the contents of the diagram editpart.
	 * 
	 * @return an image in AWT format
	 */
	final public Image createAWTImageForDiagram() {
		List editparts = getDiagramEditPart().getPrimaryEditParts();

		return createAWTImageForParts(editparts);
	}

	/**
	 * Creates an AWT image for the list of editparts passed in.
	 * 
	 * @param editparts
	 *            the list of <code>IGraphicalEditParts</code> that will be
	 *            rendered to the Image
	 * @return an image in AWT format
	 */
	public Image createAWTImageForParts(List editparts) {
		org.eclipse.swt.graphics.Rectangle diagramArea = calculateImageRectangle(editparts);
		return createAWTImageForParts(editparts, diagramArea);
	}

	/**
	 * Creates an SWT image descriptor for the contents of the diagram editpart.
	 * 
	 * @return an image descriptor for an SWT image
	 */
	final public ImageDescriptor createSWTImageDescriptorForDiagram() {
		List editparts = getDiagramEditPart().getPrimaryEditParts();

		return createSWTImageDescriptorForParts(editparts);
	}

	/**
	 * Creates an SWT image descriptor for the list of editparts passed in.Any
	 * connections where both the source and target editparts are passed in are
	 * also drawn.
	 * 
	 * @param editparts
	 *            the list of <code>IGraphicalEditParts</code> that will be
	 *            rendered to the Image
	 * @return an image descriptor for an SWT image
	 */
	final public ImageDescriptor createSWTImageDescriptorForParts(List editparts) {
		org.eclipse.swt.graphics.Rectangle sourceRect = calculateImageRectangle(editparts);
		return createSWTImageDescriptorForParts(editparts, sourceRect);
	}

	/**
	 * @return
	 */
	protected IMapMode getMapMode() {
		IMapMode mm = MapModeUtil.getMapMode(getDiagramEditPart().getFigure());
		return mm;
	}

	/**
	 * Renders the list of editparts to the graphics object. Any connections
	 * where both the source and target editparts are passed in are also drawn.
	 * 
	 * @param graphics
	 *            the graphics object on which to draw
	 * @param translateOffset
	 *            a <code>Point</code> that the value the
	 *            <code>graphics</code> object will be translated by in
	 *            relative coordinates.
	 * @param editparts
	 *            the list of <code>IGraphicalEditParts</code> that will be
	 *            rendered to the graphics object
	 */
	final protected void renderToGraphics(Graphics graphics,
			Point translateOffset, List editparts) {

		List sortedEditparts = sortSelection(editparts);

		graphics.translate((-translateOffset.x), (-translateOffset.y));
		graphics.pushState();

		List<GraphicalEditPart> connectionsToPaint = new LinkedList<GraphicalEditPart>();

		Map decorations = findDecorations(editparts);

		for (Iterator editPartsItr = editparts.listIterator(); editPartsItr.hasNext();) {
			IGraphicalEditPart editPart = (IGraphicalEditPart) editPartsItr.next();

			// do not paint selected connection part
			if (editPart instanceof ConnectionEditPart) {
				connectionsToPaint.add(editPart);
			} else {				
				connectionsToPaint.addAll(findConnectionsToPaint(editPart));
				// paint shape figure
				IFigure figure = editPart.getFigure();
				paintFigure(graphics, figure);

				paintDecorations(graphics, figure, decorations);
			}
		}
		
		// paint the connection parts after shape parts paint
		decorations = findDecorations(connectionsToPaint);

		for (Iterator<GraphicalEditPart> connItr = connectionsToPaint.iterator(); connItr.hasNext();) {
			IFigure figure = connItr.next().getFigure();
			paintFigure(graphics, figure);
			paintDecorations(graphics, figure, decorations);
		}
	}
	
	/**
	 * Collects all connections contained within the given edit part
	 * 
	 * @param editPart the container editpart
	 * @return connections within it
	 */
	private Collection<ConnectionEditPart> findConnectionsToPaint(IGraphicalEditPart editPart) {
		/*
		 * Set of node editparts contained within the given editpart
		 */
		HashSet<GraphicalEditPart> editParts = new HashSet<GraphicalEditPart>();
		
		/*
		 * All connection editparts that have a source contained within the given editpart
		 */
		HashSet<ConnectionEditPart> connectionEPs = new HashSet<ConnectionEditPart>();
		
		/*
		 * Connections contained within the given editpart (or just the connections to paint
		 */
		HashSet<ConnectionEditPart> connectionsToPaint = new HashSet<ConnectionEditPart>();
		
		/*
		 * Populate the set of node editparts
		 */
		getNestedEditParts(editPart, editParts);
		
		/*
		 * Populate the set of connections whose source is within the given editpart
		 */
		for (Iterator<GraphicalEditPart> editPartsItr = editParts.iterator(); editPartsItr.hasNext();) {
			connectionEPs.addAll(getAllConnectionsFrom(editPartsItr.next()));
		}
		
		/*
		 * Create a set of connections constained within the given editpart
		 */
		while (!connectionEPs.isEmpty()) {
			/*
			 * Take the first connection and check whethe there is a path
			 * through that connection that leads to the target contained within
			 * the given editpart
			 */
			Stack<ConnectionEditPart> connectionsPath = new Stack<ConnectionEditPart>();
			ConnectionEditPart conn = connectionEPs.iterator().next();
			connectionEPs.remove(conn);
			connectionsPath.add(conn);
			
			/*
			 * Initialize the target for the current path
			 */
			EditPart target = conn.getTarget();
			while(connectionEPs.contains(target)) {
				/*
				 * If the target end is a connection, check if it's one of the
				 * connection's whose target is a connection and within the
				 * given editpart. Append it to the path if it is. Otherwise
				 * check if the target is within the actual connections or nodes
				 * contained within the given editpart
				 */
				ConnectionEditPart targetConn = (ConnectionEditPart) target;
				connectionEPs.remove(targetConn);
				connectionsPath.add(targetConn);
				
				/*
				 * Update the target for the new path
				 */
				target = targetConn.getTarget();
			}
			
			/*
			 * The path is built, check if it's target is a node or a connection
			 * contained within the given editpart
			 */
			if (editParts.contains(target) || connectionsToPaint.contains(target)) {
				connectionsToPaint.addAll(connectionsPath);
			}
		}
		return connectionsToPaint;
	}
	
	/**
	 * Returns all connections orginating from a given editpart. All means that
	 * connections originating from connections that have a source given
	 * editpart will be included
	 * 
	 * @param ep the editpart 
	 * @return all source connections
	 */
	private List<ConnectionEditPart> getAllConnectionsFrom(GraphicalEditPart ep) {
		LinkedList<ConnectionEditPart> connections = new LinkedList<ConnectionEditPart>();
		for (Iterator itr = ep.getSourceConnections().iterator(); itr.hasNext();) {
			ConnectionEditPart sourceConn = (ConnectionEditPart) itr.next();
			connections.add(sourceConn);
			connections.addAll(getAllConnectionsFrom(sourceConn));
		}
		return connections;
	}

	/**
	 * This method is used when a figure needs to be painted to the graphics.
	 * The figure will be translated based on its absolute positioning.
	 * 
	 * @param graphics
	 *            Graphics object to render figure
	 * @param figure
	 *            the figure to be rendered
	 */
	private void paintFigure(Graphics graphics, IFigure figure) {

		if (!figure.isVisible())
			return;

		// Calculate the Relative bounds and absolute bounds
		Rectangle relBounds = null;
		if (figure instanceof IExpandableFigure)
			relBounds = ((IExpandableFigure) figure).getExtendedBounds()
					.getCopy();
		else
			relBounds = figure.getBounds().getCopy();

		Rectangle abBounds = relBounds.getCopy();
		translateToPrintableLayer(figure, abBounds);

		// Calculate the difference
		int transX = abBounds.x - relBounds.x;
		int transY = abBounds.y - relBounds.y;

		// Paint the figure
		graphics.pushState();
		graphics.translate(transX, transY);
		figure.paint(graphics);
		graphics.popState();
		graphics.restoreState();
	}

	/**
	 * Find the decorations that adorn the specified <code>editParts</code>.
	 * 
	 * @param editparts
	 *            the list of <code>IGraphicalEditParts</code> for which to
	 *            find decorations
	 * @return a mapping of {@link IFigure}to ({@link Decoration}or
	 *         {@link Collection}of decorations})
	 */
	private Map findDecorations(Collection editparts) {
		// create inverse mapping of figures to edit parts (need this to map
		// decorations to edit parts)
		Map figureMap = mapFiguresToEditParts(editparts);

		Map result = new java.util.HashMap();

		if (!editparts.isEmpty()) {
			IGraphicalEditPart first = (IGraphicalEditPart) editparts.iterator().next();

			IFigure decorationLayer = LayerManager.Helper.find(first).getLayer(
					DiagramRootEditPart.DECORATION_PRINTABLE_LAYER);

			if (decorationLayer != null) {
				// compute the figures of the shapes
				List figures = new java.util.ArrayList(editparts);
				for (ListIterator iter = figures.listIterator(); iter.hasNext();) {
					iter.set(((IGraphicalEditPart) iter.next()).getFigure());
				}

				// find the decorations on figures that were selected
				for (Iterator iter = decorationLayer.getChildren().iterator(); iter
						.hasNext();) {
					Object next = iter.next();

					if (next instanceof Decoration) {
						Decoration decoration = (Decoration) next;
						IFigure owner = decoration.getOwnerFigure();

						while (owner != null) {
    						if (figureMap.containsKey(owner)) {
    							Object existing = result.get(owner);
    
    							if (existing == null) {
    								result.put(owner, decoration);
    							} else if (existing instanceof Collection) {
    								((Collection) existing).add(decoration);
    							} else {
    								Collection c = new java.util.ArrayList(2);
    								c.add(existing);
    								c.add(decoration);
    								result.put(owner, c);
    							}
    							break;
    						} else {
    						    owner = owner.getParent();
    						}
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * Constructs a mapping of figures to their corresponding edit parts.
	 * 
	 * @param editParts
	 *            a collection of <code>IGraphicalEditParts</code>
	 * @return a mapping of {@link IFigure}to {@link IGraphicalEditPart}
	 */
	private Map mapFiguresToEditParts(Collection editParts) {
		Map result = new java.util.HashMap();

		for (Iterator iter = editParts.iterator(); iter.hasNext();) {
			IGraphicalEditPart next = (IGraphicalEditPart) iter.next();

			result.put(next.getFigure(), next);
		}

		return result;
	}

	/**
	 * Paints the decorations adorning the specified <code>figure</code>, if
	 * any.
	 * 
	 * @param graphics
	 *            the graphics to paint on
	 * @param figure
	 *            the figure
	 * @param decorations
	 *            mapping of figures to decorations, in which we will find the
	 *            <code>figure</code>'s decorations
	 */
	private void paintDecorations(Graphics graphics, IFigure figure,
			Map decorations) {
		Object decoration = decorations.get(figure);

		if (decoration != null) {
			if (decoration instanceof Collection) {
				for (Iterator iter = ((Collection) decoration).iterator(); iter
						.hasNext();) {
					paintFigure(graphics, (IFigure) iter.next());
				}
			} else {
				paintFigure(graphics, (IFigure) decoration);
			}
		}
	}

	/**
	 * This is a recursive method that search a tree of edit parts looking for
	 * edit parts contained in the open list. If the edit part is found it is
	 * removed from the open list and placed in the closed list.
	 * 
	 * @param editPart
	 * @param open
	 * @param closed
	 */
	private void sortSelection(GraphicalEditPart editPart, List open,
			List closed) {

		// Do nothing if the open list is empty
		if (open.isEmpty()) {
			return;
		}

		// IF the edit part is contained in the open list (we are searching for
		// it)
		if (open.contains(editPart)) {
			// Add the Edit Part to the closed list and remove it from
			// the open list
			closed.add(editPart);
			open.remove(editPart);
		}

		for (Iterator iter = editPart.getChildren().iterator(); iter.hasNext();) {
			GraphicalEditPart child = (GraphicalEditPart) iter.next();
			sortSelection(child, open, closed);
		}
	}

	private List sortSelection(List toSort) {
		List closed = new ArrayList(toSort.size());
		List open = new ArrayList(toSort.size());
		open.addAll(toSort);

		sortSelection(getDiagramEditPart(), open, closed);
		if (!open.isEmpty()) {
			closed.addAll(open);
		}

		return closed;
	}

	/**
	 * This method is used to obtain the list of child edit parts for shape
	 * compartments.
	 * 
	 * @param childEditPart
	 *            base edit part to get the list of children editparts
	 * @param editParts
	 *            list of nested shape edit parts
	 */
	private void getNestedEditParts(IGraphicalEditPart childEditPart,
			Collection editParts) {

		for (Iterator iter = childEditPart.getChildren().iterator(); iter
				.hasNext();) {

			IGraphicalEditPart child = (IGraphicalEditPart) iter.next();
			editParts.add(child);
			getNestedEditParts(child, editParts);
		}
	}

	/**
	 * Determine the minimal rectangle required to bound the list of editparts.
	 * A margin is used around each of the editpart's figures when calculating
	 * the size.
	 * 
	 * @param editparts
	 *            the list of <code>IGraphicalEditParts</code> from which
	 *            their figure bounds will be used
	 * @return Rectangle the minimal rectangle that can bound the figures of the
	 *         list of editparts
	 */
	public org.eclipse.swt.graphics.Rectangle calculateImageRectangle(
			List editparts) {
		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;

		IMapMode mm = getMapMode();

		for (int i = 0; i < (editparts.size()); i++) {
			IGraphicalEditPart editPart = (IGraphicalEditPart) editparts.get(i);

			IFigure figure = editPart.getFigure();
			Rectangle bounds = null;
			if (figure instanceof IExpandableFigure)
				bounds = ((IExpandableFigure) figure).getExtendedBounds();
			else
				bounds = figure.getBounds().getCopy();
			translateToPrintableLayer(figure, bounds);
			bounds = bounds.getExpanded(getImageMargin(), getImageMargin());

			if (i == 0) {
				minX = bounds.x;
				maxX = bounds.x + bounds.width;
				minY = bounds.y;
				maxY = bounds.y + bounds.height;
			} else {
				minX = Math.min(minX, bounds.x);
				maxX = Math.max(maxX, (bounds.x + bounds.width));
				minY = Math.min(minY, bounds.y);
				maxY = Math.max(maxY, (bounds.y + bounds.height));
			}
		}

		int width = (maxX - minX);
		int height = (maxY - minY);
		if (width <= 0) {
			width = mm.DPtoLP(100);
		}

		if (height <= 0) {
			height = mm.DPtoLP(100); // create an empty image if the
			// diagram does not contain child
		}
		org.eclipse.swt.graphics.Rectangle imageRect = new org.eclipse.swt.graphics.Rectangle(
				minX, minY, width, height);
		return imageRect;
	}
	
	/**
	 * Get the positional data and the semantic elements for each
	 * <code>ShapeEditPart</code>, <code>ShapeCompartmentEditPart</code>,
	 * and <code>ConnectionEditPart</code> on the diagram.
	 * 
	 * @return A list of {@link PartPositionInfo}objects with positional data
	 *         and the semantic element for the relevant editparts on the
	 *         diagram.
	 */
	public List getDiagramPartInfo() {
		Assert.isNotNull(_dgrmEP);
		return getDiagramPartInfo(_dgrmEP);
	}

	/**
	 * Get the positional data and the semantic elements for each
	 * <code>ShapeEditPart</code>, <code>ShapeCompartmentEditPart</code>,
	 * and <code>ConnectionEditPart</code> on the diagram.
	 * 
	 * @param diagramEditPart
	 *            The diagram edit part.
	 * @return A list of {@link PartPositionInfo}objects with positional data
	 *         and the semantic element for the relevant editparts on the
	 *         diagram.
	 */
	public List getDiagramPartInfo(DiagramEditPart diagramEditPart) {
		List<PartPositionInfo> result = new ArrayList<PartPositionInfo>();
		List<IGraphicalEditPart> editParts = new ArrayList<IGraphicalEditPart>();

		List<IGraphicalEditPart> children = (List<IGraphicalEditPart>) diagramEditPart.getPrimaryEditParts();
		IMapMode mm = getMapMode();

		// We will use the diagram generate that was used to generate the image
		// to figure out the outer-bound rectangle so that we are calculating
		// the
		// image positions using the same box as was used to create the image.
		org.eclipse.swt.graphics.Rectangle imageRect = calculateImageRectangle(children);

		for (IGraphicalEditPart part : children) {
			editParts.add(part);
			getNestedEditParts(part, editParts);
		}

		for (IGraphicalEditPart part : editParts) {
			IFigure figure = part.getFigure();

			// RATLC00139941: Need to support any kind of shape edit part
			// and shape compartments, too, because these sometimes
			// correspond to distinct semantic elements
			if (part instanceof ShapeEditPart
					|| part instanceof ShapeCompartmentEditPart) {

				PartPositionInfo position = new PartPositionInfo();

				position.setSemanticElement(ViewUtil
						.resolveSemanticElement((View) part.getModel()));

				Rectangle bounds = figure.getBounds().getCopy();
				translateToPrintableLayer(figure, bounds);
				bounds.translate(-imageRect.x, -imageRect.y);

				position.setPartHeight(mm.LPtoDP(bounds.height));
				position.setPartWidth(mm.LPtoDP(bounds.width));
				position.setPartX(mm.LPtoDP(bounds.x));
				position.setPartY(mm.LPtoDP(bounds.y));
				result.add(0, position);
			} else if (part instanceof ConnectionEditPart) {
				// find a way to get (P1, P2, ... PN) for connection edit part
				// add MARGIN and calculate "stripe" for the polyline instead of
				// bounding box.
				PartPositionInfo position = new PartPositionInfo();

				position.setSemanticElement(ViewUtil
						.resolveSemanticElement((View) part.getModel()));

				if (figure instanceof PolylineConnection) {
					PolylineConnection mainPoly = (PolylineConnection) figure;
					PointList mainPts = mainPoly.getPoints();

					translateToPrintableLayer(figure, mainPts);
					List<Point> envelopingPts = calculateEnvelopingPolyline(mainPts,
							new Point(imageRect.x, imageRect.y));
					List<Point> transformedPts = convertPolylineUnits(envelopingPts);

					position.setPolyline(transformedPts);
					result.add(0, position);
				}
			}
		}
		return result;
	}
	
	public List<PartPositionInfo> getConstrainedDiagramPartInfo(int maxWidth, int maxHeight, boolean useMargins) {
		return getConstrainedDiagramPartInfo(_dgrmEP, maxWidth, maxHeight, useMargins);
	}
	
	public List<PartPositionInfo> getConstrainedDiagramPartInfo(DiagramEditPart diagramEditPart, int maxWidth, int maxHeight, boolean useMargins) {
		List<PartPositionInfo> result = new ArrayList<PartPositionInfo>();
		List<IGraphicalEditPart> editParts = new ArrayList<IGraphicalEditPart>();

		List<IGraphicalEditPart> children = (List<IGraphicalEditPart>) diagramEditPart.getPrimaryEditParts();
		IMapMode mm = getMapMode();

		// We will use the diagram generate that was used to generate the image
		// to figure out the outer-bound rectangle so that we are calculating
		// the
		// image positions using the same box as was used to create the image.
		ConstrainedImageRenderingData data = getConstrainedImageRenderingData(children, maxWidth, maxHeight, useMargins);
		Rectangle imageRect = data.imageOriginalBounds.getCopy();
		mm.DPtoLP(imageRect);
		if (useMargins) {
			imageRect.shrink(getImageMargin(), getImageMargin());
		}
		imageRect.performScale(data.scalingFactor);
		if (useMargins) {
			imageRect.expand(getImageMargin(), getImageMargin());
		}

		for (IGraphicalEditPart part : children) {
			editParts.add(part);
			getNestedEditParts(part, editParts);
		}

		for (IGraphicalEditPart part : editParts) {
			IFigure figure = part.getFigure();

			// RATLC00139941: Need to support any kind of shape edit part
			// and shape compartments, too, because these sometimes
			// correspond to distinct semantic elements
			if (part instanceof ShapeEditPart
					|| part instanceof ShapeCompartmentEditPart) {

				PartPositionInfo position = new PartPositionInfo();

				position.setSemanticElement(ViewUtil
						.resolveSemanticElement((View) part.getModel()));

				Rectangle bounds = figure.getBounds().getCopy();
				translateToPrintableLayer(figure, bounds);
				bounds.performScale(data.scalingFactor);
				bounds.translate(-imageRect.x, -imageRect.y);

				position.setPartHeight(mm.LPtoDP(bounds.height));
				position.setPartWidth(mm.LPtoDP(bounds.width));
				position.setPartX(mm.LPtoDP(bounds.x));
				position.setPartY(mm.LPtoDP(bounds.y));
				result.add(0, position);
			} else if (part instanceof ConnectionEditPart) {
				// find a way to get (P1, P2, ... PN) for connection edit part
				// add MARGIN and calculate "stripe" for the polyline instead of
				// bounding box.
				PartPositionInfo position = new PartPositionInfo();

				position.setSemanticElement(ViewUtil
						.resolveSemanticElement((View) part.getModel()));

				if (figure instanceof PolylineConnection) {
					PolylineConnection mainPoly = (PolylineConnection) figure;
					PointList mainPts = mainPoly.getPoints();
					mainPts.performScale(data.scalingFactor);

					translateToPrintableLayer(figure, mainPts);
					List<Point> envelopingPts = calculateEnvelopingPolyline(mainPts,
							new Point(imageRect.x, imageRect.y));
					List<Point> transformedPts = convertPolylineUnits(envelopingPts);

					position.setPolyline(transformedPts);
					result.add(0, position);
				}
			}
		}

		return result;
	}

	/**
	 * Recursively moves up the figure containment tree until it reaches the
	 * printable layer or the root.
	 * 
	 * @param figure
	 *            the figure that the translatable is relative to
	 * @param translatable
	 *            the object that needs to be translated
	 */
	private void translateToPrintableLayer(IFigure figure,
			Translatable translatable) {

		IFigure printableLayer = getDiagramEditPart().getLayer(
				LayerConstants.PRINTABLE_LAYERS);
		if (figure == null || figure.equals(printableLayer)) {
			return;
		}

		figure.translateToParent(translatable);
		translateToPrintableLayer(figure.getParent(), translatable);
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
	 * @author Barys Dubauski
	 */
	private List<Point> calculateEnvelopingPolyline(PointList polyPts, Point origin) {
		List<Point> result = new ArrayList<Point>();
		List<LineSeg> mainSegs = (List<LineSeg>) PointListUtilities.getLineSegments(polyPts);

		int mainSegsLength = mainSegs.size();

		LineSeg segment = null;
		Point orthoPoint1 = null;
		Point orthoPoint2 = null;

		// collect points clockwise
		for (int i = 0; i < mainSegsLength; i++) {
			segment = (LineSeg) mainSegs.get(i);
			orthoPoint1 = segment.locatePoint(0.0, getImageMargin(),
					Sign.POSITIVE);
			orthoPoint1.translate(-origin.x, -origin.y);
			orthoPoint2 = segment.locatePoint(1.0, getImageMargin(),
					Sign.POSITIVE);
			orthoPoint2.translate(-origin.x, -origin.y);

			result.add(orthoPoint1);
			result.add(orthoPoint2);
		}

		// now add the original poly
		for (int i = mainSegsLength - 1; i >= 0; i--) {
			segment = (LineSeg) mainSegs.get(i);
			orthoPoint1 = segment.getTerminus();
			orthoPoint1.translate(-origin.x, -origin.y);
			result.add(orthoPoint1);
			orthoPoint2 = segment.getOrigin();
			orthoPoint2.translate(-origin.x, -origin.y);
			result.add(orthoPoint2);
		}

		// add first point to close the polyline per "poly" area type HTML
		// requirements
		result.add(result.get(0));

		return result;
	}

	/**
	 * transforms coordinates of the polyline from logical units to device units
	 * 
	 * @param polyPts
	 * @return List of Point type objects (that carry X and Y coordinate pair)
	 *         representing the polyline
	 * @author Barys Dubauski
	 */
	private List<Point> convertPolylineUnits(List<Point> polyPts) {
		List<Point> result = new ArrayList<Point>();
		IMapMode mm = getMapMode();
		for (Point point : polyPts) {
			Point newPoint = new Point(mm.LPtoDP(point.x), mm.LPtoDP(point.y));
			result.add(newPoint);
		}
		return result;
	}

	/**
	 * @return <code>int</code> value that is the margin around the generated
	 *         image in logical coordinates.
	 */
	protected int getImageMargin() {
		return image_margin;
	}

	/**
	 * Generates AWT image of specified editparts on the specified rectangle.
	 * 
	 * @param editParts editparts
	 * @param diagramArea clipping rectangle
	 * @return AWT image
	 */
	public Image createAWTImageForParts(List editParts,
			org.eclipse.swt.graphics.Rectangle diagramArea) {
		return null;
	}

	final public ImageDescriptor createSWTImageDescriptorForParts(
			List editparts, org.eclipse.swt.graphics.Rectangle sourceRect) {

		// initialize imageDesc to the error icon
		ImageDescriptor imageDesc = new ImageDescriptor() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.resource.ImageDescriptor#getImageData()
			 */
			public ImageData getImageData() {
				return SharedImages.get(SharedImages.IMG_ERROR).getImageData();
			}
		};

		Graphics graphics = null;
		try {
			IMapMode mm = getMapMode();
			
			PrecisionRectangle rect = new PrecisionRectangle();
			rect.setX(sourceRect.x);
			rect.setY(sourceRect.y);
			rect.setWidth(sourceRect.width);
			rect.setHeight(sourceRect.height);
			
			mm.LPtoDP(rect);

			// Create the graphics and wrap it with the HiMetric graphics object
			graphics = setUpGraphics((int) Math.round(rect.preciseWidth), (int) Math.round(rect.preciseHeight));

			RenderedMapModeGraphics mapModeGraphics = new RenderedMapModeGraphics(
					graphics, getMapMode());

			renderToGraphics(mapModeGraphics, new Point(sourceRect.x,
					sourceRect.y), editparts);
			imageDesc = getImageDescriptor(graphics);
		} finally {
			if (graphics != null)
				disposeGraphics(graphics);
		}

		return imageDesc;
	}
	
	/**
	 * Creates an SWT image descriptor for editparts. Editparts are scaled to fit in maxDeviceWidth and maxDeviceHeight
	 * frame
	 * 
	 * @param editParts editparts
	 * @param maxDeviceWidth max width for the image
	 * @param maxDeviceHeight max height for the image
	 * @param useMargins true if 10 pisels margins are required to bound the editparts image
	 * @return the image descriptor
	 */
	final public ImageDescriptor createConstrainedSWTImageDecriptorForParts(List editParts, int maxDeviceWidth, int maxDeviceHeight, boolean useMargins) {
		ImageDescriptor imageDesc = new ImageDescriptor() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.resource.ImageDescriptor#getImageData()
			 */
			public ImageData getImageData() {
				return SharedImages.get(SharedImages.IMG_ERROR).getImageData();
			}
		};
		
		Graphics graphics = null;
		try {
			IMapMode mm = getMapMode();

			ConstrainedImageRenderingData data = getConstrainedImageRenderingData(editParts, maxDeviceWidth, maxDeviceHeight, useMargins);

			// Create the graphics and wrap it with the HiMetric graphics object
			graphics = setUpGraphics(data.imageWidth, data.imageHeight);

			ScaledGraphics scaledGraphics = new ScaledGraphics(graphics);
			
			RenderedMapModeGraphics mapModeGraphics = new RenderedMapModeGraphics(
					scaledGraphics, getMapMode());
			
			graphics.translate(data.margin, data.margin);
			mapModeGraphics.scale(data.scalingFactor);

			Point location = new PrecisionPoint(data.imageOriginalBounds.preciseX(), data.imageOriginalBounds.preciseY());
			mm.DPtoLP(location);
			renderToGraphics(mapModeGraphics, location, editParts);
			imageDesc = getImageDescriptor(graphics);
		} finally {
			if (graphics != null)
				disposeGraphics(graphics);
		}

		return imageDesc;
	}
	
	class ConstrainedImageRenderingData {
		double scalingFactor;
		int imageWidth; // in pixels
		int imageHeight; // in pixels
		Rectangle imageOriginalBounds; // in pixels
		int margin; // margins size in pixels
	}
	
	ConstrainedImageRenderingData getConstrainedImageRenderingData(List editParts, int maxDeviceWidth, int maxDeviceHeight, boolean useMargins) {
		ConstrainedImageRenderingData data = new ConstrainedImageRenderingData();
		IMapMode mm = getMapMode();
		
		data.imageOriginalBounds = new PrecisionRectangle(new Rectangle(calculateImageRectangle(editParts)));
		mm.LPtoDP(data.imageOriginalBounds);
		
		int deviceMargins = mm.LPtoDP(getImageMargin());
		data.margin = useMargins ? deviceMargins : 0; 
		double xScalingFactor = 1.0, yScalingFactor = xScalingFactor;
		
		data.imageOriginalBounds.shrink(deviceMargins, deviceMargins);
		
		if (maxDeviceWidth > data.margin) {
			xScalingFactor = (maxDeviceWidth  - data.margin - data.margin)/ (data.imageOriginalBounds.preciseWidth());
		}
		if (maxDeviceHeight > data.margin) {
			yScalingFactor = (maxDeviceHeight - data.margin - data.margin) / (data.imageOriginalBounds.preciseHeight());
		}
		
		data.scalingFactor = Math.min(Math.min(xScalingFactor, yScalingFactor), 1);
		
		data.imageWidth = data.imageOriginalBounds.width + data.margin + data.margin;
		data.imageHeight = data.imageOriginalBounds.height + data.margin + data.margin;
				
		if (data.scalingFactor < 1) {
			data.imageWidth = (int) Math.round(data.imageOriginalBounds.preciseWidth() * data.scalingFactor) + data.margin + data.margin;
			data.imageHeight = (int) Math.round(data.imageOriginalBounds.preciseHeight() * data.scalingFactor) + data.margin + data.margin;
		} else {
			data.scalingFactor = 1;
		}
		return data;
	}
	
	/**
	 * Creates an AWT image for editparts. Editparts are scaled to fit in maxDeviceWidth and maxDeviceHeight
	 * frame
	 * 
	 * @param editParts editparts
	 * @param maxDeviceWidth max width for the image
	 * @param maxDeviceHeight max height for the image
	 * @param useMargins true if 10 pisels margins are required to bound the editparts image
	 * @return the image
	 */
	public Image createConstrainedAWTImageForParts(List editParts, int maxDeviceWidth, int maxDeviceHeight, boolean useMargins) {
		return null;
	}

}