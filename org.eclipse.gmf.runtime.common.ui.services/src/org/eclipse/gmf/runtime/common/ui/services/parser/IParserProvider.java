/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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