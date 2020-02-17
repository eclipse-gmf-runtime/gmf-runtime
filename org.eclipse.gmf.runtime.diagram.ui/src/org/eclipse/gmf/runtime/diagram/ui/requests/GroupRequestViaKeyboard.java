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


package org.eclipse.gmf.runtime.diagram.ui.requests;

import org.eclipse.gef.requests.GroupRequest;


/**
 * A group request that originated from the Keyboard due to the use of a hot/shortcut key
 * @author bagrodia
 * Created on Jul 13, 2004
 */
public class GroupRequestViaKeyboard
	extends GroupRequest {

	/**
	 * flag that lets the editpolicy show informational dialog. 
	 */
	private boolean showInformationDialog = true;
	
	
	/**
	 * Creates a GroupRequest with the given type.
	 *
	 * @param type The type of Request.
	 */
	public GroupRequestViaKeyboard(Object type) {
		super(type);		
	}

	/**
	 * Default constructor.
	 */
	public GroupRequestViaKeyboard() {
		super();		
	}
	
	

	/**
	 * @return Returns the showInformationDialog.
	 */
	public boolean isShowInformationDialog() {
		return showInformationDialog;
	}
	/**
	 * @param showInformationDialog The showInformationDialog to set.
	 */
	public void setShowInformationDialog(boolean showInformationDialog) {
		this.showInformationDialog = showInformationDialog;
	}
}
