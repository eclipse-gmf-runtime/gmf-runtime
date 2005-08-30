/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.clipboard.core.internal;

import org.eclipse.gmf.runtime.emf.clipboard.core.AbstractClipboardSupport;
import org.eclipse.gmf.runtime.emf.clipboard.core.IClipboardSupport;

/**
 * Default implementation of the {@link IClipboardSupport} API,
 * used in situations where no support has been provided for a metamodel.
 *
 * @author Christian W. Damus (cdamus)
 */
public class DefaultClipboardSupport
	extends AbstractClipboardSupport {

	private static final IClipboardSupport instance = new DefaultClipboardSupport();
	
	private DefaultClipboardSupport() {
		super();
	}

	/**
	 * Obtains the singleton instance of this class.
	 * 
	 * @return my instance
	 */
	public static IClipboardSupport getInstance() {
		return instance;
	}
}
