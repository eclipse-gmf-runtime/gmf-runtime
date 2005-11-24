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

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DocumentProviderRegistry;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.MEditingDomainElement;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DocumentProviderRegistry.IDocumentProviderSelector;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.parts.DiagramDocumentEditor;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;


/**
 * A matching strategy for <code>DiagramDocumentEditor</code>
 * @author mgoyal
 *
 */
public class DiagramDocumentEditorMatchingStrategy
	implements IEditorMatchingStrategy {

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorMatchingStrategy#matches(org.eclipse.ui.IEditorReference, org.eclipse.ui.IEditorInput)
	 */
	public boolean matches(IEditorReference editorRef, IEditorInput input) {
		IEditorInput existingEditorInput;
		IEditorPart editor = editorRef.getEditor(false);
		// If the editor is not instanceof DiagramDocumentEditor
		// then don't match it, This indicates that the client shouldn't
		// use this strategy for non-DiagramDocumentEditor.
		if(!(editor instanceof DiagramDocumentEditor)) {
			return false;
		}
		
		try {
			existingEditorInput = editorRef.getEditorInput();
		} catch (PartInitException e) {
			return false;
		}

		// If the ExistingEditorInput is same as the passed input, 
		// return true
		if(existingEditorInput.equals(input))
			return true;
		else if(!(input instanceof MEditingDomainElement)) {
			// If the input isn't an instanceof MEditingDomainElement,
			// only then do it.
			IDiagramDocumentProvider docProvider = (IDiagramDocumentProvider)DocumentProviderRegistry.getDefault().getDocumentProvider(input, new IDocumentProviderSelector() {
				public boolean select(String documentType) {
					// Only checking of the interface name
					return documentType.equals(IDiagramDocument.class.getName());
				}
			});
			
			if (docProvider != null) {
				IEditorInput editorInput = docProvider.createInputWithEditingDomain(input, getDefaultDomain());
				return editorInput.equals(existingEditorInput);
			}
		}
		return false;
	}

	/**
	 * Gets the default editing domain for this matching strategy.
	 * @return <code>MEditingDomain</code>
	 */
	public MEditingDomain getDefaultDomain() {
		return MEditingDomain.INSTANCE;
	}
}
