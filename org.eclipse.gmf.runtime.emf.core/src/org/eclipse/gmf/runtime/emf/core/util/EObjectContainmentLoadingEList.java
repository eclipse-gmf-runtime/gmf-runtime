/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.emf.core.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;

import org.eclipse.gmf.runtime.emf.core.internal.resources.LogicalResourceUtil;


/**
 * Specialized containment reference list that automatically loads sub-unit
 * objects when they are retrieved from the list.
 *
 * @author Christian W. Damus (cdamus)
 */
public class EObjectContainmentLoadingEList
	extends EObjectContainmentEList {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Initializes me with my contained type, owning object, and feature ID.
	 * 
	 * @param dataClass my contained type
	 * @param owner the object that owns me
	 * @param featureID the ID of the feature that I implement
	 */
	public EObjectContainmentLoadingEList(Class dataClass,
			InternalEObject owner, int featureID) {
		super(dataClass, owner, featureID);
	}

	protected Object resolve(int index, Object object) {
		EObject eObject = (EObject) object;
		Resource res = getEObject().eResource();
		
		if (res != null) {
			LogicalResourceUtil.autoload(res, eObject);
		}
		
		return eObject;
	}
}
