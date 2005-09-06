/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.palette;

import org.eclipse.gef.Tool;

import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory;

/**
 * A tool palette entry with identity
 * 
 * @author melaasar
 */
public class PaletteToolEntry
	extends org.eclipse.gef.palette.ToolEntry {

	/** the drawer's id */
	private Tool tool;
	private PaletteFactory factory;
	
	/**
	 * @param id
	 * @param label
	 * @param factory
	 */
	public PaletteToolEntry(
		String id,
		String label,
		PaletteFactory factory) {
		super(label, null, null, null);
		setId(id);
		this.factory = factory;
	}

	/**  @see org.eclipse.gef.palette.ToolEntry#createTool()
	*/
	public Tool createTool() {
		return tool != null ? tool : factory.createTool(getId());
	}

}
