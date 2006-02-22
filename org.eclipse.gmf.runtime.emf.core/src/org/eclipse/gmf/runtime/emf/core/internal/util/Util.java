/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.internal.index.ReferenceVisitor;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.EMFCoreDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.EMFCorePlugin;
import org.eclipse.gmf.runtime.emf.core.resources.IResourceHelper;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.osgi.framework.Bundle;

import com.ibm.icu.util.StringTokenizer;


/**
 * Internal utilities, for private use of the EMF Core plug-in.
 *
 * @author Christian W. Damus (cdamus)
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

		URI resolvedURI = uri;

		if (EMFCoreConstants.PLATFORM_SCHEME.equals(resolvedURI.scheme())) {

			String filePath = getFilePath(rset, resolvedURI);

			if ((filePath != null) && (filePath.length() > 0))
				resolvedURI = URI.createFileURI(filePath);
		}

		if ((resolvedURI != null) && (resolvedURI.isFile())) {

			String fileName = resolvedURI.lastSegment();

			// attempt to convert the URI to a path map URI.
			if (fileName != null) {

				URI prefix = resolvedURI.trimSegments(1);

				// find a matching pathmap.
				URI foundKeyURI = null;
				URI foundValURI = null;
				int minDiff = Integer.MAX_VALUE;

				Iterator i = rset.getURIConverter().getURIMap()
					.entrySet().iterator();

				while (i.hasNext()) {

					Map.Entry entry = (Map.Entry) i.next();

					if (entry != null) {

						URI keyURI = (URI) entry.getKey();
						URI valURI = (URI) entry.getValue();

						if ((keyURI.isHierarchical())
							&& (EMFCoreConstants.PATH_MAP_SCHEME.equals(keyURI
								.scheme())) && (valURI.isFile())) {

							int diff = computeDiff(valURI, prefix);

							if ((diff >= 0) && (diff < minDiff)) {

								minDiff = diff;

								foundKeyURI = keyURI;
								foundValURI = valURI;

								if (minDiff == 0)
									break;
							}
						}
					}
				}

				if ((foundKeyURI != null) && (foundValURI != null))
					return resolvedURI.replacePrefix(foundValURI, foundKeyURI);
			}

			// attempt to convert URI to a platform URI.
			URI platformURI = getPlatformURI(uri);

			if (platformURI != null)
				return platformURI;
		}

		return uri;
	}
	
	/**
	 * Obtains, if possible, an absolute filesystem path corresponding to the
	 * specified URI.
	 * 
	 * @param resourceSet a resource set context for the URI normalization
	 * @param uri the URI to normalize to a file path
	 * 
	 * @return the file path, or <code>null</code> if the URI does not resolve
	 *      to a file
	 */
	private static String getFilePath(ResourceSet resourceSet, URI uri) {

		String filePath = null;

		if (uri == null) {

			filePath = EMFCoreConstants.EMPTY_STRING;
			return filePath;
		}

		if ((resourceSet != null)
			&& (EMFCoreConstants.PATH_MAP_SCHEME.equals(uri.scheme())))
			uri = resourceSet.getURIConverter().normalize(uri);

		if (uri.isFile())
			filePath = uri.toFileString();

		else if (EMFCoreConstants.PLATFORM_SCHEME.equals(uri.scheme())) {

			String[] segments = uri.segments();

			if (segments.length > 2) {

				if (EMFCoreConstants.RESOURCE.equals(segments[0])) {

					IProject project = null;

					IWorkspace workspace = ResourcesPlugin.getWorkspace();

					if (workspace != null) {

						IWorkspaceRoot root = workspace.getRoot();

						if (root != null)
							project = root.getProject(URI.decode(segments[1]));
					}

					if ((project != null) && (project.exists())) {

						StringBuffer path = new StringBuffer();

						path.append(project.getLocation().toString());

						for (int i = 2; i < segments.length; i++) {

							path.append(EMFCoreConstants.PATH_SEPARATOR);

							path.append(URI.decode(segments[i]));
						}

						filePath = path.toString();
					}

				} else if (EMFCoreConstants.PLUGIN.equals(segments[0])) {

					Bundle bundle = Platform.getBundle(URI.decode(segments[1]));

					if (bundle != null) {

						StringBuffer path = new StringBuffer();

						for (int i = 2; i < segments.length; i++) {

							path.append(URI.decode(segments[i]));

							path.append(EMFCoreConstants.PATH_SEPARATOR);
						}

						URL url = bundle.getEntry(path.toString());

						if (url != null) {

							try {

								url = FileLocator.resolve(url);

								if (url != null) {

									if (EMFCoreConstants.FILE_SCHEME.equals(url
										.getProtocol()))
										filePath = url.getPath();
								}

							} catch (IOException e) {

								Trace.catching(EMFCorePlugin.getDefault(),
									EMFCoreDebugOptions.EXCEPTIONS_CATCHING,
									Util.class, "getFilePath", e); //$NON-NLS-1$
							}
						}
					}
				}
			}
		}

		if (filePath == null)
			filePath = EMFCoreConstants.EMPTY_STRING;

		else {

			if (filePath.indexOf(EMFCoreConstants.INVALID_PATH) == -1) {

				if (File.separatorChar != EMFCoreConstants.PATH_SEPARATOR)
					filePath = filePath.replace(EMFCoreConstants.PATH_SEPARATOR,
						File.separatorChar);

			} else
				filePath = EMFCoreConstants.EMPTY_STRING;
		}

		return filePath;
	}

	/**
	 * Converts a file URI to a platform URI.
	 */
	private static URI getPlatformURI(URI uri) {

		if (EMFCoreConstants.PLATFORM_SCHEME.equals(uri.scheme()))
			return URI.createURI(uri.toString(), true);

		IFile file = findFileInWorkspace(uri);

		if (file != null) {

			IProject project = file.getProject();

			if (project != null) {

				StringBuffer pathName = new StringBuffer(project.getName());

				pathName.append(EMFCoreConstants.PATH_SEPARATOR);
				pathName.append(file.getProjectRelativePath().toString());

				return URI.createURI(URI.createPlatformResourceURI(
					pathName.toString(),true).toString(), true);
			}
		}

		return null;
	}

	/**
	 * Finds a file in the workspace given its file URI.
	 */
	private static IFile findFileInWorkspace(URI uri) {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		if (workspace != null) {

			IWorkspaceRoot root = workspace.getRoot();

			if (root != null) {

				IFile[] files = root.findFilesForLocation(new Path(uri
					.toFileString()));

				if (files != null) {

					for (int i = 0; i < files.length; i++) {

						IFile file = files[i];

						IProject project = file.getProject();

						if (project != null)
							return file;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Computes segement count difference between two URIs if one is a subset of
	 * the other.
	 */
	private static int computeDiff(URI subURI, URI containerURI) {

		int subSegmentCount = subURI.segmentCount();
		int containerSegmentCount = containerURI.segmentCount();

		if ((subSegmentCount > 0)
			&& (subURI.segment(subSegmentCount - 1)
				.equals(EMFCoreConstants.EMPTY_STRING))) {

			subURI = subURI.trimSegments(1);
			subSegmentCount--;
		}

		if ((containerSegmentCount > 0)
			&& (containerURI.segment(containerSegmentCount - 1)
				.equals(EMFCoreConstants.EMPTY_STRING))) {

			containerURI = containerURI.trimSegments(1);
			containerSegmentCount--;
		}

		int diff = containerSegmentCount - subSegmentCount;

		if (diff < 0)
			return -1;

		else if (diff > 0)
			containerURI = containerURI.trimSegments(diff);

		if (!subURI.equals(containerURI))
			return -1;

		return diff;
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
	
	/**
	 * Destroys an EMF ebject.
	 */
	public static void destroy(EObject eObject) {
		EObject container = eObject.eContainer();

		teardownContainment(eObject);

		if (container != null) {

			EReference reference = eObject.eContainmentFeature();

			if (reference.isMany()) {
				((Collection) container.eGet(reference)).remove(eObject);
			} else {
				container.eSet(reference, null);
			}
			
			teardownReferences(eObject, container);
		}
	}

	/**
	 * Tear down containment of an EMF object.
	 */
	private static void teardownContainment(EObject eObject) {

		List actions = new ArrayList();

		Iterator i = eObject.eClass().getEAllContainments().iterator();

		while (i.hasNext()) {

			EReference reference = (EReference) i.next();

			if ((reference.isChangeable()) && (eObject.eIsSet(reference))) {
				actions
					.add(new TeardownAction(eObject, reference, null));
			}
		}

		Iterator j = actions.iterator();

		while (j.hasNext())
			((TeardownAction) j.next()).execute();
	}

	/**
	 * Teardown references to and from an EMF object.
	 */
	private static void teardownReferences(final EObject eObject,
			final EObject container) {

		final List actions = new ArrayList();

		Iterator i = eObject.eClass().getEAllReferences().iterator();

		while (i.hasNext()) {

			EReference reference = (EReference) i.next();

			if ((reference.isChangeable()) && (!reference.isContainer())
				&& (!reference.isContainment()) && (eObject.eIsSet(reference))) {

				actions
					.add(new TeardownAction(eObject, reference, null));
			}
		}

		ReferenceVisitor visitor = new ReferenceVisitor(eObject) {

			protected void visitedReferencer(EReference reference,
					EObject referencer) {

				actions.add(new TeardownAction(referencer, reference,
					eObject));
			}};

		visitor.visitReferencers();

		Iterator j = actions.iterator();

		while (j.hasNext())
			((TeardownAction) j.next()).execute();
	}

	
	/**
	 * Helper class used by teardown.
	 */
	private static class TeardownAction {

		private EObject container = null;

		private EReference reference = null;

		private EObject object = null;

		/**
		 * Constructor.
		 */
		public TeardownAction(EObject container,
				EReference reference, EObject object) {

			this.container = container;
			this.reference = reference;
			this.object = object;
		}

		/**
		 * Execute the action.
		 */
		public void execute() {

			if (object == null) {

				if (container.eIsSet(reference)) {

					if (reference.isMany()) {

						List objects = (List) container.eGet(reference);

						if (reference.isContainment()) {

							if (!objects.isEmpty()) {

								Collection destroyed = new ArrayList(objects);

								for (Iterator i = destroyed.iterator(); i
									.hasNext();)
									EMFCoreUtil.destroy((EObject) i.next());
							}

						} else {

							if (!objects.isEmpty()) {

								Collection detached = new ArrayList(objects);

								for (Iterator i = detached.iterator(); i
									.hasNext();) {

									EObject eObject = (EObject) i.next();

									((Collection) container.eGet(reference))
										.remove(eObject);
								}
							}
						}

					} else {

						if (reference.isContainment()) {

							object = (EObject) container.eGet(reference);

							if (object != null)
								EMFCoreUtil.destroy(object);
						} else
							container.eSet(reference, null);
					}
				}

			} else {

				if (reference.isContainment())
					EMFCoreUtil.destroy(object);

				else {

					if (reference.isMany())
						((Collection) container.eGet(reference)).remove(object);
					else
						container.eSet(reference, null);
				}
			}
		}
	}

}
