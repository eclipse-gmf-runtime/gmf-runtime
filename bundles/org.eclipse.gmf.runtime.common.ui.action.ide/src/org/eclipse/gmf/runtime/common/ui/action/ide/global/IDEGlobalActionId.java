/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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