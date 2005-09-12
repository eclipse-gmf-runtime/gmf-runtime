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

package org.eclipse.gmf.runtime.emf.type.core.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.ResourceManager;

/**
 * Status codes for errors and warnings logged by the Element Type framework.
 * <P>
 * Also includes keys into the .properties file for error and warning messages.
 * 
 * @author ldamus
 */
public class EMFTypePluginStatusCodes {

	//
	// Integer-valued error codes
	//
	public static final int ERROR_PARSING_XML = 10;

	public static final int TYPE_NOT_INITED = 11;

	public static final int ADVICE_BINDING_NOT_INITED = 12;

	public static final int ELEMENT_NOT_INITED = 13;

	public static final int FACTORY_NOT_INITED = 14;

	public static final int METAMODEL_NOT_INITED = 15;

	public static final int EDIT_HELPER_ADVICE_CLASS_NOT_FOUND = 20;

	public static final int MATCHER_CLASS_NOT_FOUND = 21;

	public static final int EDIT_HELPER_CLASS_NOT_FOUND = 22;

	public static final int EXPRESSION_EVALUATION_FAILURE = 23;

	public static final int ELEMENT_TYPE_FACTORY_CLASS_NOT_FOUND = 24;

	public static final int CONTAINMENT_FEATURE_NOT_REFERENCE_FEATURE = 25;

	public static final int CONTAINMENT_FEATURE_NO_SUCH_FEATURE = 26;

	public static final int SPECIALIZATION_TYPE_SPECIALIZES_MULTIPLE_METAMODEL_TYPES = 27;

	public static final int SPECIALIZATION_TYPE_SPECIALIZES_INVALID_ID = 28;
	
	public static final int CONTAINMENT_FEATURE_NO_METAMODEL = 29;

	//
	// XML loading failure messages
	//
	public static final String ERROR_PARSING_XML_KEY = "xml.parsing_ERROR_"; //$NON-NLS-1$

	public static final String TYPE_NOT_INITED_KEY = "type.not-init_WARN_"; //$NON-NLS-1$

	public static final String ADVICE_BINDING_NOT_INITED_KEY = "adviceBinding.not-init_WARN_"; //$NON-NLS-1$

	public static final String ELEMENT_NOT_INITED_KEY = "element.not-init_WARN_"; //$NON-NLS-1$

	public static final String FACTORY_NOT_INITED_KEY = "elementTypeFactory.not-init_WARN_"; //$NON-NLS-1$

	public static final String METAMODEL_NOT_INITED_KEY = "metamodel.not-init_WARN_"; //$NON-NLS-1$

	//
	// Reasons for XML loading failure
	//
	public static final String TYPE_DUPLICATE_KEY = "type.reason.duplicate-id_WARN_"; //$NON-NLS-1$

	public static final String TYPE_NO_ID_KEY = "type.reason.no-id_WARN_"; //$NON-NLS-1$

	public static final String TYPE_NO_ECLASS_KEY = "type.reason.no-eclass_WARN_"; //$NON-NLS-1$

	public static final String TYPE_ECLASS_NOT_FOUND_KEY = "type.reason.eclass-not-found_WARN_"; //$NON-NLS-1$

	public static final String TYPE_NO_SPECIALIZED_KEY = "type.reason.no-specialized_WARN_"; //$NON-NLS-1$

	public static final String TYPE_NO_SPECIALIZED_ID_KEY = "type.reason.no-specialized-id_WARN_"; //$NON-NLS-1$

	public static final String TYPE_NO_PARAM_NAME_KEY = "type.reason.no-param-name_WARN_"; //$NON-NLS-1$

	public static final String TYPE_NO_PARAM_VALUE_KEY = "type.reason.no-param-value_WARN_"; //$NON-NLS-1$

	public static final String ADVICE_BINDING_NO_ID_KEY = "adviceBinding.reason.no-id_WARN_"; //$NON-NLS-1$

	public static final String ADVICE_BINDING_NO_TYPE_ID_KEY = "adviceBinding.reason.no-type-id_WARN_"; //$NON-NLS-1$

	public static final String ADVICE_BINDING_NO_EDIT_HELPER_ADVICE_KEY = "adviceBinding.reason.no-edit-helper-advice-id_WARN_"; //$NON-NLS-1$

	public static final String ADVICE_BINDING_INVALID_INHERITANCE = "adviceBinding.reason.invalid-inheritance_WARN_"; //$NON-NLS-1$
	
	public static final String CONTAINMENT_FEATURE_NO_QNAME_KEY = "element.reason.no-econtainmentfeature-qname_WARN_"; //$NON-NLS-1$
	
	public static final String CONTAINMENT_FEATURE_NO_METAMODEL_KEY = "element.reason.no-econtainmentfeature-metamodel_WARN_"; //$NON-NLS-1$

	public static final String ENABLEMENT_INVALID_EXPRESSION_KEY = "element.reason.invalid-enablement-expression_WARN_"; //$NON-NLS-1$

	public static final String MATCHER_NO_CLASS_KEY = "element.reason.matcher-no-class_WARN_"; //$NON-NLS-1$

	public static final String ELEMENT_TYPE_FACTORY_NO_FACTORY_KEY = "elementTypeFactory.reason.no-factory_WARN_"; //$NON-NLS-1$

	public static final String ELEMENT_TYPE_FACTORY_NO_KIND_KEY = "elementTypeFactory.reason.no-kind_WARN_"; //$NON-NLS-1$

	public static final String METAMODEL_NO_NSURI_KEY = "metamodel.reason.no-nsURI_WARN_"; //$NON-NLS-1$

