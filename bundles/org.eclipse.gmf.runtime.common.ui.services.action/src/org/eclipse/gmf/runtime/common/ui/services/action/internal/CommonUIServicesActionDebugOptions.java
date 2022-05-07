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

package org.eclipse.gmf.runtime.common.ui.services.action.internal;

/**
 * A list of debug options for this plug-in.
 * 
 * @author ldamus
 *
 */
public final class CommonUIServicesActionDebugOptions {

    /**
     * This class should not be instantiated since it is a static constant
     * class.
     * 
     */
    private CommonUIServicesActionDebugOptions() {
		/* private constructor */
	}

    /** Debug option. */
    public static final String DEBUG = CommonUIServicesActionPlugin.getPluginId() + "/debug"; //$NON-NLS-1$

    /** Debug option to trace exception catching. */
    public static final String EXCEPTIONS_CATCHING = DEBUG + "/exceptions/catching"; //$NON-NLS-1$
    
    /** Debug option to trace exception throwing. */
    public static final String EXCEPTIONS_THROWING = DEBUG + "/exceptions/throwing"; //$NON-NLS-1$

    /** Debug option to trace method entering. */
    public static final String METHODS_ENTERING = DEBUG + "/methods/entering"; //$NON-NLS-1$
    
    /** Debug option to trace method exiting. */
    public static final String METHODS_EXITING = DEBUG + "/methods/exiting"; //$NON-NLS-1$
    
    /** Debug option to trace service configuration. */
    public static final String SERVICES_CONFIG = DEBUG + "/services/config"; //$NON-NLS-1$

}
