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

package org.eclipse.gmf.runtime.draw2d.ui.mapmode;

import org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode.HiMetricMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode.IdentityMapMode;



/**
 * @author sshaw
 * Class that defines different <code>IMapMode</code> types available for use
 *
 */
public class MapModeTypes {

	/**
	 * Constant <code>IMapMode</code> class for HiMetric coordinate mapping
	 */
	static public IMapMode HIMETRIC_MM  = new HiMetricMapMode();
	
	/**
	 * Constant <code>IMapMode</code> class for Identity coordinate mapping
	 */
	static public IMapMode IDENTITY_MM = new IdentityMapMode();
	
	/**
	 * Constant <code>IMapMode</code> class default coordinate mapping (HiMetric is
	 * current default).
	 */
	static public IMapMode DEFAULT_MM = HIMETRIC_MM;
}
