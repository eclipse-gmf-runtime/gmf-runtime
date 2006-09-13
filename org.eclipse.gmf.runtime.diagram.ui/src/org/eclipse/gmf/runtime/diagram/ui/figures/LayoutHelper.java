/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeUtil;

/**
 * A utility class used to find the location to place an <i>undefined</i>
 * child figure.
 * @author mhanner
 */
public class LayoutHelper {
	
	/**
	 * constructor 
	 * 
	 */
	public LayoutHelper() {
	    //
	}

	/** Constant that represetns an <i>undefined</i> element. */
	public static final Rectangle UNDEFINED = new Rectangle(-1,-1,-1,-1);
	
	/**
	 * Layout the supplied parent's children whose position is equal to {@link #UNDEFINED}.
	 * @see #getReferencePosition(IFigure)
	 * @param mgr the calling layout manager
	 * @param parent the containing figure.
	 * @param registry the [figure,editpart] registry
	 */
	public void layoutUndefinedChildren(
		ICanonicalShapeCompartmentLayout mgr,
		IFigure parent,
		Map registry) {
		List children = getUnpositionedChildren(parent);
		if (!children.isEmpty()) {
			for (int i = 0; i < children.size(); i++) {
				IFigure child = (IFigure) children.get(i);
				Point loc = getReferencePosition(parent);
				loc = validatePosition(parent, new Rectangle(loc, child.getSize()));
				child.setLocation( loc );
				parent.getLayoutManager().setConstraint( child, child.getBounds());
			}
		}
	}

	/**
	 * Return the supplied figures children whose location is {@link #UNDEFINED}.
	 * @param parent the containing figure.
	 * @return list of figures
	 */
	public final List getUnpositionedChildren(IFigure parent) {
		List children = parent.getChildren();
		List retval = new ArrayList();
		for (int i = 0; i < children.size(); i++) {
			IFigure child = (IFigure) children.get(i);
//			System.out.println( "getConstraints(" + child + ") " + parent.getLayoutManager().getConstraint(child));//$NON-NLS-2$//$NON-NLS-1$
			if (UNDEFINED
				.getLocation()
				.equals(child.getBounds().getLocation())) {
				retval.add(child);
			}
		}
		return retval;
	}

	/**
	 * Return an initial point to position <i>UNDEFINED</i> children.
	 * @see #validatePosition(IFigure, Rectangle)
	 * @param parent the containing figure (typically <tt>layout()</tt>'s input parameter)
	 * @return <code>parent.getBounds().getCenter().getCopy();</code>
	 */
	public Point getReferencePosition(IFigure parent) {
		return parent.getBounds().getCenter().getCopy();
	}

	/**
	 * Returns a location inside the supplied <tt>parent</tt> that is currently
	 * unoccupied by another figure.
	 * @param parent containing figure.
	 * @param bounds are being searched.
	 * @return an unoccupied position.
	 */
	public Point validatePosition( IFigure parent, Rectangle bounds ) {
		Rectangle theBounds = bounds.getCopy();
		IFigure clobber = findFigureIn(parent, bounds);
		if ( clobber != null ) {
			theBounds.setLocation( updateClobberedPosition(clobber,null) );
			return validatePosition(parent, theBounds);
		}
		return theBounds.getLocation();
	}
	
	/**
	 * Return a child figure that is already occupying the a position within
	 * the supplied <tt>bounds</tt> 
	 * figure.
	 * @param parent The parent figure of the newly added child.
	 * @param bounds the area being tested. 
	 * @return a child figure already occupying the supplied <tt>bounds</tt>
	 * or <tt>null</tt> if the bounds are unoccuppied.
	 */
	public IFigure findFigureIn(IFigure parent, Rectangle bounds) {
		ListIterator listIterator = parent.getChildren().listIterator();
		final boolean useContainCheck = UNDEFINED.getSize().equals(bounds.getSize());
		while (listIterator.hasNext()) {
			IFigure child = (IFigure)listIterator.next();
			Rectangle cBounds = child.getBounds();
			if (UNDEFINED.getLocation().equals(cBounds.getLocation())) {
				continue; //ignore this figure
			}
			if (useContainCheck) {
				if (cBounds.contains(bounds.getLocation())) {
					return child;
				}
			}
			else if (cBounds.intersects(bounds)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Returns a position <b>not</b> contained with the supplied figures bounds.
	 * @param clobbered the figure currently occuping the <i>reference position<\i>
	 * @param newlyAddedChild the child figure being added. <B>NOTE, MAY BE NULL</B>
	 * @return <code>clobbered.getBounds().getRight().getCopy().translate(30,0);</code> 
	 * @see #getReferencePosition(IFigure)
	 * @see #validatePosition(IFigure, Rectangle)
	 */
	public Point updateClobberedPosition(
		IFigure clobbered, IFigure newlyAddedChild) {
		return clobbered.getBounds().getRight().getCopy().translate(MapModeUtil.getMapMode(clobbered).DPtoLP(30), 0);
	}
}
