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
