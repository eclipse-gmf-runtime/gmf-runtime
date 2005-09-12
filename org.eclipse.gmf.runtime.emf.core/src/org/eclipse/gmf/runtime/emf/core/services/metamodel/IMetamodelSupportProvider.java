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

package org.eclipse.gmf.runtime.emf.core.services.metamodel;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The meta-model provider interface.
 * 
 * @author rafikj
 */
public interface IMetamodelSupportProvider
	extends IProvider {

	/**
	 * meta-model providers must implement this method.
	 */
	public IMetamodelSupport getMetamodelSupport(EPackage ePackage);
}