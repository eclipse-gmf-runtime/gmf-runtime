/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.palette;

import org.eclipse.gef.Tool;

import org.eclipse.gmf.runtime.diagram.ui.internal.tools.ZoomTool;
import org.eclipse.gmf.runtime.diagram.ui.internal.util.PresentationNotationType;
import org.eclipse.gmf.runtime.diagram.ui.services.palette.PaletteFactory;
import org.eclipse.gmf.runtime.diagram.ui.tools.ConnectorCreationTool;
import org.eclipse.gmf.runtime.diagram.ui.tools.CreationTool;

/**
 * A palette factory for common UML entries
 * 
 * @author melaasar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class PresentationPaletteFactory extends PaletteFactory.Adapter {

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
  			return new CreationTool(PresentationNotationType.NOTE);
		}
		if (toolId.equals(TOOL_TEXT)) {			
			return new CreationTool(PresentationNotationType.TEXT);
		}
		if (toolId.equals(TOOL_NOTEATTACHMENT)){
			return new ConnectorCreationTool(PresentationNotationType.NOTE_ATTACHMENT);
		}
		return null;
	}

}
