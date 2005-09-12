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


/**
 * Abstract implementation for element type factories. The methods in this
 * factory throw <code>UnsupportedOperationException</code>. Element type
 * factories can support either metamodel types or specialization types, or
 * both. If a concrete factory is asked to create a kind of element type that is
 * does not support, the exception will be thrown.
 * 
 * @author ldamus
 */
public abstract class AbstractElementTypeFactory
	implements IElementTypeFactory {

	/**
	 * @throws UnsupportedOperationException
	 *             when metamodel types are not supported by this factory
	 */
	public IMetamodelType createMetamodelType(IMetamodelTypeDescriptor descriptor) {
		throw new UnsupportedOperationException(
			"Factory does not support metamodel types."); //$NON-NLS-1$
	}

	/**
	 * @throws UnsupportedOperationException
	 *             when specialization types are not supported by this factory
	 */
	public ISpecializationType createSpecializationType(
			ISpecializationTypeDescriptor descriptor) {

		throw new UnsupportedOperationException(
			"Factory does not support specialization types."); //$NON-NLS-1$
	}

}