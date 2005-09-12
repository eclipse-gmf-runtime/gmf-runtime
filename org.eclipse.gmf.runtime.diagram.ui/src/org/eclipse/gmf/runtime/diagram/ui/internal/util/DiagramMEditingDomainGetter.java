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

package org.eclipse.gmf.runtime.diagram.ui.internal.util;

import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;


/**
 * @author Yasser Lulu
 *
 */
public class DiagramMEditingDomainGetter {

	public static MEditingDomain getMEditingDomain(IEditorPart activeEditor) {
		return getMEditingDomain(activeEditor, true);
	}

	public static MEditingDomain getMEditingDomain(IEditorPart activeEditor, boolean returnDefaultOnFailure){
		if (activeEditor instanceof IDiagramWorkbenchPart) {
			return getMEditingDomain((IDiagramWorkbenchPart) activeEditor,
				returnDefaultOnFailure);
		}
		return (returnDefaultOnFailure) ? MEditingDomain.INSTANCE
			: null;
	}

	public static MEditingDomain getMEditingDomain(
			IDiagramWorkbenchPart diagramWorkbenchPart) {
		return getMEditingDomain(diagramWorkbenchPart, true);
	}

	public static MEditingDomain getMEditingDomain(
			IDiagramWorkbenchPart diagramWorkbenchPart,
			boolean returnDefaultOnFailure) {
		MEditingDomain editingDomain = null;
		if (diagramWorkbenchPart != null) {
			editingDomain = MEditingDomainGetter.getMEditingDomain(diagramWorkbenchPart
				.getDiagram(), returnDefaultOnFailure);
		}
		return ((editingDomain == null) && returnDefaultOnFailure) ? MEditingDomain.INSTANCE
			: editingDomain;
	}

}
