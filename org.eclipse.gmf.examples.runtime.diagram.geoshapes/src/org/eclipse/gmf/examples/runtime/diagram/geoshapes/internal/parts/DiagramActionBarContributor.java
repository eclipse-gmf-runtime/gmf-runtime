/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.parts;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.parts.IDEEditor;

/**
 * The geoshapes diagram editor action bar contributor. This allows the
 * geoshapes diagram to inherit the Diagram main menu and the toolbar from the
 * basic diagram editor.
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.geoshapes.*
 */
public class DiagramActionBarContributor
	extends
	org.eclipse.gmf.runtime.diagram.ui.parts.DiagramActionBarContributor {

	/**
	 * The geoshapes diagram editor ID.
	 */
	protected static final String ID = "GeoshapeEditor"; //$NON-NLS-1$

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
		return ID;
	}
}