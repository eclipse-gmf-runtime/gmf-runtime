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
import org.eclipse.emf.common.util.EMap;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.notation.Guide;
import org.eclipse.gmf.runtime.notation.GuideStyle;

/**
 * This command is used to delete guidelines
 *
 *  @author jschofie
 */
public class DeleteGuideCommand extends AbstractModelCommand {

	private Guide theGuide;

	/**
	 * Command used to Delete a guide.
	 *
	 * @param guide the guide to be deleted
	 */
	public DeleteGuideCommand(Guide guide) {
		super(PresentationResourceManager.getInstance().getString( "Command.deleteGuide" ), //$NON-NLS-1$
			guide);
		theGuide = guide;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {

		// Clear the attached editparts
		EMap guideMap = theGuide.getNodeMap();
		guideMap.clear();
		
		// Remove the guide from it's container
		GuideStyle container = (GuideStyle)theGuide.eContainer();
		container.getHorizontalGuides().remove(theGuide);
		container.getVerticalGuides().remove(theGuide);

		theGuide = null;
		return newOKCommandResult();
	}
}
