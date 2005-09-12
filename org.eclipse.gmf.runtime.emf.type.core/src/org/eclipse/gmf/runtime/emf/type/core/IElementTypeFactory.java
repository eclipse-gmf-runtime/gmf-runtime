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
 * Factory for custom element types, which must implement one of the
 * <code>IMetamodelType</code> or <code>ISpecializationType</code>
 * interfaces.
 * <P>
 * Custom element types can define custom parameters whose values can be
 * specified in the element type extension point.
 * <P>
 * Clients should not implement this interface directly, but should subclass
 * {@link org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeFactory}instead.
 * 
 * @author ldamus
 */
public interface IElementTypeFactory {

	/**
	 * Creates the custom metamodel type.
	 * 
	 * @param descriptor
	 *            the metamodel type descriptor
	 * @return the custom metamodel type
	 */
	public abstract IMetamodelType createMetamodelType(
			IMetamodelTypeDescriptor descriptor);

	/**
	 * Creates the custom specialization type.
	 * 
	 * @param descriptor
	 *            the specialization type descriptor
	 * @return the custom specialization type
	 */
	public abstract ISpecializationType createSpecializationType(
			ISpecializationTypeDescriptor descriptor);
}