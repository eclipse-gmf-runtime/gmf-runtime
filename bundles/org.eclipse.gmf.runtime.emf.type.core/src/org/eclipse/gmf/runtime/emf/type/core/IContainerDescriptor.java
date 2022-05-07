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

import org.eclipse.emf.ecore.EReference;

/**
 * Describes a model element container using an element matcher and containing
 * references.
 * 
 * @author ldamus
 */
public interface IContainerDescriptor {

	/**
	 * Gets the matcher for the container. May be <code>null</code>.
	 * <P>
	 * This method may cause the plugin that defines the matcher class to be
	 * loaded.
	 * 
	 * @return the matcher
	 */
	public IElementMatcher getMatcher();

	/**
	 * Gets the containment references. May be <code>null</code>.
	 * 
	 * @return the references
	 */
	public EReference[] getContainmentFeatures();
}