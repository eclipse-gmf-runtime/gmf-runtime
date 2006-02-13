/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.internal.actions;

import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler;

/**
 * The abstract parent for all action handlers that are interested in command
 * manager change events. Instances of (subclasses of) this class will
 * automatically refresh themselves when a command manager change notification
 * is received.
 * 
 * @author khussey
 * 
 * @see org.eclipse.gmf.runtime.common.core.command.ICommandManagerChangeListener
 * @deprecated Subclass {@link AbstractActionHandler} and implement
 *             {@link #isOperationHistoryListener()} to return true.
 */
public abstract class CommandManagerActionHandler
    extends AbstractActionHandler {
 
    /**
     * Constructs a new command manager action handler for the specified
     * workbench part.
     * 
     * @param workbenchPart The workbench part to which this command manager
     *                       action handler applies.
     */
    protected CommandManagerActionHandler(IWorkbenchPart workbenchPart) {
        super(workbenchPart);
        refresh();
    }

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#isCommandStackListener()
	 */
	public boolean isCommandStackListener() {
		return true;
	}
    
    protected boolean isOperationHistoryListener() {
        return true;
    }

}
