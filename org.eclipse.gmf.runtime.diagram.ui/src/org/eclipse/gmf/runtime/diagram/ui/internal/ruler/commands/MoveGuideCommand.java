/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.ruler.commands;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.notation.Guide;


/**
 * This command is used to move a guideline
 *
 *  @author jschofie
 */
public class MoveGuideCommand extends AbstractModelCommand {

	private int pDelta;
	private Guide theGuide;
		
	/**
	 * This command is used to move the position of a given Guide
	 * 
	 * @param guide to be repositioned
	 * @param positionDelta the relative distance to move the guide
	 */
	public MoveGuideCommand(Guide guide, int positionDelta) {
		super(DiagramResourceManager.getInstance().getString( "Command.moveGuide" ),  //$NON-NLS-1$
			guide);
		theGuide = guide;
		pDelta = positionDelta;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		// Set the Guides new position
		theGuide.setPosition(theGuide.getPosition() + pDelta);
		theGuide = null;
		return newOKCommandResult();
	}

}
