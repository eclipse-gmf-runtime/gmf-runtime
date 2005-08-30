/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core;

import org.eclipse.emf.ecore.EObject;

/**
 * Determines whether or not a model element matches a fixed set of criteria.
 * <P>
 * The number of conditions declared by this matcher are used to identify the
 * most precise match. A new type can specialize an existing type by adding more
 * conditions than that of the existing type.
 * 
 * @author ldamus
 */
public interface IElementMatcher {

	/**
	 * Answers whether or not I match <code>eObject</code>.
	 * 
	 * @param eObject
	 *            the model element to match
	 * @return <code>true</code> if I match <code>eObject</code>,
	 *         <code>false</code> otherwise.
	 */
	public boolean matches(EObject eObject);
}