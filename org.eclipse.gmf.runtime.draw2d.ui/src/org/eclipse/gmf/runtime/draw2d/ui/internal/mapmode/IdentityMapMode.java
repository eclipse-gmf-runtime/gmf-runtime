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

}
