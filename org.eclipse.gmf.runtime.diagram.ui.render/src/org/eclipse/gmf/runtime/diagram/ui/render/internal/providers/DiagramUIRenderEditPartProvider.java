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
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.render.internal.providers;

import org.eclipse.gef.RootEditPart;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.diagram.ui.render.editparts.RenderedDiagramRootEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.CreateRootEditPartOperation;
import org.eclipse.gmf.runtime.notation.Diagram;

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
	public RootEditPart createRootEditPart(Diagram diagram) {
		return new RenderedDiagramRootEditPart(diagram.getMeasurementUnit());
	}
}