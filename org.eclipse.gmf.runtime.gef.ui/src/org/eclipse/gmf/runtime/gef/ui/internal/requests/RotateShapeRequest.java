/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
