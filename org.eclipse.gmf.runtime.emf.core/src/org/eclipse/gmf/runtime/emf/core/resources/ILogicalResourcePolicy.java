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
 * <p>
 * Interface implemented by policies that constrain or augment the management
 * of {@link ILogicalResource}s.
 * </p><p>
 * Policies are consulted to determine whether an object is allowed to be
 * separated and, when it is allowed, whether it is possible to separate or
 * absorb an object.  Policies also can provide hooks for augmenting the
 * separate and absorb operations, to perform additional work required by the
 * metamodel or by the client.  Policies are registered on the
 * <tt><a href="../../../../../../../../../extension-points/com_ibm_xtools_emf_msl_core_resourcePolicies.html">org.eclipse.gmf.runtime.emf.core.resourcePolicies</a></tt>
 * extension point.
 * </p><p>
 * It is recommended to extend the {@link AbstractLogicalResourcePolicy} class
 * for the convenience of implementing only the behaviours required for your
 * metamodel.
 * </p>
 *
 * @author Christian W. Damus (cdamus)
 * 
 * @see AbstractLogicalResourcePolicy
 */
public interface ILogicalResourcePolicy {
	
	/**
	 * <p>
	 * Queries whether the specified <code>eObject</code> should be allowed to
	 * be separated.  All available policies are consulted until one indicates
	 * that the <code>eObject</code> should not be separated.
	 * </p><p>
	 * Note that this method needs only consider structural or metamodel-specific
	 * semantics of the separation of an element.  It will not even be invoked
	 * by the system in cases where basic feasibility of separation is not met,
	 * such as where the <code>eObject</code> is already separated or is a
	 * logical resource root.
	 * </p>
	 * 
	 * @param resource the logical resource containing the element to be separated
	 * @param eObject a model element
	 * @return whether it can be stored separately
	 */
	boolean canSeparate(ILogicalResource resource, EObject eObject);
	
	/**
	 * Invoked before separation of an element in a logical resource, for three
	 * purposes:
	 * <ul>
	 *   <li>as an opportunity to perform additional set-up before the
	 *       separation operation.  Examples include making version-controlled
	 *       resources writable</li>
	 *   <li>to suggest an alternative URI to the one passed in</li>
	 *   <li>as an opportunity to veto the separation operation.  Examples
	 *       include attempts to modify resources requiring special protection
	 *       (such as so-called "library models")</li>
	 * </ul>
	 * 
	 * @param resource the logical resource containing the element to be separated
	 * @param eObject a model element
	 * @param uri URI for the physical resource that is intended to store the
	 *    <code>eObject</code> in
	 * @return a suggested alternative URI to the one passed in, or
	 *    <code>null</code> if the policy has no special recommendation.  The
	 *    policy must not depend on this URI being accepted by the system; it
	 *    may be rejected
	 * 
	 * @throws CannotSeparateException if the separation should not be allowed
	 *     to proceed for some reason (this is the veto)
	 * 
	 * @see #canSeparate(ILogicalResource, EObject)
	 * @see #postSeparate(ILogicalResource, EObject, URI)
	 */
	URI preSeparate(ILogicalResource resource, EObject eObject, URI uri) throws CannotSeparateException;
	
	/**
	 * Invoked after separation of an element in a logical resource, as an
	 * opportunity to perform additional work to complete the separation
	 * operation.
	 * 
	 * @param resource the logical resource containing the element that was separated
	 * @param eObject a model element that was separated
	 * @param uri URI for the physical resource that now stores the
	 *    <code>eObject</code>.  This may be different than the URI that was
	 *    passed in the {@link #preSeparate(ILogicalResource, EObject, URI)}
	 *    method, in case some policy suggested an alternative
	 * 
	 * @throws CannotSeparateException if the separation should not be allowed
	 *     to proceed for some reason (this is the veto)
	 * 
	 * @see #canSeparate(ILogicalResource, EObject)
	 * @see #preSeparate(ILogicalResource, EObject, URI)
	 */
	void postSeparate(ILogicalResource resource, EObject eObject, URI uri);
	
	/**
	 * Invoked before absorption of an element in a logical resource, for two
	 * purposes:
	 * <ul>
	 *   <li>as an opportunity to perform additional set-up before the
	 *       absorption operation.  Examples include making version-controlled
	 *       resources writable</li>
	 *   <li>as an opportunity to veto the absorption operation.  Examples
	 *       include attempts to modify resources requiring special protection
	 *       (such as so-called "library models")</li>
	 * </ul>
	 * 
	 * @param resource the logical resource containing the element to be absorbed
	 * @param eObject the element to be absorbed
	 * 
	 * @throws CannotAbsorbException if the absorption should not be allowed
	 *     to proceed for some reason (this is the veto)
	 * 
	 * @see #postAbsorb(ILogicalResource, EObject)
	 */
	void preAbsorb(ILogicalResource resource, EObject eObject) throws CannotAbsorbException;

	/**
	 * Invoked after absorption of an element in a logical resource, as an
	 * opportunity to perform additional work to complete the absorption
	 * operation.
	 * 
	 * @param resource the logical resource containing the element that was absorbed
	 * @param eObject the element that was absorbed
	 * 
	 * @see #preAbsorb(ILogicalResource, EObject)
	 */
	void postAbsorb(ILogicalResource resource, EObject eObject);
}
