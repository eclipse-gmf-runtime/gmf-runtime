/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IStorageEditorInput;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.PathEditorInputProxy;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.utils.DiagramIOUtil;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import com.ibm.xtools.notation.Diagram;


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
			
			IDiagramDocument diagramDocument = (IDiagramDocument)info.fDocument;
			diagramDocument.disableDiagramListener();
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
		if(editorInput instanceof PathEditorInputProxy) {
			PathEditorInputProxy diagramElement = (PathEditorInputProxy)editorInput;

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
}
