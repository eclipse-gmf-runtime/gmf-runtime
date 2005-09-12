/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.core;

/**
 * Interface containing constants for transfer ids used within drag and drop
 * operations
 * 
 * @author Vishy Ramaswamy
 */
public interface TransferId {

	/**
	 * Constant for custom data transfer agent
	 */
	public static final String CUSTOM_DATA_TRANSFER = "customDataTransfer"; //$NON-NLS-1$

	/**
	 * Constant for file transfer agent
	 */
	public static final String FILE_TRANSFER = "fileTransfer"; //$NON-NLS-1$;

	/**
	 * Constant for plugin transfer agent
	 */
	public static final String PLUGIN_TRANSFER = "pluginTransfer"; //$NON-NLS-1$;

	/**
	 * Constant for selection transfer agent
	 */
	public static final String SELECTION_TRANSFER = "selectionTransfer"; //$NON-NLS-1$;

	/**
	 * Constant for text transfer agent
	 */
	public static final String TEXT_TRANSFER = "textTransfer"; //$NON-NLS-1$;

	/**
	 * Constant for rich text transfer agent
	 */
	public static final String RTF_TRANSFER = "richTextTransfer"; //$NON-NLS-1$;
}