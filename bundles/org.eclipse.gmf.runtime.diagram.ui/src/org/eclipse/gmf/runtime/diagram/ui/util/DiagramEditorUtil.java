/******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    Intalio, Inc. patch for bug 264483 and some javadoc
 ****************************************************************************/
package org.eclipse.gmf.runtime.diagram.ui.util;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gmf.runtime.common.ui.services.editor.EditorService;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;

/**
 * Helper class to help find a view in an opened editor.
 *
 */
public class DiagramEditorUtil {

	/**
	 * Finds the <code>DiagramEditor</code> that is opened for the diagram
	 * with the given diagram view id.
	 * 
	 * @param id
	 *            diagram view's id
	 * @return an opened editor that displays the diagram with the given diagram
	 *         view id
	 */
	public static DiagramEditor findOpenedDiagramEditorForID(String id) {
		if (id != null) {
			List diagramEditors = EditorService.getInstance()
					.getRegisteredEditorParts();
			Iterator it = diagramEditors.iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				if (obj instanceof DiagramEditor) {
					DiagramEditor diagramEditor = (DiagramEditor) obj;
					if (diagramEditor.getDiagramEditPart() == null) {
					    continue;
					}
					if (id.equals(ViewUtil.getIdStr(diagramEditor
							.getDiagramEditPart().getDiagramView()))) {
						return diagramEditor;
					}
				}
			}
		}
		// no matching guid found
		return null;
	}

}
