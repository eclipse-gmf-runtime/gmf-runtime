/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.resources;

import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * This class changes the behavior of the default XMIHelper so that references
 * between projects are not deresolved.
 * 
 * @author rafikj
 * 
 * @deprecated Use the {@link org.eclipse.gmf.runtime.emf.core.resources.GMFHelper}
 *     class, instead
 */
public class GMFHelper
	extends org.eclipse.gmf.runtime.emf.core.resources.GMFHelper {

	/**
	 * Constructor.
	 */
	public GMFHelper(XMLResource resource) {
		super(resource);
	}
}