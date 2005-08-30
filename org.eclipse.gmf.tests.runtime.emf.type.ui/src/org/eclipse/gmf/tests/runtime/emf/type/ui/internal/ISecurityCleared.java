/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.emf.type.ui.internal;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;


/**
 * @author ldamus
 */
public interface ISecurityCleared
	extends IElementType {
	
	public static final String TOP_SECRET = "topSecret"; //$NON-NLS-1$

	public abstract String getSecurityClearance();
}
