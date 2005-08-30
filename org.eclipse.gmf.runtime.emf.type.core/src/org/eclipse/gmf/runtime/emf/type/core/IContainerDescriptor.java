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