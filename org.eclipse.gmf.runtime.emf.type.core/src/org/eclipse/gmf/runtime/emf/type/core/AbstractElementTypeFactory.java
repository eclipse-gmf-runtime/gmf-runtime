/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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