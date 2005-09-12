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

package org.eclipse.gmf.runtime.emf.core.internal.domain;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLComposedAdapterFactory;

/**
 * This class extends the EMF AdapterFactoryEditingDomain class to provide its
 * own implementation of an MSL resource set. MSL resource sets convert resource
 * URIs when searching for resources.
 * 
 * @author rafikj
 */
public class MSLAdapterFactoryEditingDomain
	extends AdapterFactoryEditingDomain {

	/**
	 * Constructor.
	 */
	public MSLAdapterFactoryEditingDomain(
			MSLComposedAdapterFactory composedFactory) {

		super(composedFactory, new BasicCommandStack());

		this.resourceSet = new MSLAdapterFactoryEditingDomainResourceSet();
	}

	protected class MSLAdapterFactoryEditingDomainResourceSet
		extends AdapterFactoryEditingDomainResourceSet {

		/**
		 * Constructor.
		 */
		public MSLAdapterFactoryEditingDomainResourceSet() {
			super();
		}

		/**
		 * @see org.eclipse.emf.ecore.resource.ResourceSet#getResource(org.eclipse.emf.common.util.URI,
		 *      boolean)
		 */
		public Resource getResource(URI uri, boolean loadOnDemand) {

			MEditingDomain domain = MEditingDomain.getEditingDomain(this);

			// no editing domain.
			if (domain == null)
				return super.getResource(uri, loadOnDemand);

			// convert URI to use platform scheme.
			URI convertedURI = domain.convertURI(uri);

			// no conversion happened.
			if (convertedURI.equals(uri))
				return super.getResource(uri, loadOnDemand);

			// try to get resource without loading using file URI.
			Resource resource = super.getResource(uri, false);

			// not found, find resource using platform URI.
			if (resource == null)
				resource = super.getResource(convertedURI, loadOnDemand);

			// need to load resource.
			else if (loadOnDemand)
				resource = super.getResource(uri, true);

			return resource;
		}
		
		public EList getResources() {
			if (resources == null) {
				// make sure that any resource that is removed from me is also
				//    removed from my package registry, in case it contained
				//    the EPackage for some namespace in one of my resources.
				//    Take this opportunity also to clean out proxies
				resources = new ResourcesEList() {
					protected void didRemove(int index, Object oldObject) {
						EPackage.Registry registry = getPackageRegistry();
						
						if (registry != EPackage.Registry.INSTANCE) {
							for (Iterator iter = getPackageRegistry().entrySet().iterator(); iter.hasNext();) {
								Map.Entry next = (Map.Entry) iter.next();
								EPackage ePackage = (EPackage) next.getValue();
								
								if (ePackage.eIsProxy()
										|| (ePackage.eResource() == oldObject)) {
									iter.remove();
									
									// must continue in case multiple packages are
									//    in the same resource
								}
							}
						}
					}
					
					protected void didClear(int oldSize, Object[] oldObjects) {
						EPackage.Registry registry = getPackageRegistry();
						
						if (registry != EPackage.Registry.INSTANCE) {
							// just clear the registry, since no resources
							//    remain to use it anyway
							registry.clear();
						}
					}
				};
			}
			
			return resources;
		}
	}
}