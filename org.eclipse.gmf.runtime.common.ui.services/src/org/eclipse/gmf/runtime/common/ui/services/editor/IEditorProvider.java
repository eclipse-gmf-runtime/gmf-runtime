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

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

/**
 * An interface for manipulating editors
 * 
 * @author melaasar
 */
public interface IEditorProvider
	extends IProvider {

	/**
	 * Opens an editor with the given editor input
	 * 
	 * @param editorInput
	 *            the editor input object
	 * @return the opened IEditorPart
	 */
	public IEditorPart openEditor(IEditorInput editorInput);

}