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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.SetConnectionEndsCommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.jface.util.Assert;

/**
 * 
 * 
 * A request to create new <code>IView</code> (s)
 * 
 * To instantiate this request, clients have to create a <code>ConnectorViewDescriptor</code>
 * or a list of <code>ConnectionViewDescriptor</code> s filling it with view
 * creation parameters. The <code>ConnectionViewDescriptor</code> is a inner
 * class to this request
 * 
 * The request object can be used to obtain a view creation command from a
 * target <code>EditPart</code> Once such command is executed, the request
 * cannot be reused again to create another view. A different instance of the
 * reqyest has to be used instead
 *  
 * @author melaasar
 * 
 */
public class CreateConnectionViewRequest extends CreateConnectionRequest {

	/**
	 * A specialized view descriptor for connection views
	 */
	public static class ConnectionViewDescriptor extends ViewDescriptor {
		/**
		 * Constructor.
		 * 
		 * @param elementAdapter
		 * @param preferencesHint
		 *            The preference hint that is to be used to find the appropriate
		 *            preference store from which to retrieve diagram preference
		 *            values. The preference hint is mapped to a preference store in
		 *            the preference registry <@link DiagramPreferencesRegistry>.
		 */
		public ConnectionViewDescriptor(IAdaptable elementAdapter, PreferencesHint preferencesHint) {
			super(elementAdapter, Edge.class, preferencesHint);
		}

		/**
		 * Constructor.
		 * 
		 * @param elementAdapter
		 * @param semanticHint
		 * @param preferencesHint
		 *            The preference hint that is to be used to find the appropriate
		 *            preference store from which to retrieve diagram preference
		 *            values. The preference hint is mapped to a preference store in
		 *            the preference registry <@link DiagramPreferencesRegistry>.
		 */
		public ConnectionViewDescriptor(
			IAdaptable elementAdapter,
			String semanticHint, 
			PreferencesHint preferencesHint) {
			super(elementAdapter, Edge.class, semanticHint, preferencesHint);
		}

		/**
		 * Constructor.
		 * 
		 * @param elementAdapter
		 * @param semanticHint
		 * @param index
		 */
		public ConnectionViewDescriptor(
			IAdaptable elementAdapter,
			String semanticHint,
			int index, 
			PreferencesHint preferencesHint) {
			super(elementAdapter, Edge.class, semanticHint, index, preferencesHint);
		}

		/**
		 * Constructor.
		 * @param elementAdapter
		 * @param semanticHint
		 * @param index
		 * @param persisted flag to indicate if this view will be persisted or not
		 */
		public ConnectionViewDescriptor(
			IAdaptable elementAdapter,
			String semanticHint,
			int index,
			boolean persisted, 
			PreferencesHint preferencesHint) {
			super(
				elementAdapter,
				Edge.class,
				semanticHint,
				index,
				persisted,
				preferencesHint);
			}
		}

	/**
	 * The connection view descriptor set by the user
	 */
	private ConnectionViewDescriptor connectionViewDescriptor;

	/**
		 * Constructor.
	 * 
	 * @param element a semantic element
	 */
	public CreateConnectionViewRequest(EObject element, PreferencesHint preferencesHint) {
		this(new ConnectionViewDescriptor(new EObjectAdapter(element), preferencesHint));
	}


	/**
		 * Constructor.
	 * 
	 * @param ViewDescriptor a  view descriptor
	 */
	public CreateConnectionViewRequest(ConnectionViewDescriptor ViewDescriptor) {
		Assert.isNotNull(ViewDescriptor);
		this.connectionViewDescriptor = ViewDescriptor;
	}

	/**
	 * Gets the descriptor for the connection view to be created.
	 * 
	 * @return the descriptor
	 */
	public ConnectionViewDescriptor getConnectionViewDescriptor() {
		return connectionViewDescriptor;
	}

	/**
	 * An <code>IAdaptable</code> object that adapts to <code>IView</code>
	 * .class
	 * 
	 * @see org.eclipse.gef.requests.CreateRequest#getNewObject()
	 */
	public Object getNewObject() {
		return getConnectionViewDescriptor();
	}

	/**
	 * The type is a <code>IAdaptable</code> object that adapters to <code>IView</code>
	 * .class
	 * 
	 * @see org.eclipse.gef.requests.CreateRequest#getNewObjectType()
	 */
	public Object getNewObjectType() {
		return IAdaptable.class;
	}

	/**
	 * The factory mechanism is not used
	 * @throws UnsupportedOperationException
	 */
	protected CreationFactory getFactory() {
		throw new UnsupportedOperationException("The Factory mechanism is not used"); //$NON-NLS-1$
	}

	/**
	 * The factory mechanism is not used
	 */

