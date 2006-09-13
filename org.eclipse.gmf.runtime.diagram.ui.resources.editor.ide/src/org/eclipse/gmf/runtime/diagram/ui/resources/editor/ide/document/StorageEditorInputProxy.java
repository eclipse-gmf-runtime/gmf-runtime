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

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.ide.document;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.util.HashUtil;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.EditorInputProxy;
import org.eclipse.gmf.runtime.diagram.ui.resources.editor.document.MEditingDomainElement;
import org.eclipse.ui.IStorageEditorInput;


/**
 * @author mgoyal
 *
 */
public class StorageEditorInputProxy extends EditorInputProxy
	implements IStorageEditorInput, MEditingDomainElement {

	/**
	 * @param input IEditorInput
	 * @param domain EditingDomain
	 */
	public StorageEditorInputProxy(IStorageEditorInput input, TransactionalEditingDomain domain) {
		super(input, domain);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IStorageEditorInput#getStorage()
	 */
	public IStorage getStorage()
		throws CoreException {
		if(fProxied != null && fProxied instanceof IStorageEditorInput)
			return ((IStorageEditorInput)fProxied).getStorage();
		assert false;
		return null;
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
