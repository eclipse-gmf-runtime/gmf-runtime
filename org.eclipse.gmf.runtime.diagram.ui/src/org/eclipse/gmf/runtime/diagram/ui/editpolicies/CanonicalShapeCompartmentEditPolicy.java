/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies.CanonicalConnectorEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.notation.CanonicalStyle;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.DrawerStyle;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A specialized implementation of <code>CanonicalShapeEditPolicy</code>. This
 * implementation can only be used with
 * {@link org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart}.
 * This editpolicy will create, if necessary, both shape and connector views.
 * @author mhanner
 */
public abstract class CanonicalShapeCompartmentEditPolicy
		extends
			CanonicalConnectorEditPolicy {
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		Style style = ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getDrawerStyle());
		if ( style != null ) {
			addListenerFilter("NotationListener_DrawerStyle", this,style); //$NON-NLS-1$
		}
		style = ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getCanonicalStyle());
		if ( style != null ) {
			addListenerFilter("NotationListener_CanonicalStyle", this, style);  //$NON-NLS-1$
		}
		super.activate();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#deactivate()
	 */
	public void deactivate() {
		removeListenerFilter("NotationListener_DrawerStyle"); //$NON-NLS-1$
		removeListenerFilter("NotationListener_CanonicalStyle"); //$NON-NLS-1$
		super.deactivate();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#shouldHandleNotationEvent(org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent)
	 */
	protected boolean shouldHandleNotificationEvent( NotificationEvent event) {
	 	return NotationPackage.eINSTANCE.getDrawerStyle_Collapsed() == event.getFeature() || 
	 		   NotationPackage.eINSTANCE.getCanonicalStyle_Canonical() == event.getFeature() ||
	 		   super.shouldHandleNotificationEvent(event);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#handleNotificationEvent(org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent)
	 */
	protected void handleNotificationEvent( NotificationEvent event ) {
		if ( NotationPackage.eINSTANCE.getCanonicalStyle_Canonical() == event.getFeature() ) {
			CanonicalStyle style = (CanonicalStyle) ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getCanonicalStyle());
			if (style != null) {
				setEnable(style.isCanonical());
				refresh();
			}
		}
		else {
			super.handleNotificationEvent(event);
		}
	}

	/**
	 * Asserts that the supplied host is an {@link ShapeCompartmentEditPart}
	 * instance.
	 */
	public void setHost(EditPart host) {
		if ( !(host instanceof ShapeCompartmentEditPart) ) {
			throw new IllegalArgumentException();
		}
		super.setHost(host);
	}
	
	/** 
	 * Tests if the installed canonical editpolicy is installed and enabled and that
	 * the host shape compartment's collapsed style is disabled.
	 * @see #getCanonicalStyle()
	 * @return <tt>true</tt> if the style is enabled; otherwise the <tt>super</tt> implementation is called.
	 */
	public boolean isEnabled() {
		DrawerStyle dstyle = (DrawerStyle) ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getDrawerStyle());
		boolean isCollapsed = dstyle == null ? false : dstyle.isCollapsed();
		
		if ( isCollapsed ) {
			return false;
		}
		
		CanonicalStyle style = getCanonicalStyle();
		return style == null 
			? super.isEnabled()
			: style.isCanonical() && super.isEnabled();
	}
	
	/** Returns the canonical style that may be installed on the host shape compartment view. */
	/**
	 * gets the canonical style that may be installed on the host shape compartment view.
	 * @return <code>CanonicalStyle</code>
	 */
	protected CanonicalStyle getCanonicalStyle() {
		return (CanonicalStyle) ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getCanonicalStyle());
	}
	
	/**
	 * Return the list of connectors between elements contained within the host
	 * compartment.  
	 * Subclasses should override {@link #shouldIncludeConnector(Edge, List)} to
	 * modify the returned collection's contents.
	 * 
	 * @return list of <code>IConnectorView</code>s.
	 */
	protected Collection getConnectorViews() {
		Collection retval = new HashSet();
		List children = getViewChildren();
		Iterator connectors = getDiagramConnectors().iterator();
		while (connectors.hasNext()) {
			Edge connector = (Edge) connectors.next();
			if ( connector.getSource() != null
					&& connector.getTarget() != null
					&& shouldIncludeConnector(connector, children) ) {
				retval.add(connector);
			}
		}
		return retval;
	}
	
	/**
	 * Called by {@link #getConnectorViews()} to determine if the underlying shape 
	 * compartment is responsible for the supplied connector.  By default, the 
	 * following conditition must be met for the connector to be accepted:
	 * <UL>
	 * <LI> its source must not be null.
	 * <LI> its target must not be null.
	 * <LI> the shape compartment contains the source (or the source's container view).
	 * <LI> the shape compartment contains the target (or the target's container view).
	 * </LI>
	 * 
	 * @param connector the connector view
	 * @param children underlying shape compartment's children.
	 * @return <tt>false</tt> if supplied connector should be ignored; otherwise <tt>true</tt>.
	 */
	protected boolean shouldIncludeConnector( Edge connector, List children ) {
		View src = connector.getSource();
		View target = connector.getTarget();
		//
		// testing the src/tgt containerview in case the src/tgt are 
		// some type of gate view.
		return ( (src != null && target != null)
				&& (children.contains(src) 
						|| children.contains(src.eContainer())
						|| children.contains(target)
						|| children.contains(target.eContainer())));
	}
	/**
	 * Return {@link UnexecutableCommand} if the editpolicy is
	 * enabled and a {@link DropObjectsRequest} is passed as an 
	 * argument and its objects are contained in the list of
	 * semantic children.
	 */
	public Command getCommand(Request request) {
		if (understandsRequest(request)) {
			if ( isEnabled() && request instanceof DropObjectsRequest ) {
				return getDropCommand((DropObjectsRequest)request);
			}
		}
		return super.getCommand(request);
	}
	
	/**
	 * gets an <code>UnexecutableCommand</code> if the droprequest
	 * cannot be supported; the semantic host cannot contain the element
	 * being dropped or this editpolicy is enabled and
	 * it already contains of view for the elements being dropped. 
	 * @param request the request to use
	 * @return <code>Command</code>
	 */
	protected Command getDropCommand( DropObjectsRequest request ) {
		boolean enabled = isEnabled();
		List children = getSemanticChildrenList();
		Iterator dropElements = request.getObjects().iterator();
		while( dropElements.hasNext() ) {
			Object dropElement = dropElements.next();
			// Allow diagram links on Canonical shapes compartments
			if (dropElement instanceof Diagram) 
				continue;
			if ( dropElement instanceof EObject
					&& preventDropElement(dropElement)) {
					return UnexecutableCommand.INSTANCE;
			}
			boolean containsElement = children.contains(dropElement);
			if ( enabled ) {
				if ( containsElement || preventDropElement(dropElement)) {
					return UnexecutableCommand.INSTANCE;
				}
			}
		}
		return null;
	}


	/**
	 * Return <tt>false</tt> if the supplied element should be prevented
	 * from being dropped into this editpolicy's host; otherwise <tt>true</tt>.
	 * This method is called by {@link #getDropCommand(DropObjectsRequest)} if
	 * this editpolicy is enabled.
	 * @param dropElement object being dropped.
	 * @return <code>EObjectUtil.canContain(getSemanticHost(), ((EObject)dropElement).eClass(), false)</code>
	 * if the supplied elemnt is an <code>EObject</code>; otherwise <tt>false</tt>
	 */
	protected boolean preventDropElement( Object dropElement ) {
		return dropElement instanceof EObject
			? !EObjectUtil.canContain(getSemanticHost(), ((EObject)dropElement).eClass(), false)				
			: false;
	}

	/**
	 * Understands the following:
	 * <UL>
	 * <LI>{@link DropObjectsRequest}
	 * <LI>{@link RequestConstants#REQ_DROP_OBJECTS}
	 * <LI>{@link org.eclipse.gef.RequestConstants#REQ_CREATE}
	 * </UL>
	 */
	public boolean understandsRequest(Request req) {
		return (RequestConstants.REQ_DROP_OBJECTS.equals(req.getType())
			|| req instanceof DropObjectsRequest
			|| RequestConstants.REQ_CREATE.equals(req.getType()) )
			? true
			: super.understandsRequest(req);
	}
}
