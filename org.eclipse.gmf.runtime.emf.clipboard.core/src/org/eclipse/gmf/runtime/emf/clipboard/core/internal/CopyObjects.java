/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.clipboard.core.internal;

import java.util.Collection;

/**
 * A data structure that holds info needed through the copy process
 * 
 * @author Yasser Lulu
 */
public class CopyObjects extends
		org.eclipse.gmf.runtime.emf.clipboard.core.CopyObjects {
	
	/**
	 * Initializes me with my original objects.
	 * 
	 * @param originalObjects
	 *            the objects originally selected for copying
	 */
	public CopyObjects(Collection originalObjects) {
		super(originalObjects);
	}

}