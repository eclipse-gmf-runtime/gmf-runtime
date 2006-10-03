/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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