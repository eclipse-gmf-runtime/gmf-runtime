/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.util;

import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeTypes;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;


/**
 * @author sshaw
 * Helper class to retrieve appropriate IMapMode implementation
 * for a given MeasurementUnit enumerated type.
 *
 */
public class MeasurementUnitHelper {

	/**
	 * @param unit the <code>MeasurementUnit</code> to find the appropriate
	 * <code>IMapMode</code> implementation that will convert from the coordinate
	 * system of the unit to device coordinates.
	 * @return the <code>IMapMode</code> class that knows how to convert the unit
	 * to device coordinates.
	 */
	static public IMapMode getMapMode(MeasurementUnit unit) {
		if (unit.equals(MeasurementUnit.PIXEL_LITERAL))
			return MapModeTypes.IDENTITY_MM;
		else if (unit.equals(MeasurementUnit.HIMETRIC_LITERAL))
			return MapModeTypes.HIMETRIC_MM;
		
		return MapModeTypes.DEFAULT_MM;
	}
}
