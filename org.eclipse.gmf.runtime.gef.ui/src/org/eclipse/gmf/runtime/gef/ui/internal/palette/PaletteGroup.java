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
 * An implementation of a palette group with identity
 * 
 * @author melaasar
 */
public class PaletteGroup
	extends org.eclipse.gef.palette.PaletteGroup {

	/**
	 * @param id
	 * @param label
	 */
	public PaletteGroup(String id, String label) {
		super(label);
		setId(id);
	}

}
