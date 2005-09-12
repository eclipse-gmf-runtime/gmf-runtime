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