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


package org.eclipse.gmf.examples.runtime.common.service.application;

import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;

/**
 * Widget Service class. Service which provides the creation of Widgets. 
 *   
 * @see org.eclipse.gmf.runtime.common.core.service
 */
public class WidgetService
	extends Service
	implements IWidgetProvider {

	/**
	 * The singleton instance of the WidgetService.
	 */
	private final static WidgetService service = new WidgetService();

	/**
	 * Retrieves the singleton instance of the WidgetService.
	 *
	 * @return The WidgetService service singleton.
	 */
	public static WidgetService getInstance() {
		return service;
	}

	/**
	 * Returns a <code>List</code> containing the Widgets requested or null if no Widget
	 * providers are found that can create orderSize Widgets.
	 * @see com.ibm.xtools.widgetapp.service.IWidgetProvider#createWidget(int)
	 */
	public Object createWidget(int orderSize) {
		return execute(new CreateWidgetOperation(orderSize));
		
	}	
	
	/**
	 * Executes the operation using a <code>ExecutingStrategy.FIRST</code> 
	 * @param operation the operation to execute
	 * @return the results of the operation
	 */
	public Object execute(IOperation operation) {
		 List results = execute(ExecutionStrategy.FIRST, operation);
		 return results.isEmpty() ? null : results;
	}
	
	/**
	 * Creates a new ProvderDescriptor based on the <code>IConfigurationElement</code>.
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(IConfigurationElement element) {
		return new WidgetProviderDescriptor(element);
	}	
	
	/**
	 * WidgetProviderDescriptor. Provides convenient access to 
	 * WidgetProvider configuration information.
	 *
	 */
	protected static class WidgetProviderDescriptor
		extends Service.ProviderDescriptor {

		/** the provider configuration parsed from XML */
		private WidgetServiceProviderConfiguration providerConfiguration;

		/**
		 * Constructs a <code>IWidgetProvider</code> descriptor for
		 * the specified configuration element.
		 * 
		 * @param element The configuration element describing the provider.
		 */
		public WidgetProviderDescriptor(IConfigurationElement element) {
			super(element);

			this.providerConfiguration =
				WidgetServiceProviderConfiguration.parse(element);
			assert null != element : "NULL configuration element"; //$NON-NLS-1$
		}

		/**
		 * Returns <code>true</code> if the given ProviderDescriptor
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
		 * Checks if the operation is supported by the XML extension
		 * @param operation
		 * @return
		 */
		private boolean isSupportedInExtention(IOperation operation) {
			if (operation instanceof CreateWidgetOperation) {
				return providerConfiguration.supports(((CreateWidgetOperation)operation).getOrderSize());
			}
			return false;
		}
	}	
}
