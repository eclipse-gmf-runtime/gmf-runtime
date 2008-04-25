/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;

/**
 * {@link ICommand} that delegates to a GEF {@link Command}.
 * 
 * @author sshaw
 */
public class CommandProxy
    extends AbstractCommand {

    /** The wrapped command */
    private Command command;

    /**
     * Method CommandProxy.
     * 
     * @param command
     */
    public CommandProxy(Command command) {
        super((command.getLabel() == null) ? StringStatics.BLANK : command.getLabel(), null);
        Assert.isNotNull(command);
        this.command = command;
    }

    protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

        command.execute();
        return CommandResult.newOKCommandResult();
    }

    protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor,
            IAdaptable info)
        throws ExecutionException {

        command.redo();
        return CommandResult.newOKCommandResult();
    }

    protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor,
            IAdaptable info)
        throws ExecutionException {

        command.undo();
        return CommandResult.newOKCommandResult();
    }

    /**
     * Returns the wrapped command.
     * 
     * @return Command
     */
    public Command getCommand() {
        return command;
    }

    public boolean canUndo() {
        return command.canUndo();
    }

    public boolean canRedo() {
        return CommandUtilities.canRedo(command);
    }

    public boolean canExecute() {
        return command.canExecute();
    }

    public void dispose() {
        super.dispose();
        command.dispose();
    }

}
