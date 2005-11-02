/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.actions;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author melaasar
 *
 * An abstract implementation of a presentation action that follows
 * the request-command architecture. 
 * 
 * Notice:
 * 1) This action retargets to the active workbench part
 * 2) This action can either be contributed programatically or through
 *    the <code>ControbutionItemService</code>. 
 * @deprecated Renamed to {@link org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction}
 */
public abstract class PresentationAction
	extends DiagramAction {

	/**
	 * Constructs a new presentation action
	 * 
	 * @param workbenchPage The workbench page associated with this action
	 */
	public PresentationAction(IWorkbenchPage workbenchPage) {
		super(workbenchPage);
	}

	/**
	 * Constructs a new presentation action.  This constructor is provided
	 * just in case a derived class needs to support both the construction
	 * of a presentation action with a workbenchpart.  Typically this is only
	 * when the diagram declares its own action in additional to the one registered
	 * with the action serivce.
	 * 
	 * @param workbenchpart The workbench part associated with this action 
	 */
	protected PresentationAction(IWorkbenchPart workbenchpart) {
		super(workbenchpart);
	}

}
