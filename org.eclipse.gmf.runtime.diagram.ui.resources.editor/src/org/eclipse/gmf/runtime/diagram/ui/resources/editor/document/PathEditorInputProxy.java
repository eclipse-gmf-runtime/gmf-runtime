/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
