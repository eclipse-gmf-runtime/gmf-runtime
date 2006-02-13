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

package org.eclipse.gmf.runtime.emf.type.core.internal;

/**
 * A list of debug options for this plug-in.
 * 
 * @author ldamus
 *
 */
public final class EMFTypeDebugOptions {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private EMFTypeDebugOptions() {
		/* private constructor */
	}

	/** Debug option. */
	public static final String DEBUG = EMFTypePlugin.getPluginId() + "/debug"; //$NON-NLS-1$

	/** Debug option for tracing exception catching. */
	public static final String EXCEPTIONS_CATCHING = DEBUG + "/exceptions/catching"; //$NON-NLS-1$
	
	/** Debug option for tracing exception throwing. */
	public static final String EXCEPTIONS_THROWING = DEBUG + "/exceptions/throwing"; //$NON-NLS-1$

	/** Debug option for tracing method entering. */
	public static final String METHODS_ENTERING = DEBUG + "/methods/entering"; //$NON-NLS-1$
	
	/** Debug option for tracing method exiting. */
	public static final String METHODS_EXITING = DEBUG + "/methods/exiting"; //$NON-NLS-1$
}
