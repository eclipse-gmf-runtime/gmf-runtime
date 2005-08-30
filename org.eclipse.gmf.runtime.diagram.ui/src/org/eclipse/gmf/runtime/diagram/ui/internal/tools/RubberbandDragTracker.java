/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.tools;

import org.eclipse.gef.DragTracker;


/**
 * @author tisrar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * 
 */
public class RubberbandDragTracker
	extends RubberbandSelectionTool
	implements DragTracker {
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#handleFinished()
	 */
	protected void handleFinished() {
		//nothing goes here
		
	}

}
