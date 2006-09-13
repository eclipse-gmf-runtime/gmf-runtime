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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.util.HashUtil;


/**
 * @author mgoyal
 *
 */
public class EditorInputProxy
	implements IEditorInput, MEditingDomainElement {

	protected IEditorInput fProxied = null;
	protected TransactionalEditingDomain editingDomain = null;
	
	/**
	 * @param input IEditorInput
	 * @param domain EditingDomain
	 */
	public EditorInputProxy(IEditorInput input, TransactionalEditingDomain domain) {
		assert input != null && domain != null;
		fProxied = input;
		editingDomain = domain;
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
	public TransactionalEditingDomain getEditingDomain() {
		return editingDomain;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if(this == arg0)
			return true;
		if(arg0 instanceof EditorInputProxy) {
			EditorInputProxy proxy = (EditorInputProxy)arg0;
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
