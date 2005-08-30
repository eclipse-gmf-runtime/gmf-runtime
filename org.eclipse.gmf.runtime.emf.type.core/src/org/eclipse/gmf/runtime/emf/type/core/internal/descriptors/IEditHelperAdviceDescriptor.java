/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core.internal.descriptors;

import org.eclipse.gmf.runtime.emf.type.core.IContainerDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;

/**
 * Descriptor for edit helper advice. Used to prevent premature loading 
 * of the plugins that define the element matcher, edit helper advice and metamodel
 * descriptor classes.
 * 
 * @author ldamus
 */
public interface IEditHelperAdviceDescriptor {

	/**
	 * Gets the element type identifier.
	 * 
	 * @return the element type identifier.
	 */
	public abstract String getTypeId();

	/**
	 * Gets the element matcher.
	 * <P>
	 * May cause the plugin defining the matcher class to be loaded.
	 * 
	 * @return the element matcher
	 */
	public abstract IElementMatcher getMatcher();
	
	/**
	 * Gets the container descriptor. May be <code>null</code>.
	 * 
	 * @return the container descriptor
	 */
	public IContainerDescriptor getContainerDescriptor();

	/**
	 * Gets my edit helper advice. The advice can return 'before' or 'after'
	 * editing commands for editing elements of the types that I specialize.
	 * <P>
	 * May cause the plugin defining the matcher class to be loaded.
	 * 
	 * @return the edit helper advice.
	 */
	public abstract IEditHelperAdvice getEditHelperAdvice();

	/**
	 * Indicates the related element types that should inherit this advice.
	 * 
	 * @return the kind of inheritance
	 */
	public abstract AdviceBindingInheritance getInheritance();
}