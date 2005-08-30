/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.requests;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.SetConnectorEndsCommand;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Edge;

/**
 * 
 * 
 * A request to create new <code>IView</code> (s)
 * 
 * To instantiate this request, clients have to create a <code>ConnectorViewDescriptor</code>
 * or a list of <code>ConnectorViewDescriptor</code> s filling it with view
 * creation parameters. The <code>ConnectorViewDescriptor</code> is a inner
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
public class CreateConnectorViewRequest extends CreateConnectionRequest {

	/**
	 * A specialized view descriptor for connector views
	 */
	public static class ConnectorViewDescriptor extends ViewDescriptor {
		/**
		 * Constructor for ConnectorViewDescriptor.
		 * 
		 * @param elementAdapter
		 * @param preferencesHint
		 *            The preference hint that is to be used to find the appropriate
		 *            preference store from which to retrieve diagram preference
		 *            values. The preference hint is mapped to a preference store in
		 *            the preference registry <@link DiagramPreferencesRegistry>.
		 */
		public ConnectorViewDescriptor(IAdaptable elementAdapter, PreferencesHint preferencesHint) {
			super(elementAdapter, Edge.class, preferencesHint);
		}

		/**
		 * Constructor for ConnectorViewDescriptor.
		 * 
		 * @param elementAdapter
		 * @param semanticHint
		 * @param preferencesHint
		 *            The preference hint that is to be used to find the appropriate
		 *            preference store from which to retrieve diagram preference
		 *            values. The preference hint is mapped to a preference store in
		 *            the preference registry <@link DiagramPreferencesRegistry>.
		 */
		public ConnectorViewDescriptor(
			IAdaptable elementAdapter,
			String semanticHint, 
			PreferencesHint preferencesHint) {
			super(elementAdapter, Edge.class, semanticHint, preferencesHint);
		}

		/**
		 * Constructor for ConnectorViewDescriptor.
		 * 
		 * @param elementAdapter
		 * @param semanticHint
		 * @param index
		 */
		public ConnectorViewDescriptor(
			IAdaptable elementAdapter,
			String semanticHint,
			int index, 
			PreferencesHint preferencesHint) {
			super(elementAdapter, Edge.class, semanticHint, index, preferencesHint);
		}

		/**
		 * Constructor for ConnectorViewDescriptor.
		 * @param elementAdapter
		 * @param semanticHint
		 * @param index
		 * @param persisted flag to indicate if this view will be persisted or not
		 */
		public ConnectorViewDescriptor(
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
	 * The connector view descriptor set by the user
	 */
	private ConnectorViewDescriptor connectorViewDescriptor;

	/**
	 * Convenience constructor for CreateConnectorViewRequest using a <code>IElement</code>
	 * 
	 * @param element a semantic element
	 */
	public CreateConnectorViewRequest(EObject element, PreferencesHint preferencesHint) {
		this(new ConnectorViewDescriptor(new EObjectAdapter(element), preferencesHint));
	}


	/**
	 * Constructor for CreateConnectorViewRequest using a <code>ConnectorConnectorViewDescriptor</code>
	 * 
	 * @param connectorViewDescriptor a connector view descriptor
	 */
	public CreateConnectorViewRequest(ConnectorViewDescriptor connectorViewDescriptor) {
		Assert.isNotNull(connectorViewDescriptor);
		this.connectorViewDescriptor = connectorViewDescriptor;
	}

	/**
	 * Returns the connectorViewDescriptor.
	 * 
	 * @return ConnectorViewDescriptor
	 */
	public ConnectorViewDescriptor getConnectorViewDescriptor() {
		return connectorViewDescriptor;
	}

	/**
	 * An <code>IAdaptable</code> object that adapts to <code>IView</code>
	 * .class
	 * 
	 * @see org.eclipse.gef.requests.CreateRequest#getNewObject()
	 */
	public Object getNewObject() {
		return getConnectorViewDescriptor();
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

		CreateConnectorViewRequest request =
			new CreateConnectorViewRequest(element, preferencesHint);

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
		CreateConnectorViewRequest request,
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
				new ConnectorViewDescriptor(elementAdapter, preferencesHint),
				diagramEditPart.getDiagramView().getDiagram());

		IAdaptable viewAdapter =
			(IAdaptable) createCommand.getCommandResult().getReturnValue();

		SetConnectorEndsCommand sceCommand = new SetConnectorEndsCommand(PresentationResourceManager.getI18NString("Commands.SetConnectorEndsCommand.Source")); //$NON-NLS-1$
		sceCommand.setConnectorAdaptor(viewAdapter);
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

		SetConnectorEndsCommand sceCommand = new SetConnectorEndsCommand(PresentationResourceManager.getI18NString("Commands.SetConnectorEndsCommand.Source")); //$NON-NLS-1$
		sceCommand.setConnectorAdaptor(viewAdapter);
		sceCommand.setNewSourceAdaptor(sourceViewAdapter);
		//sceCommand.setNewSourceTerminal("anchor"); //$NON-NLS-1$
		sceCommand.setNewTargetAdaptor(targetViewAdapter);
		//sceCommand.setNewTargetTerminal("anchor"); //$NON-NLS-1$

		// Need some bendpoints set, otherwise a null exception occurs
		// when the user tries to bend the connector.
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
