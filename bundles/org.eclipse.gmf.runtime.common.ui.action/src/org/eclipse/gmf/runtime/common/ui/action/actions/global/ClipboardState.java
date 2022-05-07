/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.action.actions.global;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;

/**
 * Enumeration class for the clipboard state.
 * 
 * @author dmisic
 */
public class ClipboardState
	extends EnumeratedType {

	private static final long serialVersionUID = 1L;

	/**
	 * An internal unique identifier for this enumerated type.
	 */
	private static int nextOrdinal = 0;

	/**
	 * Clipboard state: NORMAL
	 */
	public static final ClipboardState NORMAL = new ClipboardState("Normal"); //$NON-NLS-1$

	/**
	 * Clipboard state: PRE_OVERWRITE
	 */
	public static final ClipboardState PRE_OVERWRITE = new ClipboardState(
		"PreOverwrite"); //$NON-NLS-1$

	/**
	 * Clipboard state: OVERWRITE
	 */
	public static final ClipboardState OVERWRITE = new ClipboardState(
		"Overwrite"); //$NON-NLS-1$

	/**
	 * The list of values for this enumerated type.
	 */
	private static final ClipboardState[] VALUES = {NORMAL, PRE_OVERWRITE,
		OVERWRITE};

	/**
	 * Constructs a new clipboard state with the specified name. The method is
	 * private so that the class can not be directly instantiated.
	 * 
	 * @param name
	 *            The name of the state
	 */
	private ClipboardState(String name) {
		super(name, nextOrdinal++);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.internal.util.EnumeratedType#getValues()
	 */
	protected List getValues() {
		return Collections.unmodifiableList(Arrays.asList(VALUES));
	}
}