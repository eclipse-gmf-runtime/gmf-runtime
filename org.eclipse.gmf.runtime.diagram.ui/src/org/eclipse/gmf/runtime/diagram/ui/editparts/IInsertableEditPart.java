/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
