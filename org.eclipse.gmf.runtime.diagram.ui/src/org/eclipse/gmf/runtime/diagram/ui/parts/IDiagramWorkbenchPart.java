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

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author melaasar
 *
 * The diagram workbench part interface
 */
public interface IDiagramWorkbenchPart extends IWorkbenchPart {

	/**
	 * Method getDiagramGraphicalViewer.
	 * @return IDiagramGraphicalViewer
	 */
	public IDiagramGraphicalViewer getDiagramGraphicalViewer();

	/**
	 * Method getEditDomain.
	 * @return DefaultEditDomain
	 */
	public IDiagramEditDomain getDiagramEditDomain();

	/**
	 * Method getDiagram.
	 * @return Diagram
	 */
	public Diagram getDiagram();

	/**
	 * Method getDiagramEditPart.
	 * @return DiagramEditPart
	 */
	public DiagramEditPart getDiagramEditPart();

}
