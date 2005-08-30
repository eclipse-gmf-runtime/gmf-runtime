/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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