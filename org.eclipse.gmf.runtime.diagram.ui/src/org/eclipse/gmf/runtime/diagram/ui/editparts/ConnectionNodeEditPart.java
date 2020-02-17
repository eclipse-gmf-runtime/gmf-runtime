/******************************************************************************
 * Copyright (c) 2002, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.NoteAttachmentReorientEditPolicy;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IAnchorableFigure;
import org.eclipse.gmf.runtime.notation.Anchor;
import org.eclipse.gmf.runtime.notation.IdentityAnchor;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

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

        // Disable note attachment reorient between two shapes where neither is a note.
        installEditPolicy("NoteAttachmentReorient", //$NON-NLS-1$
            new NoteAttachmentReorientEditPolicy());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected List getModelSourceConnections(){
		return ViewUtil.getSourceConnections(getEdge());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	protected List getModelTargetConnections(){
		return ViewUtil.getTargetConnections(getEdge());
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
		final ConnectionNodeEditPart connection = (ConnectionNodeEditPart) connEditPart;
		String t = ""; //$NON-NLS-1$
		try {
			t = (String) getEditingDomain().runExclusive(
				new RunnableWithResult.Impl() {

				public void run() {
					Anchor a = connection.getEdge().getSourceAnchor();
					if (a instanceof IdentityAnchor)
						setResult(((IdentityAnchor) a).getId());
					else 
					    setResult(""); //$NON-NLS-1$
				}
			});
		} catch (InterruptedException e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"getSourceConnectionAnchor", e); //$NON-NLS-1$
			Log.error(DiagramUIPlugin.getInstance(),
				DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
				"getSourceConnectionAnchor", e); //$NON-NLS-1$
		}
		return ((IAnchorableFigure)getFigure()).getConnectionAnchor(t);
	}

	/*
	 * @see NodeEditPart#getSourceConnectionAnchor(Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		Point center = getFigure().getBounds().getCenter();
		getFigure().translateToAbsolute(center);
		Point pt = ((DropRequest)request).getLocation()==null ? 
			center : new Point(((DropRequest)request).getLocation()); 
		if (request instanceof CreateRequest) {
			getFigure().translateToRelative(pt);
		}
		return ((IAnchorableFigure)getFigure()).getSourceConnectionAnchorAt(pt);
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
		final ConnectionNodeEditPart connection = (ConnectionNodeEditPart) connEditPart;
		String t = ""; //$NON-NLS-1$
		try {
			t = (String) getEditingDomain().runExclusive(
				new RunnableWithResult.Impl() {

				public void run() {
					Anchor a = connection.getEdge().getTargetAnchor();
					if (a instanceof IdentityAnchor)
						setResult(((IdentityAnchor) a).getId());
					else 
					    setResult(""); //$NON-NLS-1$
				}
			});
		} catch (InterruptedException e) {
			Trace.catching(DiagramUIPlugin.getInstance(),
				DiagramUIDebugOptions.EXCEPTIONS_CATCHING, getClass(),
				"getTargetConnectionAnchor", e); //$NON-NLS-1$
			Log.error(DiagramUIPlugin.getInstance(),
				DiagramUIStatusCodes.IGNORED_EXCEPTION_WARNING,
				"getTargetConnectionAnchor", e); //$NON-NLS-1$
		}
		return ((IAnchorableFigure)getFigure()).getConnectionAnchor(t);
	}
	
	/*
	 * @see NodeEditPart#getTargetConnectionAnchor(Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		Point center = getFigure().getBounds().getCenter();
		getFigure().translateToAbsolute(center);
		Point pt = ((DropRequest)request).getLocation()==null ? 
			center : new Point(((DropRequest)request).getLocation()); 
		if (request instanceof CreateRequest) {
			getFigure().translateToRelative(pt);
		}
		return ((IAnchorableFigure)getFigure()).getTargetConnectionAnchorAt(pt);
	}
	
    /* (non-Javadoc)
     * @see org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart#mapConnectionAnchorToTerminal(org.eclipse.draw2d.ConnectionAnchor)
     */
    final public String mapConnectionAnchorToTerminal(ConnectionAnchor c) {
        return ((IAnchorableFigure) getFigure()).getConnectionAnchorTerminal(c);
    }

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart#mapTerminalToConnectionAnchor(String)
	 */
	final public ConnectionAnchor mapTerminalToConnectionAnchor(String terminal) {
		return ((IAnchorableFigure) getFigure()).getConnectionAnchor(terminal);
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
	protected void handleNotificationEvent(Notification notification) {
		Object feature = notification.getFeature();
		if (NotationPackage.eINSTANCE.getView_SourceEdges().equals(feature))
			refreshSourceConnections();
		else
		if (NotationPackage.eINSTANCE.getView_TargetEdges().equals(feature))
			refreshTargetConnections();
		else
			super.handleNotificationEvent(notification);

        if (  NotationPackage.eINSTANCE.getIdentityAnchor_Id().equals(feature) ||
        	  notification.getNewValue() instanceof IdentityAnchor ||
        	  notification.getOldValue() instanceof IdentityAnchor) {
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
    
    /**
     * Retrieve the list of all source and target connections for the connection.
     * @param set HashSet to add the connections to.
     * @param connectionEditPart the connection edit part.
     */
    private void getSourceAndTargetConnections(HashSet set, 
            org.eclipse.gef.ConnectionEditPart connectionEditPart) {
        
        if (connectionEditPart == null || set == null)
            return;
            
        for (Iterator i = connectionEditPart.getSourceConnections().iterator(); 
        i.hasNext();) {
            
            org.eclipse.gef.ConnectionEditPart next = 
                (org.eclipse.gef.ConnectionEditPart) i.next();
            Connection sourceConnection = (Connection) next.getFigure();
            set.add(sourceConnection);
            getSourceAndTargetConnections(set, next);
        }

        for (Iterator i = connectionEditPart.getTargetConnections().iterator(); 
        i.hasNext();) {
            
            org.eclipse.gef.ConnectionEditPart next = 
                (org.eclipse.gef.ConnectionEditPart) i.next();
            Connection targetConnection = (Connection) next.getFigure();
            set.add(targetConnection);
            getSourceAndTargetConnections(set, next);
        }
    }
    
    /**
     * Figure out if a cyclic dependency will arise if target connection edit part
     * is connected to the source connection edit part.
     * @param targetCEP the target connection edit part
     * @param sourceCEP the source connection edit part
     * @param checkSourceAndTargetEditParts check both the source and taret edit parts 
     * for cyclic dependencies
     * @param doNotCheckSourceEditPart (if checkSourceAndTargetEditParts is false) check 
     * only the target edit part if true, otherwise check only the source edit part
     * @return true if a cyclic dependency would be create when targetCEP and 
     * sourceCEP were to be connected, false otherwise.  
     */
    private boolean isCyclicConnectionRequest(org.eclipse.gef.ConnectionEditPart targetCEP,
            org.eclipse.gef.ConnectionEditPart sourceCEP, 
            boolean checkSourceAndTargetEditParts, boolean doNotCheckSourceEditPart) {
        
        if (targetCEP == null || sourceCEP == null)
            return false;
        
        if (sourceCEP == targetCEP)
        	return true;
        
        // first, do a cyclic check on source and target connections 
        // of the source connection itself.
        // (as every connection is also a node).
        
        HashSet set = new HashSet();
        getSourceAndTargetConnections(set, sourceCEP);
        if (set.contains(targetCEP.getFigure()))
            return true;
        
        
        // now do the cyclic check on the source and target of the source connection...  
        EditPart sourceEP = sourceCEP.getSource(),
                 targetEP = sourceCEP.getTarget();
                 
        if ((sourceEP == targetCEP) || (targetEP == targetCEP)) {
            return true;
        }
        else {
            
            if (!checkSourceAndTargetEditParts && doNotCheckSourceEditPart) {
                // .
            }
            else
                if (sourceEP instanceof org.eclipse.gef.ConnectionEditPart && 
                        isCyclicConnectionRequest(targetCEP, 
                            (org.eclipse.gef.ConnectionEditPart)sourceEP,
                            true, doNotCheckSourceEditPart))
                        return true;
            
            if (!checkSourceAndTargetEditParts && !doNotCheckSourceEditPart) {
                // .
            }
            else
                if (targetEP instanceof org.eclipse.gef.ConnectionEditPart &&
                     isCyclicConnectionRequest(targetCEP, 
                         (org.eclipse.gef.ConnectionEditPart)targetEP,
                         true, doNotCheckSourceEditPart))
                return true;
        }
        
        return false;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.gef.editparts.AbstractEditPart#getTargetEditPart(org.eclipse.gef.Request)
     */
    public EditPart getTargetEditPart(Request request) {
        EditPart ep = super.getTargetEditPart(request);
        
        if (ep != null && ep instanceof org.eclipse.gef.ConnectionEditPart) {
            if (request instanceof ReconnectRequest) {
                ReconnectRequest rRequest = (ReconnectRequest)request; 
                
                // If this is just moving an anchor point on the same target or
                // source, then it is fine.  See bugzilla# 208408. 
                if (rRequest.isMovingStartAnchor()) {
                    if (rRequest.getConnectionEditPart().getSource() == ep) {
                        return ep;
                    } 
                } else if (rRequest.getConnectionEditPart().getTarget() == ep) {
                    return ep;
                }
                
                // If source anchor is moved, the connection's source edit part
                // should not be taken into account for a cyclic dependency
                // check so as to avoid false checks. Same goes for the target
                // anchor. See bugzilla# 155243 -- we do not want to target a
                // connection that is already connected to us so that we do not
                // introduce a cyclic connection                
                if (isCyclicConnectionRequest((org.eclipse.gef.ConnectionEditPart)ep, 
                    rRequest.getConnectionEditPart(), false, rRequest.isMovingStartAnchor()))
                    return null;
            }
        }
        
        return ep;
    }
}
