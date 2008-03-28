/******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.osgi.util.NLS;
 
/**
 * Command to refresh and revalidate a given <code>IGraphicalEditPart</code>
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class RefreshEditPartCommand 
	extends AbstractCommand {

	private View notation;
	private EditPartViewer viewer;
	private boolean revalidate;
	
	/**
	 * constructor
	 * @param editPart edit part to use 
	 * @param revalidate revalidate flag
	 */
	public RefreshEditPartCommand(IGraphicalEditPart editPart, boolean revalidate) {
		super("", null);
		this.revalidate = revalidate;
		notation = (View)editPart.getModel();
		viewer = editPart.getRoot().getViewer();
	}
	
	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		Map epRegistry = viewer.getEditPartRegistry();
		IGraphicalEditPart editPart = (IGraphicalEditPart)epRegistry.get(notation);

		if ( editPart != null ) {
			editPart.refresh();
		
			if (revalidate) {
				editPart.getFigure().invalidate();
				editPart.getFigure().validate();
			}
		}
		else {
			//
			// problem with editpart registry
			String eMsg = NLS.bind(  
				DiagramUIMessages.RefreshEditPartCompartment_execute_failed_ERROR_,
				notation);
			Log.error( DiagramUIPlugin.getInstance(), IStatus.ERROR, eMsg);
		}

		notation = null; // for garbage collection
		viewer = null; // for garbage collection
		return CommandResult.newOKCommandResult();
	}

    public boolean canRedo() {
		return true;
	}

    public boolean canUndo() {
		return true;
	}

    protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
        
		return CommandResult.newOKCommandResult();
	}

    protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		return CommandResult.newOKCommandResult();
	}

}
