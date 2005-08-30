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
 * Interface used to define application-layer types that describe a
 * specialization of a metamodel type. The specifics of the specialization are
 * expressed in a <code>IElementMatcher</code> class and an
 * <code>IContainerDescriptor</code>.
 * <P>
 * Specializations can contribute 'before' and 'after' editing behaviour to
 * their metamodel type's default behaviour using <code>IEditHelperAdvice</code>.
 * <P>
 * Clients should not implement this interface directly, but should extend the
 * abstract implementation {@link org.eclipse.gmf.runtime.emf.type.core.SpecializationType}
 * instead.
 * 
 * @author ldamus
 */
public interface ISpecializationType
	extends IElementType {

	/**
	 * Gets the container descriptor.
	 * 
	 * @return the container descriptor.
	 */
	public abstract IContainerDescriptor getEContainerDescriptor();

	/**
	 * Gets the element matcher.
	 * 
	 * @return the element matcher.
	 */
	public abstract IElementMatcher getMatcher();

	/**
	 * Gets the element types that this type specializes.
	 * 
	 * @return an array of types that are specialized by this type.
	 */
	public abstract IElementType[] getSpecializedTypes();

	/**
	 * Gets the IDs of the specialized types.
	 * 
	 * @return the IDs of the specialized types
	 */
	public abstract String[] getSpecializedTypeIds();

	/**
	 * Answers whether or not I am a specialization of <code>type</code>.
	 * 
	 * @param type
	 *            the type to be tested
	 * @return <code>true</code> if I am a specialization of <code>type</code>,
	 *         <code>false</code> otherwise.
	 */
	public abstract boolean isSpecializationOf(IElementType type);

	/**
	 * Gets my edit helper advice. The advice can return 'before' or 'after'
	 * editing commands for editing elements of the types that I specialize.
	 * 
	 * @return the edit helper advice.
	 */
	public abstract IEditHelperAdvice getEditHelperAdvice();

	/**
	 * Gets the metamodel type that this type is a specialization of. This type
	 * may be a direct specialization of the metamodel type, or indirect through
	 * other specializations. It can specialize any number of other
	 * specializations, so long as they in turn all specialize the same
	 * metamodel type.
	 * 
	 * @return the metamodel type that I specialize
	 */
	public abstract IMetamodelType getMetamodelType();

}