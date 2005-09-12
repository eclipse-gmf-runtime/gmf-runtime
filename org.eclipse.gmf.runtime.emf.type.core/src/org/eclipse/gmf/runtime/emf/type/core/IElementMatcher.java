/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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