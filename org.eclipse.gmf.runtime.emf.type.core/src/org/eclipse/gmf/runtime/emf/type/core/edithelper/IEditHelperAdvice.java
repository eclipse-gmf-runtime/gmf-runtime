/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
    
    /**
     * Configures the <code>request</code>. Advisors may modify the request
     * parameters in this method. This method is consulted before the request is
     * approved by {@link #approveRequest(IEditCommandRequest)} and before the
     * edit command is constructed.
     * 
     * @param request
     *            the edit request to be configured.
     */
    public void configureRequest(IEditCommandRequest request);
    
    /**
     * Approves the edit gesture described in the <code>request</code>. This
     * method will be consulted before the edit command is constructed, but
     * after {@link #configureRequest(IEditCommandRequest)} has been called on
     * all applicable advice.
     * 
     * @param request
     *            the edit request
     * @return <code>true</code> if the edit request is approved,
     *         <code>false</code> otherwise. No edit command will be
     *         constructed if the request is not approved.
     */
    public boolean approveRequest(IEditCommandRequest request);

}