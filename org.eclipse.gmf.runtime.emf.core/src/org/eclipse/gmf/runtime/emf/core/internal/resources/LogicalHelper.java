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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIHelperImpl;


public class LogicalHelper
	extends XMIHelperImpl {

	private final LogicalResource logicalResource;
	private final LogicalResourceUnit unit;
	private boolean urisRelativeToRoot = true;
	
	public LogicalHelper(LogicalResourceUnit resource) {
		super(resource);
		
		logicalResource = resource.getLogicalResource();
		unit = resource;
	}
	
	LogicalResource getLogicalResource() {
		return logicalResource;
	}
	
	LogicalResourceUnit getUnit() {
		return unit;
	}
	
	boolean urisRelativeToRoot() {
		return urisRelativeToRoot;
	}
	
	void setUrisRelativeToRoot(boolean b) {
		urisRelativeToRoot = b;
	}

	protected URI getHREF(Resource otherResource, EObject obj) {
		if (otherResource instanceof LogicalResource) {
			LogicalResource otherLogical = (LogicalResource) otherResource;
			URI uri;
			
			if (urisRelativeToRoot()) {
				uri = otherLogical.getURI();
			} else {
				uri = otherLogical.getUnit(obj).getURI();
			}
			
			return uri.appendFragment(otherResource.getURIFragment(obj));
		}
		
		return super.getHREF(otherResource, obj);
	}
	
	/**
	 * Extends the superclass implementation to ensure that proxies for elements
	 * in unloaded resources are not discarded.
	 */
	protected URI handleDanglingHREF(EObject object) {
		URI result;
		
		URI proxyUri = EcoreUtil.getURI(object);
		if ((proxyUri != null)
				&& proxyUri.trimFragment().equals(getLogicalResource().getURI())) {
			// do not discard unresolved references to unloaded units
			//   of the logical resource
			result = proxyUri;
		} else {
			result = super.handleDanglingHREF(object);
		}
		
		return result;
	}
}
