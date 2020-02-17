/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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