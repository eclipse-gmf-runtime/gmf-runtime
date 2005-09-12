/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.l10n.GeoshapesResourceManager;

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

	// Geometric Shape Icons
	public static final String ICON_OVAL            = "IconEllipse.gif";  //$NON-NLS-1$
	public static final String ICON_TRIANGLE        = "IconTriangle.gif"; //$NON-NLS-1$
	public static final String ICON_RECTANGLE       = "IconRectangle.gif"; //$NON-NLS-1$
	public static final String ICON_SHADOWRECTANGLE = "IconShadowRectangle.gif"; //$NON-NLS-1$
	public static final String ICON_3DRECTANGLE     = "Icon3DRectangle.gif"; //$NON-NLS-1$
	public static final String ICON_ROUNDRECTANGLE  = "IconRoundRectangle.gif"; //$NON-NLS-1$
	public static final String ICON_HEXAGON         = "IconHexagon.gif"; //$NON-NLS-1$
	public static final String ICON_OCTAGON         = "IconOctagon.gif"; //$NON-NLS-1$
	public static final String ICON_PENTAGON        = "IconPentagon.gif"; //$NON-NLS-1$
	public static final String ICON_DIAMOND         = "IconDiamond.gif"; //$NON-NLS-1$
	public static final String ICON_CYLINDER        = "IconCylinder.gif"; //$NON-NLS-1$

	// Supported Connections
	public static final String TOOL_LINE = "line"; //$NON-NLS-1$

	// Connection Icons
	public static final String ICON_LINE            = "IconLine.gif"; //$NON-NLS-1$


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
	
	public static String getShapeLabel( String toGet ) {
		
		if( toGet.equals( TOOL_OVAL ) )
			return GeoshapesResourceManager.getInstance().getString("geoshape.OvalTool.Label"); //$NON-NLS-1$
		else if ( toGet.equals( TOOL_TRIANGLE ) )
			return GeoshapesResourceManager.getInstance().getString("geoshape.TriangleTool.Label"); //$NON-NLS-1$
		else if( toGet.equals( TOOL_RECTANGLE ) )
			return GeoshapesResourceManager.getInstance().getString("geoshape.RectangleTool.Label"); //$NON-NLS-1$
		else if( toGet.equals( TOOL_SHADOWRECTANGLE ) )
			return GeoshapesResourceManager.getInstance().getString("geoshape.ShadowRectangleTool.Label"); //$NON-NLS-1$
		else if( toGet.equals( TOOL_3DRECTANGLE ) )
			return GeoshapesResourceManager.getInstance().getString("geoshape.3DRectangleTool.Label"); //$NON-NLS-1$
		else if( toGet.equals( TOOL_ROUNDRECTANGLE ) )
			return GeoshapesResourceManager.getInstance().getString("geoshape.RoundRectangleTool.Label"); //$NON-NLS-1$
		else if( toGet.equals( TOOL_HEXAGON ) )
			return GeoshapesResourceManager.getInstance().getString("geoshape.HexagonTool.Label"); //$NON-NLS-1$
		else if( toGet.equals( TOOL_OCTAGON ) )
			return GeoshapesResourceManager.getInstance().getString("geoshape.OctagonTool.Label"); //$NON-NLS-1$
		else if( toGet.equals( TOOL_PENTAGON ) )
			return GeoshapesResourceManager.getInstance().getString("geoshape.PentagonTool.Label"); //$NON-NLS-1$
		else if( toGet.equals( TOOL_DIAMOND ) )
			return GeoshapesResourceManager.getInstance().getString("geoshape.DiamondTool.Label"); //$NON-NLS-1$
		else if( toGet.equals( TOOL_CYLINDER ) )
			return GeoshapesResourceManager.getInstance().getString("geoshape.CylinderTool.Label"); //$NON-NLS-1$

		return "Geometric shape"; //$NON-NLS-1$
	}
}
