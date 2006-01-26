/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
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
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.osgi.framework.Bundle;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.EventTypes;
import org.eclipse.gmf.runtime.emf.core.IValidationStatus;
import org.eclipse.gmf.runtime.emf.core.edit.MDestroyOption;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.edit.MRunnable;
import org.eclipse.gmf.runtime.emf.core.internal.commands.MCommand;
import org.eclipse.gmf.runtime.emf.core.internal.commands.MSLAbandonActionCommand;
import org.eclipse.gmf.runtime.emf.core.internal.commands.MSLEventCommand;
import org.eclipse.gmf.runtime.emf.core.internal.commands.MSLProxyCommand;
import org.eclipse.gmf.runtime.emf.core.internal.domain.MSLEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.index.MSLReferenceVisitor;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.MSLPlugin;
import org.eclipse.gmf.runtime.emf.core.internal.resources.MSLResource;
import org.eclipse.gmf.runtime.emf.core.internal.services.metamodel.MetamodelSupportService;
import org.eclipse.gmf.runtime.emf.core.services.metamodel.IMetamodelSupport;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;

import com.ibm.icu.util.StringTokenizer;

/**
 * Some internal utility classes.
 * 
 * @author rafikj
 */
public class MSLUtil {

	private final static Charset xmiCharset = Charset
		.forName(MSLConstants.XMI_ENCODING);

	// A char ch is encoded as itself if (nonEncodedMin <= ch && ch <=
	// nonEncodedMax && same[ch - sameMin]).
	private final static boolean[] nonEncoded;

	private final static char nonEncodedMin;

	private final static char nonEncodedMax;

	private final static char[] hexChars = "0123456789abcdef".toCharArray(); //$NON-NLS-1$

	static {

		char[] sameChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_.!~*'()".toCharArray(); //$NON-NLS-1$

		int len = sameChars.length;
		char min = '0';
		char max = '0';
		int i;
		char c;

		for (i = 0; i < len; ++i) {
			c = sameChars[i];
			if (min > c)
				min = c;
			if (max < c)
				max = c;
		}

		nonEncoded = new boolean[max - min + 1];
		nonEncodedMin = min;
		nonEncodedMax = max;

		for (i = 0; i < len; ++i)
			nonEncoded[sameChars[i] - min] = true;
	}

	/**
	 * Get the meta-model of an EMF object.
	 */
	public static IMetamodelSupport getMetaModel(EObject eObject) {

		if (eObject instanceof ENamedElement) {

			EObject root = EcoreUtil.getRootContainer(eObject);

			if (root instanceof EPackage)
				return MetamodelSupportService.getInstance().getMetamodelSupport(
					(EPackage) root);
		}

		IMetamodelSupport metaModel = null;

		EPackage ePackage = null;

		for (EObject current = eObject; (metaModel == null)
			&& (current != null); current = current.eContainer()) {

			ePackage = current.eClass().getEPackage();

			metaModel = MetamodelSupportService.getInstance()
				.getMetamodelSupport(ePackage);
		}

		return metaModel;
	}

	/**
	 * Create an EMF object instance given its EClass.
	 */
	public static EObject create(MSLEditingDomain domain, EClass eClass,
			boolean sendEvents) {

		EObject eObject = eClass.getEPackage().getEFactoryInstance().create(
			eClass);

		domain.getContentAdapter().listenToModifications(eObject);

		if (sendEvents)
			sendCreateEvent(domain, eObject);

		return eObject;
	}

	/**
	 * Send a create event.
	 */
	public static void sendCreateEvent(MSLEditingDomain domain, EObject eObject) {

		Notification createNotification = new ENotificationImpl(
			(InternalEObject) eObject, EventTypes.CREATE,
			(EStructuralFeature) null, (Object) null, (Object) null, -1);

		Notification uncreateNotification = new ENotificationImpl(
			(InternalEObject) eObject, EventTypes.UNCREATE,
			(EStructuralFeature) null, (Object) null, (Object) null, -1);

		execute(domain, MSLEventCommand.create(domain, createNotification,
			uncreateNotification));
	}

