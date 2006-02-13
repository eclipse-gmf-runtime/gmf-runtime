/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.properties.internal;

/**
 * A list of status codes for the EMF properties plugin
 * 
 * @author ldamus
 * 
 */
public final class EMFPropertiesStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private EMFPropertiesStatusCodes() {
		//static class: prevent instatiation
	}

	/**
	 * Status code indicating that no errors occurred
	 */
	public static final int OK = 0;
    
    /**
     * Status code indicating that an error occurred with an action.
     */
    public static final int ACTION_FAILURE = 3;

    /**
	 * Status code indicating that an error occurred with a command.
	 */
	public static final int COMMAND_FAILURE = 4;
	
}
