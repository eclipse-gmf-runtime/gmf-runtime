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