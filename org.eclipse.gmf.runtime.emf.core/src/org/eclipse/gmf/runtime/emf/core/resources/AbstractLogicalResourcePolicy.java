/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.core.resources;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;


/**
 * Abstract implementation of a logical resource policy, convenient for defining
 * subclasses.
 *
 * @author Christian W. Damus (cdamus)
 */
public abstract class AbstractLogicalResourcePolicy
	implements ILogicalResourcePolicy {

	/**
	 * Initializes me.
	 */
	public AbstractLogicalResourcePolicy() {
		super();
	}

	/**
	 * The default implementation always returns <code>true</code>.
	 */
	public boolean canSeparate(ILogicalResource resource, EObject eObject) {
		return true;
	}

	/**
	 * The default implementation does nothing and returns a <code>null</code>
	 * URI suggestion.
	 */
	public URI preSeparate(ILogicalResource resource, EObject eObject, URI uri)
		throws CannotSeparateException {
		return null;
	}

	/**
	 * The default implementation does nothing.
	 */
	public void postSeparate(ILogicalResource resource, EObject eObject, URI uri) {
		// nothing to do
	}

	/**
	 * The default implementation does nothing.
	 */
	public void preAbsorb(ILogicalResource resource, EObject eObject)
		throws CannotAbsorbException {
		// nothing to do
	}

	/**
	 * The default implementation does nothing.
	 */
	public void postAbsorb(ILogicalResource resource, EObject eObject) {
		// nothing to do
	}
}
