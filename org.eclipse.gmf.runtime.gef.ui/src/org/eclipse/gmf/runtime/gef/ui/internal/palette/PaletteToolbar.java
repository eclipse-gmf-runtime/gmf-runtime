/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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
 * An implementation of a palette toolbar with identity.
 * 
 * @author crevells
 */
public class PaletteToolbar
	extends org.eclipse.gef.palette.PaletteToolbar {

	/**
	 * @param id
	 * @param label
	 */
	public PaletteToolbar(String id, String label) {
		super(label);
		setId(id);
	}

}
