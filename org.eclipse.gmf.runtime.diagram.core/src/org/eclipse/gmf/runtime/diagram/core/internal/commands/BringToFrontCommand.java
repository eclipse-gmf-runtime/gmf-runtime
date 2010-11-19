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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramCoreMessages;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This command moves a view from it's container and appends it to the "Front"
 * of the list.
 * 
 * @author jschofie
 */
public class BringToFrontCommand extends ZOrderCommand {

    /**
     * 
     * @param editingDomain
     *            the editing domain through which model changes are made
     * @param toMove
     */
	public BringToFrontCommand(TransactionalEditingDomain editingDomain, View toMove ) {
		super(editingDomain, DiagramCoreMessages.BringToFrontCommand_Label, toMove ); 
	}


	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        
		ViewUtil.repositionChildAt(containerView,toMove, containerView.getChildren().size()-1);
		return CommandResult.newOKCommandResult();
	}

}
