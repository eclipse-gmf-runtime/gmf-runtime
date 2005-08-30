/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.handles;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RelativeLocator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.tools.ResizeTracker;

import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.figures.GatedPaneFigure;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;

/**
 * Creates a  non-visual resize handle for the given <code>ResizableCompartmentEditPart</code>.
 * @param owner The <code>GraphicalEditPart</code> owner.
 * 
 * @author jcorchis
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 * @author melaasar
 */
public class CompartmentResizeHandle extends AbstractHandle {

	private int location = PositionConstants.NORTH;

	/**
	 * Constructs an instance of a resize handle for a resizable compartment.
	 * @param owner The edit part which own's the compartment resize handle
	 */
	public CompartmentResizeHandle(IGraphicalEditPart owner, int location) {
		setOwner(owner);
		this.location = location;
		setLocator(new RelativeLocator(owner.getFigure(), location));
		setCursor(Cursors.getDirectionalCursor(location));
	}

	/**
	 * @return the drag tracker for the resizable edit part 
	 */
	protected DragTracker createDragTracker() {
		// TODO Eclipse 3.0 Check
		return new ResizeTracker(getOwner(), location) {
			protected List createOperationSet() {
				return Collections.singletonList(getOwner());
			}
		};
	}

	/**
	 * @see org.eclipse.draw2d.IFigure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		Rectangle rect = getOwnerFigure().getBounds().getCopy();

		MapMode.translateToDP(rect);
		if ((location & PositionConstants.NORTH_SOUTH) != 0)
			return new Dimension(rect.width, 3);
		else
			return new Dimension(3, rect.height);
	}

	
	protected IFigure getCompartmentFigure()
	{
		if( getOwner().getFigure() instanceof GatedPaneFigure ){
			return ((GatedPaneFigure)getOwner().getFigure()).getElementPane();
		}
		return getOwner().getFigure();
	}
	
	/**
	 * @see org.eclipse.draw2d.IFigure#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {
		if (super.containsPoint(x, y)) {
			ResizableCompartmentFigure f =
				(ResizableCompartmentFigure) getCompartmentFigure();
			boolean before = (location & PositionConstants.NORTH_WEST) != 0;
			return f.getAdjacentSibling(before) != null;
		}
		return false;
	}

}
