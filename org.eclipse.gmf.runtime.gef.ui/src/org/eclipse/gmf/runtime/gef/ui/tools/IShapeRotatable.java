/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.gef.ui.tools;


/**
 * Interface checks whether editpart supports rotation. 
 * 
 * @author oboyko
 */
public interface IShapeRotatable {

	/**
	 * This determines if an <code>EditPart</code> can be rotated or not.  By introducing
	 * an interface, this allows the clients to determine dynamically based on some state whether or
	 * not the EditPart can be rotated.
	 * 
	 * @return <code>booean</code> <code>true</code> if the shape is rotatable, 
	 * or <code>false</code> otherwise.
	 */
	abstract public boolean isRotatable();
}
