/******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.util;

import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.core.resources.IResourceHelper;


/**
 * Internal utilities, for private use of the EMF Core plug-in.
 * This class was made public, but a request was made to maintain internals.
 * See Bugzilla 300540
 *
 * @author Christian W. Damus (cdamus)
 */
public class Util {

	/** Cannot instantiate. */
	private Util() {
		super();
	}

	/**
	 * Gets the helper for the specified resource, if any.
	 * 
	 * @param resource a resource (may be <code>null</code>)
	 * 
	 * @return the helper, if one is attached, or <code>null</code> if none
	 *     or if no resource is specified
	 */
	public static IResourceHelper getHelper(Resource resource) {
		return org.eclipse.gmf.runtime.emf.core.util.Util.getHelper(resource);
	}

	/**
	 * Encodes the specified qualified name.
	 * 
	 * @param qName
	 *            The qualified name to be encoded.
	 * @return The encoded qualified name.
	 */
	public static String encodeQualifiedName(String qName) {
		return org.eclipse.gmf.runtime.emf.core.util.Util.encodeQualifiedName(qName);
	}

	/**
	 * Appends an encoded version of the specified qualified name to the
	 * specified buffer. All excluded characters, such as space and
	 * <code>#</code>, are escaped, as are <code>/</code> and
	 * <code>?</code>.
	 * 
	 * @param buffer
	 *            The buffer to which to append.
	 * @param qName
	 *            The qualified name to be encoded.
	 * @return The buffer.
	 */
	public static StringBuffer appendQualifiedName(StringBuffer buffer, String qName) {
		return org.eclipse.gmf.runtime.emf.core.util.Util.appendQualifiedName(buffer, qName);
	}
	
	/**
	 * Decodes the specified qualified name by replacing each three-digit escape
	 * sequence by the character that it represents.
	 * 
	 * @param qName
	 *            The qualified name to be decoded.
	 * @return The decoded qualified name.
	 */
	public static String decodeQName(String qName) {
		return org.eclipse.gmf.runtime.emf.core.util.Util.decodeQName(qName);
	}

	/**
	 * Attempts to obtain the most abstract URI possible for the specified URI,
	 * preferring "platform:" scheme over "file:" scheme for files in the
	 * workspace, and "pathmap:" scheme over these where a file resides in a
	 * path-mapped location.
	 * 
	 * @param uri the URI to denormalize
	 * @param rset the resource set context for this URI (defining, among other
	 *     things, a URI converter)
	 * @return the URI denormalized as much as possible
	 */
	public static URI denormalizeURI(URI uri, ResourceSet rset) {
		return org.eclipse.gmf.runtime.emf.core.util.Util.denormalizeURI(uri, rset);
	}

	/**
	 * Gets the proxy ID by parsing the proxy URI.
	 * 
	 * @param proxy
	 *            The proxy object.
	 * @return The ID.
	 */
	public static String getProxyID(EObject proxy) {
		return org.eclipse.gmf.runtime.emf.core.util.Util.getProxyID(proxy);
	}

	/**
	 * Attempts to resolve the specified <code>proxy</code> object, returning
	 * <code>null</code> (rather than the original proxy) if it is unresolvable.
	 * 
	 * @param domain
	 *            The editing domain.
	 * @param proxy
	 *            The proxy object.
	 * @return The resolved object, or <code>null</code> if not resolved
	 */
	public static EObject resolve(TransactionalEditingDomain domain,
			EObject proxy) {
		return org.eclipse.gmf.runtime.emf.core.util.Util.resolve(domain, proxy);
	}

	/**
	 * Can an instance of class1 contain an instance of class2.
	 */
	public static boolean canContain(EClass class1, EClass class2, Set visited) {
		return org.eclipse.gmf.runtime.emf.core.util.Util.canContain(class1, class2, visited);
	}
}
