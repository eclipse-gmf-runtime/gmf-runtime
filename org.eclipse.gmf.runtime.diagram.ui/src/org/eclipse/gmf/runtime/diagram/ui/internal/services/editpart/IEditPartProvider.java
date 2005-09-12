/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.services.editpart;

import org.eclipse.gef.RootEditPart;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Defines the factory methods for creating the various editpart elements.
 */
/*
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public interface IEditPartProvider extends IProvider 
{

	/** Create an editpart mapped to the supplied view element. */
	public IGraphicalEditPart createGraphicEditPart(View view);
	
	/**
	 * Creates a <code>RootEditPart</code>. 
	 * @return the <code>RootEditPart</code>
	 */
	public RootEditPart createRootEditPart();
}

