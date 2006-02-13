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


package org.eclipse.gmf.runtime.diagram.ui.internal.ruler.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.internal.ruler.DiagramRuler;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Guide;
import org.eclipse.gmf.runtime.notation.NotationFactory;


/**
 * This command is used to create a guide line
 * @author Jody Schofield
 */
public class CreateGuideCommand extends AbstractTransactionalCommand {

	private DiagramRuler parent;
	private int position;

	/**
	 * Constructor
	 * This Command is used to create a new guide for a given ruler
	 * 
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param parent the ruler that will own the guide
	 * @param position the position that the guide will initially be created at
	 */
	public CreateGuideCommand(TransactionalEditingDomain editingDomain, DiagramRuler parent, int position) {
		super(editingDomain, DiagramUIMessages.Command_createGuide, null);
		this.parent = parent;
		this.position = position;
	}

	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		Guide theGuide = NotationFactory.eINSTANCE.createGuide();
		theGuide.setPosition(position);
		parent.addGuide(theGuide);

		parent = null;
		return CommandResult.newOKCommandResult(theGuide);
	}
}
