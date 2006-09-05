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

package org.eclipse.gmf.runtime.common.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;
import org.eclipse.gmf.runtime.common.core.service.Service.ProviderDescriptor;
import org.eclipse.gmf.runtime.common.core.util.EnumeratedType;
import org.eclipse.gmf.runtime.common.core.util.Log;

/**
 * An enumeration of provider execution strategies.
 * <P>
 * Each service provider has a <code>ProviderPriority</code> that is declared
 * in its extension descriptor. It is the
 * {@link org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy} that
 * determines how service provider priorities are used to select a provider to
 * service each client request. For example, if the
 * {@link org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy#FIRST} 
 * is used, the provider with the highest priority will give an answer to the
 * request.
 * 
 * @see org.eclipse.gmf.runtime.common.core.service
 * 
 * @author khussey
 * @canBeSeenBy %partners
 */
public abstract class ExecutionStrategy extends EnumeratedType {

	/**
	 * The list of pre-defined provider priorities.
	 */
	public static final ProviderPriority[] PRIORITIES =
		{
			ProviderPriority.HIGHEST,
			ProviderPriority.HIGH,
			ProviderPriority.MEDIUM,
			ProviderPriority.LOW,
			ProviderPriority.LOWEST };

	/**
	 * An internal unique identifier for provider execution strategies.
	 */
	private static int nextOrdinal = 0;

	/**
	 * Executes an operation on the first provider of the highest priority
	 * that provides the operation.
	 */
	public static final ExecutionStrategy FIRST =
		new ExecutionStrategy("First") { //$NON-NLS-1$
		
		private static final long serialVersionUID = 1L;

		public List execute(Service service, IOperation operation) {
			for (int i = 0; i < PRIORITIES.length; ++i) {
				List providers = service.getProviders(this, PRIORITIES[i], operation);

				if (providers.size() != 0) {
					return Collections.singletonList(operation.execute((IProvider) providers.get(0)));
				}
			}

			return Collections.EMPTY_LIST;
		}

		public List getUncachedProviders(
			Service service,
			ProviderPriority priority,
			IOperation operation) {

			List descriptors = service.getProviders(priority);
			int size = descriptors.size();

			for (int i = 0; i < size; ++i) {
				ProviderDescriptor descriptor = (ProviderDescriptor)descriptors.get(i);

				if (safeProvides(descriptor, operation)) {
					return Collections.singletonList(descriptor.getProvider());
				}
			}

			return Collections.EMPTY_LIST;
		}
	};

	/**
	 * Executes an operation on the last provider of the lowest priority
	 * that provides the operation.
	 */
	public static final ExecutionStrategy LAST =
		new ExecutionStrategy("Last") { //$NON-NLS-1$

		private static final long serialVersionUID = 1L;

		public List execute(Service service, IOperation operation) {
			for (int i = PRIORITIES.length; --i >= 0;) {
				List providers = service.getProviders(this, PRIORITIES[i], operation);
				int size = providers.size();

				if (size != 0) {
					return Collections.singletonList(
						operation.execute(
							(IProvider) providers.get(size - 1)));
				}
			}

			return Collections.EMPTY_LIST;
		}

		public List getUncachedProviders(
				Service service,
				ProviderPriority priority,
				IOperation operation) {

			List descriptors = service.getProviders(priority);

			for (int i = descriptors.size(); --i >= 0;) {
				ProviderDescriptor descriptor = (ProviderDescriptor)descriptors.get(i);

				if (safeProvides(descriptor, operation)) {
					return Collections.singletonList(descriptor.getProvider());
				}
			}

			return Collections.EMPTY_LIST;
		}
	};

	/**
	 * Executes an operation on all providers that provide the operation, in
	 * order from highest to lowest priority.
	 */
	public static final ExecutionStrategy FORWARD =
		new ExecutionStrategy("Forward") { //$NON-NLS-1$
		
		private static final long serialVersionUID = 1L;

		public List execute(Service service, IOperation operation) {
			List results = new ArrayList();

			for (int i = 0; i < PRIORITIES.length; ++i) {
				List providers = service.getProviders(this, PRIORITIES[i], operation);
				int size = providers.size();

				for (int j = 0; j < size; ++j) {
					results.add(operation.execute((IProvider) providers.get(j)));
				}
			}

			return results;
		}
	};

	/**
	 * Executes an operation on all providers that provide the operation, in
	 * reverse order from lowest to highest priority.
	 */
	public static final ExecutionStrategy REVERSE =
		new ExecutionStrategy("Reverse") { //$NON-NLS-1$
		
		private static final long serialVersionUID = 1L;

		public List execute(Service service, IOperation operation) {
			List results = new ArrayList();

			for (int i = PRIORITIES.length; --i >= 0;) {
				List providers = service.getProviders(this, PRIORITIES[i], operation);

				for (int j = providers.size(); --j >= 0;) {
					results.add(operation.execute((IProvider) providers.get(j)));
				}
			}

			return results;
		}
	};

