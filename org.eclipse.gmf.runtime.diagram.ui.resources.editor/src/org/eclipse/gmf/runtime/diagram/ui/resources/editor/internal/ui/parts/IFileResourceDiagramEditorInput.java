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

import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditorInput;


/**
 * An editor input that's based on both an IFile and a Diagram.
 * 
 * @author wdiu, Wayne Diu
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.resources.editor.*
 * @deprecated
 */
public interface IFileResourceDiagramEditorInput extends IDiagramEditorInput, IFileResourceEditorInput {

	//This interface contains no additional methods

}
