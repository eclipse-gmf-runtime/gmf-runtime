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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;

import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.MEditingDomainElement;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;


/**
 * FileEditorInput Proxy.
 * 
 * @author mgoyal
 *
 */
public class FileEditorInputProxy extends StorageEditorInputProxy
	implements IFileEditorInput, IPathEditorInput, IPersistableElement, MEditingDomainElement {
	/**
	 * @param proxied
	 * @param domain
	 */
	public FileEditorInputProxy(IFileEditorInput proxied, MEditingDomain domain) {
		super(proxied, domain);
		assert proxied != null && domain != null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPersistableElement#getFactoryId()
	 */
	public String getFactoryId() {
		IPersistableElement persistable = getPersistableElement();
		if(persistable != null)
			return persistable.getFactoryId();
		assert false;
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IFileEditorInput#getFile()
	 */
	public IFile getFile() {
		IFileEditorInput fileInput = getFileInput();
		if(fileInput != null)
			return fileInput.getFile();
		assert false;
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPathEditorInput#getPath()
	 */
	public IPath getPath() {
		IPathEditorInput pathInput = getPathInput();
		if(pathInput != null)
			return pathInput.getPath();
		assert false;
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPersistable#saveState(org.eclipse.ui.IMemento)
	 */
	public void saveState(IMemento memento) {
		IPersistableElement persistable = getPersistableElement();
		if(persistable != null)
			persistable.saveState(memento);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return fProxied.toString();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.MEditingDomainElement#getEditingDomain()
	 */
	public MEditingDomain getEditingDomain() {
		return editingDomain;
	}
	
	/**
	 * @return FileEditorInput
	 */
	private IFileEditorInput getFileInput() {
		return fProxied instanceof IFileEditorInput ? (IFileEditorInput)fProxied : null;
	}

	/**
	 * @return PathEditorInput
	 */
	private IPathEditorInput getPathInput() {
		return fProxied instanceof IPathEditorInput ? (IPathEditorInput)fProxied : null;
	}
	
	/**
	 * @return Persistable Element
	 */
	private IPersistableElement getPersistableElement() {
		return fProxied instanceof IPersistableElement ? (IPersistableElement)fProxied : null;
	}
}
