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

package org.eclipse.gmf.runtime.diagram.ui.internal.services.layout;

import org.eclipse.gef.commands.Command;


/**
 * @author sshaw
 *
 * Interface for accessing the wrapped GEF command
 */
public interface IInternalLayoutRunnable extends Runnable {
	
	/**
	 * @return the wrapped GEF command to be executed from the layout service.
	 */
	public Command getCommand();
}
