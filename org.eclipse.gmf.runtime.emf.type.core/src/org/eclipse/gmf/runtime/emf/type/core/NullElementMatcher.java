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
