/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.services.editpolicy;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.diagram.ui.internal.DiagramUIPlugin;
import org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.CreateEditPoliciesOperation;
import org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.IEditPolicyProvider;

/**
 * This service is used to install editpolicies on editparts.
 * 
 * @author chmahone
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class EditPolicyService extends Service implements IEditPolicyProvider {

	/**
	 * A descriptor for <code>ISemanticProvider</code> defined
	 * by a configuration element.
	 */
	protected static class ProviderDescriptor
		extends Service.ProviderDescriptor {

		/** the provider configuration parsed from XML */
		private EditPolicyProviderConfiguration providerConfiguration;

		/**
		 * Constructs an <code>IEditPolicyProvider</code> descriptor for
		 * the specified configuration element.
		 * 
		 * @param element The configuration element describing the provider.
		 */
		public ProviderDescriptor(IConfigurationElement element) {
			super(element);

			this.providerConfiguration =
				EditPolicyProviderConfiguration.parse(element);
			Assert.isNotNull(providerConfiguration);
		}

		/**
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			if (!policyInitialized){
				policy = getPolicy();
				policyInitialized = true;
			}
			if (policy != null)
				return policy.provides(operation);
			if (provider == null) {
				if (isSupportedInExtention(operation)) {
					providerConfiguration = null;
					return getProvider().provides(operation);
				}
				return false;
			}
			return getProvider().provides(operation);
		}

		/**
		 * Cheks if the operation is supported by the XML extension
		 * @param operation
		 * @return
		 */
		private boolean isSupportedInExtention(IOperation operation) {
			CreateEditPoliciesOperation o =
				(CreateEditPoliciesOperation) operation;

			return providerConfiguration.supports(o.getEditPart());
		}
		
		/** 
		 * the default implementation is overriden here to make it easier to debug
		 * XML providers, now when you select the ProviderDescriptor in the debug
		 * window the provider class name will be displayed
		 * @return the provider class name
		 */
		public String toString() {
			return getElement().getAttribute("class"); 	 //$NON-NLS-1$
	}
	}

	/** The singleton instance of the editpolicy service. */
	private final static EditPolicyService service = new EditPolicyService();
	
	static {
		service.configureProviders(DiagramUIPlugin.getPluginId(), "editpolicyProviders"); //$NON-NLS-1$
	}

	/**
	 * Retrieves the singleton instance of the editpolicy service.
	 * @return The editpolicy service singleton.
	 */
	public static EditPolicyService getInstance() {
		return service;
	}

	/**
	 * Executes the specified operation using the REVERSE execution strategy.
	 * @param operation The model operation to be executed.
	 */
	private void execute(IOperation operation) {
		execute(ExecutionStrategy.REVERSE, operation);
	}

	/**
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.editpolicy.IEditPolicyProvider#createEditPolicies(org.eclipse.gef.EditPart)
	 */
	public void createEditPolicies(EditPart editPart) {
		execute(new CreateEditPoliciesOperation(editPart));
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
		IConfigurationElement element) {
		return new ProviderDescriptor(element);
	}
}
