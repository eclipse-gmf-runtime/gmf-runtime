/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
