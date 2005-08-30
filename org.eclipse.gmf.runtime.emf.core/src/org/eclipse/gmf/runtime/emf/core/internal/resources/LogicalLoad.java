/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.core.internal.resources;

import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.SAXWrapper;
import org.eclipse.emf.ecore.xmi.impl.XMILoadImpl;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Loader implementation for logical resources.  This implementation ensures
 * that XML namespace EPackages are loaded in the same resource set as the
 * logical resource, not its internal resource set for the physical resources.
 *
 * @author Christian W. Damus (cdamus)
 */
public class LogicalLoad
	extends XMILoadImpl {

	/**
	 * Initializes me with my helper helper.
	 * 
	 * @param helper my helper
	 */
	public LogicalLoad(XMLHelper helper) {
		super(helper);
	}
	
	/**
	 * Creates a {@link LogicalHandler}.
	 */
	protected DefaultHandler makeDefaultHandler() {
		return new SAXWrapper(
			new LogicalHandler((LogicalResourceUnit) resource, helper, options));
	}

}
