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
