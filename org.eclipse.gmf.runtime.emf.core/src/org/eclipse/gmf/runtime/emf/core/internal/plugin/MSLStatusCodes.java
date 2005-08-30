/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.plugin;

/**
 * A list of status codes for this plug-in.
 * 
 * @author khussey
 *
 */
public final class MSLStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private MSLStatusCodes() {
		// private constructor.
	}

	public static final int OK = 0;

	public static final int IGNORED_EXCEPTION_WARNING = 10;
	public static final int OPERATION_FAILED = 11;
	public static final int OPERATION_CANCELED_BY_USER = 12;
	
	public static final int VALIDATOR_PROTOCOL_ERROR = 20;
	
	public static final int USERMODELSUPPORT_MISSING_NSURI = 30;
	public static final int USERMODELSUPPORT_MISSING_CLASS = 31;
	public static final int USERMODELSUPPORT_UNRESOLVED_NSURI = 32;
	public static final int USERMODELSUPPORT_FACTORY_FAILED = 33;

	public static final int LOGICAL_SETURI_UNLOADED_FAILED = 40;
	public static final int LOGICAL_CREATE_LIKE_FAILED = 41;
	public static final int LOGICAL_LOAD_REMOTE_FAILED = 42;
	public static final int LOGICAL_LOAD_FAILED = 43;
	public static final int LOGICAL_AUTOLOAD_FAILED = 44;
	public static final int LOGICAL_POLICY_FAILED = 45;
	public static final int LOGICAL_INVALID_POLICY_ELEMENT = 46;
}
