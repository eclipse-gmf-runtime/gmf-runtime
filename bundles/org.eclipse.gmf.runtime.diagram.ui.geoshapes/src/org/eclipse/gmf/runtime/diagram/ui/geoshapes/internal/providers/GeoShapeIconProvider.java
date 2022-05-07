/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers;

import java.util.HashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.IIconOperation;
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
			DiagramUIGeoshapesPluginImages.IMG_OVAL);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_TRIANGLE,
			DiagramUIGeoshapesPluginImages.IMG_TRIANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_RECTANGLE,
			DiagramUIGeoshapesPluginImages.IMG_RECTANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_SHADOWRECTANGLE,
			DiagramUIGeoshapesPluginImages.IMG_SHADOWRECTANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_3DRECTANGLE,
			DiagramUIGeoshapesPluginImages.IMG_3DRECTANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_ROUNDRECTANGLE,
			DiagramUIGeoshapesPluginImages.IMG_ROUNDRECTANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_HEXAGON,
			DiagramUIGeoshapesPluginImages.IMG_HEXAGON);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_OCTAGON,
			DiagramUIGeoshapesPluginImages.IMG_OCTAGON);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_PENTAGON,
			DiagramUIGeoshapesPluginImages.IMG_PENTAGON);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_DIAMOND,
			DiagramUIGeoshapesPluginImages.IMG_DIAMOND);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_CYLINDER,
			DiagramUIGeoshapesPluginImages.IMG_CYLINDER);

		semanticHintIconMap.put(GeoshapeConstants.TOOL_LINE,
			DiagramUIGeoshapesPluginImages.IMG_LINE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.icon.IIconProvider#getIcon(org.eclipse.core.runtime.IAdaptable,
	 *      int)
	 */
	public Image getIcon(IAdaptable hint, int flags) {

        View view = (View) hint.getAdapter(View.class);
        if (view != null) {
            String semanticHint = view.getType();
            return getIcon(semanticHint);
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

		IAdaptable hint = ((IIconOperation) operation).getHint();

		if (hint != null) {
            View view = (View) hint.getAdapter(View.class);
            if (view != null) {
				String semanticHint = view.getType();
				return (getIcon(semanticHint)!=null);
			}
		}

		return false;
	}

}