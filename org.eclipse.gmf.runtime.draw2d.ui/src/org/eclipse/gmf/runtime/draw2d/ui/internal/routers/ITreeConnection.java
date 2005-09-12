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

package org.eclipse.gmf.runtime.draw2d.ui.internal.routers;

import org.eclipse.draw2d.Connection;

/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.draw2d.ui.*
 *
 * Extended interface used to distinguish between tree types.  The hint describes which
 * tree to add the connection to.
 */
public interface ITreeConnection extends Connection {

	/**
	 * getHint
	 * Gets the hint about the connection which determines which tree this connection
	 * will be contributed to.
	 * 
	 * @return String that is a hint to the tree router.
	 */
	public String getHint();
	
	/**
	 * @author sshaw
	 *
	 * enum used to determine the tree orientation
	 */
	static public class Orientation {
		
		private Orientation() {
			// The default constructor does nothing
		}
		
		static public Orientation VERTICAL = new Orientation();//$NON-NLS-1$
		static public Orientation HORIZONTAL = new Orientation();//$NON-NLS-1$
	}
	
	/**
	 * getOrientation
	 * Determines how this connection should be oriented in a tree structure.
	 *  
	 * @return Orientation enum
	 */
	public Orientation getOrientation();
}
