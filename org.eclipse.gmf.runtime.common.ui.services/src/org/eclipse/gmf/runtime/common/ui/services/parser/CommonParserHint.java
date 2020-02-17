/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
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
