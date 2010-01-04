/******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.internal.impl.DefaultMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.internal.impl.EClassUtil;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Represents an element type that maps directly to a metamodel element type (
 * <code>EClass</code>). There is only one metamodel type registered with the
 * {@link org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry}for a given EClass.
 * <P>
 * Provides an edit helper for editing model elements with the same
 * <code>EClass</code>.
 * 
 * @author ldamus
 */
public class MetamodelType
	extends ElementType
	implements IMetamodelType {

	/**
	 * The edit helper.
	 */
	private IEditHelper editHelper;

	/**
	 * Constructs a new metamodel type.
	 * 
	 * @param id
	 *            the id
	 * @param iconURL
	 *            the URL for the icon
	 * @param displayName
	 *            the display name
	 * @param eClass
	 *            the metaclass
	 * @param editHelper
	 *            the edit helper
	 */
	public MetamodelType(String id, URL iconURL, String displayName,
			EClass eClass, IEditHelper editHelper) {

		super(id, iconURL, displayName, eClass);
		this.editHelper = editHelper;
	}
	

	/**
	 * Constructs a new metamodel type using values from the type descriptor.
	 * 
	 * @param descriptor
	 *            the metamodel type descriptor.
	 */
	public MetamodelType(IMetamodelTypeDescriptor descriptor) {

		this(descriptor.getId(), descriptor.getIconURL(), descriptor.getName(),
				descriptor.getEClass(), descriptor.getEditHelper());

	}

	/**
	 * Gets the edit helper. May activate the plugin in which the edit helper is
	 * defined.
	 * 
	 * @return the edit helper.
	 */
	public IEditHelper getEditHelper() {
		
		if (editHelper == null) {
			IElementType[] superTypes = getAllSuperTypes();
			
			if (superTypes.length > 0) {
				editHelper = superTypes[superTypes.length - 1].getEditHelper();
				
			} else {
				editHelper = DefaultMetamodelType.getInstance().getEditHelper();
			}
		}
		return editHelper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementType#getCommand(org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest)
	 */
	public ICommand getEditCommand(IEditCommandRequest request) {
		return getEditHelper().getEditCommand(request);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementType#getAllSuperTypes()
	 */
	public IElementType[] getAllSuperTypes() {

		if (super.getAllSuperTypes() == null) {
			
			IClientContext context = ClientContextManager.getInstance()
					.getBinding(this);
			
			LinkedHashSet result = new LinkedHashSet();
			if (getEClass() != null) {
				// Bugzilla 298661: assume all models implicitly extend EObject
				List supertypes = EClassUtil.getEAllSuperTypes(getEClass());
				
				for (int i = 0; i < supertypes.size(); i++) {
					EClass nextEClass = (EClass) supertypes.get(i);
					IElementType nextElementType = ElementTypeRegistry
						.getInstance().getElementType(nextEClass, context);
	
					if ((nextElementType != null)
							&& (nextElementType != DefaultMetamodelType.getInstance())) {
						
						result.add(nextElementType);
					}
				}
			}
			setAllSupertypes((IElementType[]) result
				.toArray(new IElementType[] {}));
		}
		return super.getAllSuperTypes();
	}
	
	public String toString() {
		return "MetamodelType[" + getId()+ "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}