/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.palette;

import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * @author melaasar
 * @canBeSeenBy %level1
 *
 * The palette provider interface.
 */
public interface IPaletteProvider extends IProvider {

	/**
	 * Contributes to the palette of the given editor with the given content
	 * 
	 * @param editor The editor hosting the palette
	 * @param content The editor's contents
	 * @param root The editor's palette root
     * @param predefinedEntries
     *            map of predefined palette entries where the key is the palette
     *            entry id and the value is the palette entry
	 */
	public void contributeToPalette(IEditorPart editor, Object content,
            PaletteRoot root, Map predefinedEntries);

	
	/**
	 * 
	 * Sets the contributions data using the configuration elements  
	 * 
	 * @param configElement
	 */
	public void setContributions(IConfigurationElement configElement);
	
	
}
