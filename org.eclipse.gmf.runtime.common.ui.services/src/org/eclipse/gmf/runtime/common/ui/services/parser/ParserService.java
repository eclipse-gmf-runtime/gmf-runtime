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
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.common.ui.services.internal.CommonUIServicesPlugin;

/**
 * The parser service which returns a parser for an IAdaptable.
 * Also includes methods to obtain the strings for the IAdaptable and to
 * determine whether editing is possible. 
 */
public class ParserService
	extends Service
	implements IParserProvider {

	/**
	 * The singleton instance of the parser service.
	 */
	private final static ParserService service = new ParserService();

	static {
		service.configureProviders(CommonUIServicesPlugin.getPluginId(), "parserProviders"); //$NON-NLS-1$
	}
	
	/**
	 * Retrieves the singleton instance of the parser service.
	 *
	 * @return The editor service singleton.
	 */
	public static ParserService getInstance() {
		return service;
	}

    /**
     * @see org.eclipse.gmf.runtime.common.ui.services.parser.IParserProvider#getParser(IAdaptable)
     */
	public IParser getParser(IAdaptable hint) {
		return (IParser) executeUnique(ExecutionStrategy.FIRST, new GetParserOperation(hint));
	}

	/**
	 * Returns the string that is a subject to edit.
	 *
	 * @param hint - hint adaptable to IElement
	 * @param options - parser options
	 * @return String - current contents of the edit string
	 */
	public String getEditString(IAdaptable hint, int options) {
		String editString = null;
		IParser parser = service.getParser(hint);

		if (parser != null) {
			editString = parser.getEditString(hint, options);
		}

		if (editString == null) {
			editString = StringStatics.BLANK;
		}

		return editString;
	}

	/**
	 * Gets the print string to display
	 *
	 * @param hint - hint adaptable to IElement
	 * @param options - parser options
	 * @return String - the print string
	 */
	public String getPrintString(IAdaptable hint, int options) {
		IParser parser = service.getParser(hint);

		if (parser == null) {
			return StringStatics.BLANK;
		}

		return parser.getPrintString(hint, options);
	}

    /**
     * Convenience method for getting the print string to display
     * @param hint - hint adaptable to element
     * @return String - the print string
     */
	public String getPrintString(IAdaptable hint) {
		return getPrintString(hint, 0);
	}

    /**
     * Determines if element can be edited
     * @param hint - hint adaptable to element to check
     * @return boolean <code>true</code> if element is editable, otherwise <code>false</code>
     */
	public boolean canEdit(IAdaptable hint) {
		IParser parser = service.getParser(hint);

		if (parser != null) {
			String editString = parser.getEditString(hint, 0);
			if (editString != null) {
				return true;
			}
		}

		return false;
	}
}
