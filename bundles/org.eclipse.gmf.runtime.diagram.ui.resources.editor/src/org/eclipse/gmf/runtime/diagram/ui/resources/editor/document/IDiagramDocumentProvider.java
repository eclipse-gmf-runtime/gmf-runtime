/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.document;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.ui.IEditorInput;



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
	
	IEditorInput createInputWithEditingDomain(IEditorInput editorInput, TransactionalEditingDomain domain);
}
