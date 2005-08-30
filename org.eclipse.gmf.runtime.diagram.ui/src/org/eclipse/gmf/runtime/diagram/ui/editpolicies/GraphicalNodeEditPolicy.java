/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.diagram.core.commands.SetConnectorAnchorsCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.SetConnectorEndsCommand;
import org.eclipse.gmf.runtime.diagram.core.commands.SetPropertyCommand;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CreateOrSelectElementCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.EtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.SemanticCreateCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.XtoolsProxyCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.commands.SetConnectorBendpointsCommand;
import org.eclipse.gmf.runtime.diagram.ui.l10n.PresentationResourceManager;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.requests.ChangePropertyValueRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectorViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectorViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateUnspecifiedTypeConnectionRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.EditCommandRequestWrapper;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantService;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import com.ibm.xtools.notation.Diagram;
import com.ibm.xtools.notation.NotationPackage;
import com.ibm.xtools.notation.Routing;
import com.ibm.xtools.notation.RoutingStyle;
import com.ibm.xtools.notation.View;

/*
 * @canBeSeenBy %partners
 */
/**
 * the graphical node edit policy
 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy
 * 
 * @author mmostafa
 */
public class GraphicalNodeEditPolicy
		extends
			org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy {
	/** describes the view to be created. */
	private IAdaptable _viewAdapter;
	
	/**
	 * The label used for the command to create a new connector.
	 */
	private static final String CREATE_CONNECTOR_COMMAND_LABEL = PresentationResourceManager.getInstance()
		.getString("GraphicalNodeEditPolicy.createRelationshipCommand.label"); //$NON-NLS-1$
	
	protected Connection createDummyConnection(Request req) {
		PolylineConnection c = (PolylineConnection) super.createDummyConnection(req);
		c.setLineStyle(Graphics.LINE_DASHDOT);
		c.setForegroundColor(((GraphicalEditPart) getHost()).getFigure()
				.getForegroundColor());
		return c;
	}

	protected ConnectionAnchor getSourceConnectionAnchor(
			CreateConnectionRequest request) {
		EditPart source = request.getSourceEditPart();
		return source instanceof INodeEditPart ? ((INodeEditPart) source)
				.getSourceConnectionAnchor(request) : null;
	}
	
	protected ConnectionAnchor getTargetConnectionAnchor(
			CreateConnectionRequest request) {
		EditPart target = request.getTargetEditPart();
		return target instanceof INodeEditPart ? ((INodeEditPart) target)
				.getTargetConnectionAnchor(request) : null;
	}

	/**
	 * get a connectable edit part
	 * @return INodeEditPart
	 */
	protected INodeEditPart getConnectableEditPart() {
		return (INodeEditPart) getHost();
	}
	/**
	 * getConnectionCompleteEditPart
	 * 
	 * This method is used when the connection is verified and made complete to
	 * retrieve the final connecting editpart to be used in the connection
	 * creation. This is by default the "host" edit part which is what is
	 * connected to by the user feedback. Subclassing edit-policies may wish to
	 * redirect the connection to a different edit-part depending on the
	 * gesture. i.e. the tree-view for generalizations will redirect the
	 * connection to the target of the host.
	 * 
	 * @param request
	 *            Request giving some information about the user gesture.
	 * @return INodeEditPart which will be the target connection.
	 */
	protected INodeEditPart getConnectionCompleteEditPart(Request request) {
		if (getHost() instanceof INodeEditPart) {
			return (INodeEditPart) getHost();
		}
		return null;
	}
	/**
	 * getConnectionTargetAnchor Method used during reconnection to retrieve
	 * the appropriate target anchor based on a request.
	 * 
	 * @param request
	 *            Request giving some information about the user gesture.
	 * @return ConnectionAnchor the connections target end will connect to.
	 */
	protected ConnectionAnchor getConnectionTargetAnchor(Request request) {
		return getConnectableEditPart().getTargetConnectionAnchor(request);
	}
	/**
	 * get this edit policy's edit part <code>View</code>
	 * @return View
	 */
	protected View getView() {
		return (View)getHost().getModel();
	}
	/**
	 * getSemanticHint Retrieves the semanticHint from the request regarding
	 * the type of elemen being manipulated.
	 * 
	 * @param request
	 *            Request that is sent from the user gesture
	 * @return String that is the semantic type.
	 */
	protected String getSemanticHint(Request request) {
		String hint = null;
		if (request instanceof CreateConnectorViewAndElementRequest) {
			CreateConnectorViewAndElementRequest ccvr = (CreateConnectorViewAndElementRequest) request;
			// get the element descriptor
			CreateElementRequestAdapter requestAdapter = ccvr
					.getConnectorViewAndElementDescriptor()
					.getCreateElementRequestAdapter();
			// get the semantic request
			CreateRelationshipRequest createElementRequest = (CreateRelationshipRequest) requestAdapter
					.getAdapter(CreateRelationshipRequest.class);
			hint = MetaModelUtil.getDisplayName(createElementRequest.getElementType().getEClass());
		} else if (request instanceof ReconnectRequest) {
			ReconnectRequest rr = (ReconnectRequest) request;
			hint = ViewUtil.getSemanticElementClassId((View)rr.getConnectionEditPart().getModel());
		} else if (request instanceof CreateConnectorViewRequest) {
			CreateConnectorViewRequest ccvr = (CreateConnectorViewRequest) request;
			hint = ccvr.getConnectorViewDescriptor().getSemanticHint();
		}
		return hint;
	}
	/**
	 * getRoutingAdjustment method to adjust routing if the reorient has moved
	 * the connection into a different routing environment. Specifically, if
	 * the connection has been reoriented out of a tree structure it will
	 * change routing to the default set in preference. Likewise, if the
	 * connection has connected to a tree structure then the routing will
	 * create a tree.
	 * 
	 * @param connector
	 *            IAdaptable that is placeholder for not yet created connector.
	 *            Also adapts directly to a ConnectionEditPart in the case of a
	 *            reorient.
	 * @param connectorHint
	 *            String that is the semantic hint of the connector being
	 *            manipulated
	 * @param currentRouterType
	 *            Integer current representation of the routing style
	 * @param target
	 *            EditPart that is being targeted by the request.
	 * @return Command to make any routing adjustments if necessary.
	 */
	protected Command getRoutingAdjustment(IAdaptable connector,
			String connectorHint, Routing currentRouterType, EditPart target) {
		Command cmd = null;
		if (connectorHint == null ||
				target == null || target.getModel() == null
				|| ((View)target.getModel()).getElement() == null)
			return null;
		// check if router needs to change type due to reorient.
		String targetHint = ViewUtil.getSemanticElementClassId((View) target.getModel());
		Routing newRouterType = null;
		if (target instanceof ITreeBranchEditPart
				&& connectorHint.equals(targetHint)) {
			newRouterType = Routing.TREE_LITERAL;
			ChangePropertyValueRequest cpvr = new ChangePropertyValueRequest(
					StringStatics.BLANK, Properties.ID_ROUTING, newRouterType);
			Command cmdRouter = target.getCommand(cpvr);
			if (cmdRouter != null)
				cmd = cmd == null ? cmdRouter : cmd.chain(cmdRouter);
		} else {
			if (currentRouterType.equals(Routing.TREE_LITERAL)) {
				IPreferenceStore store = (IPreferenceStore)
					((IGraphicalEditPart) getHost())
						.getDiagramPreferencesHint().getPreferenceStore();
				newRouterType = Routing.get(store.getInt(IPreferenceConstants.PREF_LINE_STYLE));
			}
		}
		if (newRouterType != null) {
			// add commands for line routing. Convert the new connection and
			// also the targeted connection.
			ICommand spc = new SetPropertyCommand(connector,
				Properties.ID_ROUTING, StringStatics.BLANK, newRouterType);
			Command cmdRouter = new EtoolsProxyCommand(spc);
			if (cmdRouter != null) {
				cmd = cmd == null ? cmdRouter : cmd.chain(cmdRouter);
			}
		}
		return cmd;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		ConnectionAnchor targetAnchor = getConnectionTargetAnchor(request);
		INodeEditPart targetEP = getConnectionCompleteEditPart(request);
		if (targetEP == null) {
			return null;
		}
		SetConnectorEndsCommand sceCommand = new SetConnectorEndsCommand(null);
		sceCommand.setConnectorAdaptor(new EObjectAdapter((EObject)request
				.getConnectionEditPart().getModel()));
		sceCommand.setNewTargetAdaptor(targetEP);
		SetConnectorAnchorsCommand scaCommand = new SetConnectorAnchorsCommand(null);
		scaCommand.setConnectorAdaptor(new EObjectAdapter((EObject) request
			.getConnectionEditPart().getModel()));
		scaCommand.setNewTargetTerminal(targetEP
				.mapConnectionAnchorToTerminal(targetAnchor));
		CompositeCommand cc = new CompositeCommand(
			PresentationResourceManager.getI18NString("Commands.SetConnectorEndsCommand.Target")); //$NON-NLS-1$
		cc.compose(sceCommand);
		cc.compose(scaCommand);
		Command cmd = new EtoolsProxyCommand(cc);
		EditPart cep = request.getConnectionEditPart();
		RoutingStyle style = (RoutingStyle) ((View)cep.getModel()).getStyle(NotationPackage.eINSTANCE.getRoutingStyle());
		Routing currentRouter = Routing.MANUAL_LITERAL;		
		if (style != null) {
			currentRouter = style.getRouting();
		}
		Command cmdRouter = getRoutingAdjustment(request
				.getConnectionEditPart(), getSemanticHint(request),
				currentRouter, request.getTarget());
		if (cmdRouter != null) {
			cmd = cmd == null ? cmdRouter : cmd.chain(cmdRouter);
			// reset the bendpoints
			ConnectionAnchor sourceAnchor = getConnectableEditPart()
					.getSourceConnectionAnchor(request);
			PointList pointList = new PointList();
			pointList.addPoint(sourceAnchor.getLocation(targetAnchor
					.getReferencePoint()));
			pointList.addPoint(targetAnchor.getLocation(sourceAnchor
					.getReferencePoint()));
			SetConnectorBendpointsCommand sbbCommand = new SetConnectorBendpointsCommand();
			sbbCommand.setConnectorAdapter(request.getConnectionEditPart());
			sbbCommand.setNewPointList(pointList, sourceAnchor
					.getReferencePoint(), targetAnchor.getReferencePoint());
			Command cmdBP = new EtoolsProxyCommand(sbbCommand);
			if (cmdBP != null) {
				cmd = cmd == null ? cmdBP : cmd.chain(cmdBP);
			}
		}
		return cmd;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		ConnectionAnchor sourceAnchor = getConnectableEditPart()
				.getSourceConnectionAnchor(request);
		SetConnectorEndsCommand sceCommand = new SetConnectorEndsCommand(null);
		sceCommand.setConnectorAdaptor(new EObjectAdapter((View) request
				.getConnectionEditPart().getModel()));
		sceCommand.setNewSourceAdaptor(new EObjectAdapter((View)getConnectableEditPart()
				.getModel()));
		SetConnectorAnchorsCommand scaCommand = new SetConnectorAnchorsCommand(null);
		scaCommand.setConnectorAdaptor(new EObjectAdapter((View) request
			.getConnectionEditPart().getModel()));
		scaCommand.setNewSourceTerminal(getConnectableEditPart()
				.mapConnectionAnchorToTerminal(sourceAnchor));
		CompositeCommand cc = new CompositeCommand(
			PresentationResourceManager.getI18NString("Commands.SetConnectorEndsCommand.Source")); //$NON-NLS-1$
		cc.compose(sceCommand);
		cc.compose(scaCommand);
		return new EtoolsProxyCommand(cc);
	}
	/**
	 * Returns a command that will create the connection.
	 * 
	 * If you must override this method, you should call super.
	 *  
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
	 *
	 */
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		EtoolsProxyCommand proxy = (EtoolsProxyCommand) request
			.getStartCommand();
		if (proxy == null) {
			return null;
		}

		// reset the target edit-part for the request
		INodeEditPart targetEP = getConnectionCompleteEditPart(request);
		if (targetEP == null) {
			return null;
		}

		CompositeCommand cc = (CompositeCommand) proxy.getICommand();
		ConnectionAnchor targetAnchor = targetEP
			.getTargetConnectionAnchor(request);
		SetConnectorEndsCommand sceCommand = (SetConnectorEndsCommand) cc
			.getCommands().get(1);
		sceCommand.setNewTargetAdaptor(new EObjectAdapter(((IGraphicalEditPart) targetEP).getNotationView()));
		SetConnectorAnchorsCommand scaCommand = (SetConnectorAnchorsCommand) cc
		.getCommands().get(2);
		scaCommand.setNewTargetTerminal(targetEP
			.mapConnectionAnchorToTerminal(targetAnchor));
		setViewAdapter(sceCommand.getConnectorAdaptor());
		INodeEditPart sourceEditPart = (INodeEditPart) request
			.getSourceEditPart();
		ConnectionAnchor sourceAnchor = sourceEditPart
			.mapTerminalToConnectionAnchor(scaCommand.getNewSourceTerminal());
		PointList pointList = new PointList();
		if (request.getLocation() == null) {
			pointList.addPoint(sourceAnchor.getLocation(targetAnchor.getReferencePoint()));
			pointList.addPoint(targetAnchor.getLocation(sourceAnchor.getReferencePoint()));			
		}
		else {
			pointList.addPoint(sourceAnchor.getLocation(request.getLocation()));
			pointList.addPoint(targetAnchor.getLocation(request.getLocation()));
		}
		SetConnectorBendpointsCommand sbbCommand = (SetConnectorBendpointsCommand) cc
			.getCommands().get(3);
		sbbCommand.setNewPointList(pointList, sourceAnchor.getReferencePoint(),
			targetAnchor.getReferencePoint());
		return request.getStartCommand();
	}
	
	/**
	 * Cache the view descriptor describing the connector to be create.
	 * 
	 * @param viewAdapter
	 */
	protected final void setViewAdapter(IAdaptable viewAdapter) {
		_viewAdapter = viewAdapter;
	}
	
	/**
	 * Return the view adapter describing the element to be created.
	 * @see #setViewAdapter(IAdaptable)
	 * @return adpater that can adapt <code>View.class</code>
	 */
	protected final IAdaptable getViewAdapter() {
		return _viewAdapter;
	}
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		if (!(request instanceof CreateConnectorViewRequest))
			return null;
		CreateConnectorViewRequest req = (CreateConnectorViewRequest) request;
		CompositeCommand cc = new CompositeCommand(PresentationResourceManager.
			getI18NString("Commands.CreateCommand.Connector.Label")); //$NON-NLS-1$
		Diagram diagramView = ((View)getHost().getModel())
				.getDiagram();
		CreateCommand createCommand = new CreateCommand(req
				.getConnectorViewDescriptor(), diagramView.getDiagram());
		setViewAdapter((IAdaptable) createCommand.getCommandResult()
				.getReturnValue());
		SetConnectorEndsCommand sceCommand = new SetConnectorEndsCommand(null);
		sceCommand.setConnectorAdaptor(getViewAdapter());
		sceCommand.setNewSourceAdaptor(new EObjectAdapter(getView()));
		ConnectionAnchor sourceAnchor = getConnectableEditPart()
				.getSourceConnectionAnchor(request);
		SetConnectorAnchorsCommand scaCommand = new SetConnectorAnchorsCommand(null);
		scaCommand.setConnectorAdaptor(getViewAdapter());
		scaCommand.setNewSourceTerminal(getConnectableEditPart()
				.mapConnectionAnchorToTerminal(sourceAnchor));
		SetConnectorBendpointsCommand sbbCommand = new SetConnectorBendpointsCommand();
		sbbCommand.setConnectorAdapter(getViewAdapter());
		cc.compose(createCommand);
		cc.compose(sceCommand);
		cc.compose(scaCommand);
		cc.compose(sbbCommand);
		Command c = new EtoolsProxyCommand(cc);
		request.setStartCommand(c);
		return c;
	}
	
	public Command getCommand(Request request) {
		if (RequestConstants.REQ_CONNECTION_START.equals(request.getType())) {
			if (request instanceof CreateConnectorViewAndElementRequest) {
				return getConnectionAndRelationshipCreateCommand((CreateConnectorViewAndElementRequest) request);
			} else if (request instanceof CreateUnspecifiedTypeConnectionRequest) {
				return getUnspecifiedConnectionCreateCommand((CreateUnspecifiedTypeConnectionRequest) request);
			}
		} else if (RequestConstants.REQ_CONNECTION_END
			.equals(request.getType())) {
			if (request instanceof CreateConnectorViewAndElementRequest) {
				return getConnectionAndRelationshipCompleteCommand((CreateConnectorViewAndElementRequest) request);
			} else if (request instanceof CreateUnspecifiedTypeConnectionRequest) {
				return getUnspecifiedConnectionCompleteCommand((CreateUnspecifiedTypeConnectionRequest) request);
			}
		}
		return super.getCommand(request);
	}
	
	/**
	 * Gets the command to start the creation of a new connection and
	 * relationship. This will update the request appropriately.
	 * 
	 * @param request
	 * @return Command
	 */
	protected Command getConnectionAndRelationshipCreateCommand(
			CreateConnectorViewAndElementRequest request) {
		// get the element descriptor
		CreateElementRequestAdapter requestAdapter = request
				.getConnectorViewAndElementDescriptor().getCreateElementRequestAdapter();
		// get the semantic request
		CreateRelationshipRequest createElementRequest = (CreateRelationshipRequest) requestAdapter
				.getAdapter(CreateRelationshipRequest.class);
		// complete the semantic request by filling in the source
		View sourceView = (View)getHost().getModel();
		createElementRequest.setSource(ViewUtil.resolveSemanticElement(sourceView));
		// get the create element request based on the elementdescriptor's
		// request
		Command createElementCommand = getHost().getCommand(
			new EditCommandRequestWrapper(
						(CreateRelationshipRequest) requestAdapter
								.getAdapter(CreateRelationshipRequest.class)));
		// if element cannot be created, ignore
		if (createElementCommand == null
				|| createElementCommand instanceof UnexecutableCommand)
			return null;


		return getConnectionCreateCommand(request);
	}
	
	/**
	 * Gets the command to start the creation of a new connection and
	 * relationship (if applicable) for a unspecified type request. This will
	 * update all the individual requests appropriately.
	 * 
	 * @param request
	 *            the unspecified type request
	 * @return the command
	 */
	private Command getUnspecifiedConnectionCreateCommand(
			final CreateUnspecifiedTypeConnectionRequest request) {

		if (request.isDirectionReversed()) {
			return new Command() {

				/**
				 * All we know is the target and the possible relationship
				 * types. At this point, there is no way to validate the
				 * commands for this scenario.
				 */
				public boolean canExecute() {
					return true;
				}
			};
		} else {

			// Get the start command for each individual request, this will
			// update each request as required.
			final List commands = new ArrayList();
			for (Iterator iter = request.getAllRequests().iterator(); iter
				.hasNext();) {
				Request individualRequest = (Request) iter.next();
				Command cmd = null;
				if (individualRequest instanceof CreateConnectorViewAndElementRequest) {
					cmd = getConnectionAndRelationshipCreateCommand((CreateConnectorViewAndElementRequest) individualRequest);
				} else if (individualRequest instanceof CreateConnectorViewRequest) {
					cmd = getConnectionCreateCommand((CreateConnectorViewRequest) individualRequest);
				}
				if (cmd != null) {
					commands.add(cmd);
				}
			}

			return new Command() {

				/**
				 * If at least one of the relationship types is supported, then
				 * this is supported.
				 */
				public boolean canExecute() {
					for (Iterator iter = commands.iterator(); iter.hasNext();) {
						Command cmd = (Command) iter.next();
						if (cmd.canExecute()) {
							return true;
						}
					}
					return false;
				}
			};
		}
	}	
	
	/**
	 * Gets the command to complete the creation of a new connection and
	 * relationship.
	 * 
	 * @param request
	 * @return Command
	 */
	protected Command getConnectionAndRelationshipCompleteCommand(
			CreateConnectorViewAndElementRequest request) {
		// get the element descriptor
		CreateElementRequestAdapter requestAdapter = request
				.getConnectorViewAndElementDescriptor().getCreateElementRequestAdapter();
		// get the semantic request
		CreateRelationshipRequest createElementRequest = (CreateRelationshipRequest) requestAdapter
				.getAdapter(CreateRelationshipRequest.class);
		
		createElementRequest.setPrompt(!request.isUISupressed());
		
		// complete the semantic request by filling in the source and
		// destination
		INodeEditPart targetEP = getConnectionCompleteEditPart(request);
		View sourceView = (View)request.getSourceEditPart().getModel();
		View targetView = (View)targetEP.getModel();
		createElementRequest.setSource(ViewUtil.resolveSemanticElement(sourceView));
		createElementRequest.setTarget(ViewUtil.resolveSemanticElement(targetView));
		// get the create element request based on the elementdescriptor's
		// request
		Command createElementCommand = targetEP
				.getCommand(new EditCommandRequestWrapper(
						(CreateRelationshipRequest) requestAdapter
								.getAdapter(CreateRelationshipRequest.class)));
		
		// create the create semantic element wrapper command
		SemanticCreateCommand semanticCommand = new SemanticCreateCommand(
			requestAdapter, createElementCommand);
		// get the view command
		Command viewCommand = getConnectionCompleteCommand(request);
		if (null == viewCommand)
			return null;
		// form the compound command and return
		CompositeCommand cc = new CompositeCommand(semanticCommand.getLabel());
		cc.compose( semanticCommand );
		cc.compose( new XtoolsProxyCommand(viewCommand) );
		return new EtoolsProxyCommand(cc);
	}

	/**
	 * Gets the command to complete the creation of a new connection and
	 * relationship (if applicable) for a unspecified type request. This command
	 * includes a command to popup a menu to prompt the user for the type of
	 * relationship to be created.
	 * 
	 * @param request
	 *            the unspecified type request
	 * @return the command
	 */
	protected Command getUnspecifiedConnectionCompleteCommand(
			CreateUnspecifiedTypeConnectionRequest request) {

		if (request.isDirectionReversed()) {
			return getReversedUnspecifiedConnectionCompleteCommand(request);
		}

		List allRequests = request.getAllRequests();
		if (allRequests.isEmpty()) {
			return null;
		}
		IGraphicalEditPart sourceEP = (IGraphicalEditPart) ((CreateConnectionRequest) allRequests
			.get(0)).getSourceEditPart();
		IGraphicalEditPart targetEP = (IGraphicalEditPart) ((CreateConnectionRequest) allRequests
			.get(0)).getTargetEditPart();

		List relTypes = request.useModelingAssistantService() ? ModelingAssistantService
			.getInstance().getRelTypesOnSourceAndTarget(sourceEP, targetEP)
			: request.getElementTypes();

		final Map connectorCmds = new HashMap();
		List validRelTypes = new ArrayList();
		for (Iterator iter = relTypes.iterator(); iter.hasNext();) {
			IElementType type = (IElementType) iter.next();
			Request createConnectorRequest = request
				.getRequestForType(type);
			if (createConnectorRequest != null) {
				Command individualCmd = getHost().getCommand(
					createConnectorRequest);

				if (individualCmd != null && individualCmd.canExecute()) {
					connectorCmds.put(type, individualCmd);
					validRelTypes.add(type);
				}
			}
		}
		
		if (connectorCmds.isEmpty()) {
			return null;
		} else if (connectorCmds.size() == 1) {
			return (Command) connectorCmds.values().toArray()[0];
		} else {
			CreateOrSelectElementCommand selectAndCreateConnectorCmd = new CreateOrSelectElementCommand(
				CREATE_CONNECTOR_COMMAND_LABEL, Display.getCurrent().getActiveShell(), validRelTypes) {
				
				/**
				 * My command to undo/redo.
				 */
				private Command undoCommand;

				/**
				 * Execute the command that prompts the user with the popup
				 * menu, then executes the command prepared for the relationship
				 * type that the user selected.
				 * 
				 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
				 */
				protected CommandResult doExecute(
						IProgressMonitor progressMonitor) {
					CommandResult cmdResult = super.doExecute(progressMonitor);
					if (!cmdResult.getStatus().isOK()) {
						return cmdResult;
					}

					IElementType relationshipType = (IElementType) cmdResult
						.getReturnValue();

					Command cmd = (Command) connectorCmds.get(relationshipType);
					Assert.isTrue(cmd != null && cmd.canExecute());
					cmd.execute();
					undoCommand = cmd;

					return newOKCommandResult();
				}
				
				protected CommandResult doUndo() {
					if (undoCommand != null) {
						undoCommand.undo();
					}
					return super.doUndo();
				}
				
				protected CommandResult doRedo() {
					if (undoCommand != null) {
						undoCommand.redo();
					}
					return super.doRedo();
				}
				
			};
			

			return new EtoolsProxyCommand(selectAndCreateConnectorCmd);
		}
	}

	/**
	 * Gets the command to complete the creation of a new connection and
	 * relationship (if applicable) for a unspecified type request. This command
	 * includes a command to popup a menu to prompt the user for the type of
	 * relationship to be created.
	 * 
	 * @param request
	 *            the reversed unspecified type request
	 * @return the command
	 */
	protected Command getReversedUnspecifiedConnectionCompleteCommand(
			CreateUnspecifiedTypeConnectionRequest request) {
		EditPart realSourceEP = request.getTargetEditPart();
		EditPart realTargetEP = request.getSourceEditPart();
		for (Iterator iter = request.getAllRequests().iterator(); iter
			.hasNext();) {
			CreateConnectionRequest connectorRequest = (CreateConnectionRequest) iter
				.next();

			// First, setup the request to initialize the connection start
			// command.
			connectorRequest.setSourceEditPart(null);
			connectorRequest.setTargetEditPart(realSourceEP);
			connectorRequest.setType(RequestConstants.REQ_CONNECTION_START);
			realSourceEP.getCommand(connectorRequest);

			// Now, setup the request in preparation to get the connection end
			// command.
			connectorRequest.setSourceEditPart(realSourceEP);
			connectorRequest.setTargetEditPart(realTargetEP);
			connectorRequest.setType(RequestConstants.REQ_CONNECTION_END);
		}

		// The requests are now ready to be sent to get the connection end
		// command from real source to real target.
		request.setDirectionReversed(false);
		Command command = realTargetEP.getCommand(request);
		return command;
	}

}
