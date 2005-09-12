/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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