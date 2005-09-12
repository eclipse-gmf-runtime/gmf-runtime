/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.Tool;

import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory;
import org.eclipse.gmf.runtime.diagram.ui.tools.ConnectorCreationTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.UnspecifiedTypeCreationTool;

/**
 * A palette factory for Geoshapes Entries
 * 
 * @author jschofie
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.geoshapes.*
 */
public class GeoshapePaletteFactory
	extends PaletteFactory.Adapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rational.xtools.gef.ui.palette.PaletteFactory#createTool(java.lang.String)
	 */
	public Tool createTool(String toolId) {

		if (toolId.equals(GeoshapeConstants.GROUP_POLYGONS)) {
			List polygons = new ArrayList();

			polygons.add(GeoshapeType.TRIANGLE);
			polygons.add(GeoshapeType.HEXAGON);
			polygons.add(GeoshapeType.OCTAGON);
			polygons.add(GeoshapeType.PENTAGON);
			polygons.add(GeoshapeType.DIAMOND);
			return new UnspecifiedTypeCreationTool(polygons);
		}

		if (toolId.equals(GeoshapeConstants.GROUP_RECTANGLES)) {
			List rectangles = new ArrayList();
			
			rectangles.add(GeoshapeType.RECTANGLE);
			rectangles.add(GeoshapeType.SHADOWRECTANGLE);
			rectangles.add(GeoshapeType.THREEDRECTANGLE);
			rectangles.add(GeoshapeType.ROUNDRECTANGLE);
			return new UnspecifiedTypeCreationTool(rectangles);
		}

		if (toolId.equals(GeoshapeConstants.TOOL_LINE)) {
			return new ConnectorCreationTool(GeoshapeType.LINE);
		}

		if (toolId.equals(GeoshapeConstants.TOOL_OVAL)) {
			return new CreationTool(GeoshapeType.OVAL);
		}
		if (toolId.equals(GeoshapeConstants.TOOL_TRIANGLE)) {
			return new CreationTool(GeoshapeType.TRIANGLE);
		}
		if (toolId.equals(GeoshapeConstants.TOOL_RECTANGLE)) {
			return new CreationTool(GeoshapeType.RECTANGLE);
		}
		if (toolId.equals(GeoshapeConstants.TOOL_SHADOWRECTANGLE)) {
			return new CreationTool(GeoshapeType.SHADOWRECTANGLE);
		}
		if (toolId.equals(GeoshapeConstants.TOOL_3DRECTANGLE)) {
			return new CreationTool(GeoshapeType.THREEDRECTANGLE);
		}
		if (toolId.equals(GeoshapeConstants.TOOL_ROUNDRECTANGLE)) {
			return new CreationTool(GeoshapeType.ROUNDRECTANGLE);
		}
		if (toolId.equals(GeoshapeConstants.TOOL_HEXAGON)) {
			return new CreationTool(GeoshapeType.HEXAGON);
		}
		if (toolId.equals(GeoshapeConstants.TOOL_OCTAGON)) {
			return new CreationTool(GeoshapeType.OCTAGON);
		}
		if (toolId.equals(GeoshapeConstants.TOOL_PENTAGON)) {
			return new CreationTool(GeoshapeType.PENTAGON);
		}
		if (toolId.equals(GeoshapeConstants.TOOL_DIAMOND)) {
			return new CreationTool(GeoshapeType.DIAMOND);
		}
		if (toolId.equals(GeoshapeConstants.TOOL_CYLINDER)) {
			return new CreationTool(GeoshapeType.CYLINDER);
		}

		return null;
	}

}