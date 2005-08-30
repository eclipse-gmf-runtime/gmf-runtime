/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core.internal.descriptors;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;

/**
 * Enumeration of the kinds of advice binding inheritance that can be identified
 * for <code>adviceBinding</code> elements in the <code>elementTypes</code>
 * extension point.
 * 
 * @author ldamus
 */
public class AdviceBindingInheritance extends EnumeratedType {

	/**
	 * Version ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Advice is not inherited by related metamodel types. It is applied only to
	 * the element type and its specializations.
	 */
	public static final AdviceBindingInheritance NONE = new AdviceBindingInheritance(
			"none"); //$NON-NLS-1$

	/**
	 * Advice is inherited by all metamodel types whose EClasses are subtypes of
	 * the metamodel type to which it was applied, and to all specializations of
	 * those metamodel types.
	 */
	public static final AdviceBindingInheritance ALL = new AdviceBindingInheritance(
			"all"); //$NON-NLS-1$
	
	/**
	 * Gets an enum literal by <code>name</code>.
	 * 
	 * @param name
	 *            the enum literal name
	 * @return the enum literal or <code>null</code> if none with that name is
	 *         defined.
	 */
	public static final AdviceBindingInheritance getAdviceBindingInheritance(String name) {
		
		for (int i = 0; i < VALUES.length; i++) {
			AdviceBindingInheritance nextValue = VALUES[i];
			
			if (nextValue.getName().equals(name)) {
				return nextValue;
			}
		}
		return null;
	}

	/**
	 * Automaic ordinal assignment variable.
	 */
	private static int nextOrdinal = 0;

	/**
	 * The list of values for this enumerated type.
	 */
	private static final AdviceBindingInheritance[] VALUES = { NONE, ALL };

	/**
	 * Private constructor.
	 * 
	 * @param name
	 *            the enumeration literal name
	 */
	private AdviceBindingInheritance(String name) {
		super(name, nextOrdinal++);
	}

	protected List getValues() {
		return Collections.unmodifiableList(Arrays.asList(VALUES));
	}

}
