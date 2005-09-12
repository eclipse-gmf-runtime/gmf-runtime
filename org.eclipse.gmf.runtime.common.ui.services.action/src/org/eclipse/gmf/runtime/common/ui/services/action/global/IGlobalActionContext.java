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

package org.eclipse.gmf.runtime.common.ui.services.action.global;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Interface that is passed to the <code>IGlobalActionHandler</code>. This interface
 * can be extended for handling/providing more view context information.
 * 
 * @author Vishy Ramaswamy
 */
public interface IGlobalActionContext {
    /**
     * Returns the <code>GlobalActionId</code>
     * 
     * @return Returns an <code>String</code>
     */
    public String getActionId();

    /**
     * Returns the active <code>IWorkbenchPart</code>
     * 
     * @return Returns the active <code>IWorkbenchPart</code>
     */
    public IWorkbenchPart getActivePart();

    /**
     * Return the label for the action
     *
     * @return String
     */
    public String getLabel();

    /**
     * Return the selection which contains items of type defined in the
     * <code>IGlobalActionHandlerContext</code>
     *
     * @return ISelection
     */
    public ISelection getSelection();
}
