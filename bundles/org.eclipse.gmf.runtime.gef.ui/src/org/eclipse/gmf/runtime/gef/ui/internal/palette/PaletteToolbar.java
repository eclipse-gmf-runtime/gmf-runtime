/******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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
