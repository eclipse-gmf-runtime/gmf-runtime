/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.edit;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * Enumeration that describes the state of an EObject.
 * 
 * @author rafikj
 */
public final class MObjectState
	extends AbstractEnumerator {

	private static int nextOrdinal = 0;

	/**
	 * The object belongs to a resource.
	 */
	public static final MObjectState OPEN = new MObjectState("Open"); //$NON-NLS-1$

	/**
	 * The object deoes not belong to a resource and is not a proxy.
	 */
	public static final MObjectState DETACHED = new MObjectState("Detached"); //$NON-NLS-1$

	/**
	 * The object is a proxy.
	 */
	public static final MObjectState CLOSED = new MObjectState("Closed"); //$NON-NLS-1$

	private static final MObjectState[] VALUES = {OPEN, DETACHED, CLOSED};

	private MObjectState(String name, int ordinal) {
		super(ordinal, name);
	}

	private MObjectState(String name) {
		this(name, nextOrdinal++);
	}

	/**
	 * Gets the list of all possible values.
	 */
	public static List getValues() {
		return Collections.unmodifiableList(Arrays.asList(VALUES));
	}
}