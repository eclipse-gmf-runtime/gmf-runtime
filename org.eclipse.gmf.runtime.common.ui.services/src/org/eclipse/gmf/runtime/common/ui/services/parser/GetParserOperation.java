/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
		assert null!=hint : "GetParserOperation constoructor received NULL as argument"; //$NON-NLS-1$
		
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
