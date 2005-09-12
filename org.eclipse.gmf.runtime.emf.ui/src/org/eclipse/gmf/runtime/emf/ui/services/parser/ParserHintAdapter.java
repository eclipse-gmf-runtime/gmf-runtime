/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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

