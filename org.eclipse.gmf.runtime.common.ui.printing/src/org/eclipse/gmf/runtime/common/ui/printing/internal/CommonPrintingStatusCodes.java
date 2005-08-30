/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.printing.internal;

/**
 * A list of status codes for this plug-in.
 * 
 * @author wdiu, mainly copied from khussey's CommonCoreStatusCodes
 *
 */
public final class CommonPrintingStatusCodes {

	/**
	 * This class should not be instantiated since it is a static constant
	 * class.
	 * 
	 */
	private CommonPrintingStatusCodes() {
		/* private constructor */
	}

	/**
	 * Status code indicating that everything is OK.
	 */
	public static final int OK = 0;

	/**
	 * Status code indicating that an error occurred with a resource.
	 * This could be the printing DLL.
	 */
	public static final int RESOURCE_FAILURE = 1;
}
