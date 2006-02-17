/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/
package org.eclipse.gmf.runtime.common.ui.services.internal.elementselection;

import org.eclipse.gmf.runtime.common.ui.services.elementselection.IElementSelectionInput;
import org.eclipse.gmf.runtime.common.ui.services.elementselection.IMatchingObjectsOperation;

/**
 * The matching objects operation used by the element selection service.
 * 
 * @author Anthony Hunter
 */
public abstract class MatchingObjectsOperation
	implements IMatchingObjectsOperation {

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
     * @inheritDoc
     */
	public IElementSelectionInput getElementSelectionInput() {
		return input;
	}

}
