/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
