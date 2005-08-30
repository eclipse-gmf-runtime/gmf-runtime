/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.commands;

import java.text.MessageFormat;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.gef.EditPartViewer;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.diagram.ui.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import com.ibm.xtools.notation.View;
 
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
		super(null);
		this.revalidate = revalidate;
		notation = (View)editPart.getModel();
		viewer = editPart.getRoot().getViewer();
	}
	
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
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
			String eMsg = MessageFormat.format(  
				PresentationResourceManager.getInstance().getString("RefreshEditPartCompartment.execute.failed_ERROR_"),//$NON-NLS-1$
				new Object[] {notation});
			Log.error( DiagramUIPlugin.getInstance(), IStatus.ERROR, eMsg);
		}

		notation = null; // for garbage collection
		viewer = null; // for garbage collection
		return newOKCommandResult();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isRedoable()
	 */
	public boolean isRedoable() {
		return true;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isUndoable()
	 */
	public boolean isUndoable() {
		return true;
	}


	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doRedo()
	 */
	protected CommandResult doRedo() {
		return newOKCommandResult();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doUndo()
	 */
	protected CommandResult doUndo() {
		return newOKCommandResult();
	}

}
