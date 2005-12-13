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

package org.eclipse.gmf.runtime.common.ui.internal.resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;

/**
 * Enumeration class for a file observer filter type.
 * 
 * @author Anthony Hunter <a
 *         href="mailto:ahunter@rational.com">ahunter@rational.com </a>
 */
public class FileObserverFilterType
	extends EnumeratedType {

	private static final long serialVersionUID = 1L;

	/**
	 * An internal unique identifier for this enumerated type.
	 */
	private static int nextOrdinal = 0;

	/**
	 * All file observer filter type.
	 */
	public static final FileObserverFilterType ALL = new FileObserverFilterType(
		"All"); //$NON-NLS-1$

	/**
	 * file extension file observer filter type.
	 */
	public static final FileObserverFilterType EXTENSION = new FileObserverFilterType(
		"Extension"); //$NON-NLS-1$

	/**
	 * folder file observer filter type.
	 */
	public static final FileObserverFilterType FOLDER = new FileObserverFilterType(
		"Folder"); //$NON-NLS-1$

	/**
	 * file filter file observer filter type.
	 */
	public static final FileObserverFilterType FILE = new FileObserverFilterType(
		"File"); //$NON-NLS-1$

	/**
	 * The list of values for this enumerated type.
	 */
	private static final FileObserverFilterType[] VALUES = {ALL, EXTENSION,
		FOLDER, FILE};

	/**
	 * Constructs a new file observer filter type with the specified name and
	 * ordinal.
	 * 
	 * @param name
	 *            The name of the new file observer filter type.
	 * @param ordinal
	 *            The ordinal for the new file observer filter type.
	 */
	protected FileObserverFilterType(String name, int ordinal) {
		super(name, ordinal);
	}

	/**
	 * Constructs a new file observer filter type with the specified name.
	 * 
	 * @param name
	 *            The name of the new file observer filter type.
	 */
	private FileObserverFilterType(String name) {
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