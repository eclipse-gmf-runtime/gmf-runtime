/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.figures;

import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gmf.runtime.diagram.ui.layout.FreeFormLayoutEx;

/**
 * Layouts the shape compartment's children.  This class provides utility methods
 * to layout figure's whose position are undefined.
 * 
 * @see org.eclipse.gmf.runtime.diagram.ui.figures.ICanonicalShapeCompartmentLayout
 * 
 * @author mhanner
 */
public class CanonicalShapeCompartmentLayout
	extends FreeFormLayoutEx
	implements ICanonicalShapeCompartmentLayout {


	/** the [figure,editpart] map. */
	private Map _visualPartMap;

	/** layout helper instance */
	private LayoutHelper _layoutHelper = null;

	/**
	 * Creates an instance
	 * @param map the [figure,editpart] map.
	 * @see org.eclipse.gef.EditPartViewer#getVisualPartMap()
	 */
	public CanonicalShapeCompartmentLayout(Map map) {
		setVisualPartMap(map);
	}

	/**
	 * Implements the algorithm to layout the components of the given container figure.
	 * Each component is laid out using it's own layout constraint specifying it's size
	 * and position. Calls {@link #layoutUndefinedChildren(IFigure)} to layout 
	 * all child figure whose position are <i>undefined</i>
	 * @see org.eclipse.gmf.runtime.diagram.ui.figures.ICanonicalShapeCompartmentLayout
	 * @param parent containing figure.
	 */
	public void layout(IFigure parent) {
		layoutUndefinedChildren(parent);
		super.layout(parent);
	}

	/**
	 * Layout the supplied parent's children whose position is equal to {@link #UNDEFINED}.
	 * Calls <code>LayoutHelper.layoutUndefinedChildren(ICanonicalShapeCompartmentLayout, IFigure, Map)</code>;
	 * @param parent the containing figure.
	 */
	public void layoutUndefinedChildren(IFigure parent) {
		GetLayoutHelper().layoutUndefinedChildren(
			this,
			parent,
			getVisualPartMap());
	}

	/** Return this manager's layout helper. */
	private LayoutHelper GetLayoutHelper() {
		if (_layoutHelper == null) {
			_layoutHelper = new LayoutHelper();
		}
		return _layoutHelper;
	}

	/**
	 * Set the [figure, editpart] map.
	 * @see org.eclipse.gef.EditPartViewer#getVisualPartMap() 
	 */
	public void setVisualPartMap(Map map) {
		_visualPartMap = map;
	}

	/** 
	 * Return the [figure, editpart] map.
	 * @see org.eclipse.gef.EditPartViewer#getVisualPartMap()
	 */
	public Map getVisualPartMap() {
		return _visualPartMap;
	}
}
