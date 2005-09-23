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

import org.eclipse.gef.editpolicies.GraphicalEditPolicy;

/**
 * Encapsulates behavior common to editpolicies that popup diagram assistants.
 * 
 * @author cmahoney
 */
public class DiagramAssistantEditPolicy
	extends GraphicalEditPolicy {

	/**
	 * The amount of time to wait before showing the diagram assistant.
	 */
	private static final int APPEARANCE_DELAY = 200;

	/**
	 * The amount of time to wait before hiding the diagram assistant after it
	 * has been made visible.
	 */
	private int DISAPPEARANCE_DELAY = 2000;

	/**
	 * The amount of time to wait before hiding the diagram assistant after the
	 * user has moved the mouse outside of the editpart.
	 */
	private static final int DISAPPEARANCE_DELAY_UPON_EXIT = 1000;

	/**
	 * Creates a new instance.
	 */
	public DiagramAssistantEditPolicy() {
		super();
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
}
