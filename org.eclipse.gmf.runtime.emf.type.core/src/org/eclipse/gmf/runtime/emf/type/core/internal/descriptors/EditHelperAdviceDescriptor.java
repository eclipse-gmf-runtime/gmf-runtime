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
	 * Constructs a new container descriptor from the configuration element.
	 * 
	 * @param specialization
	 *            the configuration element
	 * @throws CoreException
	 *             when the configuration element does not contain the required
	 *             elements and attributes
	 */
	public EditHelperAdviceDescriptor(String editHelperAdviceName,
			SpecializationTypeDescriptor specialization)
		throws CoreException {

		this.editHelperAdviceName = editHelperAdviceName;
		this.specializationDescriptor = specialization;
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
	 * @see com.ibm.xtools.emf.msl.type.IEditHelperAdviceDescriptor#getMatcher()
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