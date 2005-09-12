/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.editor;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

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