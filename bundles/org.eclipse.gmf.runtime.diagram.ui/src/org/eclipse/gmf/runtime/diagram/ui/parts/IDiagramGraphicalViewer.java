/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
 
/**
 * @author Tauseef A. Israr mailto: tisrar@rational.com
 * 
 * Gives functionality to:
 * 1. remove drag source listener
 * 2. remove drop target listener.
 *
 */
public interface IDiagramGraphicalViewer extends GraphicalViewer {
        
	/**
	 * Method getDiagramEditDomain.
	 * @return IDiagramEditDomain
	 */
	IDiagramEditDomain getDiagramEditDomain();

	/**
	 * Finds all editparts of a specific class type on the diagram that 
	 * have been registered for the given element.  
	 * @param elementIdStr the element's id string
	 * @param editPartClass the class of the editparts to be returned
	 * @return a List of editparts, if none exist an empty list is returned
	 */
	public List findEditPartsForElement(String elementIdStr, Class editPartClass);

	/**
	 * Registers an editpart for an element in the element/editpart registry.
	 * @param elementIdStr the element's id string
	 * @param ep the edit part to register for this element
	 */
	public void registerEditPartForElement(String elementIdStr, EditPart ep);

	/**
	 * Unregisters an editpart for a element in the element/editpart registry.
	 * @param elementIdStr the element's id string
	 * @param ep the edit part to register for this element
	 */
	public void unregisterEditPartForElement(String elementIdStr, EditPart ep);
}
