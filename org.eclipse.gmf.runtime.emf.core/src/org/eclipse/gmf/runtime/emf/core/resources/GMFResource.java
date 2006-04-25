/******************************************************************************
 * Copyright (c) 2004-2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.resources;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMISaveImpl;
import org.eclipse.gmf.runtime.emf.core.internal.util.EMFCoreConstants;
import org.eclipse.gmf.runtime.emf.core.internal.util.Util;

/**
 * Custom implementation of an XMIResource.
 * 
 * @author rafikj
 */
public class GMFResource
	extends XMIResourceImpl {

	/**
	 * Use this option to abort loading a resource immediately when an error occurs.
	 * The default is <code>Boolean.FALSE</code> unless set to <code>Boolean.TRUE</code> explicitly.
	 */
	public static final String OPTION_ABORT_ON_ERROR = "ABORT_ON_ERROR"; //$NON-NLS-1$

	private boolean useIDAttributes = false;
	
	/**
	 * Constructor.
	 */
	public GMFResource(URI uri) {

		super(uri);
		
		setTrackingModification(true);
	}

	protected boolean useUUIDs() {
		return true;
	}
	
	/**
	 * Should we use ID attribute?
	 */
	public void setUseIDAttributes(boolean b) {
		useIDAttributes = b;
	}

	/**
	 * Should we use ID attribute?
	 */
	protected boolean useIDAttributes() {
		return useIDAttributes;
	}

	protected XMLHelper createXMLHelper() {
		return new GMFHelper(this);
	}

	protected XMLLoad createXMLLoad() {
		return new GMFLoad(createXMLHelper());
	}

	protected XMLSave createXMLSave() {
		return new XMISaveImpl(createXMLHelper());
	}

	/**
	 * @see org.eclipse.emf.ecore.resource.Resource#getEObject(java.lang.String)
	 */
	public EObject getEObject(String uriFragment) {

		int index = uriFragment.indexOf(EMFCoreConstants.FRAGMENT_SEPARATOR);

		if (index != -1)
			uriFragment = uriFragment.substring(0, index);

		return super.getEObject(uriFragment);
	}

	/**
	 * Get the saved ID of an EObject.
	 */
	public static String getSavedID(EObject eObject) {
		return (String) DETACHED_EOBJECT_TO_ID_MAP.get(eObject);
	}

	/**
	 * @see org.eclipse.emf.ecore.resource.Resource#setURI(org.eclipse.emf.common.util.URI)
	 */
	public void setURI(URI uri) {

		if (getResourceSet() != null) {
			setRawURI(Util.denormalizeURI(uri, getResourceSet()));
		}
	}
	
	public NotificationChain basicSetResourceSet(ResourceSet rset, NotificationChain notifications) {
		// when I am added to a new resource set, my optimally denormalized URI
		//     may change according to its different URI converter
		if (rset != null) {
			setURI(getURI());
		}
		
		return super.basicSetResourceSet(rset, notifications);
	}

	/**
	 * Set the URI of the resource without processing it.
	 */
	public void setRawURI(URI uri) {

		URI oldURI = getURI();

		if ((uri == oldURI) || ((uri != null) && (uri.equals(oldURI))))
			return;

		super.setURI(uri);
	}
	
	/**
	 * The inherited implementation creates an adapter that <em>always</em> sets
	 * the modified state.  We prefer to check, first, whether the resource
	 * is already modified so that we don't generate redundant notifications.
	 * Moreover, we additionally set modified state only for changes that are
	 * in non-transient features of objects contained (recursively) by
	 * non-transient references.
	 */
	protected Adapter createModificationTrackingAdapter() {
		return new ModificationTrackingAdapter() {
			public void notifyChanged(Notification notification) {
				if (!isModified() && !isTransient(
						notification.getNotifier(), notification.getFeature())) {
					
					super.notifyChanged(notification);
				}
			}

			/**
			 * Check if the feature or one of the notifier's containers is
			 * transient.
			 * 
			 * @param notifier a notifier
			 * @param feature the feature that changed
			 * 
			 * @return <code>true</code> if the feature is transient or if the
			 *    notifier or any of its ancestors is contained by a transient
			 *    reference; <code>false</code>, otherwise
			 */
			private boolean isTransient(Object notifier, Object feature) {
				if (feature instanceof EStructuralFeature) {
					if (((EStructuralFeature) feature).isTransient())
						return true;
					else
						// calling isTransient could be a lengthy operation.
						//   It is safe to cast because the adapter is only
						//   attached to EObjects, not to the resource
						return isTransient((EObject) notifier);
				}
				return false;
			}
			
			/**
			 * Is object transient?
			 */
			private boolean isTransient(EObject eObject) {
				EStructuralFeature containmentFeature = eObject.eContainmentFeature();
				while (containmentFeature != null) {
					if (containmentFeature.isTransient())
						return true;
					eObject = eObject.eContainer();
					if (eObject != null)
						containmentFeature =  eObject.eContainmentFeature();
					else
						break;
				}
				return false;
			}};
	}
}
