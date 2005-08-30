/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.ui.parts;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramActionBarContributor;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.parts.IDEEditor;

/**
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 * 
 * Contribiute logic diagram action bar
 */
public class LogicDiagramActionBarContributor
	extends DiagramActionBarContributor {

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.DiagramActionBarContributor#getEditorClass()
	 */
	protected Class getEditorClass() {
		return IDEEditor.class;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.DiagramActionBarContributor#getEditorId()
	 */
	protected String getEditorId() {
		return LogicDiagramPlugin.EDITOR_ID;
	}
}