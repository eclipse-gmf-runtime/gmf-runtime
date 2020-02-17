/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.editpolicies;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;


/**
 * @author sshaw
 *
 * <code>EnumeratedType</code> describing the different line editing modes
 */
public class LineMode
	extends EnumeratedType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * An internal unique identifier for this enumerated type.
	 */
	private static int nextOrdinal = 0;

	/**
	 * Indicates that the line is oblique meaning that the line segments can be at any angle.
	 */
	public static final LineMode OBLIQUE = new LineMode("Oblique"); //$NON-NLS-1$
	/**
	 * Indicates that the line is orthogonal meaning that the line segments must be horizontal
	 * or vertical.
	 */
	public static final LineMode ORTHOGONAL_FREE = new LineMode("Orthogonal_Free"); //$NON-NLS-1$
	/**
	 * Indicates that the line is orthogonal meaning that the line segments must be horizontal
	 * or vertical but additionally they are constrained by some value.
	 */
	public static final LineMode ORTHOGONAL_CONSTRAINED = new LineMode("Orthogonal_Constrained"); //$NON-NLS-1$

	/**
	 * The list of values for this enumerated type.
	 */
	private static final LineMode[] VALUES = {OBLIQUE, ORTHOGONAL_FREE, ORTHOGONAL_CONSTRAINED};

	/**
	 * Constructs a new paste option with the specified name and ordinal.
	 * 
	 * @param name the name of the new paste option.
	 * @param ordinal the ordinal for the new paste option.
	 */
	protected LineMode(String name, int ordinal) {
		super(name, ordinal);
	}

	/**
	 * Constructs a new paste option with the specified name.
	 * 
	 * @param name the name of the new LineMode option.
	 */
	private LineMode(String name) {
		this(name, nextOrdinal++);
	}

	/**
	 * Retrieves the list of constants for this enumerated type.
	 * 
	 * @return The list of constants for this enumerated type.
	 * 
	 * @see EnumeratedType#getValues()
	 */
	protected List getValues() {
		return Collections.unmodifiableList(Arrays.asList(VALUES));
	}

}
