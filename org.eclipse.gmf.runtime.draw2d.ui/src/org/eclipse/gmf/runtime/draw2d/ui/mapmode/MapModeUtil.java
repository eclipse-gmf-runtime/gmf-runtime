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

package org.eclipse.gmf.runtime.draw2d.ui.mapmode;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.draw2d.ui.internal.Draw2dPlugin;

/**
 * @author sshaw
 * Utility class to retrieve the <code>IMapMode</code> class from a <code>IFigure</code> context 
 */
public class MapModeUtil {

	/**
	 * Parses up the figure containment hierarchy to find a <code>IFigure</code> that implements
	 * the <code>IMapMode</code> interface.  This is then returned as the coordinate system
	 * mapping.
	 * 
	 * @param fig <code>IFigure</code> to retrieve the <code>IMapMode</code> object from.
	 * @return appropriate <code>IMapMode</code> for the given <code>IFigure</code> object.
	 */
	static public IMapMode getMapMode( IFigure fig ) {
        if (fig == null) {
            Log.warning(Draw2dPlugin.getInstance(), IStatus.WARNING, "MapModeUtil#getMapMode(IFigure fig) fig parameter must not be null - using default MapMode");//$NON-NLS-1$
            return MapModeTypes.DEFAULT_MM;
        }
        
        if (fig.getParent() == null) {
            Log.warning(Draw2dPlugin.getInstance(), IStatus.WARNING, "MapModeUtil#getMapMode(IFigure fig) fig parameter must be attached to a parent - using default MapMode");//$NON-NLS-1$
            return MapModeTypes.DEFAULT_MM;
        }
        
		if (fig instanceof IMapMode)
			return (IMapMode)fig;
		
		return getMapMode(fig.getParent());
	}
	
	/**
	 * @return <code>IMapMode</code> that is the default coordinate system mapping supported
	 * by the diagram infrastructure.  Editors that support a different coordinate system,
	 * should use the @link{ MapModeUtil#getMapMode( IFigure fig) } to retrieve the 
	 * appropriate <code>IMapMode</code> for their specific Editor.
	 */
	static public IMapMode getMapMode() {
		return MapModeTypes.DEFAULT_MM;
	}
}
