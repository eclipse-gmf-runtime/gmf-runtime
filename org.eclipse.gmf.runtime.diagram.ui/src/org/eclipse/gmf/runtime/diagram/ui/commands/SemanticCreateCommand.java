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

import java.util.Collection;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;
import org.eclipse.jface.util.Assert;

/**
 * A Wrapper around a real element creation command
 * The main use of this command is to ensure that the semantic
 * adapter is updated appropriately upon undo and redo of the real command
 * 
 * @author melaasar
 */
public class SemanticCreateCommand extends AbstractCommand {

	/** the request adapter */
	CreateElementRequestAdapter requestAdapter;
	/** the real element creation command */
	private ICommand realSemanticCommand;

	/**
	 * Creates a new semantic create command that wraps around a real command
	 * @param requestAdapter
	 * @param realSemanticCommand
	 */
	public SemanticCreateCommand(
		CreateElementRequestAdapter requestAdapter,
		Command realSemanticCommand) {

		super(realSemanticCommand.getLabel(), null);

		Assert.isNotNull(requestAdapter);
		Assert.isNotNull(realSemanticCommand);

		this.requestAdapter = requestAdapter;
		this.realSemanticCommand =
			DiagramCommandStack.getICommand(realSemanticCommand);
		
		// propagate the contexts from the wrapped command
		IUndoContext[] contexts = this.realSemanticCommand.getContexts();
		for (int i = 0; i < contexts.length; i++) {
			addContext(contexts[i]);
		}
	}

	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		realSemanticCommand.execute(progressMonitor, info); 
		CommandResult result = realSemanticCommand.getCommandResult();
		if (result.getStatus().isOK()) {
			Object object =	result.getReturnValue();
			if (object instanceof Collection) {
				Collection col = (Collection) object;
				object = col.isEmpty() ? null : col.iterator().next();
			}
			if (object != null) {
				Assert.isTrue(object instanceof EObject, "Failed to get an IElement out of the semantic command returned value");//$NON-NLS-1$
				EObject element = (EObject) object;
				requestAdapter.setNewElement(element);
			}
			return CommandResult.newOKCommandResult(requestAdapter);
		}
		return result;
	}

    protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		realSemanticCommand.redo(progressMonitor, info);
		return realSemanticCommand.getCommandResult();
	}

    protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		realSemanticCommand.undo(progressMonitor, info);
		return realSemanticCommand.getCommandResult();
	}

    public boolean canExecute() {
		return realSemanticCommand.canExecute();
	}

    public boolean canUndo() {
		return realSemanticCommand.canUndo();
	}

    public boolean canRedo() {
		return realSemanticCommand.canRedo();
	}
    
    public List getAffectedFiles() {
        return realSemanticCommand.getAffectedFiles();
    }

}
