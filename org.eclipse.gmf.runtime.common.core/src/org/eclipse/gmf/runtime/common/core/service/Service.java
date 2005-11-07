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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreDebugOptions;
import org.eclipse.gmf.runtime.common.core.internal.CommonCorePlugin;
import org.eclipse.gmf.runtime.common.core.internal.CommonCoreStatusCodes;
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.gmf.runtime.common.core.util.Trace;

/**
 * 
 * A <code>Service</code> does some specific piece of work for clients by
 * delegating the actual work done to one or more service providers. Client
 * requests are made using {@link org.eclipse.gmf.runtime.common.core.service.IOperation}
 * s.
 * <P>
 * Modeling platform services should subclass this class.
 * <P>
 * Each service provider has a
 * {@link org.eclipse.gmf.runtime.common.core.service.ProviderPriority} that is
 * declared in its extension descriptor. It is the
 * {@link org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy} that
 * determines how service provider priorities are used to select a provider to
 * service each client request. For example, if the
 * {@link org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy#FIRST} 
 * is used, the provider with the highest priority will give an answer to the
 * request.
 * <P>
 * A <code>Service</code> may choose to have the following performance
 * optimizations:
 * <UL>
 * <LI>optimized, so that providers that provide for an operation are cached
 * the first time they are retrieved and the cache used when an operation is
 * executed. If the service is not optimized, all of the service providers may
 * be considered each time an operation is executed.</LI>
 * <LI>optmistic, so that an optimized service always trusts the contents of
 * its cache to contain providers that provide for the given operation. If the
 * optimized service is not optimistic, it double-checks the contents of the
 * cache to make sure that the cached providers still provide for the operation.
 * </LI>
 * </UL>
 * 
 * @see org.eclipse.gmf.runtime.common.core.service
 * 
 * @author khussey
 * @canBeSeenBy %partners
 */
