/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editpolicies;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.TopGraphicEditPart;

/**
 * This editpolicy forwards all mouse events to the editpolicy installed on its
 * nearest parent. The editpolicy to delegate to must implement the
 * <code>MouseMotionListener</code> interface.
 * 
 * @author cmahoney
 */
public class DelegatingMouseEventsEditPolicy
	extends AbstractEditPolicy
	implements MouseMotionListener {

	/** The editpolicy role used to retrieve the editpolicy from the parent. */
	private final String editPolicyRole;

	/**
	 * Creates a new instance.
	 * 
	 * @param editPolicyRole
	 *            The editpolicy role used to retrieve the editpolicy from the
	 *            parent.
	 */
	public DelegatingMouseEventsEditPolicy(String editPolicyRole) {
		super();
		this.editPolicyRole = editPolicyRole;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		super.activate();

		((IGraphicalEditPart) getHost()).getFigure().addMouseMotionListener(
			this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#deactivate()
	 */
	public void deactivate() {
		((IGraphicalEditPart) getHost()).getFigure().removeMouseMotionListener(
			this);

		super.deactivate();
	}

	/**
	 * Traverses the parent editpart hierarchy to find the first parent with an
	 * editpolicy of the appropriate role installed that implements the
	 * <code>MouseMotionListener</code> interface. Stops if a parent is a
	 * <code>TopGraphicEditPart</code>.
	 * 
	 * @return the parent editpolicy role or null if none was found
	 */
	private MouseMotionListener getParentEditPolicy() {
		EditPart parentEP = getHost().getParent();
		while (parentEP != null) {
			EditPolicy editPolicy = parentEP.getEditPolicy(editPolicyRole);
			if (editPolicy != null && editPolicy instanceof MouseMotionListener) {
				return (MouseMotionListener) editPolicy;
			}
			if (parentEP instanceof TopGraphicEditPart) {
				return null;
			}
			parentEP = parentEP.getParent();
		}
		return null;
	}

	/**
	 * Forwards this mouse event to the first parent of the host editpart that
	 * has the applicable editpolicy installed.
	 */
	public void mouseEntered(MouseEvent me) {
		MouseMotionListener parentPolicy = getParentEditPolicy();
		if (parentPolicy != null) {
			parentPolicy.mouseEntered(me);
		}
	}

	/**
	 * Forwards this mouse event to the first parent of the host editpart that
	 * has the applicable editpolicy installed.
	 */
	public void mouseExited(MouseEvent me) {
		MouseMotionListener parentPolicy = getParentEditPolicy();
		if (parentPolicy != null) {
			parentPolicy.mouseExited(me);
		}
	}

	/**
	 * Forwards this mouse event to the first parent of the host editpart that
	 * has the applicable editpolicy installed.
	 */
	public void mouseHover(MouseEvent me) {
		MouseMotionListener parentPolicy = getParentEditPolicy();
		if (parentPolicy != null) {
			parentPolicy.mouseHover(me);
		}
	}

	/**
	 * Forwards this mouse event to the first parent of the host editpart that
	 * has the applicable editpolicy installed.
	 */
	public void mouseMoved(MouseEvent me) {
		MouseMotionListener parentPolicy = getParentEditPolicy();
		if (parentPolicy != null) {
			parentPolicy.mouseMoved(me);
		}
	}

	/**
	 * Forwards this mouse event to the first parent of the host editpart that
	 * has the applicable editpolicy installed.
	 */
	public void mouseDragged(MouseEvent me) {
		MouseMotionListener parentPolicy = getParentEditPolicy();
		if (parentPolicy != null) {
			parentPolicy.mouseDragged(me);
		}
	}

}
