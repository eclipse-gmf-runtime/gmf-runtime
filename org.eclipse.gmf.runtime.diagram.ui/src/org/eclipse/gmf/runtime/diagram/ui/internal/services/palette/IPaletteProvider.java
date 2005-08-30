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
	 */
	public void contributeToPalette(
		IEditorPart editor,
		Object content,
		PaletteRoot root);

	
	/**
	 * 
	 * Sets the contributions data using the configuration elements  
	 * 
	 * @param configElement
	 */
	public void setContributions(IConfigurationElement configElement);
	
	
}
