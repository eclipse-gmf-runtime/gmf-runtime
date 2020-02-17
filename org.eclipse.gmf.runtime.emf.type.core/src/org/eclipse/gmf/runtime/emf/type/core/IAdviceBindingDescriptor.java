/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
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

import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;

/**
 * Descriptor for edit helper advice, providing the metadata that can be specified on the extension
 * point.
 * 
 * @since 1.9
 */
public interface IAdviceBindingDescriptor {

	/**
	 * Gets the ID of this advice.
	 * 
	 * @return the advice ID
	 */
	String getId();

	/**
	 * Gets the identifier of the element type that this advice is bound to.
	 * 
	 * @return the element type identifier.
	 */
	String getTypeId();

	/**
	 * Gets the element matcher.
	 * <P>
	 * May cause the plugin defining the matcher class to be loaded.
	 * 
	 * @return the element matcher
	 */
	IElementMatcher getMatcher();

	/**
	 * Gets the container descriptor. May be <code>null</code>.
	 * 
	 * @return the container descriptor
	 */
	IContainerDescriptor getContainerDescriptor();

	/**
	 * Gets my edit helper advice. The advice can return 'before' or 'after'
	 * editing commands for editing elements of the types that I specialize.
	 * <P>
	 * May cause the plugin defining the matcher class to be loaded.
	 * 
	 * @return the edit helper advice.
	 */
	IEditHelperAdvice getEditHelperAdvice();

	/**
	 * Indicates the related element types that should inherit this advice.
	 * 
	 * @return the kind of inheritance
	 */
	AdviceBindingInheritance getInheritance();

}