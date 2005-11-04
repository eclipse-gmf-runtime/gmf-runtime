/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.views.factories.GeoShapeViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.ConnectionViewFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.geoshapes.*
 * 
 * Notation Provider that registers all the specific Geometric Shapes
 */
public class GeoshapeViewProvider
	extends AbstractViewProvider {

	// Map to hold the Shape Views
	private Map shapeMap = new HashMap();
	{
		shapeMap.put(GeoshapeConstants.TOOL_OVAL, GeoShapeViewFactory.class);
		shapeMap.put(GeoshapeConstants.TOOL_TRIANGLE, GeoShapeViewFactory.class);
		shapeMap.put(GeoshapeConstants.TOOL_RECTANGLE, GeoShapeViewFactory.class);
		shapeMap
			.put(GeoshapeConstants.TOOL_SHADOWRECTANGLE, GeoShapeViewFactory.class);
		shapeMap.put(GeoshapeConstants.TOOL_3DRECTANGLE, GeoShapeViewFactory.class);
		shapeMap.put(GeoshapeConstants.TOOL_ROUNDRECTANGLE, GeoShapeViewFactory.class);
		shapeMap.put(GeoshapeConstants.TOOL_HEXAGON, GeoShapeViewFactory.class);
		shapeMap.put(GeoshapeConstants.TOOL_OCTAGON, GeoShapeViewFactory.class);
		shapeMap.put(GeoshapeConstants.TOOL_PENTAGON, GeoShapeViewFactory.class);
		shapeMap.put(GeoshapeConstants.TOOL_DIAMOND, GeoShapeViewFactory.class);
		shapeMap.put(GeoshapeConstants.TOOL_CYLINDER, GeoShapeViewFactory.class);
		//		shapeMap.put( GeoshapeConstants.TOOL_POLYGON, GeoShapeViewFactory.class);
	}
	
	//	 Map to hold the Line/Connection Views
	private Map connectionMap = new HashMap();
	{
		connectionMap.put(GeoshapeConstants.TOOL_LINE, ConnectionViewFactory.class);
	}
	/**
	 * Returns the shape view class to instantiate based on the passed params
	 * 
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @return Class
	 */
	protected Class getNodeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {

		return (Class) shapeMap.get(semanticHint);
	}

	/**
	 * Returns the connection view class to instantiate based on the passed
	 * params
	 * 
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @return Class
	 */
	protected Class getEdgeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		return (Class) connectionMap.get(semanticHint);
	}
}