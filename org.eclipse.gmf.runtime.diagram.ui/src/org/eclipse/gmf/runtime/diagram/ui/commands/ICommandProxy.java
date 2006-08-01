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

package org.eclipse.gmf.runtime.diagram.ui.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;

/**
 * A Command Wrapper for a GMF ICommand.
 *
 * @author melaasar
 */
public class ICommandProxy extends Command {

	/** The wrapped command */
	private ICommand iCommand;
		
	/**
	 * Constructor
	 * @param iCommand the command to wrap
	 */
	public ICommandProxy(ICommand iCommand) {
		super(iCommand.getLabel());
		Assert.isNotNull(iCommand);
		this.iCommand = iCommand;
	}

	public void dispose() {
        super.dispose();
        iCommand.dispose();
    }

    /**
	 * gets the warapped <code>ICommand</code>
	 * @return the wrapped command
	 */
	public ICommand getICommand() {
		return iCommand;
	}
	
	public boolean canExecute() {
		return getICommand().canExecute();
	}

	public boolean canUndo() {
		return getICommand().canUndo();
	}

	public void execute() {
        try {
            getICommand().execute(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            Trace.catching(DiagramUIPlugin.getInstance(),
                DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                "execute", e); //$NON-NLS-1$
            Log.error(DiagramUIPlugin.getInstance(),
                DiagramUIStatusCodes.COMMAND_FAILURE, e.getLocalizedMessage(),
                e);
        }
	}

	public void redo() {
        try {
            getICommand().redo(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            Trace.catching(DiagramUIPlugin.getInstance(),
                DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                "redo", e); //$NON-NLS-1$
            Log.error(DiagramUIPlugin.getInstance(),
                DiagramUIStatusCodes.COMMAND_FAILURE, e.getLocalizedMessage(),
                e);
        }
	}
    
	public void undo() {
        try {
            getICommand().undo(new NullProgressMonitor(), null);
        } catch (ExecutionException e) {
            Trace.catching(DiagramUIPlugin.getInstance(),
                DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
                "undo", e); //$NON-NLS-1$
            Log.error(DiagramUIPlugin.getInstance(),
                DiagramUIStatusCodes.COMMAND_FAILURE, e.getLocalizedMessage(),
                e);
        }
	}

}
