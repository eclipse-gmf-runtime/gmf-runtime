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

package org.eclipse.gmf.runtime.diagram.core.internal.view;

import java.util.List;

import org.eclipse.gmf.runtime.notation.Diagram;

/**
 * A facade inteface for the diagram views
 * @deprecated View Facades are deprectaed and will be removed soon; use the 
 * notation view instead; you can reach it by calling getModel on a view EditPart
 * or by using the view service to create a new view
 * @author melaasar
 */
public interface IDiagramView extends IContainerView {

	/**
	 * Gets all the connectors in the diagram
	 * The connectors are currently stored in the diagram, however, this
	 * could change in the future (might get stored in source shapes)
	 * @return List the connector children list (could be an empty list)
	 */
	public List getConnectors();
	
	/**
	 * Method getDiagram. gets the view casted into
	 * Diagram
	 * 
	 * @return Diagram
	 */
	public Diagram getDiagram() ;

}
