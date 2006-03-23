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
package org.eclipse.gmf.examples.runtime.diagram.logic.internal.editpolicies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Circuit;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Element;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.Wire;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalConnectionEditPolicy;
import org.eclipse.gmf.runtime.notation.View;

/**
 * CanonicalConnectionEditPolic implementation that synchronizes with the semantic 
 * contents of the Circuit element.
 * 
 * @author sshaw
 *
 */
public class CircuitCompartmentCanonicalEditPolicy extends CanonicalConnectionEditPolicy {

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#getSemanticChildrenList()
	 */
	protected List getSemanticChildrenList() {
		EObject modelRef = resolveSemanticElement();
		
		Circuit circuitElement = (Circuit) modelRef;
		if (circuitElement==null)
			return Collections.EMPTY_LIST;
		List allChildren = circuitElement.getChildren();
		List ledElements = new ArrayList();
		
		ListIterator li = allChildren.listIterator();
		while (li.hasNext()) {
			Object obj = li.next();
			if (obj instanceof Element && !(obj instanceof Wire))
				ledElements.add(obj);
		}
		
		return ledElements;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalConnectionEditPolicy#getSemanticConnectionsList()
	 */
	protected List getSemanticConnectionsList() {
		EObject modelRef = resolveSemanticElement();
		
		Circuit circuitElement = (Circuit) modelRef;
		if (circuitElement==null)
			return Collections.EMPTY_LIST;
		List allChildren = circuitElement.getChildren();
		ListIterator li = allChildren.listIterator();
		UniqueEList wires = new UniqueEList();
		while (li.hasNext()) {
			Object obj = li.next();
			if (obj instanceof Wire) {
				Wire wire = (Wire)obj;
				if (EcoreUtil.isAncestor(circuitElement, wire.getSource()) &&
                    EcoreUtil.isAncestor(circuitElement, wire.getTarget())) {
					wires.add(wire);
				}
                else {
                    if ((!wire.getSource().eContainer().equals(circuitElement) && 
                        EcoreUtil.isAncestor(circuitElement, wire.getSource().eContainer())) ||
                        (!wire.getTarget().eContainer().equals(circuitElement) &&
                        EcoreUtil.isAncestor(circuitElement, wire.getTarget().eContainer()))) {
                        wires.add(wire);
                    }
                }
			}
		}
		
		return wires;
	}


	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy#shouldDeleteView(org.eclipse.gmf.runtime.notation.View)
	 */
	protected boolean shouldDeleteView(View view) {
		return true;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalConnectionEditPolicy#getSourceElement(org.eclipse.emf.ecore.EObject)
	 */
	protected EObject getSourceElement(EObject relationship) {
		Wire wire = (Wire)relationship;
		return wire.getSource();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalConnectionEditPolicy#getTargetElement(org.eclipse.emf.ecore.EObject)
	 */
	protected EObject getTargetElement(EObject relationship) {
		Wire wire = (Wire)relationship;
		return wire.getTarget();
	}
	
}