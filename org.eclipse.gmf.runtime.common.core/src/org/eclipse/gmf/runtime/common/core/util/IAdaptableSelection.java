/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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