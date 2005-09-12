/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.internal.services.semantic;

import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * A service for manipulating the semantic model by returning commands
 * 
 * @author melaasar
 * 
 * @deprecated Use the Element Type API
 *             {@link org.eclipse.gmf.runtime.emf.type.core.IElementType#getEditCommand(IEditCommandRequest)}
 *             to get semantic commands.
 */
public class SemanticService extends Service implements ISemanticProvider {

	/**
	 * A descriptor for <code>ISemanticProvider</code> defined
	 * by a configuration element.
	 */
	protected static class ProviderDescriptor
		extends Service.ProviderDescriptor {

		/** the provider configuration parsed from XML */
		private SemanticProviderConfiguration providerConfiguration;

		/**
		 * Constructs a <code>ISemanticProvider</code> descriptor for
		 * the specified configuration element.
		 * 
		 * @param element The configuration element describing the provider.
		 */
		public ProviderDescriptor(IConfigurationElement element) {
			super(element);

			this.providerConfiguration =
				SemanticProviderConfiguration.parse(element);
			assert null != providerConfiguration : "Null providerConfiguration in ProviderDescriptor";//$NON-NLS-1$			
		}

		/**
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			if (getPolicy() != null)
				return getPolicy().provides(operation);
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
			if (operation instanceof GetCommandOperation) {
				GetCommandOperation o = (GetCommandOperation) operation;
				SemanticRequest request = o.getSemanticRequest();

				return providerConfiguration.supports(request);
			}
			return false;
		}

	}

	/**
	 * The singleton instance of the semantic service.
	 */
	private final static SemanticService instance = new SemanticService();

	/**
	 * Retrieves the singleton instance of the semantic service.
	 * 
	 * @return The semantic service singleton.
	 */
	public static SemanticService getInstance() {
		return instance;
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
		IConfigurationElement element) {
		return new ProviderDescriptor(element);
	}

	/**
	 * Executes the specified operation using the FIRST execution
	 * strategy.
	 * @return The result of executing the model operation.
	 * @param operation The model operation to be executed.
	 */
	private Object execute(IOperation operation) {
		List results = execute(ExecutionStrategy.FIRST, operation);
		return results.isEmpty() ? null : results.get(0);
	}
	
	/**
	 * @see org.eclipse.gmf.runtime.diagram.core.internal.services.semantic.ISemanticProvider#getCommand(SemanticRequest, ModelOperationContext)
	 */
	public ICommand getCommand(
		SemanticRequest semanticRequest) {
		return (ICommand) execute(
			new GetCommandOperation(semanticRequest));
	}
	
}
