/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.internal.resources;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLConstants;

/**
 * Custom implementation of an XMIResource.
 * 
 * @author rafikj
 */
public class MSLResource
	extends LogicalResource {

	private boolean useIDAttributes = false;

	/**
	 * Constructor.
	 */
	public MSLResource(URI uri) {

		super(MEditingDomain.INSTANCE.convertURI(uri));
	}

	protected LogicalResourceUnit createUnit(URI unitUri) {
		return new MSLResourceUnit(unitUri, this);
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

	/**
	 * @see org.eclipse.emf.ecore.resource.Resource#getEObject(java.lang.String)
	 */
	public EObject getEObject(String uriFragment) {

		int index = uriFragment.indexOf(MSLConstants.FRAGMENT_SEPARATOR);

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

		MEditingDomain domain = MEditingDomain.getEditingDomain(this);

		if (domain == null)
			domain = MEditingDomain.INSTANCE;

		setRawURI(domain.convertURI(uri));
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
}