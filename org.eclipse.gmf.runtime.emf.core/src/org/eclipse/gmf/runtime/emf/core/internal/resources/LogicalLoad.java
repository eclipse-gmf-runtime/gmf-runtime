/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


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
