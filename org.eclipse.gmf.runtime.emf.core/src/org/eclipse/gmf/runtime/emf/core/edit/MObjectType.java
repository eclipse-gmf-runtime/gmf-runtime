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

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * Enumeration that describes the type of an EObject.
 * 
 * @author rafikj
 */
public class MObjectType
	extends AbstractEnumerator {

	private static int nextOrdinal = 0;

	/**
	 * The object is a modeling object. Other types could be added by extending
	 * the class.
	 */
	public static final MObjectType MODELING = new MObjectType("Modeling"); //$NON-NLS-1$

	/**
	 * Gets the list of all possible values.
	 */
	protected MObjectType(String name) {
		super(nextOrdinal++, name);
	}
}