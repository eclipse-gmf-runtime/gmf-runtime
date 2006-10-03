/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.document;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditorInput;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.l10n.EditorMessages;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;


/**
 * An implementation of <code>IDiagramDocumentProvider</code> that
 * connects a <code>org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditorInput</code>
 * and a <code>IDiagramDocument</code>.
 * 
 * @author mgoyal
 */
public class DiagramInputDocumentProvider
	extends AbstractDocumentProvider
	implements IDiagramDocumentProvider {
	
	/**
	 * Bundle of all required information to allow {@link org.eclipse.core.resources.IStorage} as underlying document resources.
	 */
	protected class DiagramResourceInfo extends ElementInfo {

		/** The flag representing the cached state whether the storage is modifiable. */
		public boolean fIsModifiable= false;
		/** The flag representing the cached state whether the storage is read-only. */
		public boolean fIsReadOnly= true;
		/** The flag representing the need to update the cached flag.  */
		public boolean fUpdateCache= true;
		
		public DiagramModificationListener fListener = null;

		/**
		 * Creates a new storage info.
		 *
		 * @param document the document
		 * @param model the annotation model
		 */
		public DiagramResourceInfo(IDocument document, DiagramModificationListener listener) {
			super(document);
			fListener = listener;
		}
	}

	/*
	 * @see AbstractDocumentProvider#createDocument(Object)
	 */
	protected IDocument createDocument(Object element) throws CoreException {

		if (element instanceof IDiagramEditorInput) {
			IDocument document= createEmptyDocument();
			if (setDocumentContent(document, (IEditorInput) element)) {
				setupDocument(element, document);
				return document;
			}
		}

		return null;
	}

	/**
	 * Sets up the given document as it would be provided for the given element. The
	 * content of the document is not changed. This default implementation is empty.
	 * Subclasses may reimplement.
	 *
	 * @param element the blue-print element
	 * @param document the document to set up
	 */
	protected void setupDocument(Object element, IDocument document) {
		// for subclasses
	}

	/**
	 * Factory method for creating empty documents.
	 * @return the newly created document
	 * 
	 */
	protected IDocument createEmptyDocument() {
		return new DiagramDocument();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#createElementInfo(java.lang.Object)
	 */
	protected ElementInfo createElementInfo(Object element) throws CoreException {
		if (element instanceof IDiagramEditorInput) {

			IDocument document= null;
			IStatus status= null;

			try {
				document= createDocument(element);
			} catch (CoreException x) {
				handleCoreException(x, EditorMessages.DiagramInputDocumentProvider_createElementInfo);
				status= x.getStatus();
				document= createEmptyDocument();
			}

			DiagramModificationListener listener = new DiagramModificationListener(this, (DiagramDocument)document);
			ElementInfo info= new DiagramResourceInfo(document, listener);
			info.fStatus= status;
			listener.startListening();

			return info;
		}

		return super.createElementInfo(element);
	}
	
	protected void disposeElementInfo(Object element, ElementInfo info) {
		super.disposeElementInfo(element, info);
		
		((DiagramResourceInfo)info).fListener.stopListening();
	}

	/**
	 * Initializes the given document from the given editor input using the given character encoding.
	 *
	 * @param document the document to be initialized
	 * @param editorInput the input from which to derive the content of the document
	 * @param encoding the character encoding used to read the editor input
	 * @return <code>true</code> if the document content could be set, <code>false</code> otherwise
	 * @throws CoreException if the given editor input cannot be accessed
	 * 
	 */
	protected boolean setDocumentContent(IDocument document, IEditorInput editorInput) throws CoreException {
		if (editorInput instanceof IDiagramEditorInput) {
			Diagram diagram = ((IDiagramEditorInput) editorInput).getDiagram();
			document.setContent(diagram);
			return true;
		}
		return false;
	}

	/*
	 * @see AbstractDocumentProvider#doSaveDocument(IProgressMonitor, Object, IDocument, boolean)
	 */
	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite) throws CoreException {
		// for subclasses
	}

	/**
	 * Defines the standard procedure to handle <code>CoreExceptions</code>. Exceptions
	 * are written to the plug-in log.
	 *
	 * @param exception the exception to be logged
	 * @param message the message to be logged
	 * 
	 */
	protected void handleCoreException(CoreException exception, String message) {

		Bundle bundle = Platform.getBundle(PlatformUI.PLUGIN_ID);
		ILog log= Platform.getLog(bundle);

		if (message != null)
			log.log(new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, 0, message, exception));
		else
			log.log(exception.getStatus());
	}

	/**
	 * Updates the internal cache for the given input.
	 *
	 * @param input the input whose cache will be updated
	 * @throws CoreException if the storage cannot be retrieved from the input
	 * 
	 */
	protected void updateCache(IDiagramEditorInput input) throws CoreException {
		DiagramResourceInfo info= (DiagramResourceInfo) getElementInfo(input);
		if (info != null) {
			IStorage storage= (IStorage)input.getAdapter(IStorage.class);
			if (storage != null) {
				boolean readOnly= storage.isReadOnly();
				info.fIsReadOnly=  readOnly;
				info.fIsModifiable= !readOnly;
			}
			info.fUpdateCache= false;
		}
	}

	/*
	 * @see IDocumentProviderExtension#isReadOnly(Object)
	 * 
	 */
	public boolean isReadOnly(Object element) {
		if (element instanceof IDiagramEditorInput) {
			DiagramResourceInfo info= (DiagramResourceInfo) getElementInfo(element);
			if (info != null) {
				if (info.fUpdateCache) {
					try {
						updateCache((IDiagramEditorInput) element);
					} catch (CoreException x) {
						handleCoreException(x, EditorMessages.DiagramInputDocumentProvider_isReadOnly);
					}
				}
				return info.fIsReadOnly;
			}
		}
		return super.isReadOnly(element);
	}

	/*
	 * @see IDocumentProviderExtension#isModifiable(Object)
	 * 
	 */
	public boolean isModifiable(Object element) {
		if (element instanceof IDiagramEditorInput) {
			DiagramResourceInfo info= (DiagramResourceInfo) getElementInfo(element);
			if (info != null) {
				if (info.fUpdateCache) {
					try {
						updateCache((IDiagramEditorInput) element);
					} catch (CoreException x) {
						handleCoreException(x, EditorMessages.DiagramInputDocumentProvider_isModifiable);
					}
				}
				return info.fIsModifiable;
			}
		}
		return super.isModifiable(element);
	}

	/*
	 * @see AbstractDocumentProvider#doUpdateStateCache(Object)
	 * 
	 */
	protected void doUpdateStateCache(Object element) throws CoreException {
		if (element instanceof IDiagramEditorInput) {
			DiagramResourceInfo info= (DiagramResourceInfo) getElementInfo(element);
			if (info != null)
				info.fUpdateCache= true;
		}
		super.doUpdateStateCache(element);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editor.AbstractDocumentProvider#getOperationRunner(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IRunnableContext getOperationRunner(IProgressMonitor monitor) {
		return null;
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
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.IDiagramDocumentProvider#createInputWithEditingDomain(org.eclipse.ui.IEditorInput, org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain)
	 */
	public IEditorInput createInputWithEditingDomain(IEditorInput editorInput, TransactionalEditingDomain domain) {
		if(editorInput instanceof IDiagramEditorInput)
			return new EditorInputProxy(editorInput, domain);
		assert false;
		return null;
	}
}
