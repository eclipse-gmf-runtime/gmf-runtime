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
 * @author melaaser, choang
 *
 * A org.eclipse.gef.palette.PaletteDrawer Palette category with and id and priority and
 * who will be collapsed by default.
 * 
 */
public class PaletteDrawer
	extends org.eclipse.gef.palette.PaletteDrawer {

	/**
	 * @param id The drawer's id
	 * @param label The drawer's label
	 */
	public PaletteDrawer(String id, String label) {
		super(label);
		setId(id);
		this.setInitialState(INITIAL_STATE_CLOSED);
	}

}
