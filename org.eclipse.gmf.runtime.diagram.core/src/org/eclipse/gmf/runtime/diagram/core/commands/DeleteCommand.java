/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Deletes a view.  The views affected connections are also deleted if
 * it is a primary view.
 * @author melaasar
 */
public class DeleteCommand extends AbstractModelCommand {

	private View view;

	/**
	 * Creates a new Delete command
	 * @param view
	 */
	public DeleteCommand(View view) {
		super(DiagramResourceManager.getI18NString("DeleteCommand.Label"), view); //$NON-NLS-1$
		this.view = view;
	}

	/**
	 * getter for the View that will be deleted
	 * @return the view to be deleted
	 */
	protected View getView() {
		return view;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		ViewUtil.destroy(view);
		return newOKCommandResult();
	}

}
