/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.services.palette;

import org.eclipse.gef.Tool;

/**
 * @author melaasar
 * 
 * An interface for defining a factory for palette entries : toolss & templates
 * <p>
 * This interface is <EM>not</EM> intended to be implemented by clients as new
 * methods may be added in the future. Extend the <code>Adapter</code> class
 * instead.
 * </p>
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
