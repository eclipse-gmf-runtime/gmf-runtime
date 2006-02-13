/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.commands.core.commands;

import org.eclipse.gmf.runtime.emf.type.core.commands.MoveElementsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;

/**
 * Command to move model elements.
 * <P>
 * If the target features are not specified in the request and the old
 * containment features do not exist in the target element, the first feature in
 * the target container that can contain each moved element will be used.
 * 
 * @author ldamus
 * 
 * @deprecated Use {@link MoveElementsCommand} instead.
 */
public class MSLMoveElementsCommand extends MoveElementsCommand {

	/**
	 * Constructs a new command to move model elements
	 * 
	 * @param request
	 *            the move element request
	 */
	public MSLMoveElementsCommand(MoveRequest request) {

		super(request);
	}
}