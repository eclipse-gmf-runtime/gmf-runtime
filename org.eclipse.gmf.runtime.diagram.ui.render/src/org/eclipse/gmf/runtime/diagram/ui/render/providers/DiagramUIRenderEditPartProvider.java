/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.          	       |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.render.providers;

import org.eclipse.gef.RootEditPart;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart.CreateRootEditPartOperation;
import org.eclipse.gmf.runtime.diagram.ui.render.editparts.RenderedDiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;

/**
 * EditPart provider for the Diagram UI Render plug-in.
 * 
 * @author cmahoney
 */
public class DiagramUIRenderEditPartProvider
	extends AbstractEditPartProvider {

	/**
	 * Provides for a <code>CreateRootEditPartOperation</code> only.
	 */
	public boolean provides(IOperation operation) {
		if (operation instanceof CreateRootEditPartOperation) {
			return true;
		}
		return false;
	}

	/**
	 * Creates a diagram root editpart that supports rendering of images.
	 */
	public RootEditPart createRootEditPart() {
		return new RenderedDiagramRootEditPart();
	}
}