	/**
	 * Destroys an EMF ebject.
	 */
	public static void destroy(MSLEditingDomain domain, EObject eObject,
			int options) {

		EObject container = eObject.eContainer();

		if (container == null)
			return;

		IMetamodelSupport metaModel = getMetaModel(container);

		if ((metaModel != null) && (!metaModel.canDestroy(eObject)))
			return;

		eObject.eNotify(new ENotificationImpl(
			(InternalEObject) eObject, EventTypes.PRE_DESTROY,
			(EStructuralFeature) null,
			eObject, eObject));
		
		Resource resource = container.eResource();

		teardownContainment(domain, eObject, options);

		container = eObject.eContainer();

		if (container != null) {

			String uriFragment = null;

			if (((options & MDestroyOption.MAKE_PROXY) != 0)
				&& (resource != null))
				uriFragment = resource.getURIFragment(eObject);

			EReference reference = eObject.eContainmentFeature();

			if (reference.isMany())
				((Collection) container.eGet(reference)).remove(eObject);
			else
				container.eSet(reference, null);

			if ((options & MDestroyOption.NO_EVENTS) == 0)
				sendDestroyEvent(domain, eObject);

			if ((options & MDestroyOption.KEEP_REFERENCES) == 0)
				teardownReferences(domain, eObject, container, options);

			if (uriFragment != null) {

				URI resourceURI = resource.getURI();

				if (resourceURI != null) {

					execute(domain, MSLProxyCommand.create(domain, eObject,
						resourceURI.appendFragment(uriFragment)));

					domain.sendNotification(eObject, EventTypes.UNRESOLVE);
				}
			}
		}
	}
	
	/**
	 * Abandons the current write action in the specified editing
	 * <code>domain</code>, if any.  The action is not immediately rolled back,
	 * but will rollback when it finishes.
	 * 
	 * @param domain the editing domain
	 * @param message the localized reason for the action abandonment
	 */
	public static void abandon(MEditingDomain domain, String message) {
		if (domain instanceof MSLEditingDomain) {
			MSLEditingDomain mslDomain = (MSLEditingDomain) domain;
			
			if (mslDomain.getUndoStack().isWriteActionInProgress()) {
				MSLUtil.execute(mslDomain,
					new MSLAbandonActionCommand(mslDomain, message));
			}
		}
	}

	/**
	 * Send a destroy event.
	 */
	public static void sendDestroyEvent(MSLEditingDomain domain, EObject eObject) {

		Notification createNotification = new ENotificationImpl(
			(InternalEObject) eObject, EventTypes.DESTROY,
			(EStructuralFeature) null, (Object) null, (Object) null, -1);

		Notification uncreateNotification = new ENotificationImpl(
			(InternalEObject) eObject, EventTypes.UNDESTROY,
			(EStructuralFeature) null, (Object) null, (Object) null, -1);

		execute(domain, MSLEventCommand.create(domain, createNotification,
			uncreateNotification));
	}

	/**
	 * Get the ID of an EMF object.
	 */
	public static String getID(EObject eObject) {

		Resource resource = eObject.eResource();

		String id = null;

		if (resource == null)
			id = MSLResource.getSavedID(eObject);

		else if (resource instanceof XMLResource)
			id = ((XMLResource) resource).getID(eObject);

		if (id != null)
			return id;
		else
			return MSLConstants.EMPTY_STRING;
	}

	/**
	 * Set the ID of an EMF object.
	 */
	public static void setID(EObject eObject, String id) {

		Resource resource = eObject.eResource();

		if (resource instanceof XMLResource)
			((XMLResource) resource).setID(eObject, id);
	}

