/******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.parts;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DocumentProviderRegistry;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DocumentProviderRegistry.IDocumentProviderSelector;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.MEditingDomainElement;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;

/**
 * A matching strategy for <code>DiagramDocumentEditor</code>
 * 
 * @author mgoyal
 * 
 */
public class DiagramDocumentEditorMatchingStrategy
	implements IEditorMatchingStrategy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorMatchingStrategy#matches(org.eclipse.ui.IEditorReference,
	 *      org.eclipse.ui.IEditorInput)
	 */
	public boolean matches(IEditorReference editorRef, IEditorInput input) {
        IEditorInput existingEditorInput;
        IEditorPart editor = editorRef.getEditor(false);

        try {
            existingEditorInput = editorRef.getEditorInput();
        } catch (PartInitException e) {
            return false;
        }

        // If the ExistingEditorInput is same as the passed input,
        // return true
        if (existingEditorInput.equals(input))
            return true;
        else if (!(input instanceof MEditingDomainElement) && (editor instanceof DiagramDocumentEditor)) {
            // If the input isn't an instanceof MEditingDomainElement, and the editor is instanceof DiagramDocumentEditor
            // then don't match it, This indicates that the client shouldn't use this strategy for non-DiagramDocumentEditor.
            IDiagramDocumentProvider docProvider = (IDiagramDocumentProvider) DocumentProviderRegistry
            .getDefault().getDocumentProvider(input,
                new IDocumentProviderSelector() {

                public boolean select(String documentType) {
                    // Only checking of the interface name
                    return documentType.equals(IDiagramDocument.class
                        .getName());
                }
            });

            if (docProvider != null) {
                IEditorInput editorInput = docProvider
                .createInputWithEditingDomain(input,
                    ((DiagramDocumentEditor) editor).getEditingDomain());
                return existingEditorInput.equals(editorInput);
            }
        }
        return false;
	}
}
