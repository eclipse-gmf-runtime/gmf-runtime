/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.actions;


/**
 * @author qili
 *
 * Ids for all action specific to logic diagram
 */
public interface LogicActionIds {
	
	/* Menu contribution ids */
	public final String MENU_INCREMENT = "incrementMenu"; //$NON-NLS-1$
	public final String MENU_DECREMENT = "decrementMenu"; //$NON-NLS-1$
	
	/* Action contribution ids */
    public final String ACTION_INCREMENT_VALUE = "incrementValueAction";//$NON-NLS-1$
	public final String ACTION_DECREMENT_VALUE = "decrementValueAction";//$NON-NLS-1$
	public final String DELETE_SEMANTIC_VALUE = "deleteSemanticAction"; //$NON-NLS-1$
	public final String MODIFY_PORTS_COLOR_VALUE = "modifyPortsColorAction"; //$NON-NLS-1$
	
}
