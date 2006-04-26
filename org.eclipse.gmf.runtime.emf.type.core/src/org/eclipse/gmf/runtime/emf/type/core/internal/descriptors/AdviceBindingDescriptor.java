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

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.emf.type.core.IContainerDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.impl.XMLExpressionMatcher;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * Descriptor for an advice binding. Advice can be bound to any element type.
 * 
 * @author ldamus
 */
public class AdviceBindingDescriptor
	implements IEditHelperAdviceDescriptor {

	/**
	 * The advice binding ID.
	 */
	private final String id;
	
	/**
	 * The identifier of this element type.
	 */
	private final String typeId;

	/**
	 * Indicates the related element types that should inherite this advice.
	 */
	private final AdviceBindingInheritance inheritance;

	/**
	 * The matcher class name. May be <code>null</code>.
	 */
	private String matcherClassName;
	
	/**
	 * The container element matcher. May be <code>null</code>.
	 */
	private IElementMatcher matcher;

	/**
	 * The container matcher configuration element.
	 */
	private IConfigurationElement matcherConfig;

	/**
	 * The binding configuration element.
	 */
	private final IConfigurationElement bindingConfig;

	/**
	 * The class name of the edit helper advice.
	 */
	private String editHelperAdviceName;
	
	/**
	 * The edit helper advice.
	 */
	private IEditHelperAdvice editHelperAdvice;

	/**
	 * The container descriptor.
	 */
	private IContainerDescriptor containerDescriptor;

	/**
	 * Teh metamodel descriptor.
	 */
	private final MetamodelDescriptor metamodelDescriptor;

	/**
	 * Constructs a new container descriptor from the configuration element.
	 * 
	 * @param config
	 *            the configuration element
	 * @throws CoreException
	 *             when the configuration element does not contain the required
	 *             elements and attributes
	 */
	public AdviceBindingDescriptor(IConfigurationElement config,
			MetamodelDescriptor metamodelDescriptor)
		throws CoreException {

		this.bindingConfig = config;
		this.metamodelDescriptor = metamodelDescriptor;
	
		// ID
		this.id = config.getAttribute(ElementTypeXmlConfig.A_ID);
		if (id == null) {
			throw EMFTypePluginStatusCodes.getAdviceBindingInitException(
					StringStatics.BLANK,
					EMFTypeCoreMessages.adviceBinding_reason_no_id_WARN_);
		}
		
		// EDIT HELPER ADVICE CLASS
		editHelperAdviceName = config
			.getAttribute(ElementTypeXmlConfig.A_CLASS);
		if (editHelperAdviceName == null) {
			throw EMFTypePluginStatusCodes
					.getAdviceBindingInitException(
							id,
							EMFTypeCoreMessages.adviceBinding_reason_no_edit_helper_advice_id_WARN_);
		}

		// TYPE ID
		this.typeId = config.getAttribute(ElementTypeXmlConfig.A_TYPE_ID);
		if (typeId == null) {
			throw EMFTypePluginStatusCodes.getAdviceBindingInitException(id,
					EMFTypeCoreMessages.adviceBinding_reason_no_type_id_WARN_);
		}
		
		// ECONTAINER
		IConfigurationElement[] containerConfigs = config
			.getChildren(ElementTypeXmlConfig.E_ECONTAINER);

		if (containerConfigs.length > 0) {
			containerDescriptor = new ContainerDescriptor(containerConfigs[0],
				metamodelDescriptor, getId());
		}

		// APPLY TO RELATED ELEMENT TYPES
		String inheritanceString = config
				.getAttribute(ElementTypeXmlConfig.A_INHERITANCE);
		AdviceBindingInheritance declaredInheritance = AdviceBindingInheritance
				.getAdviceBindingInheritance(inheritanceString);

		if (declaredInheritance == null) {
			if (inheritanceString != null) {
				// Invalid inheritance value
				throw EMFTypePluginStatusCodes.getAdviceBindingInitException(id,
						EMFTypeCoreMessages.adviceBinding_reason_invalid_inheritance_WARN_);
			}
			// Default inheritance is NONE
			declaredInheritance = AdviceBindingInheritance.NONE;
		}
		
		this.inheritance = declaredInheritance;

		// XML MATCHER EXPRESSION
		IConfigurationElement[] enablementConfigs = config
			.getChildren(ElementTypeXmlConfig.E_ENABLEMENT);

		if (enablementConfigs.length > 0) {
			matcher = new XMLExpressionMatcher(enablementConfigs[0], id);
		}

		// MATCHER CLASS
		if (matcher == null) {
			IConfigurationElement[] matcherConfigs = config
				.getChildren(ElementTypeXmlConfig.E_MATCHER);

			if (matcherConfigs.length > 0) {
				matcherClassName = matcherConfigs[0].getAttribute(ElementTypeXmlConfig.A_CLASS);
				
				if (matcherClassName == null) {
					throw EMFTypePluginStatusCodes.getInitException(id,
							EMFTypeCoreMessages.element_reason_matcher_no_class_WARN_, null);
				}
				matcherConfig = matcherConfigs[0];
				
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.internal.impl.IEditHelperAdviceDescriptor#getTypeId()
	 */
	public String getTypeId() {
		return typeId;
	}
	
	/**
	 * Returns the advice binding id.
	 * @return the advice binding id
	 */
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.internal.impl.IEditHelperAdviceDescriptor#getMatcher()
	 */
	public IElementMatcher getMatcher() {

		if (matcher == null && matcherClassName != null && matcherConfig != null) {
			try {
				matcher = (IElementMatcher) matcherConfig
					.createExecutableExtension(ElementTypeXmlConfig.A_CLASS);

			} catch (CoreException e) {
				Log
						.error(
								EMFTypePlugin.getPlugin(),
								EMFTypePluginStatusCodes.MATCHER_CLASS_NOT_FOUND,
								EMFTypeCoreMessages
										.bind(
												EMFTypeCoreMessages.matcher_class_not_found_ERROR_,
												matcherClassName), e);

				// Don't try to recompute the matcher class if it has failed.
				matcherClassName = null;
			}
		}
		return matcher;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.internal.impl.IEditHelperAdviceDescriptor#getEditHelperAdvice()
	 */
	public IEditHelperAdvice getEditHelperAdvice() {

		if (editHelperAdvice == null) {

			if (editHelperAdviceName != null) {
				try {
					editHelperAdvice = (IEditHelperAdvice) bindingConfig
						.createExecutableExtension(ElementTypeXmlConfig.A_CLASS);

				} catch (CoreException e) {
					Log
							.error(
									EMFTypePlugin.getPlugin(),
									EMFTypePluginStatusCodes.EDIT_HELPER_ADVICE_CLASS_NOT_FOUND,
									EMFTypeCoreMessages
											.bind(
													EMFTypeCoreMessages.editHelperAdvice_class_not_found_ERROR_,
													editHelperAdviceName), e);
					// Don't recompute the edit helper advice after it has
					// failed once.
					editHelperAdviceName = null;
				}
			}
		}
		return editHelperAdvice;
	}

	/**
	 * Gets the metamodel descriptor for this advice binding.
	 * 
	 * @return the metamodel descriptor
	 */
	public MetamodelDescriptor getMetamodelDescriptor() {
		return metamodelDescriptor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.type.core.internal.descriptors.IEditHelperAdviceDescriptor#getInheritance()
	 */
	public AdviceBindingInheritance getInheritance() {
		return inheritance;
	}
	
	/**
	 * Gets the container descriptor.
	 * 
	 * @return the container descriptor
	 */
	public IContainerDescriptor getContainerDescriptor() {
		return containerDescriptor;
	}
	
	public String toString() {
		return "AdviceBindingDescriptor[" + getId()+ "]";
	}
}