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

package org.eclipse.gmf.runtime.diagram.core.internal.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
/**
 * Command to set the semantic reference for the provided model view to the given 
 * notational diagram.
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */

public class CreateDiagramLinkCommand extends AbstractTransactionalCommand {

	private View view = null;
	private Diagram diagram = null;

	/**
	 * Constructor for SetDiagramLinkCommand.
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param label
	 * @param context
	 */
	public CreateDiagramLinkCommand(TransactionalEditingDomain editingDomain, 
		String label,
		View view,
		Diagram diagram) {
		super(editingDomain, label, getWorkspaceFiles(view));

		this.view = view;
		this.diagram = diagram;
	}
	
	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        
		view.setElement(diagram);
		return CommandResult.newOKCommandResult();
	}

	public boolean canExecute() {
		if (view == null) {
			return false;
		}
		return true;
	}

}
