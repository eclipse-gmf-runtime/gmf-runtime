/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.gef.ui.internal.requests;

import org.eclipse.gef.requests.ChangeBoundsRequest;


/**
 * Provides support for shape rotations
 * Essentially, same as ChangeBoundsRequest with an extra variable that allows rotation
 * 
 * @author oboyko
 */
public class RotateShapeRequest
	extends ChangeBoundsRequest {
	
	// Rotate permission: true if rotation permitted
	private boolean rotate;
	
	/**
	 * Builds an instance of the request
	 * 
	 * @param type
	 */
	public RotateShapeRequest(Object type) {
		super(type);
		rotate = true; 
	}
	
	/**
	 * Sets the rotation permission 
	 * 
	 * @param rotate the <code>boolean</code> <code>true</code> if rotation is permitted, 
	 * <code>false</code> otherwise.
	 */
	public void setRotate(boolean rotate) {
		this.rotate = rotate;
	}
	
	/**
	 * Returns the rotation permission
	 * 
	 * @return <code>boolean</code> <code>true</code> if rotation is permitted, 
	 * <code>false</code> otherwise.
	 */
	public boolean shouldRotate() {
		return rotate;
	}
}
