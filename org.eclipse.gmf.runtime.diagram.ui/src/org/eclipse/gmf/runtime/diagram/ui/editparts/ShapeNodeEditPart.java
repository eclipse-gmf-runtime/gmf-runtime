/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectorHandleEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SortFilterCompartmentItemsEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.Anchor;
import org.eclipse.gmf.runtime.notation.IdentityAnchor;
import org.eclipse.gmf.runtime.notation.View;

/*
 * @canBeSeenBy %partners
 */
/**
 * the shape node controller
 * @author mmostafa
 *
 */
public abstract class ShapeNodeEditPart
	extends ShapeEditPart
	implements INodeEditPart {

	/**
	 * constructor
	 * @param view the view controlled by this edit part
	 */
	public ShapeNodeEditPart(View view) {
		super(view);
	}

	protected List getModelSourceConnections() {
		return ViewUtil.getSourceConnections((View)getModel());
	}

	protected List getModelTargetConnections() {
		return ViewUtil.getTargetConnections((View)getModel());
	}

	protected void createDefaultEditPolicies() {
		// node edit policy needs to be installed before component editpolicy
		// from the super
		// since connections of a node need to be deleted before the node
		//installEditPolicy(EditPolicy.NODE_ROLE, new NodeEditPolicy());
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
			new GraphicalNodeEditPolicy());
		installEditPolicy(EditPolicyRoles.SORT_FILTER_ROLE,
			new SortFilterCompartmentItemsEditPolicy());
			installEditPolicy(EditPolicyRoles.CONNECTOR_HANDLES_ROLE,
				new ConnectorHandleEditPolicy());
	}

	protected final IFigure createFigure() {
		return createNodeFigure();
	}

	/**
	 * creates a node figure
	 * @return <code>NodeFigure</code>
	 */
	abstract protected NodeFigure createNodeFigure();

	/**
	 * getter for the node Figure
	 * @return <code>NodeFigure</code>
	 */
	protected NodeFigure getNodeFigure() {
		return (NodeFigure) getFigure();
	}

	/*
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connEditPart) {
		final ConnectionNodeEditPart connector = (ConnectionNodeEditPart) connEditPart;
		String t = (String) MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead(new MRunnable() {

			public Object run() {
				Anchor a = connector.getConnectorView().getSourceAnchor();
				if (a instanceof IdentityAnchor)
					return ((IdentityAnchor) a).getId();
				return ""; //$NON-NLS-1$
			}
		});
		return getNodeFigure().getConnectionAnchor(t);
	}

	/*
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		if (request instanceof ReconnectRequest) {
			Point pt = ((DropRequest) request).getLocation().getCopy();
			((ReconnectRequest) request).getConnectionEditPart().getFigure()
				.translateToAbsolute(pt);
			return getNodeFigure().getSourceConnectionAnchorAt(pt);
		}
		else if (request instanceof DropRequest){
			return getNodeFigure().getSourceConnectionAnchorAt(
				((DropRequest) request).getLocation());
		}
		return getNodeFigure().getSourceConnectionAnchorAt(null);
	}

	/*
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connEditPart) {
		final ConnectionNodeEditPart connector = (ConnectionNodeEditPart) connEditPart;
		String t = (String) MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead(new MRunnable() {

			public Object run() {
				Anchor a = connector.getConnectorView().getTargetAnchor();
				if (a instanceof IdentityAnchor)
					return ((IdentityAnchor) a).getId();
				return ""; //$NON-NLS-1$
			}
		});
		return getNodeFigure().getConnectionAnchor(t);
	}

	/*
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		if (request instanceof ReconnectRequest) {
			Point pt = ((DropRequest) request).getLocation().getCopy();
			((ReconnectRequest) request).getConnectionEditPart().getFigure()
				.translateToAbsolute(pt);
			return getNodeFigure().getTargetConnectionAnchorAt(pt);
		}
		else if (request instanceof DropRequest){
			return getNodeFigure().getTargetConnectionAnchorAt(
				((DropRequest) request).getLocation());
		}
		return getNodeFigure().getTargetConnectionAnchorAt(null);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart#mapConnectionAnchorToTerminal(ConnectionAnchor)
	 */
	final public String mapConnectionAnchorToTerminal(ConnectionAnchor c) {
		return getNodeFigure().getConnectionAnchorTerminal(c);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart#mapTerminalToConnectionAnchor(String)
	 */
	final public ConnectionAnchor mapTerminalToConnectionAnchor(String terminal) {
		return getNodeFigure().getConnectionAnchor(terminal);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent evt) {
		if (Properties.ID_SOURCECONNECTIONS.equals(evt.getPropertyName()))
			refreshSourceConnections();
		else if (Properties.ID_TARGETCONNECTIONS.equals(evt.getPropertyName()))
			refreshTargetConnections();
		else
			super.handlePropertyChangeEvent(evt);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
		super.activate();
		//
		// bad hack - check if really required by sequence diagram plugin.
		//	if (getParent() instanceof GraphicalEditPart &&
		// ((GraphicalEditPart)getParent()).isCanonical()){
		//		EditPolicy editPolicy =
		// getParent().getEditPolicy(EditPolicyRoles.CANONICAL_ROLE);
		//		if (editPolicy != null){
		//			editPolicy.deactivate();
		//			editPolicy.activate();
		//		}
		//	}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.INoteableEditPart#canAttachNote()
	 */
	public boolean canAttachNote() {
		return true;
	}

}

