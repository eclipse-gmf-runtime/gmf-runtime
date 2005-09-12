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

package org.eclipse.gmf.runtime.diagram.ui.resources.editor.internal.ui.parts;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;


/**
 * An editor input that is file based.
 * 
 * @author wdiu, Wayne Diu
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.resources.editor.*
 * @deprecated
 */
public interface IFileResourceEditorInput extends IEditorInput {
	
	/**
	 * The IFile for this editor input
	 */
	public IFile getFile();
}
