/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.plugin;

/**
 * A list of status codes for this plug-in.
 * 
 * @author khussey
 *
 */
public final class EMFCoreStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private EMFCoreStatusCodes() {
		// private constructor.
	}

	public static final int OK = 0;

	public static final int IGNORED_EXCEPTION_WARNING = 10;
	public static final int OPERATION_FAILED = 11;
	public static final int OPERATION_CANCELED_BY_USER = 12;
	
	public static final int VALIDATOR_PROTOCOL_ERROR = 20;
	
	public static final int TRANSACTION_ABORTED = 30;
}
