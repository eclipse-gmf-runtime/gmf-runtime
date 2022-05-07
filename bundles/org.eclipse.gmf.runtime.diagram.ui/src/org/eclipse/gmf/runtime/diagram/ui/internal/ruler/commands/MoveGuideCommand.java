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


package org.eclipse.gmf.runtime.diagram.ui.internal.ruler.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Guide;


/**
 * This command is used to move a guideline
 *
 *  @author jschofie
 */
public class MoveGuideCommand extends AbstractTransactionalCommand {

	private int pDelta;
	private Guide theGuide;
		
	/**
	 * This command is used to move the position of a given Guide
	 * 
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param guide to be repositioned
	 * @param positionDelta the relative distance to move the guide
	 */
	public MoveGuideCommand(TransactionalEditingDomain editingDomain, Guide guide, int positionDelta) {
		super(editingDomain, DiagramUIMessages.Command_moveGuide,
			getWorkspaceFiles(guide));
		theGuide = guide;
		pDelta = positionDelta;
	}

	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        
		// Set the Guides new position
		theGuide.setPosition(theGuide.getPosition() + pDelta);
		theGuide = null;
		return CommandResult.newOKCommandResult();
	}

}
