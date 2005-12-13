/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
f * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramModificationListener;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.util.DiagramIOUtil;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IStorageEditorInput;


/**
 * An implementation of <code>IDiagramDocumentProvider</code> for 
 * connecting <code>org.eclipse.ui.IStorageEditorInput</code> and
 * <code>IDiagramDocument</code>.
 * 
 * @author mgoyal
 *
 */
public class StorageDiagramDocumentProvider
	extends StorageDocumentProvider
	implements IDiagramDocumentProvider {
	
	//a StorageInfo with a DiagramModificationListener 
	private class DiagramStorageInfo extends StorageInfo {

		DiagramModificationListener fListener;
		public DiagramStorageInfo(IDocument document, DiagramModificationListener listener) {
			super(document);
			fListener = listener;
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.StorageDocumentProvider#createEmptyDocument()
	 */
	protected IDocument createEmptyDocument() {
		return new DiagramDocument();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.IDiagramDocumentProvider#getDiagramDocument(java.lang.Object)
	 */
	public IDiagramDocument getDiagramDocument(Object element) {
		IDocument doc = getDocument(element);
		if(doc instanceof IDiagramDocument)
			return (IDiagramDocument)doc;
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#disposeElementInfo(java.lang.Object, org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider.ElementInfo)
	 */
	protected void disposeElementInfo(Object element, ElementInfo info) {
		super.disposeElementInfo(element, info);
		Object content = info.fDocument.getContent();
		if(content instanceof Diagram && info.fDocument instanceof IDiagramDocument) {
			MEditingDomain domain = ((IDiagramDocument)info.fDocument).getEditingDomain();
			DiagramIOUtil.unload(domain, (Diagram)content);
			
			assert info instanceof DiagramStorageInfo;
			((DiagramStorageInfo)info).fListener.stopListening();
		}
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.StorageDocumentProvider#setDocumentContentFromStorage(org.eclipse.gmf.runtime.diagram.ui.editor.IDocument, org.eclipse.core.resources.IStorage)
	 */
	protected void setDocumentContentFromStorage(IDocument document, IStorage storage)
		throws CoreException {
		IDiagramDocument diagramDocument = (IDiagramDocument)document;
		MEditingDomain domain = diagramDocument.getEditingDomain();
		Diagram diagram = DiagramIOUtil.load(domain, storage, true, getProgressMonitor());
		document.setContent(diagram);
	}

	protected boolean setDocumentContent(IDocument document, IEditorInput editorInput)
		throws CoreException {
		if(editorInput instanceof StorageEditorInputProxy) {
			StorageEditorInputProxy diagramElement = (StorageEditorInputProxy)editorInput;

			((IDiagramDocument)document).setEditingDomain(diagramElement.getEditingDomain());
			boolean docContentSet = super.setDocumentContent(document, editorInput);
			return docContentSet;
		}
		return super.setDocumentContent(document, editorInput);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocumentProvider#createInputWithEditingDomain(org.eclipse.ui.IEditorInput, org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain)
	 */
	public IEditorInput createInputWithEditingDomain(IEditorInput editorInput, MEditingDomain domain) {
		if(editorInput instanceof IStorageEditorInput)
			return new StorageEditorInputProxy((IStorageEditorInput)editorInput, domain);
		assert false;
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document.StorageDocumentProvider#createNewElementInfo(org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument)
	 */
	public ElementInfo createNewElementInfo(IDocument document) {
		DiagramModificationListener listener = new DiagramModificationListener(this, (DiagramDocument)document);
		DiagramStorageInfo info = new DiagramStorageInfo(document, listener);
		listener.startListening();
		return info;
	}

}
