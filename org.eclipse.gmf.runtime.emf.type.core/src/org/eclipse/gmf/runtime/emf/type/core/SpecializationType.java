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

package org.eclipse.gmf.runtime.emf.type.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Represents an application type that specializes other application types.
 * <P>
 * Specializations can contribute 'before' and 'after' advice to the editing
 * commands associated with the application types that they specialize.
 * 
 * @author ldamus
 */
public class SpecializationType
	extends ElementType
	implements ISpecializationType {

	/**
	 * The container descriptor.
	 */
	private final IContainerDescriptor containerDescriptor;

	/**
	 * The element matcher.
	 */
	private IElementMatcher matcher;

	/**
	 * The element types that are specialized by this type.
	 */
	private final IElementType[] specializedTypes;

	/**
	 * The identifiers of the element types that are specialized by this type.
	 */
	private String[] specializedTypeIds;

	/**
	 * The edit helper advice.
	 */
	private final IEditHelperAdvice editHelperAdvice;

	/**
	 * The metamodel type
	 */
	private IMetamodelType metamodelType;

	/**
	 * Constructs a new specialization type.
	 * 
	 * @param id
	 *            the type id
	 * @param iconURL
	 *            the URL for the icon
	 * @param displayName
	 *            the display name
	 * @param elementTypes
	 *            the element types that are specialized by this type
	 * @param matcher
	 *            the element matcher
	 * @param descriptor
	 *            the container descriptor
	 * @param editHelperAdvice
	 *            the edit helper advice
	 */
	public SpecializationType(String id, URL iconURL, String displayName,
			IElementType[] elementTypes, IElementMatcher matcher,
			IContainerDescriptor descriptor, IEditHelperAdvice editHelperAdvice) {

		super(id, iconURL, displayName);

		this.containerDescriptor = descriptor;
		this.matcher = matcher;
		this.specializedTypes = elementTypes;
		this.editHelperAdvice = editHelperAdvice;
	}
	

	/**
	 * Constructs a new instance using values from the type descriptor.
	 * 
	 * @param descriptor
	 *            the specialization type descriptor
	 */
	public SpecializationType(ISpecializationTypeDescriptor descriptor) {

		this(descriptor.getId(), descriptor.getIconURL(), descriptor.getName(),
				descriptor.getSpecializedTypes(), descriptor.getMatcher(),
				descriptor.getContainerDescriptor(), descriptor
						.getEditHelperAdvice());
	}

	/**
	 * Gets the container descriptor.
	 * 
	 * @return the container descriptor.
	 */
	public IContainerDescriptor getEContainerDescriptor() {
		return containerDescriptor;
	}

	/**
	 * Gets the element matcher.
	 * 
	 * @return the element matcher.
	 */
	public IElementMatcher getMatcher() {
		return matcher;
	}

	/**
	 * Gets the element types that this type specializes.
	 * 
	 * @return an array of types that are specialized by this type.
	 */
	public IElementType[] getSpecializedTypes() {
		return specializedTypes;
	}

	/**
	 * Gets the IDs of the specialized types.
	 * 
	 * @return the IDs of the specialized types
	 */
	public String[] getSpecializedTypeIds() {

		if (specializedTypeIds == null && specializedTypes != null) {
			specializedTypeIds = new String[specializedTypes.length];

			for (int i = 0; i < specializedTypes.length; i++) {
				specializedTypeIds[i] = specializedTypes[i].getId();
			}
		}
		return specializedTypeIds;
	}

	/**
	 * Answers whether or not I am a specialization of <code>type</code>.
	 * 
	 * @param type
	 *            the type to be tested
	 * @return <code>true</code> if I am a specialization of <code>type</code>,
	 *         <code>false</code> otherwise.
	 */
	public boolean isSpecializationOf(IElementType type) {

        String[] ids = getSpecializedTypeIds();
        
		for (int i = 0; i < ids.length; i++) {
			if (type.getId().equals(ids[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets my edit helper advice. The advice can return 'before' or 'after'
	 * editing commands for editing elements of the types that I specialize.
	 * 
	 * @return the edit helper advice.
	 */
	public IEditHelperAdvice getEditHelperAdvice() {
		return editHelperAdvice;
	}

	/**
	 * Gets the metaclass that is specialized by this type.
	 * 
	 * @return the metaclass
	 */
	public EClass getEClass() {

		return getMetamodelType().getEClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.ISpecializationType#getMetamodelType()
	 */
	public IMetamodelType getMetamodelType() {

		if (metamodelType == null) {

			for (int i = 0; i < getSpecializedTypes().length; i++) {
				IElementType nextElementType = getSpecializedTypes()[i];

				if (nextElementType instanceof IMetamodelType) {
					metamodelType = (IMetamodelType) nextElementType;
					break;

				} else if (nextElementType instanceof ISpecializationType) {
					metamodelType = ((ISpecializationType) nextElementType)
						.getMetamodelType();
					break;
				}
			}
		}
		return metamodelType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditHelper()
	 */
	public IEditHelper getEditHelper() {
		return getMetamodelType().getEditHelper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementType#getCommand(org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest)
	 */
	public ICommand getEditCommand(IEditCommandRequest request) {
		return getEditHelper().getEditCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.IElementType#getAllSuperTypes()
	 */
	public IElementType[] getAllSuperTypes() {

		if (super.getAllSuperTypes() == null) {
			List result = new ArrayList();
	
			// Add all the metamodel supertypes
			IElementType[] metamodelSupertypes = getMetamodelType()
				.getAllSuperTypes();
			result.addAll(Arrays.asList(metamodelSupertypes));
			
			// Add the metamodel type
			result.add(getMetamodelType());
	
			// Add all the specialization supertypes
			result.addAll(getSpecializationSupertypes(this));

			setAllSupertypes((IElementType[]) result.toArray(new IElementType[] {}));
		}
		return super.getAllSuperTypes();
	}

	/**
	 * Gest the specialization supertypes of <code>specializationType</code>
	 * 
	 * @param specializationType
	 *            the specialization type
	 * @return the list of specialization supertypes
	 */
	private List getSpecializationSupertypes(
			ISpecializationType specializationType) {

		List result = new ArrayList();
		IElementType[] specializationSupertypes = specializationType
			.getSpecializedTypes();

		for (int i = 0; i < specializationSupertypes.length; i++) {
			IElementType nextType = specializationSupertypes[i];

			if (nextType instanceof ISpecializationType) {
				result
					.addAll(getSpecializationSupertypes((ISpecializationType) nextType));
				result.add(nextType);
			}
		}
		return result;
	}

}