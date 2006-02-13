/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.commands.core.commands;

import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

/**
 * Command to destroy a model element.
 * 
 * @author ldamus
 * 
 * @deprecated Use {@link DestroyElementCommand} instead.
 */
public class MSLDestroyElementCommand
	extends DestroyElementCommand {

	/**
	 * Constructs a new command to destroy a model element.
	 * 
	 * @param request
	 *            the destroy element request
	 */
	public MSLDestroyElementCommand(DestroyElementRequest request) {
		super(request);
	}

}