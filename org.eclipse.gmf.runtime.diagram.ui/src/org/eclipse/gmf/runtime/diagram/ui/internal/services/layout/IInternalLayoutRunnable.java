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

package org.eclipse.gmf.runtime.diagram.ui.internal.services.layout;

import org.eclipse.gef.commands.Command;


/**
 * @author sshaw
 * @canBeSeenBy %level1
 *
 * Interface for accessing the wrapped GEF command
 */
public interface IInternalLayoutRunnable extends Runnable {
	
	/**
	 * @return the wrapped GEF command to be executed from the layout service.
	 */
	public Command getCommand();
}
