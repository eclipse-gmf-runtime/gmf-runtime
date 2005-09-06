/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.services.palette;

import org.eclipse.gef.Tool;

/**
 * @author melaasar
 * 
 * An interface for defining a factory for palette entries : toolss & templates
 */
public interface PaletteFactory {

	/**
	 * Creates a new instance of the tool with the given id
	 * 
	 * @param toolId The tool Id
	 * @return the created tool
	 */
	public Tool createTool(String toolId);

	/**
	 * Gets the palette template with the given id
	 * 
	 * @param templateId The template id
	 * @return the template
	 */
	public Object getTemplate(String templateId);


	/**
	 * An empty adapter for the interface
	 */
	public class Adapter implements PaletteFactory {
		/**
		 * @see org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory#getTemplate(java.lang.String)
		 */
		public Object getTemplate(String templateId) {
			return null;
		}

		/**
		 * @see org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory#createTool(java.lang.String)
		 */
		public Tool createTool(String toolId) {
			return null;
		}
	}
}
