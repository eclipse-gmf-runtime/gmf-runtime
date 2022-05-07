/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.palette;

/**
 * An interface that adds identity to a palette entry
 * 
 * @author melaasar
 */
public interface PaletteIdentity {

	/**
	 * Returns the id of a palette entry
	 * 
	 * @return The id of the palette entry
	 */
	String getId();
}

