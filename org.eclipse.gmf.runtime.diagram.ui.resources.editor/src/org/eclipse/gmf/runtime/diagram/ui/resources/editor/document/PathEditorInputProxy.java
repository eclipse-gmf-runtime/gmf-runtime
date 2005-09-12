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

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.document;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IPathEditorInput;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;


/**
 * @author mgoyal
 *
 */
public class PathEditorInputProxy extends EditorInputProxy
	implements IPathEditorInput, MEditingDomainElement {

	/**
	 * @param input IEditorInput
	 * @param domain EditingDomain
	 */
	public PathEditorInputProxy(IPathEditorInput input, MEditingDomain domain) {
		super(input, domain);
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPathEditorInput#getPath()
	 */
	public IPath getPath() {
		IPathEditorInput pathEditorInput = getPathEditorInput();
		if(pathEditorInput != null)
			return pathEditorInput.getPath();
		assert false;
		return null;
	}
	
	/**
	 * @return PathEditorInput
	 */
	private IPathEditorInput getPathEditorInput() {
		return fProxied instanceof IPathEditorInput ? (IPathEditorInput) fProxied
			: null;
	}
}
