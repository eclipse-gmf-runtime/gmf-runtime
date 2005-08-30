/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

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