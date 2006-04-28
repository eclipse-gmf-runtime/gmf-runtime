/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.clipboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.XMIHelperImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMISaveImpl;
import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.EMFCoreDebugOptions;
import org.eclipse.gmf.runtime.emf.core.internal.plugin.EMFCorePlugin;
import org.eclipse.gmf.runtime.emf.core.resources.GMFResource;
import org.eclipse.gmf.runtime.emf.core.internal.util.EMFCoreConstants;
import org.eclipse.gmf.runtime.emf.core.internal.util.Util;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;

/**
 * @author Yasser Lulu
 */
public class CopyingResource
	extends XMIResourceImpl {

	private XMLResource xmlResource;

	private CopyingResourceSet mslCopyingResourceSet;

	public CopyingResource(XMLResource mslResource, URI uri,
			CopyingResourceSet mslCopyingResourceSet) {
		this(mslResource, uri, mslCopyingResourceSet, true);
	}

	public CopyingResource(XMLResource mslResource, URI uri,
			CopyingResourceSet mslCopyingResourceSet, boolean regenerateIds) {
		super(uri);
		this.xmlResource = mslResource;
		this.mslCopyingResourceSet = mslCopyingResourceSet;
		setEncoding(mslResource.getEncoding());
		//needed to allow calls to unload() to proceed
		setLoaded(true);
		getDefaultSaveOptions().putAll(mslResource.getDefaultSaveOptions());
		mslCopyingResourceSet.getResources().add(this);
		mslCopyingResourceSet.getResourcesMap().put(mslResource, this);
		if (regenerateIds) {
			createNewIDs();
		} else {
			copyIDs();
		}
	}

	/**
	 *  
	 */
	private void createNewIDs() {
		// OK to get all contents because we have to copy
		//    the entire model content of this resource
		Iterator it = xmlResource.getAllContents();
		while (it.hasNext()) {
			setID((EObject) it.next(), EcoreUtil.generateUUID());
		}
	}

	protected XMLLoad createXMLLoad() {
		throwUnsupportedOperationException("createXMLLoad", //$NON-NLS-1$
			new UnsupportedOperationException(
				"Can't call load on MSLCopyingResource resource"));//$NON-NLS-1$
		return null;
	}

	protected XMLHelper createXMLHelper() {
		return new XMIHelperImpl(this) {

			/**
			 * @see org.eclipse.emf.ecore.xmi.XMLHelper#deresolve(org.eclipse.emf.common.util.URI)
			 */
			public URI deresolve(URI anUri) {

				// if this both target and container are within a platform resource and
				// projects
				// or plugins are different then do not deresolve.
				if (((EMFCoreConstants.PLATFORM_SCHEME.equals(anUri.scheme())) && (EMFCoreConstants.PLATFORM_SCHEME
					.equals(resourceURI.scheme())))
					&& ((anUri.segmentCount() > 2) && (resourceURI.segmentCount() > 2))
					&& ((!anUri.segments()[0].equals(resourceURI.segments()[0])) || (!anUri
						.segments()[1].equals(resourceURI.segments()[1]))))
					return anUri;

				return super.deresolve(anUri);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.emf.ecore.xmi.impl.XMLHelperImpl#getHREF(org.eclipse.emf.ecore.EObject)
			 */
			public String getHREF(EObject obj) {
				EObject eObj = obj;
				
				if (obj.eIsProxy()) {
					eObj = EcoreUtil.resolve(obj, xmlResource);
					if (eObj == null) {
						eObj = null;
					}
				}
				
				if ((eObj != null) && (eObj.eResource() != null)) {
					URI objectURI = getHREF(eObj.eResource(), eObj);
					objectURI = deresolve(objectURI);
					return objectURI.toString();
				}
				return super.getHREF(obj);
			}

			protected URI getHREF(Resource otherResource, EObject obj) {
				if ((otherResource instanceof CopyingResource) == false) {
					CopyingResource copyingResource = (CopyingResource) getResourcesMap()
						.get(otherResource);
					if (copyingResource != null) {
						otherResource = copyingResource;
					}
				}
				if (otherResource instanceof GMFResource) {
					String qName = EMFCoreUtil.getQualifiedName(obj, true);
					if (qName.length() > 0) {
						StringBuffer buffer = new StringBuffer(otherResource
							.getURIFragment(obj));
						buffer.append(EMFCoreConstants.FRAGMENT_SEPARATOR);
						buffer.append(Util.encodeQualifiedName(qName));
						return otherResource.getURI().appendFragment(
							buffer.toString());
					}
				}
				
				return super.getHREF(otherResource, obj);
			}
		};
	}

	protected void unloaded(InternalEObject internalEObject) {
		//disable parent
	}

	/**
	 * @see org.eclipse.emf.common.notify.impl.NotifierImpl#eNotificationRequired()
	 */
	public boolean eNotificationRequired() {
		return false;
	}

	private void throwUnsupportedOperationException(String methodName,
			UnsupportedOperationException ex) {
		Trace.throwing(EMFCorePlugin.getDefault(),
			EMFCoreDebugOptions.EXCEPTIONS_THROWING, getClass(), methodName, ex);
		throw ex;
	}

	/**
	 * @return Returns the resourcesMap.
	 */
	private Map getResourcesMap() {
		return getMslCopyingResourceSet().getResourcesMap();
	}

	public void doLoad(InputStream inputStream, Map options)
		throws IOException {
		throwUnsupportedOperationException("doLoad", //$NON-NLS-1$
			new UnsupportedOperationException(
				"Can't call load on MSLCopyingResource resource"));//$NON-NLS-1$
	}

	protected XMLSave createXMLSave() {
		return new XMISaveImpl(createXMLHelper()) {

			/**
			 * @see org.eclipse.emf.ecore.xmi.impl.XMLSaveImpl#sameDocMany(org.eclipse.emf.ecore.EObject,
			 *      org.eclipse.emf.ecore.EStructuralFeature)
			 */
			protected int sameDocMany(EObject o, EStructuralFeature f) {
				InternalEList values = (InternalEList) helper.getValue(o, f);
				if (values.isEmpty()) {
					return SKIP;
				}

				for (Iterator i = values.basicIterator(); i.hasNext();) {
					EObject value = (EObject) i.next();
					if (value.eIsProxy() || (isInResource(value) == false)) {
						return CROSS_DOC;
					}
				}

				return SAME_DOC;
			}

			/**
			 * @see org.eclipse.emf.ecore.xmi.impl.XMLSaveImpl#sameDocSingle(org.eclipse.emf.ecore.EObject,
			 *      org.eclipse.emf.ecore.EStructuralFeature)
			 */
			protected int sameDocSingle(EObject o, EStructuralFeature f) {
				EObject value = (EObject) helper.getValue(o, f);
				if (value == null) {
					return SKIP;
				} else if (value.eIsProxy()) {
					return CROSS_DOC;
				} else {
					return (isInResource(value)) ? SAME_DOC
						: CROSS_DOC;
				}
			}

		};
	}

	private boolean isInResource(EObject eObject) {
		// in case of cross-resource containment, the 'eObject' may be in a
		//     different resource than xmlResource, though one of its containers
		//     may be
		while (eObject != null) {
			if (((InternalEObject) eObject).eDirectResource() == xmlResource) {
				return true;
			}
			
			eObject = eObject.eContainer();
		}
		
		return false;
	}

	public EList getContents() {
		return xmlResource.getContents();
	}

	/**
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#doUnload()
	 */
	protected void doUnload() {
		//disable parent's
	}

	public EObject getEObject(String uriFragment) {
		int index = uriFragment.indexOf('?');
		if (-1 != index) {
			uriFragment = uriFragment.substring(0, index);
		}
		return super.getEObject(uriFragment);
	}

	/**
	 * Returns the object based on the fragment as an ID.
	 */
	protected EObject getEObjectByID(String id) {
		EObject eObj = xmlResource.getEObject(id);
		if (eObj == null) {
			return super.getEObjectByID(id);
		}
		return eObj;
	}

	/**
	 * @return Returns the mslCopyingResourceSet.
	 */
	public CopyingResourceSet getMslCopyingResourceSet() {
		return mslCopyingResourceSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.resource.impl.ResourceImpl#getResourceSet()
	 */
	public ResourceSet getResourceSet() {
		return getMslCopyingResourceSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.resource.Resource.Internal#attached(org.eclipse.emf.ecore.EObject)
	 */
	public void attached(EObject eObject) {
		//disable
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.resource.Resource.Internal#detached(org.eclipse.emf.ecore.EObject)
	 */
	public void detached(EObject eObject) {
		//disable
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.xmi.XMLResource#getEObjectToExtensionMap()
	 */
	public Map getEObjectToExtensionMap() {
		return xmlResource.getEObjectToExtensionMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.ecore.resource.Resource#isTrackingModification()
	 */
	public boolean isTrackingModification() {
		return false;
	}

	private void copyIDs() {
		// OK to get all contents because we have to copy
		//    the entire model content of this resource
		XMLResource lastRes = null;
		
		for (Iterator iter = xmlResource.getAllContents(); iter.hasNext(); ) {
			InternalEObject eObject = (InternalEObject)iter.next();
			
			if (eObject.eDirectResource() != null) {
				// ensure that we only ask the resource that actually contains
				//    an object for that object's ID
				lastRes = (XMLResource) eObject.eDirectResource();
			}
			
			getEObjectToIDMap().put(eObject, lastRes.getID(eObject));
			getIDToEObjectMap().put(lastRes.getID(eObject), eObject);
		}
	}
}