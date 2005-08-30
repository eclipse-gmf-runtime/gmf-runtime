/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
