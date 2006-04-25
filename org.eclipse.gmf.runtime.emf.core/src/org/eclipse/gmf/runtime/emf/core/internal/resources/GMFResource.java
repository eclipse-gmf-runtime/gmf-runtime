/******************************************************************************
 * Copyright (c) 2004-2006 IBM Corporation and others.
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
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;

/**
 * Custom implementation of an XMIResource.
 * 
 * @author rafikj
 * 
 * @deprecated Use the {@link org.eclipse.gmf.runtime.emf.core.resources.GMFResource}
 *     class, instead
 */
public class GMFResource
	extends org.eclipse.gmf.runtime.emf.core.resources.GMFResource {
	
	/**
	 * Constructor.
	 */
	public GMFResource(URI uri) {
		super(uri);
	}

	protected XMLHelper createXMLHelper() {
		return new GMFHelper(this);
	}

	protected XMLLoad createXMLLoad() {
		return new GMFLoad(createXMLHelper());
	}
}