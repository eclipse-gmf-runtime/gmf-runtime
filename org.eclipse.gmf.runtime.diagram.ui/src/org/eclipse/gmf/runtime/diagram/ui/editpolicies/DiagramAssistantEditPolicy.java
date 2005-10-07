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

package org.eclipse.gmf.runtime.diagram.ui.editpolicies;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.swt.widgets.Display;

/**
 * Encapsulates behavior common to editpolicies that popup diagram assistants.
 * 
 * @author cmahoney
 */
public abstract class DiagramAssistantEditPolicy
	extends GraphicalEditPolicy
	implements MouseMotionListener {

	/**
	 * The <code>Runnable</code> used when a timer is started to show the
	 * diagram assistant after a certain amount of time has passed.
	 */
	private class ShowDiagramAssistantRunnable
		implements Runnable {

		/** the mouse location when the timer was started */
		private Point originalMouseLocation;

		/**
		 * Creates a new instance.
		 * 
		 * @param originalMouseLocation
		 *            the current mouse location
		 */
		protected ShowDiagramAssistantRunnable(Point originalMouseLocation) {
			this.originalMouseLocation = originalMouseLocation;
		}

		/**
		 * The diagram assistant added when this task is run if the mouse is
		 * still at the same spot where it was when the timer was started (i.e.
		 * only add the diagram assistant when the user stops moving the mouse).
		 */
		public void run() {
			if (originalMouseLocation.equals(getMouseLocation())) {
				if (isDiagramAssistantShowing()
					&& !shouldAvoidHidingDiagramAssistant()) {
					hideDiagramAssistant();
				}
				showDiagramAssistant(originalMouseLocation);
			}
		}
	}

	/**
	 * The <code>Runnable</code> used when a timer is started to hide the
	 * diagram assistant after a certain amount of time has passed.
	 */
	private Runnable hideDiagramAssistantRunnable = new Runnable() {

		/**
		 * The diagram assistant is removed when this task is run if the mouse
		 * is still outside the shape.
		 */
		public void run() {
			if (getMouseLocation() == null
				|| !shouldAvoidHidingDiagramAssistant()) {
				hideDiagramAssistant();
			}
		}
	};
	
	/**
	 * Listens to the focus events on the owner editpart so that the diagram
	 * assistant can be added when the space bar is pressed. I tried to use
	 * IFigure.addFocusListener(), but the figure isn't getting any focus change
	 * events when the space bar is pressed.
	 */
	private class FocusListener
		extends EditPartListener.Stub {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.EditPartListener#selectedStateChanged(org.eclipse.gef.EditPart)
		 */
		public void selectedStateChanged(EditPart part) {
			if (part.hasFocus()) {
				showDiagramAssistant(getMouseLocation());
			} else {
				hideDiagramAssistant();
			}
		}
	}

	/**
	 * The amount of time to wait before showing the diagram assistant.
	 */
	private static final int APPEARANCE_DELAY = 200;

	/**
	 * The amount of time to wait before hiding the diagram assistant after it
	 * has been made visible.
	 */
	private static final int DISAPPEARANCE_DELAY = 2000;

	/**
	 * The amount of time to wait before hiding the diagram assistant after the
	 * user has moved the mouse outside of the editpart.
	 */
	private static final int DISAPPEARANCE_DELAY_UPON_EXIT = 1000;

	/**
	 * The current mouse location within the host used to determine where the
	 * diagram assistant should be displayed. This will be null if the mouse is
	 * outside the host and diagram assistant figure.
	 */
	private Point mouseLocation;

	/** Listens to focus change events on the host editpart. */
	private FocusListener focusListener = new FocusListener();

	/** Flag to indicate that the diagram assistant should not be hidden. */
	private boolean avoidHidingDiagramAssistant = true;

	/**
	 * Creates a new instance.
	 */
	public DiagramAssistantEditPolicy() {
		super();
	}

	/**
	 * Checks if the object is or is part of the diagram assistant figure. This
	 * is used to determine if the mouse is hovering over the diagram assistant.
	 * 
	 * @param object
	 *            the object in question
	 * @return True if the object in question is or is part of the diagram
	 *         assistant figure; false otherwise.
	 */
	protected abstract boolean isDiagramAssistant(Object object);

	/**
	 * Returns true if the diagram assistant is currently showing; false
	 * otherwise. This is used to determine if the diagram assistant should be
	 * shown or hidden at a given point in time.
	 * 
	 * @return true if the diagram assistant is showing; false otherwise
	 */
	protected abstract boolean isDiagramAssistantShowing();

	/**
	 * Shows the diagram assistant figure(s).
	 * 
	 * @param referencePoint
	 *            The reference point which may be used to determine where the
	 *            diagram assistant should be located. This is most likely the
	 *            current mouse location. This could be null, for example, when
	 *            the host gains focus via the space bar, in which case the
	 *            diagram assistant should be shown in a default location.
	 */
	protected abstract void showDiagramAssistant(Point referencePoint);

	/**
	 * Hides the diagram assistant figure(s).
	 */
	protected abstract void hideDiagramAssistant();

	/**
	 * Shows the diagram assistant after a certain amount of time has passed.
	 * 
	 * @param delay
	 *            the delay in milliseconds
	 */
	protected void showDiagramAssistantAfterDelay(int delay) {
		Display.getCurrent().timerExec(delay,
			new ShowDiagramAssistantRunnable(getMouseLocation()));
	}

	/**
	 * Hides the diagram assistant after a certain amount of time has passed.
	 * 
	 * @param delay
	 *            the delay in milliseconds
	 */
	protected void hideDiagramAssistantAfterDelay(int delay) {
		if (isDiagramAssistantShowing()) {
			Display.getCurrent().timerExec(delay, hideDiagramAssistantRunnable);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.AbstractEditPolicy#activate()
	 */
	public void activate() {
		super.activate();
		((IGraphicalEditPart) getHost()).getFigure().addMouseMotionListener(
			this);
		((IGraphicalEditPart) getHost()).addEditPartListener(focusListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.AbstractEditPolicy#deactivate()
	 */
	public void deactivate() {
		((IGraphicalEditPart) getHost()).getFigure().removeMouseMotionListener(
			this);
		((IGraphicalEditPart) getHost()).removeEditPartListener(focusListener);

		hideDiagramAssistant();

		super.deactivate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseEntered(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseEntered(MouseEvent me) {
		setMouseLocation(me.getLocation());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseExited(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseExited(MouseEvent me) {
		setMouseLocation(null);
		hideDiagramAssistantAfterDelay(getDisappearanceDelayUponExit());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseMoved(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseMoved(MouseEvent me) {
		setMouseLocation(me.getLocation());

		// do not hide the diagram assistant if the user is hovering over it
		setAvoidHidingDiagramAssistant(isDiagramAssistant(me.getSource()));

		showDiagramAssistantAfterDelay(getAppearanceDelay());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseHover(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseHover(MouseEvent me) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.MouseMotionListener#mouseDragged(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseDragged(MouseEvent me) {
		// do nothing
	}

	/**
	 * Gets the amount of time to wait before showing the diagram assistant.
	 * 
	 * @return the time to wait in milliseconds
	 */
	protected int getAppearanceDelay() {
		return APPEARANCE_DELAY;
	}

	/**
	 * Gets the amount of time to wait before hiding the diagram assistant after
	 * it has been made visible.
	 * 
	 * @return the time to wait in milliseconds
	 */
	protected int getDisappearanceDelay() {
		return DISAPPEARANCE_DELAY;
	}

	/**
	 * Gets the amount of time to wait before hiding the diagram assistant after
	 * the user has moved the mouse outside of the editpart.
	 * 
	 * @return the time to wait in milliseconds
	 */
	protected int getDisappearanceDelayUponExit() {
		return DISAPPEARANCE_DELAY_UPON_EXIT;
	}

	/**
	 * Gets the current mouse location. This will be null if the mouse is
	 * outside the host and diagram assistant figure.
	 * 
	 * @return Returns the current mouse location
	 */
	protected Point getMouseLocation() {
		return mouseLocation;
	}

	/**
	 * Sets the current mouse location. If set to null, this implies that the
	 * mouse is outside the host and diagram assistant figure.
	 * 
	 * @param mouseLocation
	 *            the current mouse location
	 */
	protected void setMouseLocation(Point mouseLocation) {
		this.mouseLocation = mouseLocation;
	}

	/**
	 * Sets the flag to indicate that the diagram assistant should not be
	 * hidden.
	 * 
	 * @param avoidHidingDiagramAssistant
	 *            Flag to indicate that the diagram assistant should not be
	 *            hidden
	 */
	protected void setAvoidHidingDiagramAssistant(boolean avoidHidingDiagramAssistant) {
		this.avoidHidingDiagramAssistant = avoidHidingDiagramAssistant;
	}

	/**
	 * Returns true if the diagram assistant should not be hidden; false
	 * otherwise.
	 * 
	 * @return true if the diagram assistant should not be hidden; false
	 *         otherwise.
	 */
	protected boolean shouldAvoidHidingDiagramAssistant() {
		return avoidHidingDiagramAssistant;
	}

}
