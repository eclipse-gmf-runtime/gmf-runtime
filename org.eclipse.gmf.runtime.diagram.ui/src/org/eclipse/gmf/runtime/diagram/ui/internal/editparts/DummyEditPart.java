/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 **/
package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;


/**
 * Dummy edit part class
 * 
 * @author mmostafa
 */
public class DummyEditPart extends GraphicalEditPart {

	protected IFigure createFigure() {
		return null;
	}
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param model
	 */
	public DummyEditPart(EObject model) {
		super(model);
	}

	protected void addNotationalListeners() {
		// no need for Listeners
	}

	protected void addSemanticListeners() {
		//no need for Listeners
	}
}
