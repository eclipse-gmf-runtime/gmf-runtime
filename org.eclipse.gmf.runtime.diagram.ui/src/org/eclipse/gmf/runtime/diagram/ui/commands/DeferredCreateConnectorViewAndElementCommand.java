/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.commands;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SuppressibleUIRequest;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectorViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectorViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeConnectionRequest;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.notation.View;

/**
 * a command to create the both of the View and Semantic of the connector, but
 * the editparts of target and source to obtain the command has not been created
 * yet, since we have the ViewAdapter to we can use it to get its editpart once it
 * is created and hence on execution time we can get its editpart to get the command
 * for our request and then execute it.
 * 
 * <p>
 * This command can handle both
 * <code>CreateConnectorViewAndElementRequest</code> and
 * <code>CreateConnectorViewRequest</code>.
 * </p>
 * 
 * @author choang
 */
public class DeferredCreateConnectorViewAndElementCommand
	extends AbstractCommand {



	CreateRequest request = null;
	IAdaptable typeInfoAdapter = null;
	IAdaptable sourceViewAdapter = null;
	IAdaptable targetViewAdapter = null;
	Command command = null;
	EditPartViewer viewer = null;

	private DeferredCreateConnectorViewAndElementCommand(
		IAdaptable sourceViewAdapter,
		IAdaptable targetViewAdapter,
		EditPartViewer currentViewer) {
		
		super(PresentationResourceManager
			.getI18NString("Commands.CreateCommand.Connector.Label")); //$NON-NLS-1$
		Assert.isNotNull(currentViewer, "currentViewer is null"); //$NON-NLS-1$
		this.sourceViewAdapter = sourceViewAdapter;
		this.targetViewAdapter = targetViewAdapter;
		this.viewer = currentViewer;
	}
	
	/**
	 * Constructor for DeferredCreateConnectorViewAndElementCommand.
	 * 
	 * @param request
	 *            the ceate connector request
	 * @param sourceViewAdapter
	 *            will adapt to the source view at execution time
	 * @param targetViewAdapter
	 *            will adapt to the target view at execution time
	 * @param currentViewer
	 *            used to find the editparts for the views
	 */
	public DeferredCreateConnectorViewAndElementCommand(
		CreateConnectorViewAndElementRequest request,
		IAdaptable sourceViewAdapter,
		IAdaptable targetViewAdapter,
		EditPartViewer currentViewer) {
		this(
			sourceViewAdapter,
			targetViewAdapter,
			currentViewer);
		this.request = request;
	}

	/**
	 * Constructor for DeferredCreateConnectorViewAndElementCommand.
	 * 
	 * @param type
	 *            the type to be used in a new
	 *            <code>CreateConnectorViewAndElementRequest</code>
	 * @param sourceViewAdapter
	 *            will adapt to the source view at execution time
	 * @param targetViewAdapter
	 *            will adapt to the target view at execution time
	 * @param currentViewer
	 *            used to find the editparts for the views
	 */
	public DeferredCreateConnectorViewAndElementCommand(
		IElementType type,
		IAdaptable sourceViewAdapter,
		IAdaptable targetViewAdapter,
		EditPartViewer currentViewer,
		PreferencesHint preferencesHint) {
		this(
			new CreateConnectorViewAndElementRequest(type, preferencesHint),
			sourceViewAdapter,
			targetViewAdapter,
			currentViewer);
	}
	
	/**
	 * Constructor for DeferredCreateConnectorViewAndElementCommand.
	 * 
	 * @param request
	 *            the ceate connector request
	 * @param typeInfoAdapter
	 *            extracts the type to be used in a new
	 *            <code>CreateConnectorViewAndElementRequest</code> at
	 *            execution time
	 * @param sourceViewAdapter
	 *            will adapt to the source view at execution time
	 * @param targetViewAdapter
	 *            will adapt to the target view at execution time
	 * @param currentViewer
	 *            used to find the editparts for the views
	 */
	public DeferredCreateConnectorViewAndElementCommand(
		CreateRequest request,
		IAdaptable typeInfoAdapter,
		IAdaptable sourceViewAdapter,
		IAdaptable targetViewAdapter,
		EditPartViewer currentViewer) {
		this(
			sourceViewAdapter,
			targetViewAdapter,
			currentViewer);
		this.request = request;
		this.typeInfoAdapter = typeInfoAdapter;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#getAffectedObjects()
	 */
	public Collection getAffectedObjects() {
		if( viewer != null ) {
			EditPart editpart = viewer.getRootEditPart().getContents();
			if (editpart instanceof IGraphicalEditPart) {
				View view = (View)editpart.getModel();
				if (view != null) {
					IFile f = EObjectUtil.getWorkspaceFile(view);
					return f != null ? Collections.singletonList(f) : Collections.EMPTY_LIST;
				}
			}
		}
		return super.getAffectedObjects();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isUndoable()
	 */
	public boolean isUndoable() {
		return command != null && command.canUndo();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.ICommand#isRedoable()
	 */
	public boolean isRedoable() {
		return command != null && command.canExecute();
	}
	
	/**
	 * gives access to the connection source edit part, which is the edit
	 * part of the connector's source <code>View</code> 
	 * @return the source edit part
	 */
	protected EditPart getSourceEditPart() {
		return (IGraphicalEditPart) viewer
		.getEditPartRegistry().get(
			sourceViewAdapter.getAdapter(View.class));
	}

	/**
	 * gives access to the connection target edit part, which is the edit
	 * part of the connector's target <code>View</code> 
	 * @return the source edit part
	 */
	protected EditPart getTargetEditPart() {
		return (IGraphicalEditPart) viewer
		.getEditPartRegistry().get(
			targetViewAdapter.getAdapter(View.class));
	}
	
	/**
	 * Finds the source and target editparts by extracting the views from the
	 * view adapaters and searching in the editpart viewer. Creates a connector
	 * view and element using the request.
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {
		CreateConnectorViewRequest req = null;
		if (request != null) {
			if (request instanceof CreateConnectorViewRequest) {
				req = (CreateConnectorViewRequest) request;
			}
		}
		else {
			return newErrorCommandResult(getLabel());			
		}
		if (typeInfoAdapter != null) {
			IElementType typeInfo = (IElementType) typeInfoAdapter
				.getAdapter(IElementType.class);
			if (typeInfo == null) {
				newErrorCommandResult(getLabel());
			}

			if (request instanceof CreateUnspecifiedTypeConnectionRequest) {
				req = ((CreateConnectorViewRequest) ((CreateUnspecifiedTypeConnectionRequest) request)
						.getRequestForType(typeInfo));
			}
		}

		req.setLocation(null);
				
		// Suppressing UI if the target edit part has not been created yet
		// this is so that if we are creating a new target the connector
		// creation will just take default data instead of prompting 
		// For Defect RATLC00524293
		if (targetViewAdapter.getAdapter(IGraphicalEditPart.class) == null
			&& req instanceof SuppressibleUIRequest)
			((SuppressibleUIRequest) req).setSuppressibleUI(true);
		
		EditPart sourceEP = getSourceEditPart();

		EditPart targetEP = getTargetEditPart();
		
		// There are situations where src or target can be null and we must check for these
		// ie. When a Select Existing Dialog is presnetd to the user and the user presses 
		// cancel either the src or target can be null.
		// So the following assertions have been replaced with a check for null on the editparts.
		// old code ... Assert.isNotNull(sourceEP);	Assert.isNotNull(targetEP);
		if((sourceEP==null) || (targetEP==null))
			return null;
		
		if (req instanceof CreateConnectorViewAndElementRequest) {
			command = CreateConnectorViewAndElementRequest.getCreateCommand(
				req, sourceEP, targetEP);
		} else {
			command = CreateConnectorViewRequest.getCreateCommand(req,
				sourceEP, targetEP);
		}

		if (command != null && command.canExecute()) {
			command.execute();
		}
		viewer = null;// for garbage collection

		View view = (View) req.getConnectorViewDescriptor().getAdapter(View.class);
		if (null == view) {
			return newCancelledCommandResult();
		}
		return newOKCommandResult(req.getNewObject());
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

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doRedo()
	 */
	protected CommandResult doRedo() {
		if (command != null) {
			command.redo();
		}
		return newOKCommandResult();
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doUndo()
	 */
	protected CommandResult doUndo() {
		if (command != null) {
			command.undo();
		}
		return newOKCommandResult();
	}

}
