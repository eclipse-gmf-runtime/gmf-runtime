/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

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