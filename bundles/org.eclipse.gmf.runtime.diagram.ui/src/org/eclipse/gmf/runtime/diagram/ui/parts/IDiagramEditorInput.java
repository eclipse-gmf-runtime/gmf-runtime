/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.parts;

import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.IEditorInput;

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
