/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.commands.core.internal;


/**
 * A list of debug options for this plug-in.
 * 
 * @author Christian W. Damus
 */
public final class MSLCommandsDebugOptions {

	private MSLCommandsDebugOptions() {
		// private
	}

	/** Debug option. */
	public static final String DEBUG = MSLCommandsPlugin.getPluginId() + "/debug"; //$NON-NLS-1$

	/** Debug option to trace exception catching. */
	public static final String EXCEPTIONS_CATCHING = DEBUG
		+ "/exceptions/catching"; //$NON-NLS-1$

	/** Debug option to trace exception throwing. */
	public static final String EXCEPTIONS_THROWING = DEBUG
		+ "/exceptions/throwing"; //$NON-NLS-1$

	/** Debug option to trace method entering. */
	public static final String METHODS_ENTERING = DEBUG + "/methods/entering"; //$NON-NLS-1$

	/** Debug option to trace method exiting. */
	public static final String METHODS_EXITING = DEBUG + "/methods/exiting"; //$NON-NLS-1$
	
	/** Debug option to trace execution of model operations. */
	public static final String MODEL_OPERATIONS = DEBUG + "/model/operations"; //$NON-NLS-1$
}