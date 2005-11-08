/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.providers;

import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.editparts.GeoshapesDiagramRootEditPart;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.CreateGraphicEditPartOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.CreateRootEditPartOperation;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Editpart provider for the geoshape diagram.
 * 
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.geoshapes.*
 */

public class DiagramEditPartProvider extends AbstractEditPartProvider {	
	
	/**
	 * Gets a diagram's editpart class.
	 * This method should be overridden by a provider if it wants to provide this service. 
	 * @param view the view to be <i>controlled</code> by the created editpart
	 */
	protected Class getDiagramEditPartClass(View view ) {
		return(DiagramEditPart.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {
		String dgrmType ="Geoshape"; //$NON-NLS-1$
		if (operation instanceof CreateRootEditPartOperation) {
			View view = ((CreateRootEditPartOperation)operation).getView();
			if (view instanceof Diagram && view.getType().equals(dgrmType)) 
				return true;
		}
		else if (operation instanceof CreateGraphicEditPartOperation) {
			View view = ((CreateGraphicEditPartOperation)operation).getView();
			if (view instanceof Diagram && view.getType().equals(dgrmType)) 
				return true;
		}
		return false;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.IEditPartProvider#createRootEditPart()
	 */
	public RootEditPart createRootEditPart(Diagram diagram) {
		return new GeoshapesDiagramRootEditPart();
	}
	
	
}
