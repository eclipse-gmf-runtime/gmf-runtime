/***************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004.  All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
***************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.ruler.commands;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import com.ibm.xtools.notation.Guide;


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
		super(PresentationResourceManager.getInstance().getString( "Command.moveGuide" ),  //$NON-NLS-1$
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
