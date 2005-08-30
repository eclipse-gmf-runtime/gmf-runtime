/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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