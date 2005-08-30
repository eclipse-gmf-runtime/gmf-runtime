/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.ResourceManager;

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

		// ECLASS
		String eClassName = configElement
			.getAttribute(ElementTypeXmlConfig.A_ECLASS);

		if (eClassName == null) {
			throw EMFTypePluginStatusCodes.getTypeInitException(getId(),
				EMFTypePluginStatusCodes.TYPE_NO_ECLASS_KEY, null);
		}

		EPackage ePackage = metamodelDescriptor.getEPackage();
		ENamedElement namedElement = ePackage.getEClassifier(eClassName);

		if (namedElement instanceof EClass) {
			eClass = (EClass) namedElement;
		}

		if (eClass == null) {
			throw EMFTypePluginStatusCodes.getTypeInitException(getId(),
				EMFTypePluginStatusCodes.TYPE_ECLASS_NOT_FOUND_KEY, null);
		}

		// NAME
		if (getName() == null || getName().length() < 1) {
			setName(eClass.getName());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.IMetamodelTypeDescriptor#getEClass()
	 */
	public EClass getEClass() {
		return eClass;
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
							ResourceManager
								.getMessage(
									EMFTypePluginStatusCodes.EDIT_HELPER_CLASS_NOT_FOUND_KEY,
									new Object[] {editHelperName}), e);
					// Don't recompute the edit helper class after it has failed
					// once.
					editHelperName = null;
				}
			}
		}
		return editHelper;
	}

}