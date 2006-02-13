/******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.util;

import org.eclipse.emf.ecore.EClass;

/**
 * Defines what a Element Type Info should contain in terms of information
 * 
 * @author choang
 * 
 * @deprecated Use {@link org.eclipse.gmf.runtime.emf.type.core.IElementType} instead.
 */
public interface IElementType extends org.eclipse.gmf.runtime.emf.type.core.IElementType {
	/**
	 * Returns the element kind.
	 * @return int the element kind
	 */
	public abstract EClass getEClass();
	
	/**
	 * Returns the element kind id string .
	 * @return String the element kind id string
	 */
	public abstract String getEClassName();
	
	/**
	 * Returns the String.
	 * @return String the element kind
	 */
	public abstract String getDisplayName();
	
	
	
} 
