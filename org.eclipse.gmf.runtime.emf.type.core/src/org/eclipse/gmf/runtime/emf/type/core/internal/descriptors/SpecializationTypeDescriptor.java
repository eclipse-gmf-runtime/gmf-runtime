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

package org.eclipse.gmf.runtime.emf.type.core.internal.descriptors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.emf.type.core.ElementType;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IContainerDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IElementTypeFactory;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.impl.XMLExpressionMatcher;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.ResourceManager;

/**
 * Descriptor for a specialization element type that has been defined in XML
 * using the <code>elementTypes</code> extension point.
 * 
 * @author ldamus
 */
public class SpecializationTypeDescriptor
	extends ElementTypeDescriptor
	implements ISpecializationTypeDescriptor {

	/**
	 * The matcher class name. May be <code>null</code>.
	 */
	private String matcherClassName;

	/**
	 * The container element matcher. May be <code>null</code>.
	 */
	private IElementMatcher matcher;

	/**
	 * The container matcher configuration element. May be <code>null</code>.
	 */
	private IConfigurationElement matcherConfig;

	/**
	 * The identifiers of the element types that this type specializes.
	 */
	private String[] specializedTypeIds;

	/**
	 * The element types that are specialized by this type.
	 */
	private IElementType[] specializedTypes;

	/**
	 * The container descriptor.
	 */
	private IContainerDescriptor containerDescriptor;

	/**
	 * The edit helper advice descriptor.
	 */
	private IEditHelperAdviceDescriptor editHelperAdviceDescriptor;
	
	/**
	 * The edit helper advice.
	 */
	private IEditHelperAdvice editHelperAdvice;

	/**
	 * The specialization type.
	 */
	private ISpecializationType specializationType;

	/**
	 * The class name of the edit helper advice.
	 */
	private String editHelperAdviceName;

	/**
	 * Create a descriptor from a specialization type.
	 * 
	 * @param specializationType the specialization type
	 */
	public SpecializationTypeDescriptor(ISpecializationType specializationType) {
		
		super(specializationType);
		
		this.containerDescriptor = specializationType.getEContainerDescriptor();
		this.editHelperAdvice = specializationType.getEditHelperAdvice();
		this.matcher = specializationType.getMatcher();
		this.specializationType = specializationType;
		this.specializedTypeIds = specializationType.getSpecializedTypeIds();
		this.specializedTypes = specializationType.getSpecializedTypes();
	}
			
	/**
	 * Create a descriptor from a configuration element.
	 * 
	 * @param configElement
	 *            the configuration element
	 * @param metamodelDescriptor
	 *            the metamodel descriptor. Attributes of the container
	 *            descriptor are defined relative to this metmodel.
	 */
	public SpecializationTypeDescriptor(IConfigurationElement configElement,
			MetamodelDescriptor metamodelDescriptor)
		throws CoreException {

		super(configElement);

		editHelperAdviceName = configElement
			.getAttribute(ElementTypeXmlConfig.A_EDIT_HELPER_ADVICE);

		editHelperAdviceDescriptor = new EditHelperAdviceDescriptor(
			editHelperAdviceName, this);

		//SPECIALIZES
		List specializes = new ArrayList();
		IConfigurationElement[] specializesConfigs = configElement
			.getChildren(ElementTypeXmlConfig.E_SPECIALIZES);

		for (int i = 0; i < specializesConfigs.length; i++) {
			IConfigurationElement nextSpecializesConfig = specializesConfigs[i];

			String specializedId = nextSpecializesConfig
				.getAttribute(ElementTypeXmlConfig.A_ID);

			if (specializedId == null) {
				throw EMFTypePluginStatusCodes.getTypeInitException(getId(),
					EMFTypePluginStatusCodes.TYPE_NO_SPECIALIZED_ID_KEY, null);
			}

			specializes.add(specializedId);
		}
		specializedTypeIds = (String[]) specializes.toArray(new String[] {});

		if (specializedTypeIds.length < 1) {
			throw EMFTypePluginStatusCodes.getTypeInitException(getId(),
				EMFTypePluginStatusCodes.TYPE_NO_SPECIALIZED_KEY, null);
		}

		// ECONTAINER
		IConfigurationElement[] containerConfigs = configElement
			.getChildren(ElementTypeXmlConfig.E_ECONTAINER);

		if (containerConfigs.length > 0) {
			containerDescriptor = new ContainerDescriptor(containerConfigs[0],
				metamodelDescriptor, getId());
		}

		// XML MATCHER EXPRESSION
		IConfigurationElement[] enablementConfigs = configElement
			.getChildren(ElementTypeXmlConfig.E_ENABLEMENT);

		if (enablementConfigs.length > 0) {
			matcher = new XMLExpressionMatcher(enablementConfigs[0], getId());
		}

		// MATCHER CLASS
		if (matcher == null) {
			IConfigurationElement[] matcherConfigs = configElement
				.getChildren(ElementTypeXmlConfig.E_MATCHER);

			if (matcherConfigs.length > 0) {
				matcherClassName = matcherConfigs[0]
					.getAttribute(ElementTypeXmlConfig.A_CLASS);

				if (matcherClassName == null) {
					throw EMFTypePluginStatusCodes.getInitException(getId(),
						EMFTypePluginStatusCodes.MATCHER_NO_CLASS_KEY, null);
				}
				matcherConfig = matcherConfigs[0];
			}
		}
	}

	/**
	 * Returns the specialization type for this descriptor. Lazy creation of the
	 * specialization type to avoid premature plugin loading.
	 * 
	 * @return the element type
	 */
	public IElementType getElementType() {

		if (specializationType == null) {

			if (getKindName() != null && getKindName().length() > 0) {
				IElementTypeFactory factory = ElementTypeRegistry.getInstance()
					.getElementTypeFactory(getKindName());

				if (factory != null) {
					specializationType = factory.createSpecializationType(this);
				}
			}
		}
		return specializationType;
	}

	/**
	 * Gets the identifiers of the element types that this type specializes. May
	 * be <code>null</code>.
	 * 
	 * @return the ids of the types that are specialized
	 */
	public String[] getSpecializationTypeIds() {
		return specializedTypeIds;
	}

	/**
	 * Gets the element matcher configuration element. May be <code>null</code>.
	 * 
	 * @return the element matcher configuration element
	 */
	public IConfigurationElement getMatcherConfiguration() {
		return matcherConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.ISpecializationTypeDescriptor#getContainerDescriptor()
	 */
	public IContainerDescriptor getContainerDescriptor() {
		return containerDescriptor;
	}

	/**
	 * Gets the edit helper advice descriptor. May be <code>null</code>.
	 * 
	 * @return the edit helper advice descriptor
	 */
	public IEditHelperAdviceDescriptor getEditHelperAdviceDescriptor() {
		return editHelperAdviceDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.ISpecializationTypeDescriptor#getSpecializedTypes()
	 */
	public IElementType[] getSpecializedTypes() {

		if (specializedTypes == null) {
			specializedTypes = new ElementType[specializedTypeIds.length];

			for (int i = 0; i < specializedTypeIds.length; i++) {
				specializedTypes[i] = ElementTypeRegistry.getInstance()
					.getType(specializedTypeIds[i]);
			}
		}
		return specializedTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.ISpecializationTypeDescriptor#getMatcher()
	 */
	public IElementMatcher getMatcher() {

		if (matcher == null && matcherClassName != null
			&& matcherConfig != null) {
			try {
				matcher = (IElementMatcher) matcherConfig
					.createExecutableExtension(ElementTypeXmlConfig.A_CLASS);

			} catch (CoreException e) {
				Log.error(EMFTypePlugin.getPlugin(),
					EMFTypePluginStatusCodes.MATCHER_CLASS_NOT_FOUND,
					ResourceManager.getMessage(
						EMFTypePluginStatusCodes.MATCHER_CLASS_NOT_FOUND_KEY,
						new Object[] {matcherClassName}), e);

				// Don't try to recompute the matcher class if it has failed.
				matcherClassName = null;
			}
		}
		return matcher;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.ISpecializationTypeDescriptor#getEditHelperAdvice()
	 */
	public IEditHelperAdvice getEditHelperAdvice() {

		if (editHelperAdvice == null && getEditHelperAdviceDescriptor() != null) {
			editHelperAdvice = getEditHelperAdviceDescriptor().getEditHelperAdvice();
		}
		return editHelperAdvice;
	}
	
	/**
	 * Gets the icon URL from the the first type I specialize, if I don't have an icon.
	 */
	public URL getIconURL() {

		if ((super.getIconURL() == null) && (getSpecializedTypes().length > 0)) {
			return getSpecializedTypes()[0].getIconURL();
		}
		return super.getIconURL();
	}
}