/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateViewAndOptionallyElementCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredCreateConnectionViewAndElementCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.PromptForConnectionAndEndCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeConnectionRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;

/**
 * This is installed on a container editpart. It is responsible for creating
 * connections from a source shape to an unspecified target and a target shape to
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
	 * Only handles connection end requests. Cannot start a connection on a
	 * container.
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

		CompoundCommand cc = new CompoundCommand(DiagramUIMessages.Command_CreateRelationship_Label);

		// Flags the case where the connection is to be created from a known
		// target
		// to unspecified source.
		boolean isDirectionReversed = request instanceof CreateUnspecifiedTypeConnectionRequest
			&& ((CreateUnspecifiedTypeConnectionRequest) request)
				.isDirectionReversed();

		// Adds the command for the popup menu to get the relationship type and
		// end element.
		PromptForConnectionAndEndCommand menuCmd = getPromptForConnectionAndEndCommand(
			request);
		cc.add(new ICommandProxy(menuCmd));

		// Adds the command to create a view (and optionally an element) for
		// the other end.
		CreateViewAndOptionallyElementCommand createOtherEndCmd = getCreateOtherEndCommand(
			menuCmd.getEndAdapter(), request.getLocation());
		cc.add(new ICommandProxy(createOtherEndCmd));
		
		// Adds the command to create the connection view and element.
		ICommand connectionCmd = isDirectionReversed 
			? getCreateConnectionCommand( 
				request,
				menuCmd.getConnectionAdapter(),
				createOtherEndCmd.getResult(), 
				request.getSourceEditPart())
			: getCreateConnectionCommand(
				request,
				menuCmd.getConnectionAdapter(), 
				request.getSourceEditPart(),
				createOtherEndCmd.getResult());	
			
		cc.add(new ICommandProxy(connectionCmd));

		return cc;
	}
	
	/**
	 * Gets a command that pops up a menu which can allow the user to select the
	 * type of connector to be created and whether they want to create a new
	 * type or select an existing element for the other end of the connector.
	 * 
	 * @param request
	 *            A single create connection request or unspecified connection
	 *            requests (i.e. where the popup also prompts the user for the
	 *            type of relationship to created).
	 * @return the command to popup up the menu
	 */
	protected PromptForConnectionAndEndCommand getPromptForConnectionAndEndCommand(
			CreateConnectionRequest request) {
		return new PromptForConnectionAndEndCommand(request,
			(IGraphicalEditPart) getHost());
	}

	/**
	 * Called by {@link #getConnectionAndEndCommands}.
	 * @param endAdapter the end adapter 
	 * @param location the location
	 * @return command
	 */
	protected CreateViewAndOptionallyElementCommand getCreateOtherEndCommand(
			IAdaptable endAdapter, Point location ) {
		return new CreateViewAndOptionallyElementCommand(endAdapter,
			(IGraphicalEditPart) getHost(), location,
			((IGraphicalEditPart) getHost()).getDiagramPreferencesHint());
	}
	
	/**
	 * Called by {@link #getConnectionAndEndCommands} .
	 * @param request the create connection request
	 * @param typeInfoAdapter
	 * @param sourceViewAdapter
	 * @param targetViewAdapter
	 * @return a <code>DeferredCreateConnectionViewAndElementCommand</code>
	 */
	protected ICommand getCreateConnectionCommand(
			CreateRequest request,
			IAdaptable typeInfoAdapter,
			IAdaptable sourceViewAdapter,
			IAdaptable targetViewAdapter) {
		return new DeferredCreateConnectionViewAndElementCommand(request,
			typeInfoAdapter, sourceViewAdapter, targetViewAdapter, getHost()
				.getViewer());
	}

}