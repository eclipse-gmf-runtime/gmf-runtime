/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/


package org.eclipse.gmf.runtime.common.ui.services.properties;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;


/**
 * Use to get the IPropertySource adapter for an object so that its properties
 * can be build by the properties service.
 * 
 * @author Tauseef A. Israr
 * @canBeSeenBy %partners 
 */
public class PropertiesServiceAdapterFactory
	implements IAdapterFactory, IPropertySourceProvider {

	/**
	 * PropertiesServiceAdapterFactory constructor
	 */
	public PropertiesServiceAdapterFactory() {

		super();
	}

	/**
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(Object, Class)
	 */
	public Object getAdapter(Object adaptableObject, Class adapterType) {

		assert null != adaptableObject;
		assert null != adapterType;
		if (adapterType.equals(IPropertySource.class))
			return getPropertySource(adaptableObject);
		return null;
	}

	/**
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class[] getAdapterList() {

		return new Class[] {IPropertySource.class};
	}

	/**
	 * @see org.eclipse.ui.views.properties.IPropertySourceProvider#getPropertySource(java.lang.Object)
	 */
	public IPropertySource getPropertySource(Object object) {

		if (object instanceof IPropertySource)
			return (IPropertySource) object;

		IPropertiesProvider service = PropertiesService.getInstance();
		assert null != service;
		return service.getPropertySource(object);
	}
}