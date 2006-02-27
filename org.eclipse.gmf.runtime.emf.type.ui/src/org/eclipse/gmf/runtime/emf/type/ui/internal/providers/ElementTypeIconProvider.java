/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.ui.internal.providers;

import java.net.URL;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.service.AbstractProvider;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.GetIconOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.IIconOperation;
import org.eclipse.gmf.runtime.common.ui.services.icon.IIconProvider;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * Default icon provider for {@link org.eclipse.gmf.runtime.emf.type.core.IElementType}s and
 * for model elements that match an {@link org.eclipse.gmf.runtime.emf.type.core.IElementType}
 * registered in the {@link org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry}.
 * 
 * @author ldamus
 */
public class ElementTypeIconProvider
	extends AbstractProvider
	implements IIconProvider {

	/**
	 * My image registry keyed on element type ID. The first time a request is
	 * made for an icon, the image descriptor for the icon is constructed using
	 * the element type's iconURL, and added to the registry. Subsequent
	 * retrieval of the icon will be done by direct access to the image
	 * registry.
	 */
	private ImageRegistry imageRegistry;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.services.internal.icon.IIconProvider#getIcon(org.eclipse.core.runtime.IAdaptable,
	 *      int)
	 */
	public Image getIcon(IAdaptable hint, int flags) {

		IElementType type = getType(hint);

		if (type != null) {
			Image image = getImageRegistry().get(type.getId());

			if (image == null) {
				URL url = type.getIconURL();
				if (url != null) {
					ImageDescriptor descriptor = ImageDescriptor
						.createFromURL(url);
					getImageRegistry().put(type.getId(), descriptor);
					image = getImageRegistry().get(type.getId());
				}
			}
			return image;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
	 */
	public boolean provides(IOperation operation) {

		if (operation instanceof GetIconOperation) {
			IIconOperation getIconOperation = (IIconOperation) operation;
			IAdaptable hint = getIconOperation.getHint();
			
			return getType(hint) != null;
		}

		return false;
	}

	/**
	 * Gets my image registry.
	 * 
	 * @return my image registry
	 */
	private ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
		}
		return imageRegistry;
	}

	/**
	 * Tries to get an element type from <code>hint</code>, first by adapting
	 * to <code>IElementType</code>, the by adapting to <code>EObject</code>
	 * and finding an element type in the {@link ElementTypeRegistry}for that
	 * object. Arbitrarily picks the most specific matching element type in this
	 * last case.
	 * 
	 * @param hint
	 *            the icon provider hint
	 * @return the element type, or <code>null</code> if none can be found
	 */
	private IElementType getType(IAdaptable hint) {
		IElementType type = (IElementType) hint.getAdapter(IElementType.class);

		if (type == null) {
			EObject eObject = (EObject) hint.getAdapter(EObject.class);

			if (eObject != null) {
				IElementType[] types = ElementTypeRegistry.getInstance()
					.getAllTypesMatching(eObject);

				if (types.length > 0) {
					// Pick the first match
					type = types[0];
				}
			}
		} else if (ElementTypeRegistry.getInstance().getType(type.getId()) == null) {
			// Check that the type is in the registry
			// TODO remove this check when UMLIconProvider is removed (when all element types are in the registry)
			type = null;
		}
		return type;
	}
}