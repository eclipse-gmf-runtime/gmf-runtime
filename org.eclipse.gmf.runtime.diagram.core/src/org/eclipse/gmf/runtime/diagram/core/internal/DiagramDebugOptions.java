/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.core.internal;

/**
 * A list of debug options for this plug-in.
 * 
 *  @author Yasser Lulu
 *
 */
public final class DiagramDebugOptions {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private DiagramDebugOptions() {
		//static class: prevent instatiation
	}

	public static final String DEBUG = DiagramPlugin.getPluginId()
		+ "/debug"; //$NON-NLS-1$

	public static final String EXCEPTIONS_CATCHING = DEBUG
		+ "/exceptions/catching"; //$NON-NLS-1$

	public static final String EXCEPTIONS_THROWING = DEBUG
		+ "/exceptions/throwing"; //$NON-NLS-1$

	public static final String METHODS_ENTERING = DEBUG + "/methods/entering"; //$NON-NLS-1$

	public static final String METHODS_EXITING = DEBUG + "/methods/exiting"; //$NON-NLS-1$
	
	public static final String EVENTS = DEBUG + "/filter/events";//$NON-NLS-1$
}