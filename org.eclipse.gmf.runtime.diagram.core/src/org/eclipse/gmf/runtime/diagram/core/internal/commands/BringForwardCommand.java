/****************************************************************************
  Licensed Materials - Property of IBM
  (C) Copyright IBM Corp. 2004. All Rights Reserved.

  US Government Users Restricted Rights - Use, duplication or disclosure
  restricted by GSA ADP Schedule Contract with IBM Corp.
*****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.commands;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import com.ibm.xtools.notation.View;

/**
 * This command moves a view from its current location to one step
 * closer to the "front" of the list.
 * 
 * @author jschofie
 * @canBeSeenBy %level1
 */
public class BringForwardCommand extends ZOrderCommand {
	
	/**
	 * Create a BringForward Command.
	 * 
	 * @param toMove The child view to move
	 */
	public BringForwardCommand(View toMove ) {
		super( "BringForwardCommand", toMove ); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		List children = containerView.getChildren();
		int oldIndex = children.indexOf(toMove);
		if (oldIndex < children.size()-1)
			ViewUtil.repositionChildAt(containerView,toMove, oldIndex + 1 );
		return newOKCommandResult();
	}
}
