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

import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;

/**
 * Descriptor for a specialization element type that has been defined in XML
 * using the <code>elementTypes</code> extension point.
 * 
 * @author ldamus
 */
public interface ISpecializationTypeDescriptor extends IElementTypeDescriptor {

	/**
	 * Gets the container descriptor.
	 * 
	 * @return the container descriptor
	 */
	public abstract IContainerDescriptor getContainerDescriptor();

	/**
	 * Gets the element types that this type specializes.
	 * 
	 * @return an array of types that are specialized by this type.
	 */
	public abstract IElementType[] getSpecializedTypes();

	/**
	 * Gets the element matcher. May cause the plugin defining the element
	 * matcher class to be loaded.
	 * 
	 * @return the element matcher.
	 */
	public abstract IElementMatcher getMatcher();

	/**
	 * Gets my edit helper advice. The advice can return 'before' or 'after'
	 * editing commands for editing elements of the types that I specialize.
	 * <P>
	 * May cause the plugin defining the edit helper advice class to be loaded.
	 * 
	 * @return the edit helper advice.
	 */
	public abstract IEditHelperAdvice getEditHelperAdvice();
}