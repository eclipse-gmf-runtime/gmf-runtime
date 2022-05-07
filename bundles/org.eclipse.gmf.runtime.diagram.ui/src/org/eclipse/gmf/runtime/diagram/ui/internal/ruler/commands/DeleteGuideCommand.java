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
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Guide;
import org.eclipse.gmf.runtime.notation.GuideStyle;

/**
 * This command is used to delete guidelines
 *
 *  @author jschofie
 */
public class DeleteGuideCommand extends AbstractTransactionalCommand {

	private Guide theGuide;

	/**
	 * Command used to Delete a guide.
	 * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param guide the guide to be deleted
	 */
	public DeleteGuideCommand(TransactionalEditingDomain editingDomain, Guide guide) {
        super(editingDomain, DiagramUIMessages.Command_deleteGuide,
            getWorkspaceFiles(guide));
        theGuide = guide;
    }

	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
	    throws ExecutionException {

		// Clear the attached editparts
		EMap guideMap = theGuide.getNodeMap();
		guideMap.clear();
		
		// Remove the guide from it's container
		GuideStyle container = (GuideStyle)theGuide.eContainer();
		container.getHorizontalGuides().remove(theGuide);
		container.getVerticalGuides().remove(theGuide);

		theGuide = null;
		return CommandResult.newOKCommandResult();
	}
}
