/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
