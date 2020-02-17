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

package org.eclipse.gmf.runtime.diagram.ui.internal.tools;

import org.eclipse.gef.DragTracker;


/**
 * @author tisrar
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
