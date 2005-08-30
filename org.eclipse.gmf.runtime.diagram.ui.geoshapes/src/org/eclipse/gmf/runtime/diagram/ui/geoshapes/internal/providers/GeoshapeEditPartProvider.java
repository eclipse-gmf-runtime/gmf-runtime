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

import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.CylinderEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.DiamondEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.EllipseEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.HexagonEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.LineEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.OctagonEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.PentagonEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.Rectangle3DEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.RectangleEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.RoundedRectangleEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.ShadowRectangleEditPart;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.editparts.TriangleEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import com.ibm.xtools.notation.View;

/**
 * @author jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.geoshapes.*
 *
 * Geoshape Edit Part provider
 */
public class GeoshapeEditPartProvider extends AbstractEditPartProvider {

	/** list of supported shape editparts. */
	private Map shapeMap = new HashMap();
	{
		shapeMap.put( GeoshapeConstants.TOOL_OVAL, EllipseEditPart.class);
		shapeMap.put( GeoshapeConstants.TOOL_TRIANGLE, TriangleEditPart.class);
		shapeMap.put( GeoshapeConstants.TOOL_RECTANGLE, RectangleEditPart.class);
		shapeMap.put( GeoshapeConstants.TOOL_SHADOWRECTANGLE, ShadowRectangleEditPart.class);
		shapeMap.put( GeoshapeConstants.TOOL_3DRECTANGLE, Rectangle3DEditPart.class);
		shapeMap.put( GeoshapeConstants.TOOL_ROUNDRECTANGLE, RoundedRectangleEditPart.class);
		shapeMap.put( GeoshapeConstants.TOOL_HEXAGON, HexagonEditPart.class);
		shapeMap.put( GeoshapeConstants.TOOL_OCTAGON, OctagonEditPart.class);
		shapeMap.put( GeoshapeConstants.TOOL_PENTAGON, PentagonEditPart.class);
		shapeMap.put( GeoshapeConstants.TOOL_DIAMOND, DiamondEditPart.class);
		shapeMap.put( GeoshapeConstants.TOOL_CYLINDER, CylinderEditPart.class);
//		shapeMap.put( GeoshapeConstants.TOOL_POLYGON, PolygonEditPart.class);
	}
	
	/** list of supported connector editparts. */
	private Map connectorMap = new HashMap();
	{
		connectorMap.put( GeoshapeConstants.TOOL_LINE, LineEditPart.class);
	}
	
	
	/**
	 * Gets a Node's editpart class.
	 * This method should be overridden by a provider if it wants to provide this service. 
	 * @param view the view to be <i>controlled</code> by the created editpart
	 */
	protected Class getNodeEditPartClass(View view ) {
		String semanticHint = view.getType();
		if(semanticHint!=null && semanticHint.length()>0)
			return ((Class)shapeMap.get(semanticHint));
		return null;
		
	}
	
	/**
	 * Set the editpart class to the editpart mapped to the supplied view's semantic hint.
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider#setConnectorEditPartClass(org.eclipse.gmf.runtime.diagram.ui.internal.view.IConnectorView)
	 */
	protected Class getConnectorEditPartClass(View view) {
		String semanticHint = view.getType();
		if(semanticHint!=null && semanticHint.length()>0)
			return ((Class)connectorMap.get(semanticHint));
		return null;
	}
}