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

package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramCommandStack;

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

		super(realSemanticCommand.getLabel());

		Assert.isNotNull(requestAdapter);
		Assert.isNotNull(realSemanticCommand);

		this.requestAdapter = requestAdapter;
		this.realSemanticCommand =
			DiagramCommandStack.getICommand(realSemanticCommand);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		realSemanticCommand.execute(progressMonitor); 
		CommandResult result = realSemanticCommand.getCommandResult();
		if (result.getStatus().isOK()) {
			Object object =	result.getReturnValue();
			if (object instanceof Collection) {
				Collection col = (Collection) object;
				if (!col.isEmpty())
					object = col.iterator().next();
			}
			if (object != null) {
				Assert.isTrue(object instanceof EObject, "Failed to get an IElement out of the semantic command returned value");//$NON-NLS-1$
				EObject element = (EObject) object;
				requestAdapter.setNewElement(element);
			}
			return newOKCommandResult(requestAdapter);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doRedo()
	 */
	protected CommandResult doRedo() {
		realSemanticCommand.redo();
		return realSemanticCommand.getCommandResult();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doUndo()
	 */
	protected CommandResult doUndo() {
		realSemanticCommand.undo();
		return realSemanticCommand.getCommandResult();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isExecutable()
	 */
	public boolean isExecutable() {
		return realSemanticCommand.isExecutable();
	}

	/*
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean isUndoable() {
		return realSemanticCommand.isUndoable();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isRedoable()
	 */
	public boolean isRedoable() {
		return realSemanticCommand.isUndoable();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getAffectedObjects()
	 */
	public Collection getAffectedObjects() {
		return realSemanticCommand.getAffectedObjects();
	}

}