	/**
	 * Default implementation of the MSL proxy URI parser. Parsing of name.
	 */
	public static String getProxyName(EObject eObject) {

		if (eObject == null)
			return MSLConstants.EMPTY_STRING;

		if (!eObject.eIsProxy())
			return EObjectUtil.getName(eObject);

		String name = MSLConstants.EMPTY_STRING;

		String proxyQName = getProxyQName(eObject);

		if ((proxyQName != null) && (proxyQName.length() > 0)) {

			String[] segments = proxyQName
				.split(MSLConstants.QUALIFIED_NAME_SEPARATOR);

			name = segments[segments.length - 1];
		}

		if ((name == null) || (name.length() == 0)) {

			EAttribute nameAttribute = MetaModelUtil.getNameAttribute(eObject
				.eClass());

			if (nameAttribute != null) {

				name = (String) eObject.eGet(nameAttribute);

				if (name != null)
					return name;
			}

			return MSLConstants.EMPTY_STRING;
		}

		return name;
	}

	/**
	 * Default implementation of the MSL proxy URI parser. Parsing of qualified
	 * name.
	 */
	public static String getProxyQName(EObject eObject) {

		if (eObject == null)
			return MSLConstants.EMPTY_STRING;

		if (!eObject.eIsProxy())
			return EObjectUtil.getQName(eObject, true);

		String proxyQName = MSLConstants.EMPTY_STRING;

		String uriFragment = EcoreUtil.getURI(eObject).fragment();

		int index = uriFragment.indexOf(MSLConstants.FRAGMENT_SEPARATOR);

		if (index != -1) {
			proxyQName = decodeQName(uriFragment.substring(index + 1,
				uriFragment.length()));
		}

		if ((proxyQName == null) || (proxyQName.length() == 0)) {

			EAttribute qNameAttribute = MetaModelUtil.getQNameAttribute(eObject
				.eClass());

			if (qNameAttribute != null) {

				String qualifiedName = (String) eObject.eGet(qNameAttribute);

				if (qualifiedName != null)
					return qualifiedName;
				else
					return MSLConstants.EMPTY_STRING;
			}
		}

		return proxyQName;
	}

	/**
	 * Default implementation of the MSL proxy URI parser. Parsing of ID.
	 */
	public static String getProxyID(EObject eObject) {

		if (eObject == null)
			return MSLConstants.EMPTY_STRING;

		if (!eObject.eIsProxy())
			return getID(eObject);

		String uriFragment = EcoreUtil.getURI(eObject).fragment();

		int index = uriFragment.indexOf(MSLConstants.FRAGMENT_SEPARATOR);

		return index != -1 ? uriFragment.substring(0, index)
			: uriFragment;
	}

	/**
	 * Default implementation of the MSL proxy URI parser. Parsing of Class ID.
	 */
	public static String getProxyClassID(EObject eObject) {
		return eObject == null ? MSLConstants.EMPTY_STRING
			: MetaModelUtil.getID(eObject.eClass());
	}

	/**
	 * Resolve a proxy object.
	 */
	public static EObject resolve(MSLEditingDomain domain,
			EObject eObject, boolean resolve) {

		if (eObject == null)
			return null;

		if (!eObject.eIsProxy())
			return eObject;

		if (resolve) {

			EObject resolved = EcoreUtil.resolve(eObject, domain
				.getResourceSet());

			return (resolved.eIsProxy() ? null
				: resolved);
		}

		return null;
	}

