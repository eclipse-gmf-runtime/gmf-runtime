/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.internal.resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;

/**
 * Enumeration class for a file change event type.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:ahunter@rational.com">ahunter@rational.com </a>
 */
public class FileChangeEventType
	extends EnumeratedType {

	/**
	 * An internal unique identifier for this enumerated type.
	 */
	private static int nextOrdinal = 0;

	/**
	 * Moved file change event type.
	 */
	public static final FileChangeEventType MOVED = new FileChangeEventType(
		"Moved"); //$NON-NLS-1$

	/**
	 * Renamed file change event type.
	 */
	public static final FileChangeEventType RENAMED = new FileChangeEventType(
		"Renamed"); //$NON-NLS-1$

	/**
	 * Deleted file change event type.
	 */
	public static final FileChangeEventType DELETED = new FileChangeEventType(
		"Deleted"); //$NON-NLS-1$

	/**
	 * Changed file change event type.
	 */
	public static final FileChangeEventType CHANGED = new FileChangeEventType(
		"Changed"); //$NON-NLS-1$

	/**
	 * The list of values for this enumerated type.
	 */
	private static final FileChangeEventType[] VALUES = {MOVED, RENAMED,
		DELETED, CHANGED};

	/**
	 * Constructs a new file change event type with the specified name and
	 * ordinal.
	 * 
	 * @param name
	 *            The name of the new file change event type.
	 * @param ordinal
	 *            The ordinal for the new file change event type.
	 */
	protected FileChangeEventType(String name, int ordinal) {
		super(name, ordinal);
	}

	/**
	 * Constructs a new file change event type with the specified name.
	 * 
	 * @param name
	 *            The name of the new file change event type.
	 */
	private FileChangeEventType(String name) {
		this(name, nextOrdinal++);
	}

	/**
	 * Retrieves the list of constants for this enumerated type.
	 * 
	 * @return The list of constants for this enumerated type.
	 */
	protected List getValues() {
		return Collections.unmodifiableList(Arrays.asList(VALUES));
	}
}