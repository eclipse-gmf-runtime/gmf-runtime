/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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