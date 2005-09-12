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

package org.eclipse.gmf.runtime.gef.ui.internal.tools;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.swt.events.MouseEvent;

/**
 * A drag tracker that allows a delegating editpart to be selected while
 * another (delegate) editpart to be dragable.
 * 
 * This class subclasses <code>SelectionTool</code> since it already implements
 * the delegating pattern to a drag tracker.
 * 
 * @author melaasar
 */
public class DelegatingDragEditPartsTracker
	extends SelectionTool
	implements DragTracker {

	/**
	 * The delegating editpart (the selectable)
	 */
	private EditPart delegatingEditPart;
	/**
	 * The delegate editpart (the dragable)
	 */
	private EditPart delegateEditPart;
	/**
	 * The initial mouse event upon dragging
	 */
	private MouseEvent initialME;

	/**
	 * Creates an instance of the delegating drag editparts tracker
	 * 
	 * @param delegatingEditPart the <code>EditPart</code> that the selection gets delegated to
	 * @param delegateEditPart the <code>EditPart</code> that the drag gets delegated to.
	 */
	public DelegatingDragEditPartsTracker(
		EditPart delegatingEditPart,
		EditPart delegateEditPart) {
		this.delegatingEditPart = delegatingEditPart;
		this.delegateEditPart = delegateEditPart;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonDown(int)
	 */
	protected boolean handleButtonDown(int button) {
		setDragTracker(new SelectEditPartTracker(delegatingEditPart));
		lockTargetEditPart(delegatingEditPart);
		return true;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gef.Tool#mouseDown(org.eclipse.swt.events.MouseEvent, org.eclipse.gef.EditPartViewer)
	 */
	public void mouseDown(MouseEvent e, EditPartViewer viewer) {
		initialME = e;
		super.mouseDown(e, viewer);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#handleDragStarted()
	 */
	protected boolean handleDragStarted() {
		DragTracker tracker = delegateEditPart.getDragTracker(getTargetRequest());
		if (tracker != null){
			setDragTracker(tracker);
			lockTargetEditPart(delegateEditPart);
			tracker.mouseDown(initialME, getCurrentViewer());
		}
		return super.handleDragStarted();
	}

}
