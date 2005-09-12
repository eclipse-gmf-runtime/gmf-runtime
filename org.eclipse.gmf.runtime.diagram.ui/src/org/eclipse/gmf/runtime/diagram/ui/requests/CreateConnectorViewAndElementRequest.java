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

package org.eclipse.gmf.runtime.diagram.ui.requests;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.SuppressibleUIRequest;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;

/**
 * @author melaasar
 * 
 * A request to create a relationship element and its connector view
 */
public class CreateConnectorViewAndElementRequest
	extends CreateConnectorViewRequest implements SuppressibleUIRequest{

	private boolean suppressUI = false;

	/**
	 * @return true if the command will suppress all ui prompting and just use default data
	 */
	public boolean isUISupressed() {
		
		return suppressUI;
	}
	
	/**
	 * @param suppressUI true if you do not wish the command to prompt with UI but instead
	 * take the default value that it would have prompt for.
	 */
	public void setSuppressibleUI(boolean suppressUI) {
		this.suppressUI = suppressUI; 

	}
	
	
	/**
	 * An extended view descriptor that takes an <code>ElementDescriptor</code>
	 * instead of <code>IAdaptable</code> as the element adapter
	 */
	public static class ConnectorViewAndElementDescriptor
		extends ConnectorViewDescriptor {

		/**
		 * Constructor for ConnectorViewAndElementDescriptor.
		 * 
		 * @param requestAdapter
		 * @param preferencesHint
		 *            The preference hint that is to be used to find the
		 *            appropriate preference store from which to retrieve
		 *            diagram preference values. The preference hint is mapped
		 *            to a preference store in the preference registry <@link
		 *            DiagramPreferencesRegistry>.
		 */
		public ConnectorViewAndElementDescriptor(CreateElementRequestAdapter requestAdapter, PreferencesHint preferencesHint) {
			super(requestAdapter, preferencesHint);
		}

		/**
		 * Constructor for ConnectorViewAndElementDescriptor.
		 * 
		 * @param requestAdapter
		 * @param semanticHint
		 * @param preferencesHint
		 *            The preference hint that is to be used to find the appropriate
		 *            preference store from which to retrieve diagram preference
		 *            values. The preference hint is mapped to a preference store in
		 *            the preference registry <@link DiagramPreferencesRegistry>.
		 */
		public ConnectorViewAndElementDescriptor(
				CreateElementRequestAdapter requestAdapter,
			String semanticHint, PreferencesHint preferencesHint) {
			super(requestAdapter, semanticHint, preferencesHint);
		}

		/**
		 * Constructor for ConnectorViewAndElementDescriptor.
		 * 
		 * @param requestAdapter
		 * @param semanticHint
		 * @param index
		 * @param preferencesHint
		 *            The preference hint that is to be used to find the appropriate
		 *            preference store from which to retrieve diagram preference
		 *            values. The preference hint is mapped to a preference store in
		 *            the preference registry <@link DiagramPreferencesRegistry>.
		 */
		public ConnectorViewAndElementDescriptor(
				CreateElementRequestAdapter requestAdapter,
			String semanticHint,
			int index, PreferencesHint preferencesHint) {
			super(requestAdapter, semanticHint, index, preferencesHint);
		}

		/**
		 * Method getElementDescriptor.
		 * 
		 * @return ElementDescriptor
		 */
		public CreateElementRequestAdapter getCreateElementRequestAdapter() {
			return (CreateElementRequestAdapter) getElementAdapter();
		}

	}

	/**
	 * Constructor for CreateElementAndViewRequest.
	 * 
	 * @param connectorViewAndElementDescriptor
	 */
	public CreateConnectorViewAndElementRequest(ConnectorViewAndElementDescriptor connectorViewAndElementDescriptor) {
		super(connectorViewAndElementDescriptor);
	}
		
	/**
	 * Constructor for CreateElementAndViewRequest with a semantic hint.
	 * 
	 * @param type
	 * @param semanticHint
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateConnectorViewAndElementRequest(
		IElementType type,
		String semanticHint, PreferencesHint preferencesHint) {
		super(
			new ConnectorViewAndElementDescriptor(
				new CreateElementRequestAdapter(
					new CreateRelationshipRequest(
						type)),
				semanticHint, preferencesHint));
	}
	
	/**
	 * Constructor for CreateElementAndViewRequest.
	 * 
	 * @param type
	 * @param preferencesHint
	 *            The preference hint that is to be used to find the appropriate
	 *            preference store from which to retrieve diagram preference
	 *            values. The preference hint is mapped to a preference store in
	 *            the preference registry <@link DiagramPreferencesRegistry>.
	 */
	public CreateConnectorViewAndElementRequest(IElementType type, PreferencesHint preferencesHint) {
		super(
			new ConnectorViewAndElementDescriptor(
				new CreateElementRequestAdapter(
					new CreateRelationshipRequest(type)), preferencesHint));
	}

	/**
	 * Method getConnectorViewAndElementDescriptor.
	 * 
	 * @return ConnectorViewAndElementDescriptor
	 */
	public ConnectorViewAndElementDescriptor getConnectorViewAndElementDescriptor() {
		return (ConnectorViewAndElementDescriptor) getConnectorViewDescriptor();
	}

	/**
	 * Method getCreateCommand.
	 * TODO: remove after msl migration
	 * @param request
	 * @param sourceEditPart
	 * @param targetEditPart
	 * @return Command
	 **/
	public static Command getCreateCommand(
		CreateConnectorViewAndElementRequest request,
		EditPart sourceEditPart,
		EditPart targetEditPart) {

		Assert.isNotNull(request);
		Assert.isNotNull(sourceEditPart);
		Assert.isNotNull(targetEditPart);

		request.setSourceEditPart(sourceEditPart);
		request.setTargetEditPart(targetEditPart);
		request.setType(RequestConstants.REQ_CONNECTION_START);
		sourceEditPart.getCommand(request);
		request.setType(RequestConstants.REQ_CONNECTION_END);
		Command command = targetEditPart.getCommand(request);
		
		return command;
	}
	
	/**
	 * Method getCreateCommand.
	 * 
	 * @param elementType
	 * @param sourceEditPart
	 * @param targetEditPart
	 * @return Command
	 */
	public static Command getCreateCommand(IElementType elementType,
			EditPart sourceEditPart, EditPart targetEditPart,
			PreferencesHint preferencesHint) {
		return getCreateCommand(new CreateConnectorViewAndElementRequest(
			elementType, preferencesHint), sourceEditPart, targetEditPart);
	}
	
	
	

}
