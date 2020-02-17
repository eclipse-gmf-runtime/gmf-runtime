/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelper;

/**
 * Descriptor for a metamodel element type that has been defined in XML using
 * the <code>elementTypes</code> extension point.
 * 
 * @author ldamus
 */
public interface IMetamodelTypeDescriptor extends IElementTypeDescriptor {

	/**
	 * Gets the metaclass that this type represents.
	 * 
	 * @return the metaclass
	 */
	public abstract EClass getEClass();

	/**
	 * Gets the edit helper. May activate the plugin in which the edit helper is
	 * defined.
	 * 
	 * @return the edit helper.
	 */
	public abstract IEditHelper getEditHelper();
}