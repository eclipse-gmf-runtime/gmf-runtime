/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.action.ide.global;

import org.eclipse.ui.ide.IDEActionFactory;

/**
 * Action IDs for global actions with IDE dependencies.
 * 
 * @author wdiu, Wayne Diu
 */
public final class IDEGlobalActionId {

	/**
	 * Private constructor prevents instantiating this class
	 */
	private IDEGlobalActionId() {
		//Private constructor prevents instantiating this class
	}

	/** Action id for the add bookmark action */
	public static final String BOOKMARK = IDEActionFactory.BOOKMARK.getId();

	/** Action id for the open project action */
	public static final String OPEN_PROJECT = IDEActionFactory.OPEN_PROJECT
		.getId();

	/** Action id for the close project action */
	public static final String CLOSE_PROJECT = IDEActionFactory.CLOSE_PROJECT
		.getId();
}