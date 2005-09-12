/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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