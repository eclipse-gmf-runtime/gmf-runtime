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


package org.eclipse.gmf.runtime.diagram.core.internal.commands;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.notation.View;

/**
 * This command moves a view from its current location to one step
 * closer to the "back" of the list.
 * 
 * @author jschofie
 * @canBeSeenBy %level1
 */
public class SendBackwardCommand extends ZOrderCommand {
	
	/**
	 * Create a SendBackward Command.
	 * 
	 * @param toMove The child view to move
	 */
	public SendBackwardCommand(View toMove ) {
		super( "SendBackwardCommand", toMove ); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		int oldIndex = containerView.getChildren().indexOf(toMove);
		if (oldIndex > 0)
			ViewUtil.repositionChildAt(containerView,toMove, oldIndex - 1 );
		return newOKCommandResult();
	}

}
