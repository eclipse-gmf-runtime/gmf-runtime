/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal.commands;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;
import com.ibm.xtools.notation.Diagram;
import com.ibm.xtools.notation.View;
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
