/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.editor;

import org.eclipse.ui.IEditorInput;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * An operation that opens an editor with a given input
 * 
 * @author melaasar
 */
public class OpenEditorOperation
	implements IOperation {

	/**
	 * the input to be passed to the editor
	 */
	private IEditorInput editorInput;

	/**
	 * Constructs a new OpenEditorOperation with the given editor input
	 * 
	 * @param anEditorInput
	 *            the editor input
	 */
	protected OpenEditorOperation(IEditorInput anEditorInput) {
		assert null != anEditorInput;

		this.editorInput = anEditorInput;
	}

	/**
	 * Retrieves the editor input
	 * 
	 * @return IEditorInput
	 */
	public IEditorInput getEditorInput() {
		return editorInput;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IEditorProvider) provider).openEditor(getEditorInput());
	}

}