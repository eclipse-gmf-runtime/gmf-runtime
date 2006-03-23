/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.examples.runtime.diagram.logic.internal.edithelpers;

import java.util.Collection;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.examples.runtime.diagram.logic.semantic.SemanticPackage;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyDependentsRequest;

/**
 * Edit helper for logic terminal elements.
 * 
 * @author Christian W. Damus (cdamus)
 */
public class TerminalEditHelper
	extends LogicElementEditHelper {
	
	/**
	 * References from wires to terminals.
	 */
	private static final EReference[] WIRE_TERMINAL_REFERENCES = new EReference[] {
		SemanticPackage.eINSTANCE.getWire_Source(),
		SemanticPackage.eINSTANCE.getWire_Target(),
	};

	/**
	 * Gets a command to destroy the dependents of the terminal that is being
	 * destroyed.  These will be wires.
	 */
	protected ICommand getDestroyDependentsCommand(DestroyDependentsRequest request) {
		Collection wires = EMFCoreUtil.getReferencers(
				request.getElementToDestroy(),
				WIRE_TERMINAL_REFERENCES);

		return request.getDestroyDependentsCommand(wires);
	}

}