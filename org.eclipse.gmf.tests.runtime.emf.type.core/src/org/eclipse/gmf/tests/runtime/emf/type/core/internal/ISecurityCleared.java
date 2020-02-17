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

package org.eclipse.gmf.tests.runtime.emf.type.core.internal;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;


/**
 * @author ldamus
 */
public interface ISecurityCleared
	extends IElementType {
	
	public static final String TOP_SECRET = "topSecret"; //$NON-NLS-1$

	public abstract String getSecurityClearance();
}
