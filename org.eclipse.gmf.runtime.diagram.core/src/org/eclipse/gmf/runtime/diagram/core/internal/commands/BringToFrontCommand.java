/****************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.commands;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This command moves a view from it's container and appends it to the "Front"
 * of the list.
 * 
 * @author jschofie
 * @canBeSeenBy %level1
 */
public class BringToFrontCommand extends ZOrderCommand {

	public BringToFrontCommand(View toMove ) {
		super( "BringToFrontCommand", toMove ); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		ViewUtil.repositionChildAt(containerView,toMove, containerView.getChildren().size()-1);
		return newOKCommandResult();
	}

}
