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

package org.eclipse.gmf.examples.runtime.diagram.logic.model.util;

import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeEnumerator;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;

/**
 * Element types for the logic elements.
 * 
 * @author qili, ldamus
 */
public class LogicSemanticType
	extends AbstractElementTypeEnumerator {

	public static final IElementType MODEL = getElementType("logic.model"); //$NON-NLS-1$

	public static final IElementType LED = getElementType("logic.led"); //$NON-NLS-1$

	public static final IElementType WIRE = getElementType("logic.wire"); //$NON-NLS-1$

	public static final IElementType CIRCUIT = getElementType("logic.circuit"); //$NON-NLS-1$

	public static final IElementType FLOWCONTAINER = getElementType("logic.flowcontainer"); //$NON-NLS-1$

	public static final IElementType ANDGATE = getElementType("logic.andgate"); //$NON-NLS-1$

	public static final IElementType ORGATE = getElementType("logic.orgate"); //$NON-NLS-1$

	public static final IElementType XORGATE = getElementType("logic.xorgate"); //$NON-NLS-1$

	public static final IElementType TERMINAL = getElementType("logic.terminal"); //$NON-NLS-1$

	public static final IElementType INPUT_TERMINAL = getElementType("logic.inputterminal"); //$NON-NLS-1$

	public static final IElementType OUTPUT_TERMINAL = getElementType("logic.outputterminal"); //$NON-NLS-1$

	public static final IElementType INPUT_OUTPUT_TERMINAL = getElementType("logic.inputoutputterminal"); //$NON-NLS-1$

	public static final IElementType HALF_ADDER = getElementType("logic.halfAdder"); //$NON-NLS-1$

}