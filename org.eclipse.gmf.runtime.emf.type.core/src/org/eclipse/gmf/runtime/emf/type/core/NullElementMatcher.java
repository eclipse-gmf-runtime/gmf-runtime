/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
