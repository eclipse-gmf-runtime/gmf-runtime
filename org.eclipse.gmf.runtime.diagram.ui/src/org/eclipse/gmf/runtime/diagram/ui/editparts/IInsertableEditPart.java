/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.editparts;

import org.eclipse.gef.EditPart;

import org.eclipse.gmf.runtime.emf.type.core.IElementType;



/**
 * @author sshaw
 *
 * Interface for an editpart that can support a default insert operation.
 */
public interface IInsertableEditPart extends EditPart {
	
	/**
	 * getElementType
	 * Method for returning the semantic type of the element that can be inserted by default
	 * into this editpart.
	 * 
	 * @return IElementType
	 */
	IElementType getElementType();

}
