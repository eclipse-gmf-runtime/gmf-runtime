/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The interface for all actions that could potentially be repeated.
 * 
 * @author khussey
 * @deprecated Repeatable actions are no longer supported. Use
 *             {@link org.eclipse.gmf.runtime.common.ui.action.IActionWithProgress}
 *             instead.
 */
public interface IRepeatableAction extends IActionWithProgress {
 
    /**
     * Retrieves a Boolean indicating whether this repeatable action can be
     * repeated.
     * 
     * @return <code>true</code> if this repeatable action can be repeated;
     *          <code>false</code> otherwise.
     */
    public boolean isRepeatable();

    /**
     * Re-runs this repeatable action.
     * 
     * @param progressMonitor <code>IProgressMonitor</code> monitoring the execution of this action
     */
    public void repeat(IProgressMonitor progressMonitor);
}
