/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.diagram.ui.internal.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.diagram.core.internal.util.MEditingDomainGetter;
import org.eclipse.gmf.runtime.diagram.core.internal.view.IDiagramView;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;

/**
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 * 
 * @author melaasar
 */
public abstract class AbstractDiagramView extends AbstractView implements IDiagramView {

	/**
	 * Incarnation Constructor
	 * @param state
	 */
	protected AbstractDiagramView(Object state) {
		super((Diagram) state);
	}
	
	/**
	 * Creation Constructor
	 * @param semanticAdapter
	 * @param diagramKind
	 */
	protected AbstractDiagramView(
		IAdaptable semanticAdapter, 
		String diagramKind) {
		super((Diagram)MEditingDomainGetter.getMEditingDomain(semanticAdapter).create(NotationPackage.eINSTANCE.getDiagram()));//TODO which MEditingDomain to use? Is it necessary? 
		Diagram diagram = getDiagram();
		diagram.getStyles().addAll(createStyles());

		if (diagramKind != null)
			setSemanticType(diagramKind);
		if (semanticAdapter != null)
			setSemanticElement((EObject) semanticAdapter.getAdapter(EObject.class));
		else
			// enforce a set to NULL
			setSemanticElement(null);

		// do the necessary initializations (creating children, setting properties...etc)
		decorateView(semanticAdapter, diagramKind);
	}

	/**
	 * Method NodeView.
	 * creation constructor
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @param index
	 * @param persisted
	 */
	protected void decorateView(IAdaptable semanticAdapter, String diagramKind){
		initializeFromPreferences();
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.view.IDiagramView#getConnectorChildren()
	 */
	public List getConnectors() {
		List connectors = Collections.EMPTY_LIST;
		Iterator edges = getDiagram().getEdges().iterator();
		
		if (edges.hasNext()) {
			connectors = new ArrayList();
			while(edges.hasNext())
				connectors.add(incarnateView((Edge)edges.next()));
		}
		return connectors;
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.view.IDiagramView#getDiagram()
	 */
	public Diagram getDiagram() {
		return (Diagram) getViewElement();
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.view.AbstractView#createStyles()
	 */
	protected List createStyles() {
		List styles = new ArrayList();
		styles.add(NotationFactory.eINSTANCE.createDiagramStyle());
		return styles;
	}
}