public abstract class Service
	extends AbstractProvider
	implements IProvider, IProviderChangeListener {

	/**
	 * A descriptor for providers defined by a configuration element.
	 * 
	 * @author khussey
	 */
	public static class ProviderDescriptor
		extends AbstractProvider
		implements IProvider, IProviderChangeListener {

		/**
		 * The name of the 'class' XML attribute.
		 */
		protected static final String A_CLASS = "class"; //$NON-NLS-1$

		/**
		 * The name of the 'plugin' XML attribute.
		 * 
		 */
		protected static final String A_PLUGIN = "plugin"; //$NON-NLS-1$

		/**
		 * The name of the 'Policy' XML element.
		 */
		protected static final String E_POLICY = "Policy"; //$NON-NLS-1$

		/**
		 * The configuration element describing this descriptor's provider.
		 */
		private final IConfigurationElement element;

		/**
		 * The provider for which this object is a descriptor.
		 */
		protected IProvider provider;

		/**
		 * The policy associated with this descriptor's provider (if specified).
		 */
		protected IProviderPolicy policy;

		/**
		 * Constructs a new provider descriptor for the specified configuration
		 * element.
		 * 
		 * @param element The configuration element describing the provider.
		 */
		protected ProviderDescriptor(IConfigurationElement element) {
			super();
			this.element = element;
		}

		/**
		 * Retrieves the configuration element describing this descriptor's
		 * provider.
		 * 
		 * @return The configuration element describing this descriptor's
		 *         provider.
		 */
		protected final IConfigurationElement getElement() {
			return element;
		}

		/**
		 * Retrieves the provider for which this object is a descriptor.
		 * Lazy-initializes the value by instantiating the class described by
		 * this provider descriptor's configuration element.
		 * 
		 * @return The provider for which this object is a descriptor.
		 */
		public IProvider getProvider() {
			if (null == provider) {
				CommonCorePlugin corePlugin = CommonCorePlugin.getDefault();

				try {
					Log.info(corePlugin, CommonCoreStatusCodes.OK, "Activating provider '" + element.getAttribute(A_CLASS) + "'..."); //$NON-NLS-1$ //$NON-NLS-2$
					provider = (IProvider)element.createExecutableExtension(A_CLASS);
					provider.addProviderChangeListener(this);
					Trace.trace(corePlugin, CommonCoreDebugOptions.SERVICES_ACTIVATE, "Provider '" + provider + "' activated."); //$NON-NLS-1$ //$NON-NLS-2$
				} catch (CoreException ce) {
					Trace.catching(corePlugin, CommonCoreDebugOptions.EXCEPTIONS_CATCHING, getClass(), "getProvider", ce); //$NON-NLS-1$
					IStatus status = ce.getStatus();
					Log.log(
						corePlugin,
						status.getSeverity(),
						CommonCoreStatusCodes.SERVICE_FAILURE,
						status.getMessage(),
						status.getException());
				}
			}
			return provider;
		}

		/**
		 * Retrieves the policy associated with this descriptor's provider (if
		 * specified). Lazy-initializes the value by instantiating the class
		 * described by this provider descriptor's configuration element, if
		 * specified.
		 * 
		 * @return The policy associated with this descriptor's provider (if
		 *         specified).
		 */
		protected IProviderPolicy getPolicy() {
			if (null == policy) {
				IConfigurationElement[] elements = element.getChildren(E_POLICY);

				working: {
					if (elements.length == 0) 
						break working; // no child elements

					CommonCorePlugin corePlugin = CommonCorePlugin.getDefault();

					try {
						Log.info(corePlugin, CommonCoreStatusCodes.OK, "Activating provider policy '" + elements[0].getAttribute(A_CLASS) + "'..."); //$NON-NLS-1$ //$NON-NLS-2$

						// the following results in a core dump on Solaris if
						// the policy plug-in cannot be found
						
						policy = (IProviderPolicy)element.createExecutableExtension(E_POLICY);
						
						Trace.trace(corePlugin, CommonCoreDebugOptions.SERVICES_ACTIVATE, "Provider policy '" + policy + "' activated."); //$NON-NLS-1$ //$NON-NLS-2$
					} catch (CoreException ce) {
						Trace.catching(corePlugin, CommonCoreDebugOptions.EXCEPTIONS_CATCHING, getClass(), "getPolicy", ce); //$NON-NLS-1$
						IStatus status = ce.getStatus();
						Log.log(
							corePlugin,
							status.getSeverity(),
							CommonCoreStatusCodes.SERVICE_FAILURE,
							status.getMessage(),
							status.getException());
					}
				}
			}

			return policy;
		}

		/**
		 * Indicates whether this provider descriptor can provide the
		 * functionality described by the specified <code>operation</code>.
		 * 
		 * @param operation
		 *            The operation in question.
		 * @return <code>true</code> if this descriptor's policy or provider
		 *         provides the operation; <code>false</code> otherwise.
		 */
		public boolean provides(IOperation operation) {
			IProviderPolicy thePolicy = getPolicy();

			if (null != thePolicy) {
				try {
					return thePolicy.provides(operation);
				}
				catch (Exception e) {
					Log.log(
						CommonCorePlugin.getDefault(),
						IStatus.ERROR,
						CommonCoreStatusCodes.SERVICE_FAILURE,
						"Ignoring provider since policy " + thePolicy + " threw an exception in the provides() method",  //$NON-NLS-1$ //$NON-NLS-2$
						e);
					return false;
				}
			}

			IProvider theProvider = getProvider();

			return (theProvider != null) ?
				safeProvides(theProvider, operation) : false;
		}

		/**
		 * Handles an event indicating that a provider has changed.
		 * 
		 * @param event The provider change event to be handled.
		 */
		public void providerChanged(ProviderChangeEvent event) {
			fireProviderChange(event);
		}

	}

	/**
	 * A pattern for error messages indicating an invalid XML element.
	 * 
	 */
	protected static final String INVALID_ELEMENT_MESSAGE_PATTERN = "Invalid XML element ({0})."; //$NON-NLS-1$

	/**
	 * The name of the 'name' XML attribute.
	 */
	private static final String A_NAME = "name"; //$NON-NLS-1$

	/**
	 * The name of the 'Priority' XML element.
	 */
	private static final String E_PRIORITY = "Priority"; //$NON-NLS-1$

	/**
	 * The size of a cache which is indexed by {@link ProviderPriority} ordinals.
	 */
	private static final int priorityCount;

	// Initialize priorityCount.
	static {
		// any priority will do to get the list of values
		List priorities = ProviderPriority.HIGHEST.getValues();
		int maxOrdinal = 0;

		for (Iterator i = priorities.iterator(); i.hasNext();) {
			int ordinal = ((ProviderPriority) i.next()).getOrdinal();

			if (maxOrdinal < ordinal)
				maxOrdinal = ordinal;
		}

		priorityCount = maxOrdinal + 1;
	}

	/**
	 * The cache of providers (for optimization) indexed by
	 * {@link ProviderPriority} ordinals.
	 */
	private final Map[] cache;

	/**
	 * The lists of registered providers.
	 */
	private final ArrayList[] providers;
	
	/**
	 * Whether the service uses optimistic caching.
	 */
	private final boolean optimistic;

	/**
	 * Constructs a new service that is not optimized.
	 */
	protected Service() {
		this(false);
	}

	/**
	 * Constructs a new service that is (not) optimized as specified.
	 * <P>
	 * If the service is optimized, the service providers that provide for an
	 * operation are cached the first time they are retrieved. When an operation
	 * is executed, this cache is used to find the service providers for the
	 * execution. If the service is not optimized, all of the service providers
	 * may be considered each time an operation is executed.
	 * 
	 * @param optimized
	 *            <code>true</code> if the new service is optimized,
	 *            <code>false</code> otherwise.
	 */
	protected Service(boolean optimized) {
		this(optimized, true);
	}

	/**
	 * Constructs a new service that is (not) optimized as specified.
	 * <P>
	 * If the service is optimized, the service providers that provide for an
	 * operation are cached the first time they are retrieved. When an operation
	 * is executed, this cache is used to find the service providers for the
	 * execution. If the service is not optimized, all of the service providers
	 * may be considered each time an operation is executed.
	 * <P>
	 * If the optimized service is optimistic, it always trusts the contents of
	 * its cache to contain providers that provide for the given operation. If
	 * the optimized service is not optimistic, it double-checks the contents of
	 * the cache to make sure that the cached providers still provide for the
	 * operation.
	 * <P>
	 * The value of <code>optimistic</code> is meaningless if
	 * <code>optimized</code> is false.
	 * 
	 * @param optimized
	 *            <code>true</code> if the new service is optimized,
	 *            <code>false</code> otherwise.
	 * @param optimistic
	 *            <code>true</code> if the new service uses optmistic caching,
	 *            <code>false</code> otherwise.
	 */
	protected Service(boolean optimized, boolean optimistic) {
		super();

		if (optimized) {
			cache = new Map[priorityCount];

			for (int ordinal = priorityCount; --ordinal >= 0;) {
				cache[ordinal] = createPriorityCache();
			}
		} else {
			cache = null;
		}
		this.optimistic = optimistic;

		providers = new ArrayList[priorityCount];

		for (int ordinal = priorityCount; --ordinal >= 0;)
			providers[ordinal] = new ArrayList(0);
	}

	/**
	 * Creates a map for caching service providers keyed by
	 * the values returned in {@link #getCachingKey(IOperation)}.
	 * 
	 * @return the new map
	 */
	protected Map createPriorityCache() {
		return new WeakHashMap();
	}
	
	/**
	 * Gets the key used to cache service providers that provide for
	 * <code>operation</code> in the map created by
	 * {@link #createPriorityCache()}.
	 * 
	 * @param operation <code>IOperation</code> for which the key will be retrieved
	 * @return the key into the service providers cache
	 */
	protected Object getCachingKey(IOperation operation) {
		return operation;
	}

	/**
	 * Answers whether or not this service is optimized by caching its service
	 * providers.
	 * <P>
	 * If the service is optimized, the service providers that provide for an
	 * operation are cached the first time they are retrieved. When an operation
	 * is executed, this cache is used to find the service providers for the
	 * execution. If the service is not optimized, all of the service providers
	 * may be considered each time an operation is executed.
	 * 
	 * @return <code>true</code> if the new service is optimized,
	 *         <code>false</code> otherwise.
	 */
	protected final boolean isOptimized() {
		return null != cache;
	}

	/**
	 * Answers whether or not this service uses optimistic caching. This value
	 * is only meaningful if {@link #isOptimized()}returns <code>true</code>.
	 * <P>
	 * If the optimized service is optimistic, it always trusts the contents of
	 * its cache to contain providers that provide for the given operation. If
	 * the optimized service is not optimistic, it double-checks the contents of
	 * the cache to make sure that the cached providers still provide for the
	 * operation.
	 * 
	 * @return <code>true</code> if the new service uses optmistic caching,
	 *         <code>false</code> otherwise.
	 */
	protected final boolean isOptimistic() {
		return optimistic;
	}

	/**
	 * Clears the service provider cache (if this service is optimized).
	 */
	protected final void clearCache() {
		if (null != cache) {
			for (int ordinal = priorityCount; --ordinal >= 0;) {
				cache[ordinal].clear();
			}
		}
	}

	/**
	 * Retrieves a complete list of all the providers registered with this
	 * service that have the specified <code>priority</code>.
	 * <P>
	 * This method does not consider the optimized state of the service.
	 * @param priority
	 *            The priority of providers to be retrieved.
	 * @return A complete list of providers of the specified priority.
	 */
	final List getProviders(ProviderPriority priority) {
		return providers[priority.getOrdinal()];
	}

	/**
	 * Retrieves a list of providers of the specified <code>priority</code>
	 * that provide for the specified <code>operation</code>.
	 * <P>
	 * If the service is optimized, the result will be cached the first time it
	 * is retrieved. If caching is not optimistic, the providers from the cache
	 * will be asked again if they still provide for the operation.
	 * 
	 * @param strategy
	 *            The strategy used by the service.
	 * @param priority
	 *            The priority of providers to be retrieved.
	 * @param operation
	 *            The operation that the provides must provide.
	 * @return A list of providers that provide for the operation (from the
	 *         cache, if appropriate).
	 */
	protected final List getProviders(
		ExecutionStrategy strategy,
		ProviderPriority priority,
		IOperation operation) {

		assert null != priority : "getProviders received null priority as argument"; //$NON-NLS-1$
		assert null != operation : "getproviders received null operation as argument"; //$NON-NLS-1$

		List providerList;

		if (null == cache) {
			providerList = strategy.getUncachedProviders(this, priority, operation);
		} else {
			Object cachingKey = getCachingKey(operation);
			Map map = cache[priority.getOrdinal()];
			providerList = (List)map.get(cachingKey);

			if (null != providerList) {
				if (optimistic)
					return providerList;

				int n = providerList.size();

				if (n != 0) {
					for (int i = 0;;) {
						IProvider provider = (IProvider)providerList.get(i);

						if (!safeProvides(provider, operation))
							break;

						if (++i == n)
							return providerList;
					}
				}
			}

			providerList = strategy.getUncachedProviders(this, priority, operation);
			map.put(cachingKey, providerList);
		}

		return providerList;
	}
	
	/**
	 * Retrieves a list of all providers of all priorities for this service.
	 * 
	 * @return A list of all providers of all priorities.
	 */
	protected final List getAllProviders() {
		int i;
		int n = priorityCount;
		int total;

		for (i = n, total = 0; --i >= 0;)
			total += providers[i].size();

		List allProviders = new ArrayList(total);

		for (i = 0; i < n; ++i)
			allProviders.addAll(providers[i]);

		return allProviders;
	}

	/**
	 * Registers the <code>provider</code> as a provider for this service,
	 * with the specified <code>priority</code>.
	 * 
	 * @param priority
	 *            The priority at which to add the provider.
	 * @param provider
	 *            The provider to be added.
	 */
	protected final void addProvider(
		ProviderPriority priority,
		ProviderDescriptor provider) {

		assert null != priority : "null ProviderPriority"; //$NON-NLS-1$
		assert null != provider : "null ProviderDescriptor"; //$NON-NLS-1$

		int ordinal = priority.getOrdinal();

		if (null != cache) {
			cache[ordinal].clear();
		}

		providers[ordinal].add(provider);
		provider.addProviderChangeListener(this);
	}

	/**
	 * Removes the <code>provider</code> as a provider for this service.
	 * 
	 * @param provider
	 *            The provider to be removed.
	 */
	protected final void removeProvider(ProviderDescriptor provider) {
		assert null != provider : "null provider"; //$NON-NLS-1$
		
		for (int i = 0, n = priorityCount; i < n; ++i) {
			if (providers[i].remove(provider)) {
				provider.removeProviderChangeListener(this);
				clearCache();
				break;
			}
		}
	}

	/**
	 * Executes the <code>operation</code> based on the specified execution
	 * <code>strategy</code>.
	 * 
	 * @param strategy
	 *            The execution strategy to use.
	 * @param operation
	 *            The operation to be executed.
	 * @return The list of results.
	 */
	protected final List execute(
		ExecutionStrategy strategy,
		IOperation operation) {

		assert null != strategy : "null strategy"; //$NON-NLS-1$
		assert null != operation : "null operation"; //$NON-NLS-1$

		List results = strategy.execute(this, operation);
		
		if (Trace.shouldTrace(CommonCorePlugin.getDefault(), CommonCoreDebugOptions.SERVICES_EXECUTE)) {
			Trace.trace(
					CommonCorePlugin.getDefault(),
					CommonCoreDebugOptions.SERVICES_EXECUTE,
					"Operation '" + operation + "' executed using strategy '" + strategy + "'."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}

		return results;
	}

	/**
	 * Executes the <code>operation</code> based on the specified execution
	 * <code>strategy</code>. If the result is a single object, return it.
	 * Otherwise return <code>null</code>.
	 * 
	 * @param strategy
	 *            The execution strategy to use.
	 * @param operation
	 *            The operation to be executed.
	 * @return The unique result.
	 */
	protected final Object executeUnique(
			ExecutionStrategy strategy,
			IOperation operation) {

		List results = execute(strategy, operation);

		return results.size() == 1 ? results.get(0) : null;
	}

	/**
	 * Indicates whether or not this service can provide the functionality
	 * described by the specified <code>operation</code>.
	 * <P>
	 * This method does not consider the optimized state of the service. All of
	 * the providers registered with the service are consulted to determine if
	 * they provide for the operation.
	 * 
	 * @param operation
	 *            The operation that describes the requested functionality.
	 * @return <code>true</code> if any of this service's providers provide
	 *         the operation; <code>false</code> otherwise.
	 */
	public final boolean provides(IOperation operation) {
		assert null != operation : "null operation passed to provides(IOperation)"; //$NON-NLS-1$

		for (int priority = 0, n = priorityCount; priority < n; ++priority)
		{
			List providerList = providers[priority];
			int providerCount = providerList.size();

			for (int provider = 0; provider < providerCount; ++provider)
				if (safeProvides(((IProvider)providerList.get(provider)), operation))
					return true;

		}

		return false;
	}

	/**
	 * Indicates whether or not this service can provide the functionality
	 * described by the specified <code>operation</code> using the given
	 * execution <code>strategy</code>.
	 * <P>
	 * This method considers the optimized state of the service. If the service
	 * is optimized, it will consult only those providers that have been cached.
	 * 
	 * @param operation
	 *            The operation in question.
	 * @param strategy
	 *            The strategy to be used.
	 * @return <code>true</code> if any of this service's providers provide
	 *         the operation; <code>false</code> otherwise.
	 */
	protected final boolean provides(ExecutionStrategy strategy, IOperation operation) {
		assert null != strategy : "null strategy";  //$NON-NLS-1$
		assert null != operation : "null operation"; //$NON-NLS-1$

		for (int i = 0; i < ExecutionStrategy.PRIORITIES.length; ++i) {
			ProviderPriority priority = ExecutionStrategy.PRIORITIES[i];
			List providerList = getProviders(strategy, priority, operation);
			int providerCount = providerList.size();

			for (int provider = 0; provider < providerCount; ++provider)
				if (safeProvides (((IProvider)providerList.get(provider)), operation))
					return true;
		}

		return false;
	}

	/**
	 * Handles an event indicating that a provider has changed.
	 * 
	 * @param event
	 *            The provider change event to be handled.
	 */
	public final void providerChanged(ProviderChangeEvent event) {
		assert null != event : "null event"; //$NON-NLS-1$

		event.setSource(this);
		fireProviderChange(event);
	}

	/**
	 * Registers the service providers described by the extensions of the
	 * specified namespace and extension point name with this service.
	 *
	 * @param namespace the namespace for the given extension point 
	 *		(e.g. <code>"org.eclipse.gmf.runtime.common.core"</code>)
	 * @param extensionPointName the simple identifier of the 
	 *		extension point (e.g. <code>"parserProviders"</code>)
	 */
	public final void configureProviders(String namespace, String extensionPointName) {
		configureProviders(Platform.getExtensionRegistry()
									.getExtensionPoint(namespace, extensionPointName)
									.getConfigurationElements());
	}

	/**
	 * Registers the service providers described by the specified configuration
	 * <code>elements</code> with this service.
	 * 
	 * @param elements
	 *            The configuration elements describing the providers.
	 */
	public final void configureProviders(IConfigurationElement[] elements) {
		assert null != elements : "null elements"; //$NON-NLS-1$

		for (int i = 0; i < elements.length; ++i)
		{
			IConfigurationElement element = elements[i];

			try
			{
				addProvider(ProviderPriority.parse(getPriority(element)),
						newProviderDescriptor(element));
			}
			finally
			{
				if (Trace.shouldTrace(CommonCorePlugin.getDefault(), CommonCoreDebugOptions.SERVICES_CONFIG))
				{
					IExtension extension = element.getDeclaringExtension();
					String identifier = extension.getUniqueIdentifier();

					if (identifier == null)
						identifier = String.valueOf(extension.getNamespace());

					extension.getExtensionPointUniqueIdentifier();

					Trace.trace(CommonCorePlugin.getDefault(), CommonCoreDebugOptions.SERVICES_CONFIG,
							"Provider of '" + extension.getExtensionPointUniqueIdentifier() //$NON-NLS-1$
								+ "' configured from extension '" + identifier + "'."); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		}

		for (int i = priorityCount; --i >= 0;)
			providers[i].trimToSize();
	}

	/**
	 * Get the priority of the Provider's configuration element
	 * 
	 * @param element
	 *            The configuration elements describing the provider.
	 * @return the priority of the specified configuration element
	 */
	public String getPriority(IConfigurationElement element) {
		return element.getChildren(E_PRIORITY)[0].getAttribute(A_NAME);
	}

	/**
	 * Creates a new provider descriptor for the specified configuration
	 * <code>element</code>.
	 * 
	 * @param element
	 *            The configuration element from which to create the descriptor.
	 * @return A new provider descriptor.
	 */
	protected ProviderDescriptor newProviderDescriptor(IConfigurationElement element) {
		return new ProviderDescriptor(element);
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
		catch (Exception e) {
			Log.log(
				CommonCorePlugin.getDefault(),
				IStatus.ERROR,
				CommonCoreStatusCodes.SERVICE_FAILURE,
				"Ignoring provider " + provider + " since it threw an exception in the provides() method", //$NON-NLS-1$ //$NON-NLS-2$
				e);
			return false;
		}
		
	}

}
