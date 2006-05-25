/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal.descriptors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IElementTypeFactory;
import org.eclipse.gmf.runtime.emf.type.core.IMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.IMetamodelTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelper;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * Descriptor for a metamodel element type that has been defined in XML using
 * the <code>elementTypes</code> extension point.
 * 
 * @author ldamus
 */
public class MetamodelTypeDescriptor
	extends ElementTypeDescriptor
	implements IMetamodelTypeDescriptor {

	/**
	 * The metaclass that this type represents.
	 */
	private EClass eClass;
	
	/**
	 * The name of the metaclass that this type represents.
	 */
	private String eClassName;

	/**
	 * Flag indicating that a metaclass could not be found with the specified
	 * name.
	 */
	private boolean eClassNotFound = false;

	/**
	 * Describes the metamodel in which to find the metaclass.
	 */
	private final MetamodelDescriptor metamodelDescriptor;

	/**
	 * The edit helper.
	 */
	private IEditHelper editHelper;

	/**
	 * The metamodel type.
	 */
	private IMetamodelType metamodelType;

	/**
	 * The edit helper class name. May be <code>null</code>.
	 */
	private String editHelperName;

	/**
	 * Constructs a descriptor for the <code>metamodelType</code>.
	 * 
	 * @param metamodelType
	 *            the metamodel type
	 */
	public MetamodelTypeDescriptor(IMetamodelType metamodelType) {

		super(metamodelType);

		this.eClass = metamodelType.getEClass();
		this.editHelper = metamodelType.getEditHelper();
		this.metamodelType = metamodelType;
		this.metamodelDescriptor = null;
	}
	
	/**
	 * Create a descriptor from a config element.
	 * 
	 * @param configElement
	 *            the configuration element
	 * @throws CoreException
	 *             when the configuration element is missing required attributes
	 */
	public MetamodelTypeDescriptor(IConfigurationElement configElement,
			MetamodelDescriptor metamodelDescriptor)
		throws CoreException {

		super(configElement);
		
		this.metamodelDescriptor = metamodelDescriptor;

		// ECLASS
		eClassName = configElement
			.getAttribute(ElementTypeXmlConfig.A_ECLASS);

		if (eClassName == null) {
			throw EMFTypePluginStatusCodes.getTypeInitException(getId(),
				EMFTypeCoreMessages.type_reason_no_eclass_WARN_, null);
		}

		editHelperName = getConfigElement().getAttribute(
			ElementTypeXmlConfig.A_EDIT_HELPER);
	}

	/**
	 * Returns the metamodel type for this descriptor. Lazy creation of the
	 * metamodel type to avoid premature plugin loading.
	 * 
	 * @return the metamodel type
	 */
	public IElementType getElementType() {

		if (metamodelType == null) {

			if (getKindName() != null && getKindName().length() > 0) {
				IElementTypeFactory factory = ElementTypeRegistry.getInstance()
					.getElementTypeFactory(getKindName());

				if (factory != null) {
					metamodelType = factory.createMetamodelType(this);
				}
			}
		}
		return metamodelType;
	}

	/**
	 * Gets the <code>EClass</code> that this type represents.
	 */
	public EClass getEClass() {
		if (eClass == null && !eClassNotFound && metamodelDescriptor != null) {
			EPackage ePackage = metamodelDescriptor.getEPackage();
			ENamedElement namedElement = ePackage.getEClassifier(getEClassName());

			if (namedElement instanceof EClass) {
				eClass = (EClass) namedElement;
			}

			if (eClass == null) {
				eClassNotFound = true;
				Log
				.error(
						EMFTypePlugin.getPlugin(),
						EMFTypePluginStatusCodes.TYPE_NOT_INITED,
						EMFTypeCoreMessages
								.bind(
										EMFTypeCoreMessages.type_not_init_WARN_,
										getEClassName(),
										EMFTypeCoreMessages.type_reason_eclass_not_found_WARN_));
			}
		}
		return eClass;
	}
	
	/**
	 * Gets the name of the <code>EClass</code> that this type represents.
	 * @return the name of the EClass, or <code>null</code> if this type doesn't represent an EClass.
	 */
	public String getEClassName() {
		if (eClassName == null && eClass != null) {
			eClassName = eClass.getName();
		}
		return eClassName;
	}
	
	/**
	 * Gets the namespace URI of the metamodel that owns the <code>EClass</code>
	 * that I represent.
	 * 
	 * @return the namespace URI, or <code>null</code> if I don't have one
	 */
	public String getNsURI() {
		if (metamodelDescriptor == null) {
			if (eClass != null) {
				return eClass.getEPackage().getNsURI();
			}
			return null;
		}
		return metamodelDescriptor.getNsURI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.IMetamodelTypeDescriptor#getEditHelper()
	 */
	public IEditHelper getEditHelper() {

		if (editHelper == null) {

			if (editHelperName != null) {
				try {
					editHelper = (IEditHelper) getConfigElement()
						.createExecutableExtension(
							ElementTypeXmlConfig.A_EDIT_HELPER);

				} catch (CoreException e) {
					Log
							.error(
									EMFTypePlugin.getPlugin(),
									EMFTypePluginStatusCodes.EDIT_HELPER_CLASS_NOT_FOUND,
									EMFTypeCoreMessages
											.bind(
													EMFTypeCoreMessages.editHelper_class_not_found_ERROR_,
													editHelperName), e);
					// Don't recompute the edit helper class after it has failed
					// once.
					editHelperName = null;
				}
			}
		}
		return editHelper;
	}
	
	/**
	 * Gets my name. If no name is specified, uses the name of the
	 * <code>EClass</code> that I represent.
	 */
	public String getName() {
		String name = super.getName();
		
		if ((name == null || name.length() < 1) && getEClass() != null) {
			name = getEClass().getName();
			setName(name);
		}
		return name;
	}
	
	public String toString() {
		return "MetamodelTypeDescriptor[" + getId()+ "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}