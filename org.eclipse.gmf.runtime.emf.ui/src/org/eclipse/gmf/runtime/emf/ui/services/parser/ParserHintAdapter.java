/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.ui.services.parser;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;


public class ParserHintAdapter implements IAdaptable {
	private EObject element = null;
	private String parserHint = null;

	/**
	 * Method ParserHintAdapter.
	 * @param element
	 * @param parserHint
	 */
	public ParserHintAdapter(EObject element, String parserHint) {
		this.element = element;
		this.parserHint = parserHint;
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
	 */
	public Object getAdapter(Class adapter) {

		if (adapter.equals(EObject.class))
			return element;
		else if (adapter.equals(String.class))
			return parserHint;
		return null;
	}

}

