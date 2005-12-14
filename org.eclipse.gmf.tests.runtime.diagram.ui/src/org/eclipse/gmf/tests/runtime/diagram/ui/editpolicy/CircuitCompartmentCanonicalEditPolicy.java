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

package org.eclipse.gmf.tests.runtime.diagram.ui.editpolicy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Circuit;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.LED;
import org.eclipse.gmf.examples.runtime.diagram.logic.model.Wire;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalConnectionEditPolicy;
import org.eclipse.gmf.runtime.notation.View;

/**
 * Canonical edit policy for the Circuit compartment
 * @author sshaw
 */

public class CircuitCompartmentCanonicalEditPolicy extends CanonicalConnectionEditPolicy {

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
			if (obj instanceof LED)
				ledElements.add(obj);
		}
		
		return ledElements;
	}
	
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
				wires.add(wire);
			}
		}
		
		return wires;
	}

	protected boolean shouldDeleteView(View view) {
		return true;
	}
	
	protected EObject getSourceElement(EObject relationship) {
		Wire wire = (Wire)relationship;
		return wire.getSource();
	}

	protected EObject getTargetElement(EObject relationship) {
		Wire wire = (Wire)relationship;
		return wire.getTarget();
	}
	
}