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

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.draw2d.geometry.PrecisionRectangle;

/**
 * resizable edit lable
 * 
 * @author mmostafa
 */
public class ResizableLabelEditPolicy
	extends ShapeLabelResizableEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xtools.presentation.internal.editpolicies.ShapeLabelResizableEditPolicy#adjustRect(org.eclipse.draw2d.geometry.PrecisionRectangle)
	 */
	protected void adjustRect(PrecisionRectangle rect) {
		// do nothing, resizbale lables does not need rect adjustment
	}
}