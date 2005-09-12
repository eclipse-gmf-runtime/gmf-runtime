/******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.gef.ui.internal.requests;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

/**
 * @author jschofie
 *
 * This DirectEditRequest captures the initial character entered
 * before the direct edit request was started.
 */
public class DirectEditRequestWrapper extends Request {

	/** First character entered before direct edit request has started */
	private char firstCharacter;

	/**
	 * Constructs a Direct Edit Request and caches the first character
	 * @param initialCharacter character to cache as the first character
	 * to be used for the edit
	 */
	public DirectEditRequestWrapper( char initialCharacter ) {
		
		super( RequestConstants.REQ_DIRECT_EDIT );
		firstCharacter = initialCharacter;
	}
	
	/**
	 * Return the cached character
	 * @return char cached character
	 */
	public char getInitialCharacter() {
		return firstCharacter;
	}
}
