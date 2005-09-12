/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.parts;

import org.eclipse.ui.IEditorInput;

import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * Diagram Editor Input interface.
 * 
 * @author melaasar  
 */
public interface IDiagramEditorInput extends IEditorInput {

	/**
	 * Method getDiagram.
	 * @return Diagram
	 */
	public Diagram getDiagram();

}
