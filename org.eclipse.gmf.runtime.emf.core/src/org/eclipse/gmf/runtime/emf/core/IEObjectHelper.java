/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;

/**
 * An interface that provides various utility functions to manage and validate 
 * {@link org.eclipse.emf.ecore.EObject EObject} and its proxies.
 * <p>
 * API clients should <b>not </b> implement this interface.
 * </p>
 * <p>
 * Unless specified differently, members of this interface should always be called in the
 * context of a ResourceSetReadOperation
 * </p>
 */
public interface IEObjectHelper {

	/**
	 * Destroys the object by removing it from its container and severing all
	 * references from and to the object. It is also recursive, i.e., it gets
	 * called on the children. The object must be attached to a container for
	 * the method to have any effect.
	 * 
	 * This member must always be called in the context of a
	 * ResourceSetModifyOperation
	 * 
	 * @param eObject
	 *            The object to destroy.
	 */
	public void destroy(EObject eObject);

	/**
	 * Returns the ID if there is one for the given object; if there is no ID,
	 * it returns an empty string.
	 * 
	 * @param eObject
	 *            The object to get the ID for.
	 * @return The ID of the object.
	 */
	public String getID(EObject eObject);

	/**
	 * Gets the name of an object if the object has name, returns an empty
	 * string otherwise.
	 * 
	 * @param eObject
	 *            The object to get the name of.
	 * @return The object's name.
	 */
	public String getName(EObject eObject);

	/**
	 * Sets the name of an object if the object can be assigned a name. The
	 * object must be nameable
	 * 
	 * @param eObject
	 *            The object to set the name of.
	 * @param name
	 *            The object's new name.
	 * @return <code>true</code> if the name was set
	 * @throws IllegalArgumentException
	 *             The specified <code>eObject</code> is not nameable.
	 */
	public boolean setName(EObject eObject, String name);

	/**
	 * Gets the fully qualified name of an object.
	 * 
	 * @param eObject
	 *            The object to get the fully qualified name of.
	 * @param formatted
	 *            When <code>true</code>, unnamed parents will be listed
	 *            using their meta-class name.
	 * @return The object's fully qualified name.
	 */
	public String getQName(EObject eObject, boolean formatted);

	/**
	 * Finds all referencers of a particular object. The search can be narrowed
	 * down by passing the list of reference features to match.
	 * 
	 * @param eObject
	 *            the referenced object
	 * @param features
	 *            the reference features, can be null or empty
	 * @return the collection of referencers
	 */
	public Collection getReferencers(EObject eObject, EReference[] features);

	/**
	 * This method validates a collection of elements by evaluating the batch
	 * constraints that apply to them, recursively, over their containment
	 * sub-trees. Any problems found are returned as status objects. Any
	 * containment relationships amongst the specified elements are handled so
	 * that no duplication occurs. This can be a long-running operation (hence
	 * the progress monitor).
	 * 
	 * @param objects
	 *            the objects to validate
	 * @param monitor
	 *            the progress monitor
	 * @return The validation results. The status may be a single
	 *         {@link IValidationStatus IValidationStatus}, or a multi-status
	 *         composed of <code>IValidationStatus</code> es, or neither
	 */
	public IStatus validate(Collection objects, IProgressMonitor monitor);

	/*
	 * Proxy Utilities
	 */

	/**
	 * Gets the name of the object referenced by the proxy by parsing the proxy
	 * URI.
	 * 
	 * @param eObject
	 *            the proxy object
	 * @return the name of the object referenced by the proxy
	 */
	public String getProxyName(InternalEObject eObject);

	/**
	 * Gets the fully qualified name of the object referenced by the proxy by
	 * parsing the proxy URI.
	 * 
	 * @param eObject
	 *            the proxy object
	 * @return the fully qualified name of the object referenced by the proxy
	 */
	public String getProxyQName(InternalEObject eObject);

	/**
	 * Gets the ID of the object referenced by the proxy by parsing the proxy
	 * URI.
	 * 
	 * @param eObject
	 *            the proxy object
	 * @return the ID of the object referenced by the proxy
	 */
	public String getProxyID(InternalEObject eObject);

	/**
	 * Gets the EClass of the object referenced by the proxy by parsing the
	 * proxy URI.
	 * 
	 * @param eObject
	 *            the proxy object
	 * @return the EClass of the object referenced by the proxy
	 */
	public EClass getProxyClass(InternalEObject eObject);
}