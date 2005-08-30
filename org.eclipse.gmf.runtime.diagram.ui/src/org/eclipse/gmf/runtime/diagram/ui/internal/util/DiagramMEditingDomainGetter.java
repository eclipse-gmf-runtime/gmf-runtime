/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
