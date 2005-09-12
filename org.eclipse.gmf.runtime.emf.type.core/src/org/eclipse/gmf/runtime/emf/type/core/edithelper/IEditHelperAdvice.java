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

package org.eclipse.gmf.runtime.emf.type.core.edithelper;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Provides 'before' and 'after' editing behaviour for modifying model elements.
 * <P>
 * Clients should not implement this interface directly. They should instead
 * subclass {@link org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice}and
 * override the methods for the specific requests that they provide advice for.
 * 
 * @author ldamus
 */
public interface IEditHelperAdvice {

	/**
	 * Gets a command to be executed before the base editing behaviour.
	 * 
	 * @param request
	 *            the request
	 * @return the 'before' command, or <code>null</code> if I do not provide
	 *         'before' behaviour.
	 */
	public ICommand getBeforeEditCommand(IEditCommandRequest request);

	/**
	 * Gets a command to be executed after the base editing behaviour.
	 * 
	 * @param request
	 *            the request
	 * @return the 'after' command, or <code>null</code> if I do not provide
	 *         'after' behaviour.
	 */
	public ICommand getAfterEditCommand(IEditCommandRequest request);

}