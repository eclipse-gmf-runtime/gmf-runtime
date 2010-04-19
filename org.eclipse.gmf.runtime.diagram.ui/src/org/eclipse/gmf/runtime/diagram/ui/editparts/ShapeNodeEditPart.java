/******************************************************************************
 * Copyright (c) 2002, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConnectionHandleEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.SortFilterCompartmentItemsEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.NoteAttachmentReorientEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.Anchor;
import org.eclipse.gmf.runtime.notation.FillStyle;
import org.eclipse.gmf.runtime.notation.IdentityAnchor;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.GradientData;
import org.eclipse.swt.graphics.Color;

/**
 * The shape node controller
 * @author mmostafa
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
        return ViewUtil.getSourceConnectionsConnectingVisibleViews((View)getModel());
	}

	protected List getModelTargetConnections() {
        return ViewUtil.getTargetConnectionsConnectingVisibleViews((View)getModel());
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
			installEditPolicy(EditPolicyRoles.CONNECTION_HANDLES_ROLE,
				new ConnectionHandleEditPolicy());

        // Disable note attachment reorient between two shapes where neither is a note.
        installEditPolicy("NoteAttachmentReorient", //$NON-NLS-1$
            new NoteAttachmentReorientEditPolicy());
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
		final org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart connection = 
            (org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart)connEditPart;
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
		return getNodeFigure().getConnectionAnchor(t);
	}

	/*
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		if (request instanceof ReconnectRequest) {
			if (((DropRequest) request).getLocation() == null) {
				return getNodeFigure().getSourceConnectionAnchorAt(null);
			}
			Point pt = ((DropRequest) request).getLocation().getCopy();
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
        final org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart connection = 
            (org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart)connEditPart;
        
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

		return getNodeFigure().getConnectionAnchor(t);
	}

	/*
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		if (request instanceof ReconnectRequest) {
			if (((DropRequest) request).getLocation() == null) {
				return getNodeFigure().getTargetConnectionAnchorAt(null);
			}
			Point pt = ((DropRequest) request).getLocation().getCopy();
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
	protected void handleNotificationEvent(Notification notification) {
		Object feature = notification.getFeature();
		if (NotationPackage.eINSTANCE.getView_SourceEdges().equals(feature))
			refreshSourceConnections();
		else if (NotationPackage.eINSTANCE.getView_TargetEdges().equals(feature))
			refreshTargetConnections();
		else if (NotationPackage.eINSTANCE.getFillStyle_Gradient().equals(feature) ||
				NotationPackage.eINSTANCE.getFillStyle_FillColor().equals(feature)) {				
			refreshBackgroundColor();
		} else if (NotationPackage.eINSTANCE.getFillStyle_Transparency().equals(feature)) {
			refreshTransparency();			
		} else {
			super.handleNotificationEvent(notification);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.editparts.INoteableEditPart#canAttachNote()
	 */
	public boolean canAttachNote() {
		return true;
	}
	
	/**
	 * Refresh the editpart's figure background color. Background color can be gradient, if
	 * supported.
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#refreshBackgroundColor()
	 * @since 1.2
	 */
    protected void refreshBackgroundColor() {
        FillStyle style = (FillStyle)getPrimaryView().getStyle(NotationPackage.Literals.FILL_STYLE);
        if ( style != null ) {
        	if (style.getGradient() == null || !supportsGradient()) { 
        		setBackgroundColor(DiagramColorRegistry.getInstance().getColor(new Integer(style.getFillColor())));
        	} else {
        		setGradient(style.getGradient());
        	}        	
        }
    }
    
    /**
     * Refresh figure's background transparency.
     * @since 1.2
     */
    protected void refreshTransparency() {
        FillStyle style = (FillStyle)getPrimaryView().getStyle(NotationPackage.Literals.FILL_STYLE);
        if ( style != null ) {    	
        	setTransparency(style.getTransparency());
        }
    }
    
	/**
	 * Sets the figure's background color, and also indicates to the figure that
	 * gradient should not be used.
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart#setBackgroundColor(org.eclipse.swt.graphics.Color)
	 * @since 1.2
	 */
	protected void setBackgroundColor(Color c) {
		NodeFigure fig = getNodeFigure();
		fig.setBackgroundColor(c);
		fig.setIsUsingGradient(false);
		fig.setGradientData(-1, -1, 0);
	}
    
    /**
 	 * Sets the figure's gradient data, and also indicates to the figure that
	 * gradient should be used for fill.
     * 
     * @param gradient GradientData object
     * @since 1.2
     */
    protected void setGradient(GradientData gradient) {
    	NodeFigure fig = getNodeFigure();
    	if (gradient != null) {    		    		
    		fig.setIsUsingGradient(true);
    		fig.setGradientData(gradient.getGradientColor1(), gradient.getGradientColor2(), gradient.getGradientStyle()); 		
    	} else {
    		fig.setIsUsingGradient(false);
    	}
    }
    
	/**
	 * Sets the figures background transparency. Value of -1 indicates that
	 * transparency is not supported and will not have effect.
	 * 
	 * @param transp
	 * @since 1.2
	 */
    protected void setTransparency(int transp) {
		NodeFigure fig = getNodeFigure();
		fig.setTransparency(transp);
    }
    
	/**
	 * Returns true if gradient is supported, false, otherwise. By default, this
	 * method returns false. In order for gradient to be fully supported, figure
	 * must support it in addition to the EditPart support. Clients who do
	 * support gradient fully should override this method to return true. This
	 * method can be used by UI as an indication if gradient should be available or
	 * not.
	 * 
	 * @return true if gradient is supported, false otherwise
	 * @since 1.2
	 */
	public boolean supportsGradient() {
		return false;
	}    

	/**
	 * Returns true if rounded corners is supported, false, otherwise. By
	 * default, this method returns false. Clients who do support rounded
	 * corners should override this method to return true. This method can be
	 * used by UI as an indication if rounded corners should be available or
	 * not. Note that rounded corners style is part of line style, meaning that
	 * most or all shapes will have it installed (but not all of them will support it) 
	 * 
	 * @return true if rounded corners style is supported, false otherwise
	 * @since 1.4
	 */	
	public boolean supportsRoundedCorners() {
		return false;
	}
}

