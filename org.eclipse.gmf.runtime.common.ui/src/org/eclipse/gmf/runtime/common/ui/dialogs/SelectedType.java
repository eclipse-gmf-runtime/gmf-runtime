/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.dialogs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;

/**
 * Describes how a {@link
 * org.eclipse.gmf.runtime.common.ui.dialogs.SelectableElement
 * SelectableElement} is selected.  This is either selected,
 * unselected, or leave.
 * 
 * @author wdiu, Wayne Diu
 */

public class SelectedType
	extends EnumeratedType {

	/**
	 * An internal unique identifier for selection of elements.
	 */
	private static int nextOrdinal = 0;

	/**
	 * The element was selected.
	 */
	public static final SelectedType SELECTED = new SelectedType("Selected"); //$NON-NLS-1$

	/**
	 * The element was unselected.
	 */
	public static final SelectedType UNSELECTED = new SelectedType("Unselected"); //$NON-NLS-1$

	/**
	 * The element was neither selected nor unselected
	 */
	public static final SelectedType LEAVE = new SelectedType("Leave"); //$NON-NLS-1$

	/**
	 * The list of values for this enumerated type.
	 */
	private static final SelectedType[] VALUES = {SELECTED, UNSELECTED, LEAVE};

	/**
	 * Constructs a new model type with the specified name and ordinal.
	 * 
	 * @param name
	 *            The name of the new model type.
	 * @param ordinal
	 *            The ordinal for the new model type.
	 */
	protected SelectedType(String name, int ordinal) {
		super(name, ordinal);
	}

	/**
	 * Constructs a new model type with the specified name.
	 * 
	 * @param name
	 *            The name of the new model type.
	 */
	private SelectedType(String name) {
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