/******************************************************************************
 * Copyright (c) 2004, 2014 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.draw2d.ui.internal.mapmode;

import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;


/**
 * This class implements the MapMode interface to provide support for
 * an Identity mapping.  Performs a null mapping.
 * 
 * @author sshaw
 */
public class IdentityMapMode
	implements IMapMode {

	public IdentityMapMode() {
		// no initialization required
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.figure.surface.mapmode.IMapMode#LPtoDP(int)
	 */
	public int LPtoDP(int logicalUnit) {
		return logicalUnit;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.gef.ui.internal.figure.surface.mapmode.IMapMode#DPtoLP(int)
	 */
	public int DPtoLP(int deviceUnit) {
		return deviceUnit;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode#DPtoLP(org.eclipse.draw2d.geometry.Translatable)
	 */
	public Translatable DPtoLP(Translatable t) {
		t.performScale( 1.0 );
		return t;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode#LPtoDP(org.eclipse.draw2d.geometry.Translatable)
	 */
	public Translatable LPtoDP(Translatable t) {
		t.performScale( 1.0 );
		return t;
	}
	
	/**
	 * Indicates whether some other MapMode is "equal to" this MapMode.
	 * 
	 * @return <code>true</code> if this MapMode is the same as the MapMode
	 *         argument; <code>false</code> otherwise.
	 * @param mapMode
	 *            The reference MapMode with which to compare.
	 */
	public boolean equals(Object mapMode){
		// if the mapModeect is of type MapModeHolder, get the underlying MapMode
		if (mapMode instanceof IMapModeHolder){
			return this.equals(((IMapModeHolder)mapMode).getMapMode());	
		}
		if (mapMode instanceof IdentityMapMode){
			return super.equals(mapMode);	
		}
		return false;
	}


}
