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

package org.eclipse.gmf.runtime.emf.type.core.internal.impl;

import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeFactory;
import org.eclipse.gmf.runtime.emf.type.core.IMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.IMetamodelTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.MetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.SpecializationType;

/**
 * The default element type factory that creates <code>MetamodelType</code> s
 * and <code>SpecializationType</code>s. This is the factory that is used
 * when the 'kind' attribute is not specified in the element type's XML
 * contribution.
 * 
 * @author ldamus
 */
public class DefaultElementTypeFactory
	extends AbstractElementTypeFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementTypeFactory#createMetamodelType(org.eclipse.gmf.runtime.emf.type.core.internal.impl.MetamodelTypeDescriptor)
	 */
	public IMetamodelType createMetamodelType(IMetamodelTypeDescriptor descriptor) {
		return new MetamodelType(descriptor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementTypeFactory#createSpecializationType(org.eclipse.gmf.runtime.emf.type.core.internal.impl.SpecializationTypeDescriptor)
	 */
	public ISpecializationType createSpecializationType(
			ISpecializationTypeDescriptor descriptor) {

		return new SpecializationType(descriptor);
	}

}