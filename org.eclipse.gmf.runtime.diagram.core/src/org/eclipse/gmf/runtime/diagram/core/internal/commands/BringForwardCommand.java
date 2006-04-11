/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.core.internal.commands;

import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This command moves a view from its current location to one step
 * closer to the "front" of the list.
 * 
 * @author jschofie
 * @canBeSeenBy %level1
 */
public class BringForwardCommand extends ZOrderCommand {
	
	/**
	 * Create a BringForward Command.
	 * 
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param toMove The child view to move
	 */
	public BringForwardCommand(TransactionalEditingDomain editingDomain, View toMove ) {
		super(editingDomain, DiagramCoreMessages.BringForwardCommand_Label, toMove); 
	}

    protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
            IAdaptable info)
        throws ExecutionException {

		List children = containerView.getChildren();
		int oldIndex = children.indexOf(toMove);
		if (oldIndex < children.size()-1)
			ViewUtil.repositionChildAt(containerView,toMove, oldIndex + 1 );
		return CommandResult.newOKCommandResult();
	}
}
