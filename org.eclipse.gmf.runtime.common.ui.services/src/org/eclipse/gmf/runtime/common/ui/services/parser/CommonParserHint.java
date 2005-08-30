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

/** 
 * Defines common parser hints.  This file should not contain non-common hints
 * e.g. UML Hints.
 */
public interface CommonParserHint {

	/**
	 * Hint to select the parser to provide a name string.
	 */
	static final public String NAME = "Name"; //$NON-NLS-1$
	
	/**
	 * Hint to select the parser to provide a description string.
	 */
	static final public String DESCRIPTION = "Description"; //$NON-NLS-1$

}
