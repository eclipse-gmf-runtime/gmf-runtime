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
