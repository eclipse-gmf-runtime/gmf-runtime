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

package org.eclipse.gmf.runtime.emf.core.resources;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.InternalTransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gmf.runtime.emf.core.internal.util.Util;


/**
 * Default implementation of a resource helper, useful for subclassing.
 * Implements the default algorithms for the interface methods.
 *
 * @author Christian W. Damus (cdamus)
 */
public class ResourceHelperImpl
	extends AdapterImpl
	implements IResourceHelper {

	/**
	 * Obtains the resource that I help.
	 * 
	 * @return my resource
	 */
	protected Resource getResource() {
		return (Resource) getTarget();
	}
	
	public boolean isAdapterForType(Object type) {
		return type == IResourceHelper.class;
	}
	
	/**
	 * Default algorithm just uses the associated EFactory to instantiate the
	 * EClass.  If the resource is under the control of a transactional editing
	 * domain, then it will be managed already by that domain before it is
	 * attached to the resource.
	 */
	public EObject create(EClass eClass) {

		EObject eObject = eClass.getEPackage().getEFactoryInstance().create(
			eClass);

		TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(getResource());
		
		if (domain != null) {
			// this object is to be managed by this editing domain
			eObject.eAdapters().add(
				((InternalTransactionalEditingDomain) domain).getChangeRecorder());
		}

		return eObject;
	}
	
	/**
	 * Default algorithm just detaches the object and, recursively, its contents
	 * and nullifies all references to them from other objects in the resource
	 * set.
	 */
	public void destroy(EObject eObject) {
		Util.destroy(eObject);
	}
}
