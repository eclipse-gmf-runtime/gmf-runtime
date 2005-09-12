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


package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;


/**
 * A request to destroy a model element originating from the Keyboard via the 'Ctrl+d' hot/shortcut key
 * @author bagrodia
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.core.*
 * Created on Jul 13, 2004
 * 
 * @deprecated Use one of the subclasses of {@link org.eclipse.gmf.runtime.emf.type.core.requests.DestroyRequest}
 *             to get destroy commands from the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}.
 */
public class DestroyRequestViaKeyboard
	extends DestroyElementRequest {

	
	/**
	 * flag that lets the editpolicy show informational dialog. 
	 */
	private boolean showInformationDialog = true;
	
	/**
	 * Creates a new DestroyRequestViaKeyboard Request with no element yet
	 */
	public DestroyRequestViaKeyboard() {
		super();
		
	}

	/**
	 * Creates a new DestroyRequestViaKeyboard Request.
	 * @param element to be destroyed
	 */
	public DestroyRequestViaKeyboard(EObject element) {
		super(element);
		
	}

	/**
	 * Creates a new DestroyRequestViaKeyboard Request.
	 * @param requestType
	 * @param element to be destroyed
	 */
	public DestroyRequestViaKeyboard(Object requestType, EObject element) {
		super(requestType, element);
		
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
