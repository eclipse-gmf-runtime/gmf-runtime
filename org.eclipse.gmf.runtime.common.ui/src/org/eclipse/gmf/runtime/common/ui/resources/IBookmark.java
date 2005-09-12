/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.resources;

/**
 * Convenience interface for the bookmark type and its attributes
 * 
 * @author bagrodia Created on: Oct 2, 2003
 */
public interface IBookmark
	extends IMarker {

	/** bookmark type */
	public static final String TYPE = "org.eclipse.gmf.runtime.common.ui.services.bookmark"; //$NON-NLS-1$

	/*
	 * ====================================================================
	 * Marker attributes:
	 * ====================================================================
	 */
}