	public void setFactory(CreationFactory factory) {
		throw new UnsupportedOperationException("The Factory mechanism is not used"); //$NON-NLS-1$
	}

	/**
	 * Method getCreateCommand.
	 * 
	 * @param element
	 * @param sourceEditPart
	 * @param targetEditPart
	 * @return Command
	 */
	public static Command getCreateCommand(
		EObject element,
		EditPart sourceEditPart,
		EditPart targetEditPart,
		PreferencesHint preferencesHint) {

		Assert.isNotNull(element);
		Assert.isNotNull(sourceEditPart);
		Assert.isNotNull(targetEditPart);

		CreateConnectionViewRequest request =
			new CreateConnectionViewRequest(element, preferencesHint);

		request.setSourceEditPart(sourceEditPart);
		request.setTargetEditPart(targetEditPart);
		request.setType(RequestConstants.REQ_CONNECTION_START);
		sourceEditPart.getCommand(request);
		request.setType(RequestConstants.REQ_CONNECTION_END);
		return targetEditPart.getCommand(request);
	}
	
	/**
	 * Method getCreateCommand
	 * Gets the command given a request, source and target
	 * edit parts.  (No semantic element required.)
	 * 
	 * @param request
	 * @param sourceEditPart
	 * @param targetEditPart
	 * @return <code>Command</code>
	 */
	public static Command getCreateCommand(
		CreateConnectionViewRequest request,
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
		return targetEditPart.getCommand(request);
	}


	/**
	 * getCreateCommand.
	 * @param elementAdapter
	 * @param sourceViewAdapter
	 * @param targetViewAdapter
	 * @param diagramEditPart
	 * @return Command
	 */
	public static Command getCreateCommand(
		IAdaptable elementAdapter,
		IAdaptable sourceViewAdapter,
		IAdaptable targetViewAdapter,
		DiagramEditPart diagramEditPart,
		PreferencesHint preferencesHint) {

		CreateCommand createCommand =
			new CreateCommand(
				new ConnectionViewDescriptor(elementAdapter, preferencesHint),
				diagramEditPart.getDiagramView().getDiagram());

		IAdaptable viewAdapter =
			(IAdaptable) createCommand.getCommandResult().getReturnValue();

		SetConnectionEndsCommand sceCommand = new SetConnectionEndsCommand(DiagramResourceManager.getI18NString("Commands.SetConnectionEndsCommand.Source")); //$NON-NLS-1$
		sceCommand.setEdgeAdaptor(viewAdapter);
		sceCommand.setNewSourceAdaptor(sourceViewAdapter);
		sceCommand.setNewTargetAdaptor(targetViewAdapter);

		CompositeCommand cc = new CompositeCommand(null);
		cc.compose(createCommand);
		cc.compose(sceCommand);
		return new EtoolsProxyCommand(cc);
	}
	
	/**
	 * Method getCreateCommand.
	 * @param viewDescriptor
	 * @param sourceViewAdapter
	 * @param targetViewAdapter
	 * @param diagramEditPart
	 * @return Command
	 */
	public static Command getCreateCommand(
		ViewDescriptor viewDescriptor,
		IAdaptable sourceViewAdapter,
		IAdaptable targetViewAdapter,
		DiagramEditPart diagramEditPart) {

		CreateCommand createCommand =
			new CreateCommand(viewDescriptor,
				diagramEditPart.getDiagramView().getDiagram());

		IAdaptable viewAdapter =
			(IAdaptable) createCommand.getCommandResult().getReturnValue();

		SetConnectionEndsCommand sceCommand = new SetConnectionEndsCommand(DiagramResourceManager.getI18NString("Commands.SetConnectionEndsCommand.Source")); //$NON-NLS-1$
		sceCommand.setEdgeAdaptor(viewAdapter);
		sceCommand.setNewSourceAdaptor(sourceViewAdapter);
		//sceCommand.setNewSourceTerminal("anchor"); //$NON-NLS-1$
		sceCommand.setNewTargetAdaptor(targetViewAdapter);
		//sceCommand.setNewTargetTerminal("anchor"); //$NON-NLS-1$

		// Need some bendpoints set, otherwise a null exception occurs
		// when the user tries to bend the .
//		SetBendpointsCommand sbbCommand = new SetBendpointsCommand();
//		sbbCommand.setConnectorAdapter(viewAdapter);
//		PointList pointList = new PointList();
//		pointList.addPoint(new Point(0, 0));
//		pointList.addPoint(new Point(0, 0));
//		sbbCommand.setNewPointList(pointList, new Point(0, 0), new Point(0, 0));

		CompositeCommand cc = new CompositeCommand(null);
		cc.compose(createCommand);
		cc.compose(sceCommand);
//		cc.compose(sbbCommand);
		return new EtoolsProxyCommand(cc);
	}	
	
}
