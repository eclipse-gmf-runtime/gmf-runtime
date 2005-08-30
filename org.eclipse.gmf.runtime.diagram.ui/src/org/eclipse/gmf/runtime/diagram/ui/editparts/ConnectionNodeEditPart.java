/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.DropRequest;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.notation.Anchor;
import org.eclipse.gmf.runtime.notation.IdentityAnchor;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/*
 * @canBeSeenBy %partners
 */
/**
 * Connection Node EditPart, a specialized Connection EditPart that installs 
 * a <code>ditPolicy.GRAPHICAL_NODE_ROLE</code> on this edit part. it also implements
 * INodeEditPart which defines the connectable edit parts
 * @author mmostafa
 */
abstract public class ConnectionNodeEditPart
	extends ConnectionEditPart
	implements INodeEditPart {

	/**
	 * constructor 
	 * @param view owned view by this edit part 
	 */
	public ConnectionNodeEditPart(View view) {
		super(view);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#createDefaultEditPolicies()
	 */
	protected void createDefaultEditPolicies() {
		// node edit policy needs to be installed before connection editpolicy from the super
		// since connections of a node need to be deleted before the node
		//installEditPolicy(EditPolicy.NODE_ROLE, new NodeEditPolicy());
		super.createDefaultEditPolicies();
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GraphicalNodeEditPolicy());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected List getModelSourceConnections(){
		return ViewUtil.getSourceConnections(getConnectorView());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	protected List getModelTargetConnections(){
		return ViewUtil.getTargetConnections(getConnectorView());
	}

	protected ConnectionAnchor getSourceConnectionAnchor() {
		if (getSource() != null && getSource() instanceof NodeEditPart) {
			NodeEditPart editPart = (NodeEditPart) getSource();
			return editPart.getSourceConnectionAnchor(this);
		}
		return super.getSourceConnectionAnchor();
	}

	/*
	 * @see NodeEditPart#getSourceConnectionAnchor(ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart connEditPart) {
		final ConnectionNodeEditPart connector = (ConnectionNodeEditPart) connEditPart;
		String t = (String) MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead( new MRunnable() {
			public Object run() {
				Anchor a = connector.getConnectorView().getSourceAnchor();
				if (a instanceof IdentityAnchor)
					return ((IdentityAnchor) a).getId();
				return ""; //$NON-NLS-1$
			}
		});
		return ((PolylineConnectionEx)getFigure()).getConnectionAnchor(t);
	}

	/*
	 * @see NodeEditPart#getSourceConnectionAnchor(Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		Point center = getFigure().getBounds().getCenter();
		Point pt = ((DropRequest)request).getLocation()==null ? 
			center : new Point(((DropRequest)request).getLocation()); 
		if (request instanceof CreateRequest) {
			getFigure().translateToRelative(pt);
		}
		return ((PolylineConnectionEx)getFigure()).getSourceConnectionAnchorAt(pt);
	}

	protected ConnectionAnchor getTargetConnectionAnchor() {
		if (getTarget() instanceof NodeEditPart) {
			NodeEditPart editPart = (NodeEditPart) getTarget();
			return editPart.getTargetConnectionAnchor(this);
		}
		return super.getTargetConnectionAnchor();
	}

	/*
	 * @see NodeEditPart#getTargetConnectionAnchor(ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart connEditPart) {
		final ConnectionNodeEditPart connector = (ConnectionNodeEditPart) connEditPart;
		String t = (String) MEditingDomainGetter.getMEditingDomain((View)getModel()).runAsRead( new MRunnable() {
			public Object run() {
				Anchor a = connector.getConnectorView().getTargetAnchor();
				if (a instanceof IdentityAnchor)
					return ((IdentityAnchor) a).getId();
				return ""; //$NON-NLS-1$
			}
		});
		return ((PolylineConnectionEx)getFigure()).getConnectionAnchor(t);
	}
	
	/*
	 * @see NodeEditPart#getTargetConnectionAnchor(Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		Point center = getFigure().getBounds().getCenter();
		Point pt = ((DropRequest)request).getLocation()==null ? 
			center : new Point(((DropRequest)request).getLocation()); 
		if (request instanceof CreateRequest) {
			getFigure().translateToRelative(pt);
		}
		return ((PolylineConnectionEx)getFigure()).getTargetConnectionAnchorAt(pt);
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart#mapConnectionAnchorToTerminal(org.eclipse.draw2d.ConnectionAnchor)
     */
    final public String mapConnectionAnchorToTerminal(ConnectionAnchor c) {
        return ((PolylineConnectionEx) getFigure()).getConnectionAnchorTerminal(c);
    }

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart#mapTerminalToConnectionAnchor(String)
	 */
	final public ConnectionAnchor mapTerminalToConnectionAnchor(String terminal) {
		return ((PolylineConnectionEx) getFigure()).getConnectionAnchor(terminal);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#handlePropertyChangeEvent(java.beans.PropertyChangeEvent)
	 */
	protected void handlePropertyChangeEvent(PropertyChangeEvent event) {
		if (Properties.ID_SOURCECONNECTIONS.equals(event.getPropertyName()))
			refreshSourceConnections();
		else
		if (Properties.ID_TARGETCONNECTIONS.equals(event.getPropertyName()))
			refreshTargetConnections();
		else
			super.handlePropertyChangeEvent(event);

        Object element = MetaModelUtil.getElement(event.getPropertyName());
        if (element != null && element.equals(NotationPackage.eINSTANCE.getIdentityAnchor_Id())) {
        	anchorChange();
        }
	}
		
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.INoteableEditPart#canAttachNote()
	 */
	public boolean canAttachNote() {		
		return true;
	}

	/*
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart#handleNotificationEvent(org.eclipse.gmf.runtime.diagram.ui.internal.listener.NotificationEvent)
	 */
	protected void handleNotificationEvent(NotificationEvent e) {
		super.handleNotificationEvent(e);
		Notification event = e.getNotification();
		if (event.getNewValue() instanceof IdentityAnchor ||
	 			event.getOldValue() instanceof IdentityAnchor){
	   		anchorChange();
	    }
	}

    /**
     * updates identity connection anchors
     */
	public void anchorChange() {
		refreshSourceAnchor();
		refreshTargetAnchor();
	}
}
