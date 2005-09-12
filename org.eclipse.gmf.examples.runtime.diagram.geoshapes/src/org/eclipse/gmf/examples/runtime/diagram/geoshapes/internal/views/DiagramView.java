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

package org.eclipse.gmf.examples.runtime.diagram.geoshapes.internal.views;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.diagram.ui.internal.view.AbstractDiagramView;

/**
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.geoshapes.*
 *
 * An implementation of a GEO diagram view
 */
public class DiagramView extends AbstractDiagramView {

	/**
	 * Incarnation Constructor
	 * @param state
	 */
	public DiagramView(Object state) {
		super(state);
	}
	
	/**
	 * Creation Constructor
	 * @param semanticAdapter
	 * @param diagramKind
	 */
	public DiagramView(
		IAdaptable semanticAdapter, 
		String diagramKind) {
		super(semanticAdapter, diagramKind);
	}

}
