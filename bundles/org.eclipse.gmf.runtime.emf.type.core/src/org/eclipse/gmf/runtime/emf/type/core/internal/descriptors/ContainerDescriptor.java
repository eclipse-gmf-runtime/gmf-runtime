/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.internal.descriptors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.StringStatics;
import org.eclipse.gmf.runtime.emf.type.core.IContainerDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.impl.XMLExpressionMatcher;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;

/**
 * Descriptor for a model element container.
 * 
 * @author ldamus
 */
class ContainerDescriptor
	implements IContainerDescriptor {

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
	 * The containment features. May be <code>null</code>.
	 */
	private EReference[] containmentFeatures;

	/**
	 * The containment feature names.
	 */
	private List featureNames = new ArrayList();

	/**
	 * The metamodel descriptor.
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
	public ContainerDescriptor(IConfigurationElement config,
			MetamodelDescriptor metamodelDescriptor, String id)
		throws CoreException {

		this.metamodelDescriptor = metamodelDescriptor;

		// CONTAINMENT FEATURES
		IConfigurationElement[] featureConfigs = config
			.getChildren(ElementTypeXmlConfig.E_ECONTAINMENT_FEATURE);

		if (featureConfigs.length > 0 && metamodelDescriptor == null) {
			// Containment features can only be specified if a metamodel is
			// specified.
			throw EMFTypePluginStatusCodes
					.getInitException(
							id,
							EMFTypeCoreMessages
									.bind(
											EMFTypeCoreMessages.element_reason_no_econtainmentfeature_metamodel_WARN_,
											featureConfigs[0]), null);
		}

		for (int i = 0; i < featureConfigs.length; i++) {
			IConfigurationElement nextFeatureConfig = featureConfigs[i];
			String qname = nextFeatureConfig
				.getAttribute(ElementTypeXmlConfig.A_QNAME);

			if (qname == null) {
				throw EMFTypePluginStatusCodes
						.getInitException(
								id,
								EMFTypeCoreMessages.element_reason_no_econtainmentfeature_qname_WARN_,
								null);
			}
			featureNames.add(qname);
		}

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
				matcherClassName = matcherConfigs[0]
					.getAttribute(ElementTypeXmlConfig.A_CLASS);

				if (matcherClassName == null) {
					throw EMFTypePluginStatusCodes
							.getInitException(
									id,
									EMFTypeCoreMessages.element_reason_matcher_no_class_WARN_,
									null);
				}
				matcherConfig = matcherConfigs[0];
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.IContainerDescriptor#getMatcher()
	 */
	public IElementMatcher getMatcher() {

		if (matcher == null && matcherClassName != null
			&& matcherConfig != null) {
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
				// Don't recompute the matcher class if it has failed once.
				matcherClassName = null;
			}
		}
		return matcher;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.IContainerDescriptor#getContainmentFeatures()
	 */
	public EReference[] getContainmentFeatures() {

		if (containmentFeatures == null && !featureNames.isEmpty()) {

			List references = new ArrayList();
			if (metamodelDescriptor != null) {

				for (Iterator i = featureNames.iterator(); i.hasNext();) {
					String nextFeatureName = (String) i.next();

					EPackage ePackage = metamodelDescriptor.getEPackage();

					int featureIndex = nextFeatureName
						.lastIndexOf(StringStatics.PERIOD);
					String classifierName = nextFeatureName.substring(0,
						featureIndex);

					EClassifier classifier = ePackage
						.getEClassifier(classifierName);

					if ((featureIndex < nextFeatureName.length() - 1)
						&& classifier instanceof EClass) {

						String featureName = nextFeatureName
							.substring(featureIndex + 1);
						EStructuralFeature feature = ((EClass) classifier)
							.getEStructuralFeature(featureName);

						if (feature instanceof EReference) {
							references.add(feature);

						} else {
							Log
									.error(
											EMFTypePlugin.getPlugin(),
											EMFTypePluginStatusCodes.CONTAINMENT_FEATURE_NOT_REFERENCE_FEATURE,
											EMFTypeCoreMessages
													.bind(
															EMFTypeCoreMessages.eContainmentFeature_not_reference_feature_ERROR_,
															nextFeatureName),
											null);
						}
                    } else {
						Log
								.error(
										EMFTypePlugin.getPlugin(),
										EMFTypePluginStatusCodes.CONTAINMENT_FEATURE_NO_SUCH_FEATURE,
										EMFTypeCoreMessages
												.bind(
														EMFTypeCoreMessages.eContainmentFeature_no_such_feature_ERROR_,
														nextFeatureName), null);
					}
				}
			}
			containmentFeatures = (EReference[]) references
				.toArray(new EReference[] {});
		}
		return containmentFeatures;
	}

}