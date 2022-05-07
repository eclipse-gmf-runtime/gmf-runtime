/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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

/**
 * types representing the reason a given object was serialized (copied)
 *  
 * @author Yasser Lulu 
 */
public class ObjectCopyType {

	/**
	 * 
	 */
	private ObjectCopyType() {
		//private
	}

	public static final String OBJ_COPY_TYPE_PARENT = "OCT_PARENT"; //$NON-NLS-1$

	public static final String OBJ_COPY_TYPE_ALWAYS = "OCT_ALWAYS"; //$NON-NLS-1$

	static final String OBJ_COPY_TYPE_ORIGINAL = "OCT_ORIGINAL"; //$NON-NLS-1$

}