/******************************************************************************
 * Copyright (c) 2004,2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.properties.internal;


/**
 * @author nbalaba
 */
public class PSFPluginDebugOptions {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 *  
	 */
	private PSFPluginDebugOptions() {
		/* private constructor */
	}

	/** Debug option. */
    public static final String DEBUG = PSFCommonUIPlugin.getPluginId() + "/debug"; //$NON-NLS-1$
	
    /** Debug option for tracing exception catching. */
	public static final String EXCEPTIONS_CATCHING = DEBUG
			+ "/exceptions/catching"; //$NON-NLS-1$
	
	/** Debug option for tracing exception throwing. */
	public static final String EXCEPTIONS_THROWING = DEBUG + "/exceptions/throwing"; //$NON-NLS-1$

}
