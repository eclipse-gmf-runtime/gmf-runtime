/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.UnresolvedReferenceException;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.SAXWrapper;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class changes the behavior of the default XMILoader so that
 * UnresolvedReferenceExceptions are not thrown back.
 * 
 * @author rafikj
 */
public class MSLLoad
	extends LogicalLoad {

	/**
	 * Constructor.
	 */
	public MSLLoad(XMLHelper helper) {
		super(helper);
	}

	/**
	 * @see org.eclipse.emf.ecore.xmi.XMLLoad#load(org.eclipse.emf.ecore.xmi.XMLResource,
	 *      java.io.InputStream, java.util.Map)
	 */
	public void load(XMLResource r, InputStream s, Map o)
		throws IOException {

		try {

			super.load(r, s, o);

		} catch (Resource.IOWrappedException e) {

			if (!(e.getWrappedException() instanceof UnresolvedReferenceException))
				throw e;
		}
	}

	/**
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLLoadImpl#makeDefaultHandler()
	 */
	protected DefaultHandler makeDefaultHandler() {
		return new SAXWrapper(new MSLHandler((MSLResourceUnit) resource, helper, options));
	}
}