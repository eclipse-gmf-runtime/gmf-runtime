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

package org.eclipse.gmf.runtime.emf.type.core.internal.impl;

import org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeFactory;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.emf.type.core.IMetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.IMetamodelTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationType;
import org.eclipse.gmf.runtime.emf.type.core.ISpecializationTypeDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.MetamodelType;
import org.eclipse.gmf.runtime.emf.type.core.SpecializationType;

/**
 * Factory for hinted element types, which are specializations types that have a
 * semantic hint parameter.
 * 
 * @author ldamus
 */
public class HintedTypeFactory extends AbstractElementTypeFactory {

	/**
	 * The hinted type kind. This string is specified in the XML 'kind'
	 * attribute of any element type that is a hinted type.
	 */
	public static final String HINTED_TYPE_KIND = "org.eclipse.gmf.runtime.emf.core.internal.util.IHintedType"; //$NON-NLS-1$

	/**
	 * The semantic hint parameter name.
	 */
	private static final String SEMANTIC_HINT_PARAM_NAME = "semanticHint"; //$NON-NLS-1$

	/**
	 * The hinted specialization type class.
	 */
	private static final class HintedSpecializationType extends
			SpecializationType implements IHintedType {

		/**
		 * The semantic hint.
		 */
		private final String semanticHint;

		/**
		 * Constructs a new hinted type.
		 * 
		 * @param descriptor
		 *            the specialization type descriptor
		 * @param semanticHint
		 *            the semantic hint
		 */
		public HintedSpecializationType(ISpecializationTypeDescriptor descriptor,
				String semanticHint) {

			super(descriptor);
			this.semanticHint = semanticHint;
		}

		/**
		 * Gets the semantic hint.
		 */
		public String getSemanticHint() {
			return semanticHint;
		}
	}

	/**
	 * The hinted metamodel type class.
	 */
	private static final class HintedMetamodelType extends MetamodelType
			implements IHintedType {

		/**
		 * The semantic hint.
		 */
		private final String semanticHint;

		/**
		 * Constructs a new hinted type.
		 * 
		 * @param descriptor
		 *            the specialization type descriptor
		 * @param semanticHint
		 *            the semantic hint
		 */
		public HintedMetamodelType(IMetamodelTypeDescriptor descriptor,
				String semanticHint) {

			super(descriptor);
			this.semanticHint = semanticHint;
		}

		/**
		 * Gets the semantic hint.
		 */
		public String getSemanticHint() {
			return semanticHint;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.AbstractElementTypeFactory#createSpecializationType(org.eclipse.gmf.runtime.emf.type.core.internal.impl.SpecializationTypeDescriptor)
	 */
	public ISpecializationType createSpecializationType(
			ISpecializationTypeDescriptor descriptor) {

		String semanticHint = descriptor
				.getParamValue(SEMANTIC_HINT_PARAM_NAME);

		return new HintedSpecializationType(descriptor, semanticHint);
	}

	public IMetamodelType createMetamodelType(
			IMetamodelTypeDescriptor descriptor) {

		String semanticHint = descriptor
				.getParamValue(SEMANTIC_HINT_PARAM_NAME);

		return new HintedMetamodelType(descriptor, semanticHint);
	}

}