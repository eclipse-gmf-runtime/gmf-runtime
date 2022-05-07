/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.internal.util;

/**
 * Internal user interface constants used by the diagram.ui layer.
 * 
 * @author wdiu, Wayne Diu
 */
public interface IUIConstants {

	/**
	 * Default number of items to display in a drop down list.
	 * Can be passed into the setVisibleItemCount method of the
	 * Combo class. 
	 * 
	 * @see org.eclipse.swt.widgets.Combo#setVisibleItemCount(int) 
	 */
	public static final int DEFAULT_DROP_DOWN_SIZE = 20;
}
