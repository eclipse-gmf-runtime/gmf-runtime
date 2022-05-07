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

package org.eclipse.gmf.runtime.emf.type.core;

import org.eclipse.emf.ecore.EObject;

/**
 * Element matcher that matches nothing.
 * 
 * @author ldamus
 */
public class NullElementMatcher
	implements IElementMatcher {

	/**
	 * Always returns <code>false</code>.
	 */
	public boolean matches(EObject eObject) {
		return false;
	}

}
