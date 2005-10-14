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

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.ui.parts;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.LogicDiagramPlugin;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramActionBarContributor;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor.FileDiagramEditorWithFlyoutPalette;

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
		return FileDiagramEditorWithFlyoutPalette.class;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.parts.DiagramActionBarContributor#getEditorId()
	 */
	protected String getEditorId() {
		return LogicDiagramPlugin.EDITOR_ID;
	}
}