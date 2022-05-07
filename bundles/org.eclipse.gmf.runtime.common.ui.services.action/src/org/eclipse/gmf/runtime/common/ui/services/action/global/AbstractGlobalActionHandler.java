/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.action.global;



/**
 * The abstract parent of all concrete global action handlers. A concrete handler
 * needs to override the <code>getCommand()</code> and <code>canHandle()</code> methods.
 * This class implements the <code>IGlobalActionHandler</code> interface. It provides
 * access to the <code>IGlobalActionContext</code> to its decendents.
 * 
 * @author Vishy Ramaswamy
 */
public abstract class AbstractGlobalActionHandler
    implements IGlobalActionHandler {
    /**
     * Creates an AbstractGlobalActionHandler.
     */
    public AbstractGlobalActionHandler() {
        super();
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.global.IGlobalActionHandler#getLabel(org.eclipse.gmf.runtime.common.ui.action.global.IGlobalActionContext)
	 */
	public String getLabel(IGlobalActionContext cntxt) {
		return null;
	}
}
