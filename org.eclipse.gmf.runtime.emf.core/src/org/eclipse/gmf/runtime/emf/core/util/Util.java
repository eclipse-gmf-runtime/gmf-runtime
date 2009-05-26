/******************************************************************************
 * Copyright (c) 2006, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.util;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.resources.PathmapManager;
import org.eclipse.gmf.runtime.emf.core.internal.util.EMFCoreConstants;
import org.eclipse.gmf.runtime.emf.core.resources.IResourceHelper;

import com.ibm.icu.util.StringTokenizer;


/**
 * Internal utilities, for private use of the EMF Core plug-in.
 *
 * @author Christian W. Damus (cdamus)
 * @since 1.2
 */
public class Util {
	private static final String pathDelimiter = String
		.valueOf(EMFCoreConstants.PATH_SEPARATOR);

	private static final String colonEscaped = "%3A"; //$NON-NLS-1$

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
		IResourceHelper result = null;
		
		if (resource != null) {
			result = (IResourceHelper) EcoreUtil.getExistingAdapter(
				resource,
				IResourceHelper.class);
		}
		
		return result;
	}

	/**
	 * Encodes the specified qualified name.
	 * 
	 * @param qName
	 *            The qualified name to be encoded.
	 * @return The encoded qualified name.
	 */
	public static String encodeQualifiedName(String qName) {
		return appendQualifiedName(new StringBuffer(), qName).toString();
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

		String[] segments = qName.split(EMFCoreConstants.QUALIFIED_NAME_SEPARATOR);

		for (int i = 0; i < segments.length; i++) {

			String encodedSegment = URI.encodeSegment(segments[i], true);

			for (int j = 0, length = encodedSegment.length(); j < length; j++) {

				char c = encodedSegment.charAt(j);

				if (':' == c) {
					// EMF treats :'s as special characters in fragments...
					buffer.append(colonEscaped);
				} else {
					buffer.append(c);
				}
			}

			if (i + 1 < segments.length) {
				buffer.append(EMFCoreConstants.PATH_SEPARATOR);
			}
		}

		return buffer;
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

		StringBuffer buffer = new StringBuffer();

		for (StringTokenizer st = new StringTokenizer(qName, pathDelimiter); st
			.hasMoreTokens();) {

			buffer.append(URI.decode(st.nextToken()));

			if (st.hasMoreTokens())
				buffer.append(EMFCoreConstants.QUALIFIED_NAME_SEPARATOR);
		}

		return buffer.toString();
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
		URI denormalizedURI = uri;

		// First, check to see if this is a file URI and it is in the workspace.
		//  If so, we will denormalize first to a platform URI.
		if ("file".equals(denormalizedURI.scheme())) { //$NON-NLS-1$
			IContainer[] containers = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocationURI(java.net.URI.create(denormalizedURI.toString()));
			if (containers.length == 1) {
				denormalizedURI = URI.createPlatformResourceURI(containers[0].getFullPath().toString(),true);
			}
		}
		
		// Second, we will now attempt to find a pathmap for this URI
		denormalizedURI = PathmapManager.denormalizeURI(denormalizedURI);

		return denormalizedURI;
	}

	/**
	 * Gets the proxy ID by parsing the proxy URI.
	 * 
	 * @param proxy
	 *            The proxy object.
	 * @return The ID.
	 */
	public static String getProxyID(EObject proxy) {

		URI uri = EcoreUtil.getURI(proxy);
		
		String uriFragment = uri.fragment();

		int index = uriFragment.indexOf(EMFCoreConstants.FRAGMENT_SEPARATOR);

		return index != -1 ? uriFragment.substring(0, index)
			: uriFragment;
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
		
		EObject resolved = EcoreUtil.resolve(proxy, domain.getResourceSet());

		return (resolved.eIsProxy() ? null : resolved);
	}

	/**
	 * Can an instance of class1 contain an instance of class2.
	 */
	public static boolean canContain(EClass class1, EClass class2, Set visited) {

		Iterator i = class1.getEAllReferences().iterator();

		while (i.hasNext()) {

			EReference reference = (EReference) i.next();

			if (reference.isContainment()) {

				EClass eType = (EClass) reference.getEType();

				if ((eType.equals(class2)) || (eType.isSuperTypeOf(class2))) {

					return true;
				}
			}
		}

		if ((visited != null) && (!visited.contains(class1))) {

			visited.add(class1);

			i = class1.getEAllReferences().iterator();

			while (i.hasNext()) {

				EReference reference = (EReference) i.next();

				if (reference.isContainment()) {

					EClass eType = (EClass) reference.getEType();

					if (canContain(eType, class2, visited))
						return true;
				}
			}
		}

		return false;
	}

}
