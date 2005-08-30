/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action;

import org.eclipse.jface.action.IAction;

/**
 * Interface extension to <code>IAction</code> adding lifecycle methods.
 * 
 * @author melaasar
 */
public interface IDisposableAction extends IAction {

	/**
	 * init should be called after an action instance gets constructed 
	 */
	public void init();

	/**
	 * dispose should be called as soon as the action is no longer needed
	 */
	public void dispose();
	
	/**
	 * Answers whether or not this action has been disposed and has not
	 * been re-initialized.
	 * 
	 * @return <code>true</code> if the action has been disposed, 
	 * 	 	   <code>false</code> otherwise.
	 */
	public boolean isDisposed();
	
}
