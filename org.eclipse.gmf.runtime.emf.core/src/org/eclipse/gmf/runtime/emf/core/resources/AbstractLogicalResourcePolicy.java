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
