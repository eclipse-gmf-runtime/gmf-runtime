/***************************************************************************
 Licensed Materials - Property of IBM
 (C) Copyright IBM Corp. 2004.  All Rights Reserved.

 US Government Users Restricted Rights - Use, duplication or disclosure
 restricted by GSA ADP Schedule Contract with IBM Corp.
 ***************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.providers;

import java.util.HashMap;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.GetIconOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.IIconProvider;
import org.eclipse.gmf.runtime.diagram.ui.geoshapes.internal.l10n.GeoshapesResourceManager;
import org.eclipse.gmf.runtime.notation.View;

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
			GeoshapeConstants.ICON_OVAL);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_TRIANGLE,
			GeoshapeConstants.ICON_TRIANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_RECTANGLE,
			GeoshapeConstants.ICON_RECTANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_SHADOWRECTANGLE,
			GeoshapeConstants.ICON_SHADOWRECTANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_3DRECTANGLE,
			GeoshapeConstants.ICON_3DRECTANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_ROUNDRECTANGLE,
			GeoshapeConstants.ICON_ROUNDRECTANGLE);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_HEXAGON,
			GeoshapeConstants.ICON_HEXAGON);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_OCTAGON,
			GeoshapeConstants.ICON_OCTAGON);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_PENTAGON,
			GeoshapeConstants.ICON_PENTAGON);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_DIAMOND,
			GeoshapeConstants.ICON_DIAMOND);
		semanticHintIconMap.put(GeoshapeConstants.TOOL_CYLINDER,
			GeoshapeConstants.ICON_CYLINDER);

		semanticHintIconMap.put(GeoshapeConstants.TOOL_LINE,
			GeoshapeConstants.ICON_LINE);
	}

	/** map for storing icon images based on type */
	private static HashMap typeIconMap = new HashMap();
	static {

		typeIconMap.put(GeoshapeType.OVAL, GeoshapeConstants.ICON_OVAL);
		typeIconMap.put(GeoshapeType.TRIANGLE, GeoshapeConstants.ICON_TRIANGLE);
		typeIconMap.put(GeoshapeType.RECTANGLE,
			GeoshapeConstants.ICON_RECTANGLE);
		typeIconMap.put(GeoshapeType.SHADOWRECTANGLE,
			GeoshapeConstants.ICON_SHADOWRECTANGLE);
		typeIconMap.put(GeoshapeType.THREEDRECTANGLE,
			GeoshapeConstants.ICON_3DRECTANGLE);
		typeIconMap.put(GeoshapeType.ROUNDRECTANGLE,
			GeoshapeConstants.ICON_ROUNDRECTANGLE);
		typeIconMap.put(GeoshapeType.HEXAGON, GeoshapeConstants.ICON_HEXAGON);
		typeIconMap.put(GeoshapeType.OCTAGON, GeoshapeConstants.ICON_OCTAGON);
		typeIconMap.put(GeoshapeType.PENTAGON, GeoshapeConstants.ICON_PENTAGON);
		typeIconMap.put(GeoshapeType.DIAMOND, GeoshapeConstants.ICON_DIAMOND);
		typeIconMap.put(GeoshapeType.CYLINDER, GeoshapeConstants.ICON_CYLINDER);

		typeIconMap.put(GeoshapeType.LINE, GeoshapeConstants.ICON_LINE);
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
					return GeoshapesResourceManager.getInstance().getImage(
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
			return GeoshapesResourceManager.getInstance().getImage(
				fileName);
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