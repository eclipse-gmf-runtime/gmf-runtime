/******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DiagramModificationListener;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.DocumentEvent;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocumentProvider;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.EditorIDEPlugin;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.internal.l10n.EditorMessages;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.EditorStatusCodes;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.utils.DiagramIOUtil;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.util.ResourceUtil;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;


/**
 * An implementation of <code>IDiagramDocumentProvider</code> for 
 * connecting <code>org.eclipse.ui.IFileEditorInput</code> and
 * <code>IDiagramDocument</code>.
 * 
 * @author mgoyal
 *
 */
public class FileDiagramDocumentProvider
	extends FileDocumentProvider implements IDiagramDocumentProvider {
	
	public class DiagramFileInfo extends FileInfo {
		DiagramModificationListener fListener;
		/**
		 * Creates and returns a new file info.
		 *
		 * @param document the document
		 * @param model the annotation model
		 * @param fileSynchronizer the file synchronizer
		 */
		public DiagramFileInfo(IDocument document, FileSynchronizer fileSynchronizer, DiagramModificationListener listener) {
			super(document, fileSynchronizer);
			fListener = listener;
		}
		
		public void documentAboutToBeChanged(DocumentEvent event) {
			if(event.getEventKind() == DocumentEvent.CONTENT_REPLACED) {
				// release the existing content.
				IDiagramDocument diagramDoc = ((IDiagramDocument)event.getDocument());
				Diagram existingContent = diagramDoc.getDiagram();
				URI existingURI = null;
				if(existingContent != null) {
					existingURI = existingContent.eResource().getURI();
					DiagramIOUtil.unload(diagramDoc.getEditingDomain(), existingContent);
				}

				IDiagramDocument diagramDocument = (IDiagramDocument)event.getEventInfo();
				Diagram newContent = diagramDocument.getDiagram();
				if(newContent != null && existingURI != null) {
					newContent.eResource().setURI(existingURI);
				}
			}
			super.documentAboutToBeChanged(event);
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
			DiagramIOUtil.unload(((IDiagramDocument)info.fDocument).getEditingDomain(), (Diagram)content);

			assert info instanceof DiagramFileInfo;
			((DiagramFileInfo)info).fListener.stopListening();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.StorageDocumentProvider#setDocumentContentFromStorage(org.eclipse.gmf.runtime.diagram.ui.editor.IDocument, org.eclipse.core.resources.IStorage)
	 */
	protected void setDocumentContentFromStorage(IDocument document, IStorage storage)
		throws CoreException {
		Diagram diagram = (Diagram)document.getContent();
		if(diagram != null) {
			Resource resource = diagram.eResource();
			IFile resourceFile = ResourceUtil.getFile(resource);
			// unload if the resourceFile and storage is same.
			// if not same throw exception.
			if(resourceFile != null) {
				if(resourceFile.equals(storage)) {
					document.setContent(null);
				} else {
					throw new CoreException(new Status(IStatus.ERROR, EditorIDEPlugin.getPluginId(), EditorStatusCodes.ERROR, EditorMessages.FileDocumentProvider_handleElementContentChanged, null));
				}
			}
		}
		IDiagramDocument diagramDocument = (IDiagramDocument)document;
		MEditingDomain domain = diagramDocument.getEditingDomain();

		diagram = DiagramIOUtil.load(domain, storage, true, getProgressMonitor());
		document.setContent(diagram);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.editor.FileDocumentProvider#saveDocumentToFile(org.eclipse.gmf.runtime.diagram.ui.editor.IDocument, org.eclipse.core.resources.IFile, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void saveDocumentToFile(IDocument document, IFile file, boolean overwrite, IProgressMonitor monitor)
		throws CoreException {
		Diagram diagram = (Diagram)document.getContent();
		Resource resource = diagram.eResource();
		IFile resourceFile = ResourceUtil.getFile(resource);
		// if the diagram in the document is referring to another file, then we should
		// create a copy of this diagram and save it to the new file, save as scenario.
		if(resourceFile != null && !resourceFile.equals(file)) {
			diagram = copyDiagramResource(diagram);
		}
		IDiagramDocument diagramDocument = (IDiagramDocument)document;
		MEditingDomain domain = diagramDocument.getEditingDomain();

		DiagramIOUtil.save(domain, file, diagram, overwrite, DiagramIOUtil.hasUnrecognizedData(diagram.eResource()), monitor);
	}
	
	private Diagram copyDiagramResource(Diagram sourceDiagram) {
		Resource sourceRes = sourceDiagram.eResource();
		EList contents = sourceRes.getContents();
		
		int indexOfDiagram = contents.indexOf(sourceDiagram);
		Collection copiedContents = EcoreUtil.copyAll(contents);
		
		Resource newResource = ResourceUtil.getEditingDomain().createResource(null);
		newResource.getContents().addAll(copiedContents);
		
		return (Diagram)newResource.getContents().get(indexOfDiagram);
	}

	/**
	 * Updates the element info to a change of the file content and sends out
	 * appropriate notifications.
	 *
	 * @param fileEditorInput the input of an text editor
	 */
	protected void handleElementContentChanged(IFileEditorInput fileEditorInput) {
		// unload the diagram from the MSL.
		// Since MSL won't load another resource from same file if one is already loaded.
		FileInfo info= (FileInfo) getElementInfo(fileEditorInput);
		if (info == null && !(info.fDocument instanceof IDiagramDocument))
			return;
		assert fileEditorInput instanceof FileEditorInputProxy;
		IDiagramDocument diagramDoc = (IDiagramDocument)info.fDocument;
		Diagram existingContent = diagramDoc.getDiagram();
		if(existingContent != null)
			DiagramIOUtil.unload(((FileEditorInputProxy)fileEditorInput).getEditingDomain(), existingContent);
		
		super.handleElementContentChanged(fileEditorInput);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document.FileDocumentProvider#createFileInfo(org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument, org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document.FileDocumentProvider.FileSynchronizer, org.eclipse.ui.IFileEditorInput)
	 */
	protected FileInfo createFileInfo(IDocument document, FileSynchronizer synchronizer, IFileEditorInput input) {
		assert document instanceof DiagramDocument; 
		
		DiagramModificationListener diagramListener = new FileDiagramModificationListener(this, (DiagramDocument)document, input);
		DiagramFileInfo info = new DiagramFileInfo(document, synchronizer, diagramListener);
		
		diagramListener.startListening();
		return info;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.StorageDocumentProvider#setDocumentContent(org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument, org.eclipse.ui.IEditorInput)
	 */
	protected boolean setDocumentContent(IDocument document,
			IEditorInput editorInput)
		throws CoreException {
		if (editorInput instanceof FileEditorInputProxy) {
			FileEditorInputProxy diagramElement = (FileEditorInputProxy) editorInput;

			((IDiagramDocument) document).setEditingDomain(diagramElement
				.getEditingDomain());
			boolean docContentSet = super.setDocumentContent(document,
				editorInput);
			return docContentSet;
		}
		return super.setDocumentContent(document, editorInput);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocumentProvider#createInputWithEditingDomain(org.eclipse.ui.IEditorInput, org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain)
	 */
	public IEditorInput createInputWithEditingDomain(IEditorInput editorInput, MEditingDomain domain) {
		if(editorInput instanceof IFileEditorInput)
			return new FileEditorInputProxy((IFileEditorInput)editorInput, domain);
		assert false;
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.AbstractDocumentProvider#doSaveDocument(org.eclipse.core.runtime.IProgressMonitor, java.lang.Object, org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDocument, boolean)
	 */
	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite)
		throws CoreException {
		if(element instanceof IFileEditorInput) {
			// refresh the file for diagram input.
			IFileEditorInput input= (IFileEditorInput) element;
			IFile file= input.getFile();
			file.refreshLocal(IResource.DEPTH_ZERO, getProgressMonitor());
		}
		super.doSaveDocument(monitor, element, document, overwrite);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#getSaveRule(java.lang.Object)
	 */
	protected ISchedulingRule getSaveRule(Object element) {
		if (element instanceof IFileEditorInput) {
			IFileEditorInput input= (IFileEditorInput) element;
			return computeSaveSchedulingRule(input.getFile());
		}
		return null;
	}
	
	/**
	 * Computes the scheduling rule needed to create or modify a resource. If
	 * the resource exists, its modify rule is returned. If it does not, the
	 * resource hierarchy is iterated towards the workspace root to find the
	 * first parent of <code>toCreateOrModify</code> that exists. Then the
	 * 'create' rule for the last non-existing resource is returned.
	 *
	 * @param toCreateOrModify the resource to create or modify
	 * @return the minimal scheduling rule needed to modify or create a resource
	 */
	private ISchedulingRule computeSaveSchedulingRule(IResource toCreateOrModify) {
		if (toCreateOrModify.exists() && toCreateOrModify.isSynchronized(IResource.DEPTH_ZERO))
			return fResourceRuleFactory.modifyRule(toCreateOrModify);

		IResource parent= toCreateOrModify;
		do {
			 /*
			 * XXX This is a workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=67601
			 * IResourceRuleFactory.createRule should iterate the hierarchy itself.
			 */
			toCreateOrModify= parent;
			parent= toCreateOrModify.getParent();
		} while (parent != null && !parent.exists() && !parent.isSynchronized(IResource.DEPTH_ZERO));

		return fResourceRuleFactory.createRule(toCreateOrModify);
	}
	
}
