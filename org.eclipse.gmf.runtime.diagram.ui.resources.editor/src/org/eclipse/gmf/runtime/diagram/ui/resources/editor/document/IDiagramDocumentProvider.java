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

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.document;

import org.eclipse.ui.IEditorInput;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;



/**
 * An interface to be implemented by all document providers,
 * that create <code>IDiagramDocument</code>
 * 
 * @author mgoyal
 */
public interface IDiagramDocumentProvider {
	/**
	 * Returns the diagram document.
	 * 
	 * @param element The Editor Input for which the diagram document is requred.
	 * @return the Diagram document.
	 * 
	 * @see IDocumentProvider#getDocument(Object)
	 */
	IDiagramDocument getDiagramDocument(Object element);
	
	IEditorInput createInputWithEditingDomain(IEditorInput editorInput, MEditingDomain domain);
}
