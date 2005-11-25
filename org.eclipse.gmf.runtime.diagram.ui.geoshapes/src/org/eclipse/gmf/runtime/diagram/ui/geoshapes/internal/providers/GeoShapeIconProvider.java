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


package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers;

import java.util.HashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.GetIconOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.IIconProvider;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.l10n.DiagramUIGeoshapesPluginImages;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Image;

/**
 * Provides Geoshape Icons
 * 
 * @author jschofie
 */
public class GeoShapeIconProvider
	extends AbstractProvider
	implements IIconProvider {

	/** map for storing icon images based on semantic hint */
	private static HashMap semanticHintIconMap = new HashMap();
	static {

		semanticHintIconMap.put(GeoshapeConstants.TOOL_OVAL,
			DiagramUIGeoshapesPluginImages.DESC_OVAL);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_TRIANGLE,
			DiagramUIGeoshapesPluginImages.DESC_TRIANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_RECTANGLE,
			DiagramUIGeoshapesPluginImages.DESC_RECTANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_SHADOWRECTANGLE,
			DiagramUIGeoshapesPluginImages.DESC_SHADOWRECTANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_3DRECTANGLE,
			DiagramUIGeoshapesPluginImages.DESC_3DRECTANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_ROUNDRECTANGLE,
			DiagramUIGeoshapesPluginImages.DESC_ROUNDRECTANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_HEXAGON,
			DiagramUIGeoshapesPluginImages.DESC_HEXAGON);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_OCTAGON,
			DiagramUIGeoshapesPluginImages.DESC_OCTAGON);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_PENTAGON,
			DiagramUIGeoshapesPluginImages.DESC_PENTAGON);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_DIAMOND,
			DiagramUIGeoshapesPluginImages.DESC_DIAMOND);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_CYLINDER,
			DiagramUIGeoshapesPluginImages.DESC_CYLINDER);

		semanticHintIconMap.put(GeoshapeConstants.TOOL_LINE,
			DiagramUIGeoshapesPluginImages.DESC_LINE);
	}

	/** map for storing icon images based on type */
	private static HashMap typeIconMap = new HashMap();
	static {

		typeIconMap.put(GeoshapeType.OVAL, DiagramUIGeoshapesPluginImages.DESC_OVAL);
		typeIconMap.put(GeoshapeType.TRIANGLE, DiagramUIGeoshapesPluginImages.DESC_TRIANGLE);
		typeIconMap.put(GeoshapeType.RECTANGLE,
			DiagramUIGeoshapesPluginImages.DESC_RECTANGLE);
		typeIconMap.put(GeoshapeType.SHADOWRECTANGLE,
			DiagramUIGeoshapesPluginImages.DESC_SHADOWRECTANGLE);
		typeIconMap.put(GeoshapeType.THREEDRECTANGLE,
			DiagramUIGeoshapesPluginImages.DESC_3DRECTANGLE);
		typeIconMap.put(GeoshapeType.ROUNDRECTANGLE,
			DiagramUIGeoshapesPluginImages.DESC_ROUNDRECTANGLE);
		typeIconMap.put(GeoshapeType.HEXAGON, DiagramUIGeoshapesPluginImages.DESC_HEXAGON);
		typeIconMap.put(GeoshapeType.OCTAGON, DiagramUIGeoshapesPluginImages.DESC_OCTAGON);
		typeIconMap.put(GeoshapeType.PENTAGON, DiagramUIGeoshapesPluginImages.DESC_PENTAGON);
		typeIconMap.put(GeoshapeType.DIAMOND, DiagramUIGeoshapesPluginImages.DESC_DIAMOND);
		typeIconMap.put(GeoshapeType.CYLINDER, DiagramUIGeoshapesPluginImages.DESC_CYLINDER);

		typeIconMap.put(GeoshapeType.LINE, DiagramUIGeoshapesPluginImages.DESC_LINE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.icon.IIconProvider#getIcon(org.eclipse.core.runtime.IAdaptable,
	 *      int)
	 */
	public Image getIcon(IAdaptable hint, int flags) {

		if (hint != null) {
			if (hint instanceof View) {
				View view = (View) hint;

				String semanticHint = view.getType();
				return getIcon(semanticHint);
			} else {
				String fileName = (String) typeIconMap.get(hint);

				if (fileName != null) {
					return DiagramUIGeoshapesPluginImages.get(
						fileName);
				}
			}

		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * return an icon based on the geo shape's semantic hint (e.g. "oval") 
	 * this code was refactored from the previous method so that the compare merge
	 * extended notation item provider had a way to render the appropriate icons for
	 * its display
	 * 
	 */
	public Image getIcon(String semanticHint) {
		String fileName = (String) semanticHintIconMap.get(semanticHint);

		if (fileName != null) {
			return DiagramUIGeoshapesPluginImages.get(fileName);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {

		IAdaptable hint = ((GetIconOperation) operation).getHint();

		if (hint != null) {
			if (hint instanceof View) {
				String semanticHint = ((View)hint).getType();
				return (getIcon(semanticHint)!=null);
			}
			Object type = hint.getAdapter(GeoshapeType.class);
			if (type != null && typeIconMap.containsKey(type)) {
				return true;
			}
		}

		return false;
	}

}