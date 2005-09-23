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
package org.eclipse.gmf.runtime.common.ui.services.elementselection;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.ui.services.internal.elementselection.MatchingObjectsOperation;

/**
 * The matching objects operation used by the element selection service.
 * 
 * @author Anthony Hunter <a href="mailto:anthonyh@ca.ibm.com">
 *         anthonyh@ca.ibm.com </a>
 */
public class GetMatchingObjectsOperation
	extends MatchingObjectsOperation {

	/**
	 * Constructor for GetMatchingObjectsOperation.
	 * 
	 * @param input
	 *            the element selection input.
	 */
	public GetMatchingObjectsOperation(IElementSelectionInput input) {
		super(input);
	}

	/**
	 * @inheritDoc
	 */
	public Object execute(IProvider provider) {
		return ((IElementSelectionProvider) provider)
			.getMatchingObjects(getElementSelectionInput());
	}

}
