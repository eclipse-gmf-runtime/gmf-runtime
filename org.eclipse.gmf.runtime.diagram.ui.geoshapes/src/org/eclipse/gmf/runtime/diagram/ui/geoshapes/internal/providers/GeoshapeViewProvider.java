/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.views.factories.GeoShapeViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.ConnectorViewFactory;
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
	
	//	 Map to hold the Line/Connector Views
	private Map connectorMap = new HashMap();
	{
		connectorMap.put(GeoshapeConstants.TOOL_LINE, ConnectorViewFactory.class);
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
	 * Returns the connector view class to instantiate based on the passed
	 * params
	 * 
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @return Class
	 */
	protected Class getConnectorViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		return (Class) connectorMap.get(semanticHint);
	}
}