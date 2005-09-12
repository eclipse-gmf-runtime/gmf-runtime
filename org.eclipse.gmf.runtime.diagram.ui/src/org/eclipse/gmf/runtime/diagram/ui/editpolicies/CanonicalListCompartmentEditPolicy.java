/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.notation.DrawerStyle;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;

/**
 * A specialized implementation of <code>CanonicalShapeEditPolicy</code> This
 * implementation can only be used with
 * {@link org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart}.
 * 
 * This editpolicy will create, if necessary, list compartment items.
 * 
 * @author choang
 */
public abstract class CanonicalListCompartmentEditPolicy
	extends CanonicalEditPolicy {
	
	/** Asserts that the supplied host is an {@link ListCompartmentEditPart} instance. */
	public void setHost(EditPart host) {
		if ( !(host instanceof ListCompartmentEditPart) ) {
			throw new IllegalArgumentException();
		}
		super.setHost(host);
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		DrawerStyle dstyle = (DrawerStyle)((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getDrawerStyle());
		if ( dstyle != null ) {
			// add listener to host view (handle case when user changes collapsing property)
			addListenerFilter("NotationListener_Collapsed", this, dstyle); //$NON-NLS-1$
		}

		super.activate();
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPolicy#deactivate()
	 */
	public void deactivate() {
		removeListenerFilter("NotationListener_Collapsed"); //$NON-NLS-1$
		super.deactivate();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#shouldHandleNotificationEvent(org.eclipse.gmf.runtime.diagram.core.listener.NotificationEvent)
	 */
	protected boolean shouldHandleNotificationEvent(NotificationEvent event) {
	 	return NotationPackage.eINSTANCE.getDrawerStyle_Collapsed() == event.getFeature() || super.shouldHandleNotificationEvent(event);
	}

	/** 
	 * Return <tt>false</tt> if the host ListCompartmentEditPart is collapsed otherwise
	 * returns the super implementation.
	 */
	public boolean isEnabled() {
		DrawerStyle dstyle = (DrawerStyle) ((View)host().getModel()).getStyle(NotationPackage.eINSTANCE.getDrawerStyle());
		boolean isCollapsed = dstyle == null ? false : dstyle.isCollapsed();
		
		if ( isCollapsed ) {
			return false;
		}
		
		return super.isEnabled();
	}
}
