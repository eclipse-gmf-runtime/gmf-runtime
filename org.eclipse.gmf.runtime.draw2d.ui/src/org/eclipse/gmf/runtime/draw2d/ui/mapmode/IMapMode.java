/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.draw2d.ui.mapmode;

import org.eclipse.draw2d.geometry.Translatable;


/**
 * Objects implementing this interface are responsible for mapping the units
 * in the model to pixles.  For an example of how to use this interface
 * @see org.eclipse.gmf.runtime.gef.ui.internal.figure.surface.mapmode.HiMetricMapMode
 *
 * @author jschofie / sshaw
 */
public interface IMapMode {

	/**
	 * Convert a Logical Unit into a Device Unit
	 *  
	 * @param logicalUnit the value to be converted
	 * @return the value represented in device units
	 */
	public int LPtoDP(int logicalUnit);

	/**
	 * Convert a Device Unit into a Logical Unit
	 *  
	 * @param deviceUnit the value to be converted
	 * @return the value represented in logical units
	 */
	public int DPtoLP(int deviceUnit);
	
	/**
	 * Convert a <code>Translatable</code> to Device Units (pixels)
	 * 
	 * @param t the <code>Translatable</code> to convert
	 * @return the parameter <code>t</code> that was scaled for convenience.
	 */
	public Translatable LPtoDP( Translatable t );
	
	/**
	 * Convert a Translatable to Logical Units (i.e. Hi-Metrics)
	 * @param t the Translatable to convert
	 * @return the parameter <code>t</code> that was scaled for convenience.
	 */
	public Translatable DPtoLP( Translatable t );

}
