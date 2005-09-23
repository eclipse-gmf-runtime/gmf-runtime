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
package org.eclipse.gmf.runtime.common.ui.services.internal.elementselection;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.IElementSelectionInput;

/**
 * The matching objects operation used by the element selection service.
 * 
 * @author Anthony Hunter <a href="mailto:anthonyh@ca.ibm.com">
 *         anthonyh@ca.ibm.com </a>
 */
public abstract class MatchingObjectsOperation
	implements IOperation {

	/**
	 * the element selection input.
	 */
	private IElementSelectionInput input;

	/**
	 * Constructor for a MatchingObjectsOperation
	 * 
	 * @param input
	 *            the element selection input.
	 */
	public MatchingObjectsOperation(IElementSelectionInput input) {
		super();
		this.input = input;
	}

	/**
	 * Retreive the element selection input.
	 * 
	 * @return the element selection input.
	 */
	public IElementSelectionInput getElementSelectionInput() {
		return input;
	}

}
