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

import java.util.List;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.Service;

/**
 * The element selection service.
 * 
 * @author Anthony Hunter <a href="mailto:anthonyh@ca.ibm.com">
 *         anthonyh@ca.ibm.com </a>
 */
public class ElementSelectionService
	extends Service
	implements IElementSelectionProvider {

	/**
	 * The singleton instance of the type selection service.
	 */
	private final static ElementSelectionService instance = new ElementSelectionService();

	/**
	 * Constructs a new type selection service.
	 */
	protected ElementSelectionService() {
		super(true);
	}

	/**
	 * Retrieves the singleton instance of the type selection service.
	 * 
	 * @return The type selection service singleton.
	 */
	public static ElementSelectionService getInstance() {
		return instance;
	}

	/**
	 * @inheritDoc
	 */
	public List getMatchingObjects(IElementSelectionInput input) {
		return execute(ExecutionStrategy.FORWARD,
			new GetMatchingObjectsOperation(input));
	}

	/**
	 * @inheritDoc
	 */
	public Object resolve(IMatchingObject object) {
		return null;
	}

}
