/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.util.Assert;

import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.diagram.ui.services.decorator.CreateDecoratorsOperation;

/**
 * This service is used to add decorators so that shapes and connectors on a
 * diagram can be decorated with adornments.
 * 
 * @author cmahoney
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class DecoratorService
	extends Service
	implements IDecoratorProviderBase {

	/**
	 * A descriptor for <code>IDecoratorProvider</code> defined by a
	 * configuration element.
	 */
	protected static class ProviderDescriptor
		extends Service.ProviderDescriptor {

		/** the provider configuration parsed from XML */
		private DecoratorProviderConfiguration providerConfiguration;

		/**
		 * Constructs an <code>IDecoratorProvider</code> descriptor for the
		 * specified configuration element.
		 * 
		 * @param element
		 *            The configuration element describing the provider.
		 */
		public ProviderDescriptor(IConfigurationElement element) {
			super(element);

			this.providerConfiguration = DecoratorProviderConfiguration
				.parse(element);
			Assert.isNotNull(providerConfiguration);
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
		 * Checks if the operation is supported by the XML extension
		 * 
		 * @param operation
		 * @return true if the operation is supported by the XML extension,
		 *         false otherwise
		 */
		private boolean isSupportedInExtention(IOperation operation) {
			if (operation instanceof CreateDecoratorsOperation) {
				CreateDecoratorsOperation o = (CreateDecoratorsOperation) operation;

				return providerConfiguration.supports(o.getDecoratorTarget());
			}
			return false;
		}
	}

	/** The singleton instance of the decorator service. */
	private final static DecoratorService service = new DecoratorService();

	/**
	 * Retrieves the singleton instance of the decorator service.
	 * 
	 * @return The decorator service singleton.
	 */
	public static DecoratorService getInstance() {
		return service;
	}

	/**
	 * Executes a <code>CreateDecoratorsOperation</code> with a
	 * <code>REVERSE</code> execution strategy. This allows higher priority
	 * providers to install their decorators after lower priority providers so
	 * that their decorations will appear on top.
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorProvider#createDecorators(org.eclipse.gmf.runtime.diagram.ui.services.decorator.IDecoratorTarget)
	 */
	public void createDecorators(IDecoratorTargetBase decoratorTarget) {
		execute(ExecutionStrategy.REVERSE, new CreateDecoratorsOperation(
			decoratorTarget));
	}

	/**
	 * @see org.eclipse.gmf.runtime.common.core.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected Service.ProviderDescriptor newProviderDescriptor(
		IConfigurationElement element) {
		return new ProviderDescriptor(element);
	}
}