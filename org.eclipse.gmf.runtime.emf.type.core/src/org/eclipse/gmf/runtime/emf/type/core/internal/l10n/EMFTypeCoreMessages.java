/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gmf.runtime.emf.type.core.internal.l10n;

import org.eclipse.osgi.util.NLS;

public final class EMFTypeCoreMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages";//$NON-NLS-1$

	private EMFTypeCoreMessages() {
		// Do not instantiate
	}

	public static String Request_Label_Create;
	public static String Request_Label_Destroy;
	public static String Request_Label_DestroyDependents;
	public static String Request_Label_Duplicate;
	public static String Request_Label_Move;
	public static String Request_Label_ReorientSource;
	public static String Request_Label_ReorientTarget;
	public static String xml_parsing_ERROR_;
	public static String xml_parsing_elementTypeBindings_ERROR_;
	public static String type_not_init_WARN_;
	public static String type_reason_no_id_WARN_;
	public static String type_reason_duplicate_id_WARN_;
	public static String type_reason_no_eclass_WARN_;
	public static String type_reason_eclass_not_found_WARN_;
	public static String type_reason_no_specialized_WARN_;
	public static String type_reason_no_specialized_id_WARN_;
	public static String type_reason_no_param_name_WARN_;
	public static String type_reason_no_param_value_WARN_;
	public static String adviceBinding_not_init_WARN_;
	public static String adviceBinding_reason_no_id_WARN_;
	public static String adviceBinding_reason_no_type_id_WARN_;
	public static String adviceBinding_reason_no_edit_helper_advice_id_WARN_;
	public static String adviceBinding_reason_invalid_inheritance_WARN_;
	public static String elment_not_init_WARN_;
	public static String element_reason_invalid_enablement_expression_WARN_;
	public static String element_reason_no_econtainmentfeature_qname_WARN_;
	public static String element_reason_matcher_no_class_WARN_;
	public static String element_reason_no_econtainmentfeature_metamodel_WARN_;
	public static String elementTypeFactory_not_init_WARN_;
	public static String elementTypeFactory_reason_no_factory_WARN_;
	public static String elementTypeFactory_reason_no_kind_WARN_;
	public static String metamodel_not_init_WARN_;
	public static String metamodel_reason_no_nsURI_WARN_;
	public static String metamodel_reason_nsURI_not_found_WARN_;
	public static String adviceBinding_class_not_found_ERROR_;
	public static String editHelperAdvice_class_not_found_ERROR_;
	public static String editHelper_class_not_found_ERROR_;
	public static String matcher_class_not_found_ERROR_;
	public static String expression_evaluation_failure_ERROR_;
	public static String elementTypeFactory_class_not_found_ERROR_;
	public static String eContainmentFeature_not_reference_feature_ERROR_;
	public static String eContainmentFeature_no_such_feature_ERROR_;
	public static String specializationType_specializes_multiple_metamodel_types_ERROR_;
	public static String specializationType_specializes_invalid_id_ERROR_;
	public static String binding_noContextId_ERROR_;
	public static String binding_noSuchContext_ERROR_;
	public static String pattern_invalid_syntax_ERROR_;
	public static String binding_no_ref_or_pattern_ERROR_;
	public static String binding_both_ref_and_pattern_ERROR_;
	public static String client_matcherFailure_ERROR_;
	public static String clientContext_not_init_WARN_;
	public static String context_no_matcher_ERROR_;
	public static String context_no_id_ERROR_;
	public static String context_matcher_wrong_class_ERROR_;
	public static String moveElementsCommand_noTargetFeature;
	public static String destroyCommandFailed;
	public static String createElementCommand_noElementCreated;
	
	public static String defaultEditHelper_name;

	static {
		NLS.initializeMessages(BUNDLE_NAME, EMFTypeCoreMessages.class);
	}
}