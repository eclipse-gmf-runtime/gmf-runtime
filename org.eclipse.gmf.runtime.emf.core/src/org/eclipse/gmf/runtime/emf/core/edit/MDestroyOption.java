/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.edit;

/**
 * Enumeration that describes options that can be passed to destroy.
 * 
 * @author rafikj
 */
public final class MDestroyOption {

	/**
	 * No events.
	 */
	public static final int NO_EVENTS = 1;

	/**
	 * Keep incoming and outgoing references.
	 */
	public static final int KEEP_REFERENCES = 2;

	/**
	 * Make destroyed object a proxy object.
	 */
	public static final int MAKE_PROXY = 4;

	private MDestroyOption() {
		// private
	}
}