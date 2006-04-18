/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.ui.services.editor.EditorService;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditorInput;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Provides miscellaneous diagram utilities
 * 
 * @author melaasar, mmostafa
 */

public class DiagramUtil {

	/**
	 * Destroys a given view
	 * 
	 * @param view
	 *            The view to be destroyed
 	 *			deprectaion date:  Dec 19 , 05
	 * 			removal date:	   Jan 31 , 06	
     * @deprecated Use {@link DestroyElementCommand#destroy(EObject)} instead.     	
	 */
	public static void destroyView(View view) {
		DestroyElementCommand.destroy(view);
	}

	/**
	 * Opens an editor for a given diagram
	 * 
	 * @param diagram
	 *            The diagram to be opened
	 */
	public static void openDiagramEditor(Diagram diagram) {
		EditorService.getInstance().openEditor(new DiagramEditorInput(diagram));
	}

	/**
	 * Rerturns an open editor for the given diagram in the given workbench
	 * window if the window is null, the active window in the platform is
	 * considered
	 * 
	 * @param diagram
	 *            The given diagram
	 * @param window
	 *            The given window (or null to mean the active one)
	 * @return An <code>IDiagramWorkbenchPart</code>
	 */
	public static IDiagramWorkbenchPart getOpenedDiagramEditor(Diagram diagram,
			IWorkbenchWindow window) {
		if (null == diagram)
			throw new NullPointerException("Argument 'diagram' is null"); //$NON-NLS-1$
		if (window == null)
			window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		List editors = EditorService.getInstance().getRegisteredEditorParts();
		for (Iterator j = editors.iterator(); j.hasNext();) {
			IEditorPart editor = (IEditorPart) j.next();
			if (editor.getEditorSite().getWorkbenchWindow() == window) {
				IDiagramWorkbenchPart de = (IDiagramWorkbenchPart) editor;
				if (de.getDiagram() == diagram)
					return de;
			}
		}
		return null;
	}

}
