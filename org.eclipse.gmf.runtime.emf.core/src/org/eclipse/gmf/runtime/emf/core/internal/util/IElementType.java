/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