	public static final String METAMODEL_NSURI_NOT_FOUND_KEY = "metamodel.reason.nsURI-not-found_WARN_"; //$NON-NLS-1$

	
	//
	// Errors
	//
	public static final String EDIT_HELPER_ADVICE_CLASS_NOT_FOUND_KEY = "editHelperAdvice.class-not-found_ERROR_"; //$NON-NLS-1$

	public static final String MATCHER_CLASS_NOT_FOUND_KEY = "matcher.class-not-found_ERROR_"; //$NON-NLS-1$

	public static final String EDIT_HELPER_CLASS_NOT_FOUND_KEY = "editHelper.class-not-found_ERROR_"; //$NON-NLS-1$

	public static final String EXPRESSION_EVALUATION_FAILURE_KEY = "expression.evaluation-failure_ERROR_"; //$NON-NLS-1$

	public static final String ELEMENT_TYPE_FACTORY_CLASS_NOT_FOUND_KEY = "elementTypeFactory.class-not-found_ERROR_"; //$NON-NLS-1$

	public static final String CONTAINMENT_FEATURE_NOT_REFERENCE_FEATURE_KEY = "eContainmentFeature.not-reference-feature_ERROR_"; //$NON-NLS-1$

	public static final String CONTAINMENT_FEATURE_NO_SUCH_FEATURE_KEY = "eContainmentFeature.no-such-feature_ERROR_"; //$NON-NLS-1$

	public static final String SPECIALIZATION_TYPE_SPECIALIZES_MULTIPLE_METAMODEL_TYPES_KEY = "specializationType.specializes-multiple-metamodel-types_ERROR_"; //$NON-NLS-1$

	public static final String SPECIALIZATION_TYPE_SPECIALIZES_INVALID_ID_KEY = "specializationType.specializes-invalid-id_ERROR_"; //$NON-NLS-1$

	
	/**
	 * Cannot be instantiated by clients.
	 */
	private EMFTypePluginStatusCodes() {
		// nothing to do
	}

	/**
	 * Convenience method to construct a new CoreException indicating that an
	 * advice binding failed to be created.
	 * 
	 * @param adviceId
	 *            the advice binding ID
	 * @param argKey
	 *            resource bundle key of the reason why the advice binding was
	 *            not initialized
	 * @return the new CoreException
	 */
	public static CoreException getAdviceBindingInitException(String adviceId,
			String argKey) {

		return new CoreException(new Status(IStatus.WARNING, EMFTypePlugin
			.getPluginId(), ADVICE_BINDING_NOT_INITED, ResourceManager
			.getMessage(ADVICE_BINDING_NOT_INITED_KEY, new Object[] {adviceId,
				ResourceManager.getLocalizedString(argKey)}), null));
	}

	/**
	 * Convenience method to construct a new CoreException indicating that an
	 * element type failed to be created.
	 * 
	 * @param typeId
	 *            the element type ID
	 * @param argKey
	 *            resource bundle key of the reason why the element type was not
	 *            initialized
	 * @param e
	 *            an exception, if the failure was due to an exception
	 * @return the new CoreException
	 */
	public static CoreException getTypeInitException(String typeId,
			String argKey, Exception e) {

		return new CoreException(new Status(IStatus.WARNING, EMFTypePlugin
			.getPluginId(), TYPE_NOT_INITED, ResourceManager.getMessage(
			TYPE_NOT_INITED_KEY, new Object[] {typeId,
				ResourceManager.getLocalizedString(argKey)}), e));
	}

	/**
	 * Convenience method to construct a new CoreException indicating that an
	 * element initialization failed.
	 * 
	 * @param elementId
	 *            the element ID
	 * @param argKey
	 *            resource bundle key of the reason why the element was not
	 *            initialized
	 * @param e
	 *            an exception, if the failure was due to an exception
	 * @return the new CoreException
	 */
	public static CoreException getInitException(String elementId,
			String argKey, Exception e) {

		return new CoreException(new Status(IStatus.WARNING, EMFTypePlugin
			.getPluginId(), ELEMENT_NOT_INITED, ResourceManager.getMessage(
			ELEMENT_NOT_INITED_KEY, new Object[] {elementId,
				ResourceManager.getLocalizedString(argKey)}), e));
	}

	/**
	 * Convenience method to construct a new CoreException indicating that an
	 * element type factory initialization failed.
	 * 
	 * @param factoryName
	 *            the factory name
	 * @param argKey
	 *            resource bundle key of the reason why the element type factory
	 *            was not initialized
	 * @return the new CoreException
	 */
	public static CoreException getFactoryInitException(String factoryName,
			String argKey) {

		return new CoreException(new Status(IStatus.WARNING, EMFTypePlugin
			.getPluginId(), FACTORY_NOT_INITED, ResourceManager.getMessage(
			FACTORY_NOT_INITED_KEY, new Object[] {factoryName,
				ResourceManager.getLocalizedString(argKey)}), null));
	}

	/**
	 * Convenience method to construct a new CoreException indicating that an
	 * metamodel element itialization failed.
	 * 
	 * @param nsURI
	 *            the namespace URI
	 * @param argKey
	 *            resource bundle key of the reason why the metamodel element
	 *            was not initialized
	 * @return the new CoreException
	 */
	public static CoreException getMetamodelInitException(String nsURI,
			String argKey) {

		return new CoreException(new Status(IStatus.WARNING, EMFTypePlugin
			.getPluginId(), METAMODEL_NOT_INITED, ResourceManager.getMessage(
			METAMODEL_NOT_INITED_KEY, new Object[] {nsURI,
				ResourceManager.getLocalizedString(argKey)}), null));
	}
}