	/**
	 * Can an instance of class1 contain an instance of class2.
	 */
	public static boolean canContain(EClass class1, EClass class2, Set visited) {

		IMetamodelSupport metaModel = MSLUtil.getMetaModel(class1);

		{
			Iterator i = class1.getEAllReferences().iterator();

			while (i.hasNext()) {

				EReference reference = (EReference) i.next();

				if (reference.isContainment()) {

					EClass eType = (EClass) reference.getEType();

					if ((eType.equals(class2)) || (eType.isSuperTypeOf(class2))) {

						if (metaModel == null)
							return true;

						else if (metaModel
							.canContain(class1, reference, class2))
							return true;
					}
				}
			}
		}

		if ((visited != null) && (!visited.contains(class1))) {

			visited.add(class1);

			Iterator i = class1.getEAllReferences().iterator();

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
	 * Execute an MSL command.
	 */
	public static void execute(MSLEditingDomain domain, MCommand command) {

		if (!command.canExecute()) {

			RuntimeException e = new IllegalStateException(
				"cannot execute EMF command"); //$NON-NLS-1$

			Trace.throwing(MSLPlugin.getDefault(),
				MSLDebugOptions.EXCEPTIONS_THROWING, MSLUtil.class,
				"execute", e); //$NON-NLS-1$

			throw e;
		}

		command.execute();

		domain.getUndoStack().add(command);
	}

	/**
	 * Resolve the file path of a resource.
	 */
	public static String getFilePath(Resource resource) {
		return getFilePath(resource.getResourceSet(), resource.getURI());
	}

	/**
	 * Resolve a URI to a file path.
	 */
	public static String getFilePath(ResourceSet resourceSet, URI uri) {

		String filePath = null;

		if (uri == null) {

			filePath = MSLConstants.EMPTY_STRING;
			return filePath;
		}

		if ((resourceSet != null)
			&& (MSLConstants.PATH_MAP_SCHEME.equals(uri.scheme())))
			uri = resourceSet.getURIConverter().normalize(uri);

		if (uri.isFile())
			filePath = uri.toFileString();

		else if (MSLConstants.PLATFORM_SCHEME.equals(uri.scheme())) {

			String[] segments = uri.segments();

			if (segments.length > 2) {

				if (MSLConstants.RESOURCE.equals(segments[0])) {

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

							path.append(MSLConstants.PATH_SEPARATOR);

							path.append(URI.decode(segments[i]));
						}

						filePath = path.toString();
					}

				} else if (MSLConstants.PLUGIN.equals(segments[0])) {

					Bundle bundle = Platform.getBundle(URI.decode(segments[1]));

					if (bundle != null) {

						StringBuffer path = new StringBuffer();

						for (int i = 2; i < segments.length; i++) {

							path.append(URI.decode(segments[i]));

							path.append(MSLConstants.PATH_SEPARATOR);
						}

						URL url = bundle.getEntry(path.toString());

						if (url != null) {

							try {

								url = Platform.resolve(url);

								if (url != null) {

									if (MSLConstants.FILE_SCHEME.equals(url
										.getProtocol()))
										filePath = url.getPath();
								}

							} catch (IOException e) {

								Trace.catching(MSLPlugin.getDefault(),
									MSLDebugOptions.EXCEPTIONS_CATCHING,
									MSLUtil.class, "getFilePath", e); //$NON-NLS-1$
							}
						}
					}
				}
			}
		}

		if (filePath == null)
			filePath = MSLConstants.EMPTY_STRING;

		else {

			if (filePath.indexOf(MSLConstants.INVALID_PATH) == -1) {

				if (File.separatorChar != MSLConstants.PATH_SEPARATOR)
					filePath = filePath.replace(MSLConstants.PATH_SEPARATOR,
						File.separatorChar);

			} else
				filePath = MSLConstants.EMPTY_STRING;
		}

		return filePath;
	}

	/**
	 * Is resource modifiable?
	 */
	public static boolean isModifiable(Resource resource) {

		URI uri = resource.getURI();

		if (uri != null) {

			File file = null;

			if (uri.isFile())
				file = new File(uri.toFileString());
			else
				file = new File(getFilePath(resource));

			return ((file == null) || (!file.exists()) || (file.canWrite()));
		}

		return true;
	}

	/**
	 * Gets resource of an EObject given its URI or create it if not there.
	 */
	public static Resource getResource(MSLEditingDomain domain, EObject eObject) {

		if (eObject.eIsProxy()) {

			URI uri = EcoreUtil.getURI(eObject).trimFragment();

			ResourceSet resourceSet = domain.getResourceSet();

			Resource resource = resourceSet.getResource(uri, false);

			if (resource == null)
				resource = resourceSet.createResource(uri);

			return resource;

		} else
			return eObject.eResource();
	}

