/******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories.AndGateViewFactory;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories.CircuitViewFactory;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories.ConnectionPointViewFactory;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories.LEDViewFactory;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories.LogicFlowContainerViewFactory;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories.OrGateViewFactory;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.views.factories.XORGateViewFactory;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;
import org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.optimal.CompartmentViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.optimal.ConnectorViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.optimal.ListCompartmentViewFactory;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.optimal.StandardDiagramViewFactory;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 *
 * View provider for the logic diagram.
 */
public class LogicViewProvider extends AbstractViewProvider { 

	HashMap diagramMap = new HashMap(); 
	{
		diagramMap.put("logic", StandardDiagramViewFactory.class);//$NON-NLS-1$
	}
	
//	 Map to hold the Node Views
	private Map nodeMap = new HashMap();
	{
		nodeMap.put( SemanticPackage.eINSTANCE.getLED(), LEDViewFactory.class );
		nodeMap.put( SemanticPackage.eINSTANCE.getFlowContainer(), LogicFlowContainerViewFactory.class );
		nodeMap.put( SemanticPackage.eINSTANCE.getCircuit(), CircuitViewFactory.class );
		nodeMap.put( SemanticPackage.eINSTANCE.getAndGate(), AndGateViewFactory.class );
		nodeMap.put( SemanticPackage.eINSTANCE.getOrGate(), OrGateViewFactory.class );
		nodeMap.put( SemanticPackage.eINSTANCE.getXORGate(), XORGateViewFactory.class );
		nodeMap.put( SemanticPackage.eINSTANCE.getInputTerminal(), ConnectionPointViewFactory.class );
		nodeMap.put( SemanticPackage.eINSTANCE.getOutputTerminal(), ConnectionPointViewFactory.class );
		nodeMap.put( SemanticPackage.eINSTANCE.getInputOutputTerminal(), ConnectionPointViewFactory.class );
		
		// Shape Compartments
		nodeMap.put(LogicConstants.LOGIC_SHAPE_COMPARTMENT, CompartmentViewFactory.class); 
		// List Compartments
		nodeMap.put(LogicConstants.LOGIC_FLOW_COMPARTMENT, ListCompartmentViewFactory.class); 
	}
	
	// Map to hold the Line/Connector Views
	private Map connectorMap = new HashMap();
	{
		connectorMap.put(SemanticPackage.eINSTANCE.getWire(), ConnectorViewFactory.class);
	}
	
	/**
	 * Returns the shape view class to instantiate based on the passed params
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @return Class
	 */
	protected Class getNodeViewClass(
		IAdaptable semanticAdapter,
		View containerView,
		String semanticHint) {
		
		Class clazz = null;
		
		if (semanticHint != null &&
			semanticHint.length() > 0)
			clazz = (Class)nodeMap.get(semanticHint);
		
		if (clazz == null)
			clazz = (Class)nodeMap.get(getSemanticEClass(semanticAdapter));
		
		return clazz;
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.providers.AbstractViewProvider#getDiagramViewClass(IAdaptable, java.lang.String)
	 */
	protected Class getDiagramViewClass(IAdaptable semanticAdapter, String diagramKind) {
		return (Class) diagramMap.get(diagramKind);
	}
	
	/**
	 * Returns the connector view class to instantiate based on the passed
	 * params
	 * 
	 * @param semanticAdapter
	 * @param containerView
	 * @param semanticHint
	 * @return Class
	 */
	protected Class getEdgeViewClass(IAdaptable semanticAdapter,
			View containerView, String semanticHint) {
		return (Class) connectorMap.get(getSemanticEClass(semanticAdapter));
	}
	
	public static boolean isGateView(View view){
		EObject element = view.getElement();
		EClass eClass = element.eClass();
		if (eClass == SemanticPackage.eINSTANCE.getAndGate() ||
			eClass == SemanticPackage.eINSTANCE.getOrGate() ||
			eClass == SemanticPackage.eINSTANCE.getXORGate())
			return true;
		return false;
	}
}

