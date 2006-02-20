/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.requests.SuppressibleUIRequest;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeConnectionRequest;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.util.Assert;

/**
 * a command to create the both of the View and Semantic of the connection, but
 * the editparts of target and source to obtain the command has not been created
 * yet, since we have the ViewAdapter to we can use it to get its editpart once
 * it is created and hence on execution time we can get its editpart to get the
 * command for our request and then execute it.
 * 
 * <p>
 * This command can handle both
 * <code>CreateConnectionViewAndElementRequest</code> and
 * <code>CreateConnectionViewRequest</code>.
 * </p>
 * 
 * @author choang
 */
public class DeferredCreateConnectionViewAndElementCommand
	extends AbstractCommand {

	CreateRequest request = null;

	IAdaptable typeInfoAdapter = null;

	IAdaptable sourceViewAdapter = null;

	IAdaptable targetViewAdapter = null;

	Command command = null;

	EditPartViewer viewer = null;

	private DeferredCreateConnectionViewAndElementCommand(
			IAdaptable sourceViewAdapter, IAdaptable targetViewAdapter,
			EditPartViewer currentViewer) {

		super(DiagramUIMessages.Commands_CreateCommand_Connection_Label, null);
		Assert.isNotNull(currentViewer, "currentViewer is null"); //$NON-NLS-1$
		this.sourceViewAdapter = sourceViewAdapter;
		this.targetViewAdapter = targetViewAdapter;
		this.viewer = currentViewer;
	}

	/**
	 * Constructor for DeferredCreateConnectionViewAndElementCommand.
	 * 
	 * @param request
	 *            the ceate connection request
	 * @param sourceViewAdapter
	 *            will adapt to the source view at execution time
	 * @param targetViewAdapter
	 *            will adapt to the target view at execution time
	 * @param currentViewer
	 *            used to find the editparts for the views
	 */
	public DeferredCreateConnectionViewAndElementCommand(
			CreateConnectionViewAndElementRequest request,
			IAdaptable sourceViewAdapter, IAdaptable targetViewAdapter,
			EditPartViewer currentViewer) {
		this(sourceViewAdapter, targetViewAdapter, currentViewer);
		this.request = request;
	}

	/**
	 * Constructor for DeferredCreateConnectionViewAndElementCommand.
	 * 
	 * @param type
	 *            the type to be used in a new
	 *            <code>CreateConnectionViewAndElementRequest</code>
	 * @param sourceViewAdapter
	 *            will adapt to the source view at execution time
	 * @param targetViewAdapter
	 *            will adapt to the target view at execution time
	 * @param currentViewer
	 *            used to find the editparts for the views
	 */
	public DeferredCreateConnectionViewAndElementCommand(IElementType type,
			IAdaptable sourceViewAdapter, IAdaptable targetViewAdapter,
			EditPartViewer currentViewer, PreferencesHint preferencesHint) {
		this(new CreateConnectionViewAndElementRequest(type, preferencesHint),
			sourceViewAdapter, targetViewAdapter, currentViewer);
	}

	/**
	 * Constructor for DeferredCreateConnectionViewAndElementCommand.
	 * 
	 * @param request
	 *            the ceate connection request
	 * @param typeInfoAdapter
	 *            extracts the type to be used in a new
	 *            <code>CreateConnectionViewAndElementRequest</code> at
	 *            execution time
	 * @param sourceViewAdapter
	 *            will adapt to the source view at execution time
	 * @param targetViewAdapter
	 *            will adapt to the target view at execution time
	 * @param currentViewer
	 *            used to find the editparts for the views
	 */
	public DeferredCreateConnectionViewAndElementCommand(CreateRequest request,
			IAdaptable typeInfoAdapter, IAdaptable sourceViewAdapter,
			IAdaptable targetViewAdapter, EditPartViewer currentViewer) {
		this(sourceViewAdapter, targetViewAdapter, currentViewer);
		this.request = request;
		this.typeInfoAdapter = typeInfoAdapter;
	}
    
    public List getAffectedFiles() {
        if (viewer != null) {
            EditPart editpart = viewer.getRootEditPart().getContents();
            if (editpart instanceof IGraphicalEditPart) {
                View view = (View) editpart.getModel();
                if (view != null) {
                    IFile f = EObjectUtil.getWorkspaceFile(view);
                    return f != null ? Collections.singletonList(f)
                        : Collections.EMPTY_LIST;
                }
            }
        }
        return super.getAffectedFiles();
    }

	public boolean canUndo() {
		return command != null && command.canUndo();
	}

	public boolean canRedo() {
		return command != null && command.canExecute();
	}

	/**
	 * gives access to the connection source edit part, which is the edit part
	 * of the connection's source <code>View</code>
	 * 
	 * @return the source edit part
	 */
	protected EditPart getSourceEditPart() {
		return (IGraphicalEditPart) viewer.getEditPartRegistry().get(
			sourceViewAdapter.getAdapter(View.class));
	}

	/**
	 * gives access to the connection target edit part, which is the edit part
	 * of the connection's target <code>View</code>
	 * 
	 * @return the source edit part
	 */
	protected EditPart getTargetEditPart() {
		return (IGraphicalEditPart) viewer.getEditPartRegistry().get(
			targetViewAdapter.getAdapter(View.class));
	}

	/**
	 * Finds the source and target editparts by extracting the views from the
	 * view adapaters and searching in the editpart viewer. Creates a connection
	 * view and element using the request.
	 * 
	 */
	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
    
		CreateConnectionViewRequest req = null;
		if (request != null) {
			if (request instanceof CreateConnectionViewRequest) {
				req = (CreateConnectionViewRequest) request;
			}
		} else {
			return CommandResult.newErrorCommandResult(getLabel());
		}
		if (typeInfoAdapter != null) {
			IElementType typeInfo = (IElementType) typeInfoAdapter
				.getAdapter(IElementType.class);
			if (typeInfo == null) {
				CommandResult.newErrorCommandResult(getLabel());
			}

			if (request instanceof CreateUnspecifiedTypeConnectionRequest) {
				req = ((CreateConnectionViewRequest) ((CreateUnspecifiedTypeConnectionRequest) request)
					.getRequestForType(typeInfo));
			}
		}

		req.setLocation(null);

		// Suppressing UI if the target edit part has not been created yet
		// this is so that if we are creating a new target the connection
		// creation will just take default data instead of prompting
		// For Defect RATLC00524293
		if (targetViewAdapter.getAdapter(IGraphicalEditPart.class) == null
			&& req instanceof SuppressibleUIRequest)
			((SuppressibleUIRequest) req).setSuppressibleUI(true);

		EditPart sourceEP = getSourceEditPart();

		EditPart targetEP = getTargetEditPart();

		// There are situations where src or target can be null and we must
		// check for these
		// ie. When a Select Existing Dialog is presnetd to the user and the
		// user presses
		// cancel either the src or target can be null.
		// So the following assertions have been replaced with a check for null
		// on the editparts.
		// old code ... Assert.isNotNull(sourceEP); Assert.isNotNull(targetEP);
		if ((sourceEP == null) || (targetEP == null))
			return null;

		if (req instanceof CreateConnectionViewAndElementRequest) {
			command = CreateConnectionViewAndElementRequest.getCreateCommand(
				req, sourceEP, targetEP);
		} else {
			command = CreateConnectionViewRequest.getCreateCommand(req,
				sourceEP, targetEP);
		}

		if (command != null && command.canExecute()) {
			command.execute();
		}
		viewer = null;// for garbage collection

		View view = (View) req.getConnectionViewDescriptor().getAdapter(
			View.class);
		if (null == view) {
			return CommandResult.newCancelledCommandResult();
		}
		return CommandResult.newOKCommandResult(req.getNewObject());
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getLabel()
	 */
	public String getLabel() {
		if (command != null) {
			return command.getLabel();
		}
		return null;
	}

    
    protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
 
		if (command != null) {
			command.redo();
		}
		return CommandResult.newOKCommandResult();
	}

    protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		if (command != null) {
			command.undo();
		}
		return CommandResult.newOKCommandResult();
	}

}
