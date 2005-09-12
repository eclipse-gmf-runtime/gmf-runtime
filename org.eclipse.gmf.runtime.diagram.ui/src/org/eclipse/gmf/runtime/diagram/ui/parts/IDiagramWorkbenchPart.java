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

import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.Diagram;

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
