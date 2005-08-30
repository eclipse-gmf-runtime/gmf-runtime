/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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