/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
