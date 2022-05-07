/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.internal.resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;

/**
 * Enumeration for a marker change event type.
 * 
 * @author Michael Yee
 */
public class MarkerChangeEventType
	extends EnumeratedType {

	private static final long serialVersionUID = 1L;

	/**
	 * An internal unique identifier for this enumerated type.
	 */
	private static int nextOrdinal = 0;

	/**
	 * Added file change event type.
	 */
	public static final MarkerChangeEventType ADDED = new MarkerChangeEventType(
		"Added"); //$NON-NLS-1$

	/**
	 * Removed file change event type.
	 */
	public static final MarkerChangeEventType REMOVED = new MarkerChangeEventType(
		"Removed"); //$NON-NLS-1$

	/**
	 * Changed file change event type.
	 */
	public static final MarkerChangeEventType CHANGED = new MarkerChangeEventType(
		"Changed"); //$NON-NLS-1$

	/**
	 * The list of values for this enumerated type.
	 */
	private static final MarkerChangeEventType[] VALUES = {ADDED, REMOVED,
		CHANGED};

	/**
	 * Constructs a new marker change event type with the specified name and
	 * ordinal.
	 * 
	 * @param name
	 *            The name of the new file change event type.
	 * @param ordinal
	 *            The ordinal for the new file change event type.
	 */
	public MarkerChangeEventType(String name, int ordinal) {
		super(name, ordinal);
	}

	/**
	 * Constructs a new marker change event type with the specified name.
	 * 
	 * @param name
	 *            The name of the new file change event type.
	 */
	private MarkerChangeEventType(String name) {
		this(name, nextOrdinal++);
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