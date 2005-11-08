/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.editparts;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramRootEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.IMapMode;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapModeTypes;


/**
 * Override to provide the identity map mode for the editor in
 * instead of the HiMetric default.
 * 
 * @author sshaw
 *
 */
public class GeoshapesDiagramRootEditPart
	extends DiagramRootEditPart {

	/* 
	 * Overridden to use the identity mapmode for this editor.  This means that there is no
	 * mapping of device coordinates to an absolute measurement for persistance.  Some clients
	 * may not need or desire the precision of HiMetric coordinate system and by using the
	 * Identity map mode it allows easier reuse of pure GEF figures.
	 */
	public IMapMode getMapMode() {
		return MapModeTypes.IDENTITY_MM;
	}

}
