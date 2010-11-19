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

