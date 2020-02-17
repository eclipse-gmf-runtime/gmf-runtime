/******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.common.core.util;

/**
 * A specification of classes that implement the <em>Null Object</em> pattern.
 * Implementors of this interface have a unique instance that represents a
 * pointer-safe <code>null</code>. The interpretation of <code>null</code>
 * may vary; it may mean absence of a value, invalid value, etc. 
 * <p>
 * API clients should <b>not</b> implement this interface.
 * </p>
 */
public interface INullObject {

	/**
	 * Queries whether <code>this</code> is the <code>null</code> instance of a
	 * given implementation class. The <code>null</code> instance must be unique.
	 * 
	 * @return <code>true</code> if I am the special "null" instance;
	 *         <code>false</code>, otherwise
	 */
	boolean isNull();
}