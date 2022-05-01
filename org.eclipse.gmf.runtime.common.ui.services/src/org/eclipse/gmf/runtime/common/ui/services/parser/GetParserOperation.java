/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.parser;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * Operation to get a parser using an IAdaptable hint for the parser to be used 
 */
public class GetParserOperation 
	implements IOperation {

	/**
	 * Hint for the parser to be used
	 */
	private final IAdaptable hint;

	/**
	 * Method GetParserOperation.
	 * 
	 * @param hint IAdaptable hint for the parser to be used
	 */
	protected GetParserOperation(IAdaptable hint) {
		assert null!=hint : "GetParserOperation constructor received NULL as argument"; //$NON-NLS-1$
		
		this.hint = hint;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
	 */
	public Object execute(IProvider provider) {
		return ((IParserProvider) provider).getParser(getHint());
	}

	/**
	 * Method getHint.
	 * @return IAdaptable
	 */
	public final IAdaptable getHint() {
		return hint;
	}
}
