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

package org.eclipse.gmf.runtime.emf.ui.properties.providers;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.properties.GetPropertySourceOperation;
import org.eclipse.gmf.runtime.common.ui.services.properties.ICompositePropertySource;
import org.eclipse.gmf.runtime.common.ui.services.properties.IPropertiesProvider;
import org.eclipse.gmf.runtime.emf.core.internal.util.MSLAdapterFactoryManager;
import org.eclipse.gmf.runtime.emf.ui.properties.descriptors.EMFCompositePropertySource;

/**
 * Provider that provides clients with generic EMF propeties.
 * 
 * @author nbalaba
 */
public class GenericEMFPropertiesProvider
	extends AbstractProvider
	implements IPropertiesProvider {

	/**
	 * 
	 */
	public GenericEMFPropertiesProvider() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * Adapter to the EMF layer - the factory that will return IItelPropertySource adapters
	 * @return - the EMF based composite adapter factory that will return IItelPropertySource adapters
	 */
	protected static AdapterFactory getAdapterFactory() {
		return MSLAdapterFactoryManager.getAdapterFactory();
	}

	/**
	 * This implements {@link IPropertySourceProvider}.getPropertySource to
	 * forward the call to an object that implements
	 * {@link org.eclipse.emf.edit.provider.IItemPropertySource}.
	 */
	public ICompositePropertySource getPropertySource(Object object) {

		if (object instanceof ICompositePropertySource) {
			return (ICompositePropertySource) object;
		} else {
			IItemPropertySource itemPropertySource = (IItemPropertySource) (getAdapterFactory()
				.adapt(object, IItemPropertySource.class));
			return itemPropertySource != null ? createPropertySource(object,
				itemPropertySource)
				: null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {

		return operation instanceof GetPropertySourceOperation
			&& (((GetPropertySourceOperation) operation).getPropertySource() == null);
	}

	/*
	 * (non-Javadoc)
	 * Instantiates and returns property source instance appropriate for this provider
	 * 
	 */
	protected ICompositePropertySource createPropertySource(Object object,
		IItemPropertySource itemPropertySource) {

		return new EMFCompositePropertySource(object, itemPropertySource, "EMF"); //$NON-NLS-1$
	}

}
