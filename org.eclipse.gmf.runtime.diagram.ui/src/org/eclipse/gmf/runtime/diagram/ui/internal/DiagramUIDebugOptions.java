/******************************************************************************
 * Copyright (c) 2002, 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal;

/**
 * A list of debug options for the Diagram UI Plugin.
 * 
 * @author khussey
 *
 */
public final class DiagramUIDebugOptions {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private DiagramUIDebugOptions() {
		//static class: prevent instatiation
	}

	/**
	 * Debug enabler
	 */
	public static final String DEBUG = DiagramUIPlugin.getPluginId() + "/debug"; //$NON-NLS-1$

	/**
	 * exception catching flag
	 */
	public static final String EXCEPTIONS_CATCHING = DEBUG + "/exceptions/catching"; //$NON-NLS-1$
	
	/**
	 * exception throwing flag
	 */
	public static final String EXCEPTIONS_THROWING = DEBUG + "/exceptions/throwing"; //$NON-NLS-1$

	/**
	 * debug method entering flag
	 */
	public static final String METHODS_ENTERING = DEBUG + "/methods/entering"; //$NON-NLS-1$
	
	/**
	 * debug method exiting flag
	 */
	public static final String METHODS_EXITING = DEBUG + "/methods/exiting"; //$NON-NLS-1$
    
	/**
	 * Drag and drop tracing 
	 */
	public static final String DND = DEBUG + "/dnd/tracing"; //$NON-NLS-1$  
    
    /**
     * canonical debugging
     */
    public static final String CANONICAL = DEBUG + "/filter/canonical";//$NON-NLS-1$
    
    
    /**
     * Persistence manager debug flag 
     */
    public static final String PERSISTENCE_MGR = DEBUG + "/filter/persistencemgr";//$NON-NLS-1$
    
    
    /**
     * events debugging 
     */
    public static final String EVENTS = DEBUG + "/filter/events";//$NON-NLS-1$
}
