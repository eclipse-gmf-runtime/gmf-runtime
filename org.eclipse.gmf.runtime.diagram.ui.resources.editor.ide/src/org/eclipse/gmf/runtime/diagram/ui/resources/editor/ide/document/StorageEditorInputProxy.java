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
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

import org.eclipse.gmf.runtime.common.core.util.HashUtil;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.MEditingDomainElement;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;


/**
 * @author mgoyal
 *
 */
public class StorageEditorInputProxy
	implements IStorageEditorInput, MEditingDomainElement {

	IStorageEditorInput fProxied = null;
	MEditingDomain editingDomain = null;
	
	/**
	 * @param input IEditorInput
	 * @param domain EditingDomain
	 */
	public StorageEditorInputProxy(IStorageEditorInput input, MEditingDomain domain) {
		fProxied = input;
		editingDomain = domain;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStorageEditorInput#getStorage()
	 */
	public IStorage getStorage()
		throws CoreException {
		if(fProxied != null)
			return fProxied.getStorage();
		assert false;
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return fProxied.exists();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return fProxied.getImageDescriptor();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		return fProxied.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return fProxied.getPersistable();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return fProxied.getToolTipText();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return fProxied.getAdapter(adapter);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.MEditingDomainElement#getEditingDomain()
	 */
	public MEditingDomain getEditingDomain() {
		return editingDomain;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if(this == arg0)
			return true;
		if(arg0 instanceof StorageEditorInputProxy) {
			StorageEditorInputProxy proxy = (StorageEditorInputProxy)arg0;
			if(proxy.editingDomain.equals(editingDomain) &&
					proxy.fProxied.equals(fProxied))
				return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return HashUtil.hash(HashUtil.hash(fProxied), editingDomain);
	}
}
