/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.internal.command;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;

/**
 * Internal interface designating a command that allows the setting of the
 * CommandResult
 * 
 * @author wdiu, Wayne Diu
 */
public interface ICommandWithSettableResult {
    
    /**
     * Internal method to set the command result.
     * 
     * @param result CommandResult to set
     */    
    public void internalSetResult(CommandResult result);
}
