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

package org.eclipse.gmf.runtime.common.ui.services.parser;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The interface for all parser providers
 * 
 * Must implement a method to return a parser from an IAdaptable hint
 */
public interface IParserProvider
	extends IProvider {

	/**
	 * Method getParser.
	 * 
	 * @param hint
	 *            IAdaptable hint used to determine the parser to return
	 * @return IParser corresponding to the IAdaptable hint
	 */
	public IParser getParser(IAdaptable hint);

}