/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.core.resources;

/**
 * Convenience interface for the marker type and its attributes
 * 
 * @author Michael Yee
 */
public interface IMarker {

	/** marker type */
	public static final String TYPE = "org.eclipse.gmf.runtime.common.ui.services.marker"; //$NON-NLS-1$

	/** Separator between multiple IDs in the {@link #ELEMENT_ID}attribute. */
	public static final String ELEMENT_ID_SEPARATOR = " "; //$NON-NLS-1$

	/*
	 * ====================================================================
	 * Marker attributes:
	 * ====================================================================
	 */
	/** element guid */
	public static final String ELEMENT_ID = "elementId"; //$NON-NLS-1$
}