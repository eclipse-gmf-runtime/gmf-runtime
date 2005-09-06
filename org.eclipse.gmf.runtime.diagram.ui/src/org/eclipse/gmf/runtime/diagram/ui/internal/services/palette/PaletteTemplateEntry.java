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

import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory;

/**
 * A template palette entry with identity
 * 
 * @author melaasar
 */
public class PaletteTemplateEntry
	extends org.eclipse.gef.palette.PaletteTemplateEntry {

	/** the drawer's id */
	private PaletteFactory factory;

	/**
	 * @param id
	 * @param label
	 * @param factory
	 */
	public PaletteTemplateEntry(String id, String label, PaletteFactory factory) {
		super(label, null, null, null, null);
		setId(id);
		this.factory = factory;
	}

	/**
	 * @see org.eclipse.gef.palette.PaletteTemplateEntry#getTemplate()
	 */
	public Object getTemplate() {
		return factory.getTemplate(getId());
	}

}
