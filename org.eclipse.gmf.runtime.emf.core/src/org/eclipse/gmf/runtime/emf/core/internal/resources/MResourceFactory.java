/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.resources;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLUtil;

/**
 * The MResourceFactory interface can be implemented by custom Resource Factory
 * implementations. This will allow clients to change the behavior of certain
 * MSL functions.
 * 
 * @author rafikj
 */
public interface MResourceFactory {

	/**
	 * Override and return a custom implementation of Helper.
	 */
	public Helper getHelper();

	public class Helper {

		// Override to define how to extract object's name from Proxy URI.
		public String getProxyName(EObject eObject) {
			return MSLUtil.getProxyName(eObject);
		}

		// Override to define how to extract object's qualified name from Proxy
		// URI.
		public String getProxyQName(EObject eObject) {
			return MSLUtil.getProxyQName(eObject);
		}

		// Override to define how to extract object's ID from Proxy URI.
		public String getProxyID(EObject eObject) {
			return MSLUtil.getProxyID(eObject);
		}

		// Override to define how to extract object's Class ID from Proxy URI.
		public String getProxyClassID(EObject eObject) {
			return MSLUtil.getProxyClassID(eObject);
		}

		// Override to define how to resolve Proxy URI.
		public EObject resolve(MEditingDomain domain, EObject eObject,
				boolean resolve) {
			return MSLUtil.resolve((MSLEditingDomain) domain, eObject, resolve);
		}
	}
}