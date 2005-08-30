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
import com.ibm.xtools.notation.View;

/**
 * This command moves a view from it's container and inserts it in the "back"
 * of the list.
 * 
 * @author jschofie
 * @canBeSeenBy %level1
 */
public class SendToBackCommand extends ZOrderCommand {

	public SendToBackCommand(View toMove ) {
		super( "SendToBackCommand", toMove ); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		// Reposition the child
		ViewUtil.repositionChildAt(containerView,toMove, 0);
		return newOKCommandResult();
	}

}
