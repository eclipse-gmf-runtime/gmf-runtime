/******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and others.
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
import org.eclipse.gmf.runtime.emf.core.internal.util.EMFCoreConstants;

/**
 * @author Yasser Lulu
 */
public class CopyingResource
	extends XMIResourceImpl {

	private XMLResource xmlResource;
	
	private CopyingResourceSet copyingResourceSet;

	public CopyingResource(XMLResource resource, URI uri,
			CopyingResourceSet copyingResourceSet) {
		this(resource, uri, copyingResourceSet, true);
	}

	public CopyingResource(XMLResource resource, URI uri,
			CopyingResourceSet copyingResourceSet, boolean regenerateIds) {
		super(uri);
		this.xmlResource = resource;
		this.copyingResourceSet = copyingResourceSet;
		setEncoding(resource.getEncoding());
		//needed to allow calls to unload() to proceed
		setLoaded(true);
		getDefaultSaveOptions().putAll(resource.getDefaultSaveOptions());
		copyingResourceSet.getResources().add(this);
		copyingResourceSet.getResourcesMap().put(resource, this);
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
		Iterator it = getXMLResource().getAllContents();
		while (it.hasNext()) {
			setID((EObject) it.next(), EcoreUtil.generateUUID());
		}
	}

	protected XMLLoad createXMLLoad() {
		throwUnsupportedOperationException("createXMLLoad", //$NON-NLS-1$
			new UnsupportedOperationException(
				"Can't call load on CopyingResource resource"));//$NON-NLS-1$
		return null;
	}

	protected XMLHelper createXMLHelper() {
		return new CopyingHelper(this);
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
				"Can't call load on CopyingResource resource"));//$NON-NLS-1$
	}

	protected XMLSave createXMLSave() {
		return new CopyingSave(createXMLHelper());
	}

	private boolean isInResource(EObject eObject) {
		// in case of cross-resource containment, the 'eObject' may be in a
		//     different resource than xmlResource, though one of its containers
		//     may be
		while (eObject != null) {
			if (((InternalEObject) eObject).eDirectResource() == getXMLResource()) {
				return true;
			}
			
			eObject = eObject.eContainer();
		}
		
		return false;
	}

	public EList getContents() {
		return getXMLResource().getContents();
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
		EObject eObj = getXMLResource().getEObject(id);
		if (eObj == null) {
			return super.getEObjectByID(id);
		}
		return eObj;
	}

	/**
	 * @return Returns the CopyingResourceSet.
	 */
	public CopyingResourceSet getMslCopyingResourceSet() {
		return copyingResourceSet;
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
		return getXMLResource().getEObjectToExtensionMap();
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
		
		for (Iterator iter = getXMLResource().getAllContents(); iter.hasNext(); ) {
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
	
	/**
	 * Gets the XML resource that contains the model content to be copied.
	 * 
	 * @return the XML resource
	 */
	protected XMLResource getXMLResource() {
		return xmlResource;
	}
	
	/**
	 * Helper implementation for the CopyingResource.
	 */
	protected class CopyingHelper extends XMIHelperImpl {
		
		public CopyingHelper() {
			super();
		}
		  
		public CopyingHelper(XMLResource resource) {
		    super(resource);
		}

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
				eObj = EcoreUtil.resolve(obj, getXMLResource());
				if (eObj == obj) {
					// use super.getHREF() if we can't resolve the proxy
					eObj = null;
				}
			}
			
			if (eObj != null) {
				Resource resource = eObj.eResource();
				if (resource != null) {
					URI objectURI = getHREF(resource, eObj);
					objectURI = deresolve(objectURI);
					return objectURI.toString();
				}
			}
			
			return super.getHREF(obj);
		}

		protected URI getHREF(Resource otherResource, EObject obj) {
			if (!(otherResource instanceof CopyingResource)) {
				CopyingResource copyingResource = (CopyingResource) getResourcesMap()
					.get(otherResource);
				if (copyingResource != null) {
					otherResource = copyingResource;
				}
			}
			
			return super.getHREF(otherResource, obj);
		}
	};
	
	/**
	 * Save implementation for the CopyingResource.
	 */
	public class CopyingSave extends XMISaveImpl {
		
		public CopyingSave(XMLHelper helper) {
			super(helper);
		}
		
		public CopyingSave(Map options, XMLHelper helper, String encoding) {
			super(options, helper, encoding);
		}

		public CopyingSave(Map options, XMLHelper helper, String encoding,
				String xmlVersion) {
			super(options, helper, encoding, xmlVersion);
		}

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