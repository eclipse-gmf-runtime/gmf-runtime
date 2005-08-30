/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.util;

import org.eclipse.core.runtime.IAdaptable;

/**
 * A common tag interface that is supported by viewers that want to enable access to
 * their selection.
 * <p>
 * API clients should <b>not</b> implement this interface.
 * </p>
 * <p>
 * API clients typically use the interface when implementing selectionChanged methods in 
 * IActionDelegate.
 * </p>
 * <p>
 * Example:
 * <pre>
 * 
 *  	public void selectionChanged(IAction action, final ISelection selection) {
 * 
 * 		if (selection instanceof IStructuredSelection) {
 * 
 * 			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
 * 
 * 			Object firstSelection = structuredSelection.getFirstElement();
 * 
 * 			if (firstSelection instanceof IAdaptableSelection) {
 * 
 * 				myExpectedObject = (...) ((IAdaptableSelection) firstSelection).getAdapter( ... );
 * 			}
 * 		}
 *   }
 *  
 * </pre>
 */
public interface IAdaptableSelection
	extends IAdaptable {
	// Tag interface
}