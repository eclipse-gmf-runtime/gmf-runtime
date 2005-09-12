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