	/**
	 * Post-process resource
	 */
	public static void postProcessResource(final Resource resource) {

		if (resource instanceof MSLResource) {

			final MSLEditingDomain domain = (MSLEditingDomain) MEditingDomain
				.getEditingDomain(resource);

			if (domain != null) {

				domain.runAsUnchecked(new MRunnable() {

					public Object run() {

						List resourceContents = resource.getContents();

						if (resourceContents != null) {

							for (Iterator i = resourceContents.iterator(); i
								.hasNext();) {

								EObject root = (EObject) i.next();

								if ((root != null)
									&& (root.eResource() == resource)) {

									IMetamodelSupport metaModel = MSLUtil
										.getMetaModel(root);

									if (metaModel != null)
										metaModel.postProcess(root);
								}
							}
						}

						domain.getResourceIndexer()
							.registerReferences(resource);

						return null;
					}
				});
			}
		}
	}

	/**
	 * Process references and update reference maps.
	 */
	public static void registerReferences(MSLEditingDomain domain,
			EObject eObject, EReference reference, List newObjects,
			List oldObjects) {

		if (reference.isContainment()) {

			if (!newObjects.isEmpty()) {

				Iterator i = newObjects.iterator();

				while (i.hasNext()) {

					EObject newObject = (EObject) i.next();

					domain.getObjectIndexer().registerReferences(eObject,
						newObject);
				}
			}

			if (!oldObjects.isEmpty()) {

				Iterator i = oldObjects.iterator();

				while (i.hasNext()) {

					EObject oldObject = (EObject) i.next();

					domain.getObjectIndexer().deregisterReferences(eObject,
						oldObject);
				}
			}

		} else {

			if (!newObjects.isEmpty()) {

				Iterator i = newObjects.iterator();

				while (i.hasNext()) {

					EObject newObject = (EObject) i.next();

					// registers reference in reverse reference map.
					domain.getObjectIndexer().registerReference(eObject,
						newObject, reference);
				}
			}

			if (!oldObjects.isEmpty()) {

				Iterator i = oldObjects.iterator();

				while (i.hasNext()) {

					EObject oldObject = (EObject) i.next();

					// deregisters reference in reverse reference
					// map.
					domain.getObjectIndexer().deregisterReference(eObject,
						oldObject, reference);
				}
			}
		}
	}

	/**
	 * Returns all of the children of the validation <code>status</code> that
	 * indicate violated constraints (i.e., problems). These will be some
	 * combination of errors, warnings, and informational messages. No reports
	 * of successful constraint evaluations are included
	 * 
	 * @param status
	 *            the validation status (may be <code>null</code> or a
	 *            non-multi-status)
	 * @return the validation problems (as {@link IValidationStatus}es. This
	 *         list is safely modifiable
	 */
	public static List getValidationProblems(IStatus status) {
		final List result;

		if ((status == null) || status.isOK()) {
			// not problems to report
			result = new java.util.ArrayList();
		} else if (!status.isMultiStatus()) {
			// a single problem, or none if it is OK
			result = new java.util.ArrayList();

			if (status instanceof IValidationStatus) {
				result.add(status);
			} // otherwise, the result is an empty list
		} else {
			// potentially multiple problems to report
			IStatus[] children = status.getChildren();
			result = new java.util.ArrayList(children.length);

			for (int i = 0; i < children.length; i++) {
				IStatus next = children[i];

				if ((next instanceof IValidationStatus) && !next.isOK()) {
					result.add(next);
				}
			}
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
	public static String encodeQName(String qName) {
		return appendQName(new StringBuffer(), qName).toString();
	}

	private static final String colonEscaped = "%3A"; //$NON-NLS-1$

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
	public static StringBuffer appendQName(StringBuffer buffer, String qName) {

		String[] segments = qName.split(MSLConstants.QUALIFIED_NAME_SEPARATOR);

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
				buffer.append(MSLConstants.PATH_SEPARATOR);
			}
		}

		return buffer;
	}

