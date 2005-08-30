/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              	   |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.ObjectAdapter;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateViewAndOptionallyElementCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredCreateConnectorViewAndElementCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.GetConnectorTypeAndEndCommand;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeConnectionRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * This is installed on a container editpart. It is responsible for creating
 * connectors from a source shape to an unspecified target and a target shape to
 * an unspecified source. A popup will appear asking the user to select or
 * create a new source or target element. This will handle both single create
 * connection requests and multi connection requests (i.e. where the popup also
 * prompts the user for the type of relationship to created).
 * 
 * @author cmahoney
 */
public class ContainerNodeEditPolicy
	extends GraphicalNodeEditPolicy {

	/**
	 * Only handles connection end requests. Cannot start a connector on a
	 * container.
	 * 
	 * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand(Request request) {
		if (RequestConstants.REQ_CONNECTION_END.equals(request.getType())
			&& request instanceof CreateConnectionRequest) {
			return getConnectionAndEndCommands((CreateConnectionRequest) request);
		}
		return null;
	}

	/**
	 * Creates the command to prompt the user for the relationship type
	 * (optionally) and the element to be used for the unspecified end, and the
	 * create commands necessary to create the views and elements.
	 * 
	 * @param request
	 *            A single create connection request or unspecified connection
	 *            requests (i.e. where the popup also prompts the user for the
	 *            type of relationship to created).
	 * @return the command
	 */
	protected Command getConnectionAndEndCommands(
			CreateConnectionRequest request) {

		CompoundCommand cc = new CompoundCommand(PresentationResourceManager
			.getI18NString("Command.CreateRelationship.Label")); //$NON-NLS-1$

		// Flags the case where the connection is to be created from a known
		// target
		// to unspecified source.
		boolean isDirectionReversed = request instanceof CreateUnspecifiedTypeConnectionRequest
			&& ((CreateUnspecifiedTypeConnectionRequest) request)
				.isDirectionReversed();

		// Adds the command for the popup menu to get the relationship type and
		// end element.
		GetConnectorTypeAndEndCommand menuCmd = new GetConnectorTypeAndEndCommand(
			request, (IGraphicalEditPart) getHost());
		cc.add(new EtoolsProxyCommand(menuCmd));

		// Adds the command to create a view (and optionally an element) for
		// the other end.
		CreateViewAndOptionallyElementCommand createOtherEndCmd = getCreateViewAndOptionallyElementCommand(
			menuCmd.getEndAdapter(), request.getLocation());
		cc.add(new EtoolsProxyCommand(createOtherEndCmd));
		
		// Adds the command to create the connector view and element.
		ICommand connectorCmd = isDirectionReversed 
			? getDeferredCreateConnectorViewAndElementCommand( 
				request,
				menuCmd.getConnectorTypeAdapter(),
				createOtherEndCmd.getResult(), 
				request.getSourceEditPart())
			: getDeferredCreateConnectorViewAndElementCommand(
				request,
				menuCmd.getConnectorTypeAdapter(), 
				request.getSourceEditPart(),
				createOtherEndCmd.getResult());	
			
		cc.add(new EtoolsProxyCommand(connectorCmd));

		return cc;
	}
	
	/** 
	 
	 * @return a <code>CreateViewAndOptionallyElementCommand</code>
	 */
	/**
	 * Called by {@link #getConnectionAndEndCommands}.
	 * @param endAdapter the end adapter 
	 * @param location the location
	 * @return command
	 */
	protected CreateViewAndOptionallyElementCommand getCreateViewAndOptionallyElementCommand(
			ObjectAdapter endAdapter, Point location ) {
		return new CreateViewAndOptionallyElementCommand(endAdapter,
			(IGraphicalEditPart) getHost(), location,
			((IGraphicalEditPart) getHost()).getDiagramPreferencesHint());
	}
	
	/**
	 * Called by {@link #getConnectionAndEndCommands} .
	 * @param request the create connector request
	 * @param typeInfoAdapter
	 * @param sourceViewAdapter
	 * @param targetViewAdapter
	 * @return a <code>DeferredCreateConnectorViewAndElementCommand</code>
	 */
	protected DeferredCreateConnectorViewAndElementCommand getDeferredCreateConnectorViewAndElementCommand(
			CreateRequest request,
			IAdaptable typeInfoAdapter,
			IAdaptable sourceViewAdapter,
			IAdaptable targetViewAdapter) {
		return new DeferredCreateConnectorViewAndElementCommand(request,
			typeInfoAdapter, sourceViewAdapter, targetViewAdapter, getHost()
				.getViewer());
	}

}