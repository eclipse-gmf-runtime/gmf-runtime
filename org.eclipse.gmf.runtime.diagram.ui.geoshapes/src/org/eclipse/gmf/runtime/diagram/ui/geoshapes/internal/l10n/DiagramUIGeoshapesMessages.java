/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.l10n;

import org.eclipse.osgi.util.NLS;

/**
 * An accessor class for externalized strings.
 *
 * @author cmahoney
 */
public final class DiagramUIGeoshapesMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.l10n.DiagramUIGeoshapesMessages";//$NON-NLS-1$

	private DiagramUIGeoshapesMessages() {
		// Do not instantiate
	}

	public static String geoshape_addShapes_menuItem;
	public static String geoshape_addShapes_menuItem_tooltip;
	public static String geoshape_LineTool_Label;
	public static String geoshape_LineTool_Description;
	public static String geoshape_OvalTool_Label;
	public static String geoshape_OvalTool_Description;
	public static String geoshape_TriangleTool_Label;
	public static String geoshape_TriangleTool_Description;
	public static String geoshape_RectangleTool_Label;
	public static String geoshape_RectangleTool_Description;
	public static String geoshape_ShadowRectangleTool_Label;
	public static String geoshape_ShadowRectangleTool_Description;
	public static String geoshape_3DRectangleTool_Label;
	public static String geoshape_3DRectangleTool_Description;
	public static String geoshape_RoundRectangleTool_Label;
	public static String geoshape_RoundRectangleTool_Description;
	public static String geoshape_HexagonTool_Label;
	public static String geoshape_HexagonTool_Description;
	public static String geoshape_OctagonTool_Label;
	public static String geoshape_OctagonTool_Description;
	public static String geoshape_PentagonTool_Label;
	public static String geoshape_PentagonTool_Description;
	public static String geoshape_DiamondTool_Label;
	public static String geoshape_DiamondTool_Description;
	public static String geoshape_CylinderTool_Label;
	public static String geoshape_CylinderTool_Description;
	public static String geoshape_PolygonTool_Label;
	public static String geoshape_PolygonTool_Description;
	public static String geoshape_GeometricShapeTool_Label;

	static {
		NLS.initializeMessages(BUNDLE_NAME, DiagramUIGeoshapesMessages.class);
	}
}