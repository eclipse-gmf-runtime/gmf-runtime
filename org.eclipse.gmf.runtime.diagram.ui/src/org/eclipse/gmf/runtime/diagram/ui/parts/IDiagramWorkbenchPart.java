/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
