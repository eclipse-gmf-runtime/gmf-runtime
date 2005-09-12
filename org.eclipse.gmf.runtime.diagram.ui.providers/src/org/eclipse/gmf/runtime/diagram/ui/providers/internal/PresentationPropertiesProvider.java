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


package org.eclipse.gmf.runtime.diagram.ui.providers.internal;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.gef.EditPart;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.properties.GetPropertySourceOperation;
import org.eclipse.gmf.runtime.common.ui.services.properties.ICompositePropertySource;
import org.eclipse.gmf.runtime.common.ui.services.properties.IPropertiesProvider;
import org.eclipse.gmf.runtime.common.ui.services.properties.PropertiesService;
import org.eclipse.gmf.runtime.diagram.ui.properties.descriptors.NotationPropertySource;
import org.eclipse.gmf.runtime.diagram.ui.providers.internal.l10n.DiagramProvidersResourceManager;
import org.eclipse.gmf.runtime.emf.ui.properties.providers.GenericEMFPropertiesProvider;
import org.eclipse.gmf.runtime.notation.Style;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @author Tauseef A. Israr Created: Nov 15, 2002
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.providers.*
 * 
 * This is a properties provider class based on Properties Service Provider
 * infrastructure.
 */
public class PresentationPropertiesProvider
	extends GenericEMFPropertiesProvider
	implements IPropertiesProvider {

	public static String VIEW_CATEGORY = DiagramProvidersResourceManager
		.getI18NString("PresentationPropertiesProvider.viewCategory"); //$NON-NLS-1$

	/**
	 * @see com.ibm.xtools.common.service.IProvider#provides(IOperation)
	 */
	public boolean provides(IOperation operation) {

		if (operation == null)
			return false;

		if (operation instanceof GetPropertySourceOperation) {
			Object object = ((GetPropertySourceOperation) operation)
				.getObject();

			if (object == null)
				return false;

			if (object instanceof View)
				return true;
			
			if(object instanceof Style)
				return true;

			return object instanceof EditPart && ((EditPart) object).getModel() instanceof View;

		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.internal.properties.XToolsAbstractPropertiesProvider#getPropertySource(org.eclipse.ui.views.properties.IPropertySource,
	 *      java.lang.Object)
	 */
	public ICompositePropertySource getPropertySource(Object object) {

		if (object instanceof View || object instanceof Style )
			return super.getPropertySource(object);
		
		if (object instanceof EditPart) {
			Object model = ((EditPart) object).getModel();
			if (model instanceof View) {
				View view = (View) model;
				return super.getPropertySource(view);
			}
		}

		return null;
	}

	/*
	 * @param eObject @return
	 */
	protected ICompositePropertySource getElementPropertySource(EObject eObject) {

		IPropertySource elementPropertySource = null;

		if (eObject instanceof IAdaptable)
			elementPropertySource = (IPropertySource) Platform
				.getAdapterManager().getAdapter(eObject, IPropertySource.class);

		if (elementPropertySource == null)
			elementPropertySource = PropertiesService.getInstance()
				.getPropertySource(eObject);

		return (elementPropertySource instanceof ICompositePropertySource) ? (ICompositePropertySource) elementPropertySource
			: null;

	}

	/*
	 * (non-Javadoc) Instantiates and returns property source instance
	 * appropriate for this provider
	 *  
	 */
	protected ICompositePropertySource createPropertySource(Object object,
			IItemPropertySource itemPropertySource) {

		return new NotationPropertySource(object, itemPropertySource,
			VIEW_CATEGORY);
	}

}