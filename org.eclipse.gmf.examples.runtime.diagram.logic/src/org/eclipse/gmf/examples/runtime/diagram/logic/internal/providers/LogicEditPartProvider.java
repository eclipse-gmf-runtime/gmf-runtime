/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.providers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.CircuitEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.TerminalEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LEDEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LogicFlowCompartmentEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LogicFlowContainerEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LogicGateEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.LogicShapeCompartmentEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.internal.editparts.WireEditPart;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.SemanticPackage;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import com.ibm.xtools.notation.View;

/**
 * Editpart provider for the logic diagram.
 * 
 * @author qili
 * @canBeSeenBy org.eclipse.gmf.examples.runtime.diagram.logic.*
 */

public class LogicEditPartProvider extends AbstractEditPartProvider {	
	
	/** list of supported shape editparts. */
	private Map shapeMap = new HashMap();
	{
		shapeMap.put( SemanticPackage.eINSTANCE.getLED(), LEDEditPart.class );
		shapeMap.put( SemanticPackage.eINSTANCE.getFlowContainer(), LogicFlowContainerEditPart.class );
		shapeMap.put( SemanticPackage.eINSTANCE.getCircuit(), CircuitEditPart.class );
		shapeMap.put( SemanticPackage.eINSTANCE.getOrGate(), LogicGateEditPart.class );
		shapeMap.put( SemanticPackage.eINSTANCE.getAndGate(), LogicGateEditPart.class );
		shapeMap.put( SemanticPackage.eINSTANCE.getXORGate(), LogicGateEditPart.class );
		shapeMap.put( SemanticPackage.eINSTANCE.getInputTerminal(), TerminalEditPart.class );
		shapeMap.put( SemanticPackage.eINSTANCE.getOutputTerminal(), TerminalEditPart.class );
		shapeMap.put( SemanticPackage.eINSTANCE.getInputOutputTerminal(), TerminalEditPart.class );
	}
	
	/** list of supported connector editparts. */
	private Map connectorMap = new HashMap();
	{
		connectorMap.put(SemanticPackage.eINSTANCE.getWire(), WireEditPart.class );
	}
	
	/** list of supported shape compartment editparts */
	private Map shapeCompartmentMap = new HashMap();
	{
		shapeCompartmentMap.put(LogicConstants.LOGIC_SHAPE_COMPARTMENT, LogicShapeCompartmentEditPart.class); //$NON-NLS-1$
	}
	
	/** list of supported list compartment editparts */
	private Map listCompartmentMap = new HashMap();
	{
		listCompartmentMap.put(LogicConstants.LOGIC_FLOW_COMPARTMENT, LogicFlowCompartmentEditPart.class); //$NON-NLS-1$
	}

	/**
	 * Gets a diagram's editpart class.
	 * This method should be overridden by a provider if it wants to provide this service. 
	 * @param view the view to be <i>controlled</code> by the created editpart
	 */
	protected Class getDiagramEditPartClass(View view ) {
		if (view.getType().equals("logic")) { //$NON-NLS-1$
            return(DiagramEditPart.class);
        }
		return null;
	}
	
	/**
	 * Set the editpart class to the editpart mapped to the supplied view's semantic hint.
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider#setConnectorEditPartClass(org.eclipse.gmf.runtime.diagram.ui.internal.view.IConnectorView)
	 */
	protected Class getConnectorEditPartClass(View view) {
		return(Class) connectorMap.get(getReferencedElementEClass(view));
	}

	/**
	 * Gets a Node's editpart class.
	 * This method should be overridden by a provider if it wants to provide this service. 
	 * @param view the view to be <i>controlled</code> by the created editpart
	 */
	protected Class getNodeEditPartClass(View view ) {
		Class clazz = null;
		String semanticHint = view.getType();
		EClass eClass = getReferencedElementEClass(view);
		clazz = (Class) listCompartmentMap.get(semanticHint);
		if(clazz!=null)
			return clazz;
		clazz = (Class) shapeCompartmentMap.get(semanticHint);
		if(clazz!=null)
			return clazz;
		clazz =  ((Class)shapeMap.get(eClass));
		return clazz;
	}
}
