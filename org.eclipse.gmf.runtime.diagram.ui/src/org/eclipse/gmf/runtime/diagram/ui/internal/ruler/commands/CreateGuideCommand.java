/***************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004.  All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
***************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.ruler.commands;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.internal.ruler.DiagramRuler;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import com.ibm.xtools.notation.Guide;
import com.ibm.xtools.notation.NotationFactory;


/**
 * This command is used to create a guide line
 * @author Jody Schofield
 */
public class CreateGuideCommand extends AbstractModelCommand {

	private DiagramRuler parent;
	private int position;

	/**
	 * Constructor
	 * This Command is used to create a new guide for a given ruler
	 * 
	 * @param parent the ruler that will own the guide
	 * @param position the position that the guide will initially be created at
	 */
	public CreateGuideCommand(DiagramRuler parent, int position) {
		super(PresentationResourceManager.getInstance().getString( "Command.createGuide" ), //$NON-NLS-1$
			parent);
		this.parent = parent;
		this.position = position;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {

		Guide theGuide = NotationFactory.eINSTANCE.createGuide();
		theGuide.setPosition(position);
		parent.addGuide(theGuide);

		parent = null;
		return newOKCommandResult(theGuide);
	}
}
