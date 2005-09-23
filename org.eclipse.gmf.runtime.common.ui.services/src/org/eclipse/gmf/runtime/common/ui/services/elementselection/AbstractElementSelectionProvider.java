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

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;

/**
 * Abstract implementation of an IElementSelectionProvider.
 * 
 * @author Anthony Hunter <a href="mailto:anthonyh@ca.ibm.com">
 *         anthonyh@ca.ibm.com </a>
 */
public abstract class AbstractElementSelectionProvider
	extends AbstractProvider
	implements IElementSelectionProvider {

	/**
	 * @inheritDoc
	 */
	public boolean provides(IOperation operation) {
		return true;
	}

	/**
	 * @inheritDoc
	 */
	public abstract List getMatchingObjects(IElementSelectionInput input);

}
