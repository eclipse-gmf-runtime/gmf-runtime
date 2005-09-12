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