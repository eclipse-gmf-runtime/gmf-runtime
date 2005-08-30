/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.dnd.ide.core;

/**
 * Interface containing constants for transfer ids used within drag and drop
 * operations
 * 
 * @author Vishy Ramaswamy
 */
public interface IDETransferId {

	/**
	 * Constant for resuorce transfer agent
	 */
	public static final String RESOURCE_TRANSFER = "resourceTransfer"; //$NON-NLS-1$;

	/**
	 * Constant for marker transfer agent
	 */
	public static final String MARKER_TRANSFER = "markerTransfer"; //$NON-NLS-1$;

	/**
	 * Constant for resource navigator selection transfer agent
	 */
	public static final String NAV_SELECTION_TRANSFER = "navigatorSelectionTransfer"; //$NON-NLS-1$;
}