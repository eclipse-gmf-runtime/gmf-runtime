/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
/**
 * Command to set the semantic reference for the provided model view to the given 
 * notational diagram.
 * 
 * @author jcorchis
 * @canBeSeenBy %level1
 */

public class CreateDiagramLinkCommand extends AbstractModelCommand {

	private View view = null;
	private Diagram diagram = null;

	/**
	 * Constructor for SetDiagramLinkCommand.
	 * @param label
	 * @param context
	 */
	public CreateDiagramLinkCommand(
		String label,
		View view,
		Diagram diagram) {
		super(label, view);

		this.view = view;
		this.diagram = diagram;
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		view.setElement(diagram);
		return newOKCommandResult();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isExecutable()
	 */
	public boolean isExecutable() {
		if (view == null) {
			return false;
		}
		return true;
	}

}
