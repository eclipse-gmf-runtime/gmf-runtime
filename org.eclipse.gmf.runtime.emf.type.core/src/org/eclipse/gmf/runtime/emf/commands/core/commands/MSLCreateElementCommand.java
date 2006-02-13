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

import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;


/**
 * Command to create a model element. Uses the first feature in the container
 * that can contain the new element if the containment feature is not specified.
 *
 * @author ldamus
 * 
 * @deprecated Use {@link CreateElementCommand} instead.
 */
public class MSLCreateElementCommand
	extends CreateElementCommand {

	/**
	 * Constructs a new command to create a model element.
	 *
	 * @param request
	 *            the request
	 */
	public MSLCreateElementCommand(CreateElementRequest request) {
		super(request);
	}

}