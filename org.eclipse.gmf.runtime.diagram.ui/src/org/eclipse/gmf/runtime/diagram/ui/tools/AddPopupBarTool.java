/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.tools;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * This is the tool used for the AddUML commands associated with the action bars.
 * The creation tools for action bars also need to impl DragTracker since the SelectionTool
 * calls Handle.getDragTracker during mouseDown.
 * 
 * @author affrantz, cmahoney
 * @deprecated Use {@link PopupBarTool instead}
 * 
 */

public class AddPopupBarTool extends PopupBarTool {

	/**
	 * constructor
	 * @param epHost the host edit part for this tool
	 * @param elementType
	 */
	public AddPopupBarTool(EditPart epHost, IElementType elementType) {
		super(epHost, elementType);
	}
	
	/**
	 * constructor
	 * @param epHost
	 * @param theRequest the create request to be used
	 */
	public AddPopupBarTool(EditPart epHost, CreateRequest theRequest) {
		super(epHost, theRequest);
	}
	
}
