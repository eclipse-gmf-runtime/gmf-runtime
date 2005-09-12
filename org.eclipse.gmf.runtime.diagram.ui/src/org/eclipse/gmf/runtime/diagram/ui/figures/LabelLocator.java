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

package org.eclipse.gmf.runtime.diagram.ui.figures;

import org.eclipse.draw2d.AbstractLocator;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gmf.runtime.diagram.ui.internal.figures.LabelHelper;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.LabelViewConstants;
import org.eclipse.gmf.runtime.draw2d.ui.geometry.PointListUtilities;

/**
 * Label locator that supports locating labels whose parent is either a Node or
 * and Edge.
 * 
 * @author jcorchis
 */
public class LabelLocator extends AbstractLocator {


	/**
	 * the parent figure of this locator
	 */
	protected IFigure parent;
	private int alignment;
	private Point offSet;
	private Dimension extent;

	/**
	 * Constructor to create a an instance of <code>LabelLocator</code>
	 * which locates an IFigure offset relative to a calculated reference point.
	 * @param parent the parent figure
	 * @param offSet the relative location of the label
	 * @param alignment the alignment hint in the case the parent is a <code>Connection</code>
	 */
	public LabelLocator(IFigure parent, Point offSet, int alignment) {
		this.parent = parent;
		this.offSet = offSet;
		this.alignment = alignment;

	}
	
	/**
	 * Constructor for figure who are located and sized.
	 * @param parent
	 * @param bounds
	 * @param alignment
	 */
	public LabelLocator(IFigure parent, Rectangle bounds, int alignment) {
		this(parent, bounds.getLocation(), alignment);
		this.extent = bounds.getSize();
	}
	
	/**
	 * getter for the offset point
	 * @return point
	 */
	public Point getOffset() {
		return this.offSet;
	}
	
	/**
	 * setter for the offset point
	 * @param offset
	 */
	public void setOffset(Point offset) {
		this.offSet = offset;
	}

	/**
	 * Positions the lable relative to the reference point with the
	 * given offsets.
	 */
	public void relocate(IFigure target) {
		Point location = LabelHelper.relativeCoordinateFromOffset(target, getReferencePoint(), offSet);
		target.setLocation(location);
				
		if (extent != null) {
			LabelLocator currentConstraint = (LabelLocator)target.getParent().getLayoutManager().getConstraint(target);
			Dimension currentExtent = currentConstraint.getSize();
			Dimension size = new Dimension(currentExtent);
			if (currentExtent.width == -1) size.width = target.getPreferredSize().width;
			if (currentExtent.height == -1) size.height = target.getPreferredSize().height;
			target.setSize(size);
		} else {
			target.setSize(new Dimension(target.getPreferredSize().width, target.getPreferredSize().height));			
		}
	}
	
	/**
	 * Returns the reference point for the locator.
	 * @return the reference point
	 */
	protected Point getReferencePoint() {
		if (parent instanceof Connection) {
			PointList ptList = ((Connection) parent).getPoints();	
			return PointListUtilities.calculatePointRelativeToLine(ptList, 0, getLocation(), true);						
		} else {
			return parent.getBounds().getLocation();			
		}
	}

	/**
	 * Returns the 
	 */
	private int getLocation() {
		switch (getAlignment()) {
			case ConnectionLocator.SOURCE:
				return LabelViewConstants.TARGET_LOCATION;
			case ConnectionLocator.TARGET:
				return LabelViewConstants.SOURCE_LOCATION;
			case ConnectionLocator.MIDDLE:
				return LabelViewConstants.MIDDLE_LOCATION;
			default:
				return LabelViewConstants.MIDDLE_LOCATION;
		}
	}

	/**
	 * Returns the alignment of ConnectorLocator. 
	 * @return The alignment
	 * @since 2.0
	 */
	public int getAlignment() {
		return alignment;
	}
	
	/**
	 * Returns the current extent for this locator
	 * @return the extent
	 */
	public Dimension getSize() {
		return extent.getCopy();
	}
	
	/**
	 * Returns the <code>PointList</code> describing the label's parent.
	 * 
	 * @return pointList
	 */
	protected PointList getPointList() {
		if (parent instanceof Connection) {
			return ((Connection) parent).getPoints();
		} else {
			PointList ptList = new PointList();
			ptList.addPoint(parent.getBounds().getLocation());
			return ptList;
		}
	}


}