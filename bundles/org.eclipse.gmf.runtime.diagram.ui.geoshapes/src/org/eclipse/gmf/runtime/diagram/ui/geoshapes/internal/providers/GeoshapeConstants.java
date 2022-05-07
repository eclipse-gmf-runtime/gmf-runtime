/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
/*
 * Created on Nov 14, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.l10n.DiagramUIGeoshapesMessages;

/**
 * @author jschofie
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GeoshapeConstants {

	// Tool Creation Groups
	public static final String GROUP_RECTANGLES     = "rectangleMultiTool"; //$NON-NLS-1$
	public static final String GROUP_POLYGONS       = "polygon"; //$NON-NLS-1$

	// Supported Shapes
	public static final String TOOL_OVAL            = "oval"; //$NON-NLS-1$
	public static final String TOOL_TRIANGLE        = "triangle"; //$NON-NLS-1$
	public static final String TOOL_RECTANGLE       = "rectangle"; //$NON-NLS-1$
	public static final String TOOL_SHADOWRECTANGLE = "shadowRectangle"; //$NON-NLS-1$
	public static final String TOOL_3DRECTANGLE     = "rectangle3D"; //$NON-NLS-1$
	public static final String TOOL_ROUNDRECTANGLE  = "roundRectangle"; //$NON-NLS-1$
	public static final String TOOL_HEXAGON         = "hexagon"; //$NON-NLS-1$
	public static final String TOOL_OCTAGON         = "octagon"; //$NON-NLS-1$
	public static final String TOOL_PENTAGON        = "pentagon"; //$NON-NLS-1$
	public static final String TOOL_DIAMOND         = "diamond"; //$NON-NLS-1$
	public static final String TOOL_CYLINDER        = "cylinder"; //$NON-NLS-1$
//	public static final String TOOL_POLYGON         = "polygon"; //$NON-NLS-1$

	// Supported Connections
	public static final String TOOL_LINE = "line"; //$NON-NLS-1$

	public static List getSupportedShapes() {
		
		List toReturn = new ArrayList();
		
		toReturn.add( TOOL_OVAL );
		toReturn.add( TOOL_TRIANGLE );
		toReturn.add( TOOL_RECTANGLE );
		toReturn.add( TOOL_SHADOWRECTANGLE );
		toReturn.add( TOOL_3DRECTANGLE );
		toReturn.add( TOOL_ROUNDRECTANGLE );
		toReturn.add( TOOL_HEXAGON );
		toReturn.add( TOOL_OCTAGON );
		toReturn.add( TOOL_PENTAGON );
		toReturn.add( TOOL_DIAMOND );
		toReturn.add( TOOL_CYLINDER );
			
		return toReturn;
	}

	public static List getSupportedConnections() {
		
		List toReturn = new ArrayList();
		
		toReturn.add( TOOL_LINE );
			
		return toReturn;
	}
	
	public static String getShapeLocalizedType(String typeName) {
		return getShapeLabel(typeName).replaceFirst("&", "");//$NON-NLS-2$//$NON-NLS-1$
	}
	
	public static String getDisplayName(String label) {
		return label.replaceFirst("&", "");//$NON-NLS-2$//$NON-NLS-1$
	}
	
	public static String getShapeLabel(String toGet) {

		if (toGet.equals(TOOL_OVAL))
			return DiagramUIGeoshapesMessages.geoshape_OvalTool_Label;
		else if (toGet.equals(TOOL_TRIANGLE))
			return DiagramUIGeoshapesMessages.geoshape_TriangleTool_Label;
		else if (toGet.equals(TOOL_RECTANGLE))
			return DiagramUIGeoshapesMessages.geoshape_RectangleTool_Label;
		else if (toGet.equals(TOOL_SHADOWRECTANGLE))
			return DiagramUIGeoshapesMessages.geoshape_ShadowRectangleTool_Label;
		else if (toGet.equals(TOOL_3DRECTANGLE))
			return DiagramUIGeoshapesMessages.geoshape_3DRectangleTool_Label;
		else if (toGet.equals(TOOL_ROUNDRECTANGLE))
			return DiagramUIGeoshapesMessages.geoshape_RoundRectangleTool_Label;
		else if (toGet.equals(TOOL_HEXAGON))
			return DiagramUIGeoshapesMessages.geoshape_HexagonTool_Label;
		else if (toGet.equals(TOOL_OCTAGON))
			return DiagramUIGeoshapesMessages.geoshape_OctagonTool_Label;
		else if (toGet.equals(TOOL_PENTAGON))
			return DiagramUIGeoshapesMessages.geoshape_PentagonTool_Label;
		else if (toGet.equals(TOOL_DIAMOND))
			return DiagramUIGeoshapesMessages.geoshape_DiamondTool_Label;
		else if (toGet.equals(TOOL_CYLINDER))
			return DiagramUIGeoshapesMessages.geoshape_CylinderTool_Label;

		return DiagramUIGeoshapesMessages.geoshape_GeometricShapeTool_Label;
	}
}