	/**
	 * The list of values for this enumerated type.
	 */
	private static final ExecutionStrategy[] VALUES =
		{ FIRST, LAST, FORWARD, REVERSE };

	/**
	 * Constructs a new execution strategy with the specified name.
	 *
	 * @param name The name of the new execution strategy.
	 */
	protected ExecutionStrategy(String name) {
		super(name, nextOrdinal++);
	}

	/**
	 * Constructs a new execution strategy with the specified name and ordinal.
	 *
	 * @param name The name of the new execution strategy.
	 * @param ordinal The ordinal for the new execution strategy.
	 */
	protected ExecutionStrategy(String name, int ordinal) {
		super(name, ordinal);
	}

	/**
	 * Retrieves the list of constants for this enumerated type.
	 *
	 * @return The list of constants for this enumerated type.
	 */
	protected List getValues() {
		return Collections.unmodifiableList(Arrays.asList(VALUES));
	}

	/**
	 * Executes the specified operation on providers obtained from the
	 * specified service, according to this execution strategy.
	 *
	 * @param service The service from which to obtain the providers.
	 * @param operation The operation to be executed.
	 * @return The list of results.
	 */
	public abstract List execute(Service service, IOperation operation);

	/**
	 * Retrieves a list of providers of the specified priority that provide the
	 * specified operation.
	 * 
	 * @param service The service used by the strategy
	 * @param priority The priority of providers to be retrieved.
	 * @param operation The operation that the provides must provide.
	 * @return A list of uncached providers.
	 */
	public List getUncachedProviders(
		Service service,
		ProviderPriority priority,
		IOperation operation) {

		List descriptors = service.getProviders(priority);
		int size = descriptors.size();
		List providers = new ArrayList(size);

		for (int i = 0; i < size; ++i) {
			ProviderDescriptor descriptor = (ProviderDescriptor)descriptors.get(i);

			if (safeProvides(descriptor, operation)) {
				providers.add(descriptor.getProvider());
			}
		}

		return providers;
	}

	/**
	 * Retrieves a list of providers of the specified priority.
	 * 
	 * @param service The service used by the strategy
	 * @param priority The priority of providers to be retrieved.
	 * @return A list of providers of the specified priority.
	 */
	protected final List getProviders(Service service, ProviderPriority priority) {
		return service.getProviders(priority); 
	}

	/**
	 * Retrieves a list of providers of the specified priority that provide the
	 * specified operation. If this service is optimized, the result will be
	 * cached the first time it is retrieved. If caching is pessimistic, the 
	 * providers from the cache will be checked first. 
	 * 
	 * @param service The service used by the strategy
	 * @param strategy The strategy used by the service
	 * @param priority The priority of providers to be retrieved.
	 * @param operation The operation that the provides must provide.
	 * @return A list of providers (from the cache, if appropriate).
	 */
	protected final List getProviders(
		Service service,
		ExecutionStrategy strategy,
		ProviderPriority priority,
		IOperation operation) {
		return service.getProviders(strategy, priority, operation); 
	}
	
	/**
	 * Safely calls a provider's provides() method.
	 * 
	 * The provider must not be null.
	 * 
	 * Returns true if there were no exceptions thrown and the provides() method
	 * returns true.  Returns false if an exception was thrown or the provides()
	 * method returns false.
	 * 
	 * An entry is added to the log if the provider threw an exception.  
	 * 
	 * @param provider to safely execute the provides() method
	 * @param operation passed into the provider's provides() method
	 * @return true if there were no exceptions thrown and the provides() method
	 * returns true.  Returns false if an exception was thrown or the provides()
	 * method returns false.
	 */
	private static boolean safeProvides(IProvider provider, IOperation operation) {
		assert provider != null;
		
		try {
			return provider.provides(operation);
		}
		catch (Throwable e) {
			
			List ignoredProviders = Service.getIgnoredProviders();
			String providerClassName = provider.getClass().getName();
			
			if (!ignoredProviders.contains(providerClassName)) {
				// remember the ignored provider so that the error is only logged once per provider
				ignoredProviders.add(providerClassName);
				
				Log.log(
					CommonCorePlugin.getDefault(),
					IStatus.ERROR,
					CommonCoreStatusCodes.SERVICE_FAILURE,
					"Ignoring provider " + provider + " since it threw an exception in the provides() method",  //$NON-NLS-1$ //$NON-NLS-2$
					e);
			}
			return false;
		}
		
	}	
}
