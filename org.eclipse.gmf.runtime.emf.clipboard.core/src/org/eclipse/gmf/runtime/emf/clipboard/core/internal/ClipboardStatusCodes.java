/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005. All Rights Reserved.                     |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.clipboard.core.internal;


/**
 * @author Yasser Lulu  
 */
public final class ClipboardStatusCodes {

	/**
	 * 
	 */
	private ClipboardStatusCodes() {
		super();
	}
	
	public static final int OK = 0;
	public static final int ERROR = 1;
	public static final int IGNORED_EXCEPTION_WARNING = 10;

	public static final int CLIPBOARDSUPPORT_MISSING_NSURI = 30;
	public static final int CLIPBOARDSUPPORT_MISSING_CLASS = 31;
	public static final int CLIPBOARDSUPPORT_UNRESOLVED_NSURI = 32;
	public static final int CLIPBOARDSUPPORT_FACTORY_FAILED = 33;
}
