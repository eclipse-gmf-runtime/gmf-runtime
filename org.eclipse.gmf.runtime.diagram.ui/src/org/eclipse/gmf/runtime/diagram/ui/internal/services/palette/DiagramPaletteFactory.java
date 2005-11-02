/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.services.palette;

import org.eclipse.gef.Tool;

import org.eclipse.gmf.runtime.diagram.ui.internal.tools.ZoomTool;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.DiagramNotationType;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory;
import org.eclipse.gmf.runtime.diagram.ui.tools.ConnectionCreationTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool;

/**
 * A palette factory for common diagram entries.
 * 
 * @author melaasar
 */
public class DiagramPaletteFactory extends PaletteFactory.Adapter {

	private static final String TOOL_ZOOM = "zoomTool"; //$NON-NLS-1$
	private static final String TOOL_NOTE = "noteTool"; //$NON-NLS-1$
	private static final String TOOL_TEXT = "textTool"; //$NON-NLS-1$
	private static final String TOOL_NOTEATTACHMENT = "noteattachmentTool"; //$NON-NLS-1$

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory#createTool(java.lang.String)
	 */
	public Tool createTool(String toolId) {
		if (toolId.equals(TOOL_ZOOM)) {
  			return new ZoomTool(); 
		}
		if (toolId.equals(TOOL_NOTE)) {
  			return new CreationTool(DiagramNotationType.NOTE);
		}
		if (toolId.equals(TOOL_TEXT)) {			
			return new CreationTool(DiagramNotationType.TEXT);
		}
		if (toolId.equals(TOOL_NOTEATTACHMENT)){
			return new ConnectionCreationTool(DiagramNotationType.NOTE_ATTACHMENT);
		}
		return null;
	}

}
