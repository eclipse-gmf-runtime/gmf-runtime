/******************************************************************************
 * Copyright (c) 2005, 2015 IBM Corporation, Christian W. Damus, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 *    Christian W. Damus - bug 457888
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

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

    public static final int COMMAND_FAILURE = 4;
    
	public static final int ERROR_PARSING_XML = 10;

	public static final int TYPE_NOT_INITED = 11;

	public static final int ADVICE_BINDING_NOT_INITED = 12;

	public static final int ELEMENT_NOT_INITED = 13;

	public static final int FACTORY_NOT_INITED = 14;

	public static final int METAMODEL_NOT_INITED = 15;
	
	public static final int CONTEXT_NOT_INITED = 16;

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
	
	public static final int CLIENT_CONTEXT_NOT_INITED = 50;
	
	public static final int BINDING_NO_CONTEXT = 60;
	
	public static final int BINDING_NO_SUCH_CONTEXT = 61;
	
	public static final int PATTERN_INVALID_SYNTAX = 62;
	
	public static final int BINDING_NO_REF_OR_PATTERN = 63;
	
	public static final int BINDING_BOTH_REF_AND_PATTERN = 64;
	
	public static final int CLIENT_MATCHER_FAILURE = 70;
	
	public static final int CLIENT_NO_MATCHER = 71;

	public static final int WRONG_TYPE = 80;
	
	public static final int WRONG_CLIENT_CONTEXT = 81;
	
	public static final int DEPENDENCY_CONSTRAINT = 82;
	
	public static final int LISTENER_EXCEPTION = 90;
	

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
	 * @param reason
	 *            the localized reason why the advice binding was
	 *            not initialized
	 * @return the new CoreException
	 */
	public static CoreException getAdviceBindingInitException(String adviceId,
			String reason) {

		return new CoreException(new Status(IStatus.WARNING, EMFTypePlugin
				.getPluginId(), ADVICE_BINDING_NOT_INITED, EMFTypeCoreMessages
				.bind(EMFTypeCoreMessages.adviceBinding_not_init_WARN_,
						adviceId, reason), null));
	}

	/**
	 * Convenience method to construct a new CoreException indicating that an
	 * element type failed to be created.
	 * 
	 * @param typeId
	 *            the element type ID
	 * @param reason
	 *            the localized reason why the element type was not
	 *            initialized
	 * @param e
	 *            an exception, if the failure was due to an exception
	 * @return the new CoreException
	 */
	public static CoreException getTypeInitException(String typeId,
			String reason, Exception e) {

		return new CoreException(new Status(IStatus.WARNING, EMFTypePlugin
				.getPluginId(), TYPE_NOT_INITED, EMFTypeCoreMessages.bind(
				EMFTypeCoreMessages.type_not_init_WARN_, typeId, reason), e));
	}

	/**
	 * Convenience method to construct a new CoreException indicating that an
	 * element initialization failed.
	 * 
	 * @param elementId
	 *            the element ID
	 * @param reason
	 *            the localized reason why the element was not
	 *            initialized
	 * @param e
	 *            an exception, if the failure was due to an exception
	 * @return the new CoreException
	 */
	public static CoreException getInitException(String elementId,
			String reason, Exception e) {

		return new CoreException(new Status(IStatus.WARNING, EMFTypePlugin
				.getPluginId(), ELEMENT_NOT_INITED, EMFTypeCoreMessages.bind(
				EMFTypeCoreMessages.elment_not_init_WARN_, elementId, reason),
				e));
	}

	/**
	 * Convenience method to construct a new CoreException indicating that an
	 * element type factory initialization failed.
	 * 
	 * @param factoryName
	 *            the factory name
	 * @param reason
	 *            the localized reason why the element type factory
	 *            was not initialized
	 * @return the new CoreException
	 */
	public static CoreException getFactoryInitException(String factoryName,
			String reason) {

		return new CoreException(new Status(IStatus.WARNING, EMFTypePlugin
				.getPluginId(), FACTORY_NOT_INITED, EMFTypeCoreMessages.bind(
				EMFTypeCoreMessages.elementTypeFactory_not_init_WARN_,
				factoryName, reason), null));
	}

	/**
	 * Convenience method to construct a new CoreException indicating that an
	 * metamodel element itialization failed.
	 * 
	 * @param nsURI
	 *            the namespace URI
	 * @param reason
	 *            the localized reason why the metamodel element
	 *            was not initialized
	 * @return the new CoreException
	 */
	public static CoreException getMetamodelInitException(String nsURI,
			String reason) {

		return new CoreException(new Status(IStatus.WARNING, EMFTypePlugin
				.getPluginId(), METAMODEL_NOT_INITED, EMFTypeCoreMessages.bind(
				EMFTypeCoreMessages.metamodel_not_init_WARN_, nsURI, reason),
				null));
	}

	/**
	 * Convenience method to construct a new CoreException indicating that an
	 * element type factory initialization failed.
	 * 
	 * @param factoryName
	 *            the factory name
	 * @param reason
	 *            the localized reason why the element type factory
	 *            was not initialized
	 * @return the new CoreException
	 */
	public static CoreException getContextInitException(String contextId,
			String reason) {

		return new CoreException(new Status(IStatus.WARNING, EMFTypePlugin
				.getPluginId(), CONTEXT_NOT_INITED, EMFTypeCoreMessages.bind(
				EMFTypeCoreMessages.clientContext_not_init_WARN_,
				contextId, reason), null));
	}
}

