/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui;

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