	private static final String pathDelimiter = String
		.valueOf(MSLConstants.PATH_SEPARATOR);

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
				buffer.append(MSLConstants.QUALIFIED_NAME_SEPARATOR);
		}

		return buffer.toString();
	}

	/**
	 * Encode a URL to avoid corrupt strings in XML files.
	 */
	public static String encodeURL(String url) {

		int len = url.length();

		// add a little (6.25%) to leave room for some expansion during encoding
		len += len >>> 4;

		return appendURL(new StringBuffer(len), url).toString();
	}

	/**
	 * Encode a URL to avoid corrupt strings in XML files.
	 */
	public static StringBuffer appendURL(StringBuffer buffer, String url) {

		ByteBuffer byteBuffer = xmiCharset.encode(url);
		int limit = byteBuffer.limit();
		byte[] bytes;
		int offset = 0;

		if (byteBuffer.hasArray()) {
			bytes = byteBuffer.array();
			offset = byteBuffer.arrayOffset();
		} else {
			byteBuffer.get(bytes = new byte[limit], 0, limit);
		}

		while (--limit >= 0) {

			char c = (char) bytes[offset++];

			if (c == ' ')
				c = '+';

			else if (c < nonEncodedMin || nonEncodedMax < c
				|| !nonEncoded[c - nonEncodedMin]) {
				buffer.append('%');
				buffer.append(hexChars[(c >>> 4) & 0xF]);
				c = hexChars[c & 0xF];
			}

			buffer.append(c);
		}

		return buffer;
	}

	/**
	 * Decode an encoded URL.
	 */
	public static String decodeURL(String url) {
		int len = url.length();
		byte[] bytes = new byte[len];
		int size = 0;
		boolean simple = true;

		for (int i = 0; i < len; ++i) {

			byte b = (byte) url.charAt(i);

			if (b == '+') {
				simple = false;
				b = ' ';
			} else if (b == '%') {
				simple = false;

				i += 2;

				if (i >= len)
					break;

				b = getByte(url.charAt(i - 1), url.charAt(i));
			}

			bytes[size++] = b;
		}

		String result = xmiCharset.decode(ByteBuffer.wrap(bytes, 0, size))
			.toString();

		if (simple && result.equals(url))
			result = url; // reuse the input string

		return result;
	}

	/**
	 * Decode chars.
	 */
	private static byte getByte(char c1, char c2) {

		byte b = getByte(c1);

		b <<= 4;
		b += getByte(c2);

		return b;
	}

	/**
	 * Decode char.
	 */
	private static byte getByte(char c) {

		byte b = (byte) c;

		if (!Character.isDigit(c)) {

			b = (byte) Character.toLowerCase(c);
			b -= 'a' - 10;
		}

		b -= '0';
		b &= 0xF;

		return b;
	}

	/**
	 * Teardown containment of an EMF object.
	 */
	public static void teardownContainment(MSLEditingDomain domain,
			EObject eObject, int options) {

		List actions = new ArrayList();

		Iterator i = eObject.eClass().getEAllContainments().iterator();

		while (i.hasNext()) {

			EReference reference = (EReference) i.next();

			if ((reference.isChangeable()) && (eObject.eIsSet(reference))) {
				actions
					.add(new TeardownAction(domain, eObject, reference, null));
			}
		}

		Iterator j = actions.iterator();

		while (j.hasNext())
			((TeardownAction) j.next()).execute();
	}

	/**
	 * Teardown references to and from an EMF object.
	 */
	public static void teardownReferences(final MSLEditingDomain domain,
			final EObject eObject, final EObject container, final int options) {

		final List actions = new ArrayList();

		Iterator i = eObject.eClass().getEAllReferences().iterator();

		while (i.hasNext()) {

			EReference reference = (EReference) i.next();

			if ((reference.isChangeable()) && (!reference.isContainer())
				&& (!reference.isContainment()) && (eObject.eIsSet(reference))) {

				actions
					.add(new TeardownAction(domain, eObject, reference, null));
			}
		}

		MSLReferenceVisitor visitor = new MSLReferenceVisitor(domain, eObject) {

			protected void visitedReferencer(EReference reference,
					EObject referencer) {

				actions.add(new TeardownAction(domain, referencer, reference,
					eObject));
			}
		};

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
		public TeardownAction(MSLEditingDomain domain, EObject container,
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
									EObjectUtil.destroy((EObject) i.next());
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
								EObjectUtil.destroy(object);
						} else
							container.eSet(reference, null);
					}
				}

			} else {

				if (reference.isContainment())
					EObjectUtil.destroy(object);

				else {

					if (reference.isMany())
						((Collection) container.eGet(reference)).remove(object);
					else
						container.eSet(reference, null);
				}
			}
		}
	}

	/**
	 * Fix references.
	 */
	public static void fixReferences(EObject eObject, EClass newClass,
			EObject newObject, EReference reference, EClass type,
			EObject referencer) {

		if (reference.isMany()) {

			List list = (List) referencer.eGet(reference);

			int position = list.indexOf(eObject);

			((Collection) referencer.eGet(reference)).remove(eObject);

			if ((reference.getEOpposite() == null)
				&& ((newClass == type) || (type.isSuperTypeOf(newClass))))
				((List) referencer.eGet(reference)).add(position, newObject);

		} else if ((newClass == type) || (type.isSuperTypeOf(newClass)))
			referencer.eSet(reference, newObject);

		else
			referencer.eSet(reference, null);
	}

	/**
	 * Replace references.
	 */
	public static void replaceReferences(EObject referenced,
			EObject replacement, List ignoredObjects, EReference reference,
			EObject referencer) {

		// Don't fix references in elements that are in or
		// contained by elements in the ignoreElements list.
		boolean ignore = false;

		for (Iterator k = ignoredObjects.iterator(); k.hasNext();) {

			EObject ignored = (EObject) k.next();

			if (EObjectUtil.contains(ignored, referencer)) {
				ignore = true;
				break;
			}
		}

		if (!ignore) {

			if (reference.isMany()) {

				EList list = (EList) referencer.eGet(reference);
				int existingIndex = list.indexOf(replacement);

				if (existingIndex < 0) {
					// Replacement not already in the list. Do nothing if the
					// replacement is already in the list,
					// because this means it was already added,
					// e.g., adding to a subset will add to the superset
					// feature.
					int index = list.indexOf(referenced);
					list.add(index, replacement);
				}

			} else
				referencer.eSet(reference, replacement);
		}
	}

	/**
	 * Safely add object to an EList.
	 */
	public static void addObject(EList ownerList, Object object, int position) {

		int oldPosition = ownerList.indexOf(object);

		if (oldPosition != -1) {

			if ((position != -1) && (position < ownerList.size()))
				ownerList.move(position, oldPosition);

		} else if (position == -1)
			ownerList.add(object);

		else if (position <= ownerList.size())
			ownerList.add(position, object);

		else
			ownerList.add(object);
	}

	/**
	 * Safely add a collection of objects to an EList.
	 */
	public static void addObjects(EList ownerList, Collection objects,
			int position) {

		if (position == -1)
			ownerList.addAll(objects);

		else if (position <= ownerList.size())
			ownerList.addAll(position, objects);

		else
			ownerList.addAll(objects);
	}

	/**
	 * Safely add a collection of objects to an EList. Caller need to pass a
	 * List of objects to ensure order preservation.
	 */
	public static void addObjects(EList ownerList, Collection objects,
			int[] positions) {

		int index = 0;

		for (Iterator i = objects.iterator(); i.hasNext();) {

			int position = -1;

			if (index < positions.length)
				position = positions[index];

			addObject(ownerList, i.next(), position);

			index++;
		}
	}
}