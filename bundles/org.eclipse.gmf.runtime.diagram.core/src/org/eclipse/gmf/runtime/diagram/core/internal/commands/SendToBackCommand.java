/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This command moves a view from it's container and inserts it in the "back"
 * of the list.
 * 
 * @author jschofie
 */
public class SendToBackCommand extends ZOrderCommand {

    /**
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param toMove
     */
	public SendToBackCommand(TransactionalEditingDomain editingDomain, View toMove ) {
		super(editingDomain, DiagramCoreMessages.SendToBackCommand_Label, toMove ); 
	}

	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        
		// Reposition the child
		ViewUtil.repositionChildAt(containerView,toMove, 0);
		return CommandResult.newOKCommandResult();
	}

}
