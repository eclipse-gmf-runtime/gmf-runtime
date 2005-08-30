/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.dnd.internal.core;

/**
 * Interface for accessing the attributes used to determine the drag and drop
 * transfer adapters and listeners
 * 
 * @author Vishy Ramaswamy
 */
public interface IListenerContext {

	/**
	 * Constant for requesting all supporting transfer ids
	 */
	public static final String ALL_TRANSFERS = "ALL_TRANSFERS"; //$NON-NLS-1$

	/**
	 * Constant for drag operation type
	 */
	public static final String DRAG = "drag"; //$NON-NLS-1$

	/**
	 * Constant for drop operation type
	 */
	public static final String DROP = "drop"; //$NON-NLS-1$

	/**
	 * Returns the operation type
	 * 
	 * @return Returns the operation type
	 */
	public String getOperationType();

	/**
	 * Returns the transfer id requested
	 * 
	 * @return the transfer id
	 */
	public String getTransferId();
}