/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.properties.extended;

import org.eclipse.swt.widgets.Control;

/**
 * Property related action
 *
 * @author dmisic
 */
public interface IPropertyAction {

	/**
	 * Executes the action
	 * 
	 * @param owner The logical owner control; does not have to be control that
	 *              initiated the action
	 * @return The result of the action; may be null
	 */
	public Object execute(Control owner);
}
