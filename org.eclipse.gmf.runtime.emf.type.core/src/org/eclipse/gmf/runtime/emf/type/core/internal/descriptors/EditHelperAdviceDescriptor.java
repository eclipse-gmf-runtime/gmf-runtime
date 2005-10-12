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

import org.eclipse.core.runtime.CoreException;

import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.emf.type.core.IContainerDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.IEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePlugin;
import org.eclipse.gmf.runtime.emf.type.core.internal.EMFTypePluginStatusCodes;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.ResourceManager;

/**
 * Descriptor for edit helper advice.
 * 
 * @author ldamus
 */
public class EditHelperAdviceDescriptor
	implements IEditHelperAdviceDescriptor {

	private final SpecializationTypeDescriptor specializationDescriptor;

	/**
	 * The edit helper advice.
	 */
	private IEditHelperAdvice editHelperAdvice;

	/**
	 * The class name of the edit helper advice.
	 */
	private String editHelperAdviceName;

	/**
	 * Constructs a new edit helper advice descriptor from the configuration element.
	 * 
	 * @param specialization
	 *            the configuration element
	 */
	public EditHelperAdviceDescriptor(String editHelperAdviceName,
			SpecializationTypeDescriptor specialization) {

		this.editHelperAdviceName = editHelperAdviceName;
		this.specializationDescriptor = specialization;
	}
	
	/**
	 * Constructs a new descriptor for the edit helper advice.
	 * 
	 * @param editHelperAdvice
	 *            the edit helper advice
	 * @param specialization
	 *            the configuration element
	 */
	public EditHelperAdviceDescriptor(IEditHelperAdvice editHelperAdvice,
			SpecializationTypeDescriptor specialization) {

		this(editHelperAdvice.getClass().getName(), specialization);
		this.editHelperAdvice = editHelperAdvice;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.internal.impl.IEditHelperAdviceDescriptor#getTypeId()
	 */
	public String getTypeId() {
		return specializationDescriptor.getId();
	}

	/**
	 * Gets the edit helper advice class name.
	 * 
	 * @return the edit helper advice class name.
	 */
	public String getEditHelperAdviceName() {
		return editHelperAdviceName;
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
					editHelperAdvice = (IEditHelperAdvice) specializationDescriptor
					.getConfigElement()
						.createExecutableExtension(
							ElementTypeXmlConfig.A_EDIT_HELPER_ADVICE);

				} catch (CoreException e) {
					Log
						.error(
							EMFTypePlugin.getPlugin(),
							EMFTypePluginStatusCodes.EDIT_HELPER_ADVICE_CLASS_NOT_FOUND,
							ResourceManager
								.getMessage(
									EMFTypePluginStatusCodes.EDIT_HELPER_ADVICE_CLASS_NOT_FOUND_KEY,
									new Object[] {editHelperAdviceName}), e);

					// Don't recompute the advice class if it has failed once.
					editHelperAdviceName = null;
				}
			}
		}
		return editHelperAdvice;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.internal.impl.IEditHelperAdviceDescriptor#getMatcher()
	 */
	public IElementMatcher getMatcher() {
		return specializationDescriptor.getMatcher();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.type.core.internal.impl.IEditHelperAdviceDescriptor#isAppliedToSubtypes()
	 */
	public AdviceBindingInheritance getInheritance() {
		return AdviceBindingInheritance.ALL;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.emf.type.core.internal.impl.IEditHelperAdviceDescriptor#getContainerDescriptor()
	 */
	public IContainerDescriptor getContainerDescriptor() {
		return specializationDescriptor.getContainerDescriptor();
	}
}