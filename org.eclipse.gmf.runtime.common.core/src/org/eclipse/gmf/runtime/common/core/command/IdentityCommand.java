/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.common.core.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;

/**
 * A command that is always executable, undoable and redoable, but does nothing.
 * It always returns the same OK command result.
 * 
 * @author ldamus
 */
public final class IdentityCommand
    extends AbstractCommand {

    /**
     * The singleton instance.
     */
    public static final IdentityCommand INSTANCE = new IdentityCommand();

    /**
     * Initilizes me.
     */
    private IdentityCommand() {
        super(StringStatics.BLANK, null);
        setResult(CommandResult.newOKCommandResult());
    }

    /**
     * Does nothing and returns an OK command result.
     */
    protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

        return getCommandResult();
    }

    /**
     * Does nothing and returns an OK command result.
     */
    protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor,
            IAdaptable info)
        throws ExecutionException {

        return getCommandResult();
    }

    /**
     * Does nothing and returns an OK command result.
     */
    protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor,
            IAdaptable info)
        throws ExecutionException {

        return getCommandResult();
    }

}
