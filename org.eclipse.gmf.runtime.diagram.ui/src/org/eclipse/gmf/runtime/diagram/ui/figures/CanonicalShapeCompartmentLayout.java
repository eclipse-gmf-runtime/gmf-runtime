/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.figures;

import java.util.Map;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;

/**
 * Layouts the shape compartment's children.  This class provides utility methods
 * to layout figure's whose position are undefined.
 * 
 * @see org.eclipse.gmf.runtime.diagram.ui.figures.ICanonicalShapeCompartmentLayout
 * 
 * @author mhanner
 */
public class CanonicalShapeCompartmentLayout
	extends FreeformLayout
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
