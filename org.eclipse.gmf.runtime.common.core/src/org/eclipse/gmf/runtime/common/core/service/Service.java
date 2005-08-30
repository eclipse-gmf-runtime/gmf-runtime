/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
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
	protected static class ProviderDescriptor
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
					Log.info(corePlugin, CommonCoreStatusCodes.OK, "Activating provider '" + getElement().getAttribute(A_CLASS) + "'..."); //$NON-NLS-1$ //$NON-NLS-2$
					provider =
						(IProvider) getElement().createExecutableExtension(
							A_CLASS);
					provider.addProviderChangeListener(this);
					Trace.trace(corePlugin, CommonCoreDebugOptions.SERVICES_ACTIVATE, "Provider '" + String.valueOf(provider) + "' activated."); //$NON-NLS-1$ //$NON-NLS-2$
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
				IConfigurationElement[] elements =
					getElement().getChildren(E_POLICY);

				working: {
					if (elements.length == 0) 
						break working; // no child elements

					String pluginId = elements[0].getAttribute(A_PLUGIN);

					if (null == pluginId)
						break working; // no child elements

					if (null != Platform.getBundle(pluginId)) {
						CommonCorePlugin corePlugin = CommonCorePlugin.getDefault();

						try {
							Log.info(corePlugin, CommonCoreStatusCodes.OK, "Activating provider policy '" + elements[0].getAttribute(A_CLASS) + "'..."); //$NON-NLS-1$ //$NON-NLS-2$
	
							// the following results in a core dump on Solaris if
							// the policy plug-in cannot be found
							policy =
								(IProviderPolicy) getElement()
									.createExecutableExtension(
									E_POLICY);

							Trace.trace(corePlugin, CommonCoreDebugOptions.SERVICES_ACTIVATE, "Provider policy '" + String.valueOf(policy) + "' activated."); //$NON-NLS-1$ //$NON-NLS-2$
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

			if (null != thePolicy)
				return thePolicy.provides(operation);

			IProvider theProvider = getProvider();

			return null != theProvider && theProvider.provides(operation);
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
	private static final int cacheSize;

	// Initialize the cacheSize.
	static {
		// any priority will do to get the list of values
		List priorities = ProviderPriority.HIGHEST.getValues();
		int maxOrdinal = 0;

		for (Iterator i = priorities.iterator(); i.hasNext();) {
			int ordinal = ((ProviderPriority) i.next()).getOrdinal();

			if (maxOrdinal < ordinal)
				maxOrdinal = ordinal;
		}

		cacheSize = maxOrdinal + 1;
	}

	/**
	 * The cache of providers (for optimization) indexed by
	 * {@link ProviderPriority} ordinals.
	 */
	private final Map[] cache;

	/**
	 * The list of registered providers.
	 */
	private final Map providers = new HashMap();
	
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
			cache = new Map[cacheSize];

			for (int ordinal = cacheSize; --ordinal >= 0;) {
				cache[ordinal] = createPriorityCache();
			}
		} else {
			cache = null;
		}
		this.optimistic = optimistic;
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
			for (int ordinal = cacheSize; --ordinal >= 0;) {
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
	List getProviders(ProviderPriority priority) {
		List result = (List) providers.get(priority);

		if (null == result) {
			result = new ArrayList();
			providers.put(priority, result);
		}

		return result;
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

		if (!isOptimized()) {
			providerList = strategy.getUncachedProviders(this, priority, operation);
		} else {
			Map map = cache[priority.getOrdinal()];
			providerList = (List) map.get(getCachingKey(operation));

			if (!isOptimistic() && null != providerList) {
				if (providerList.isEmpty()) {
					providerList = null;
				} else {
					for (Iterator i = providerList.iterator(); i.hasNext();) {
						IProvider provider = (IProvider) i.next();
						if (!provider.provides(operation)) {
							providerList = null;
							break;
						}
					}
				}
			}
			
			if (null == providerList) {
				providerList = strategy.getUncachedProviders(this, priority, operation);
				map.put(getCachingKey(operation), providerList);
			}
		}

		return providerList;
	}
	
	/**
	 * Retrieves a list of all providers of all priorities for this service.
	 * 
	 * @return A list of all providers of all priorities.
	 */
	protected final List getAllProviders() {
		List allProviders = new ArrayList();

		for (Iterator i = providers.values().iterator(); i.hasNext();) {
			allProviders.addAll((List)i.next());
		}
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
		
		if (null != cache) {
			cache[priority.getOrdinal()].clear();
		}

		getProviders(priority).add(provider);
		provider.addProviderChangeListener(this);
	}

	/**
	 * Removes the <code>provider</code> as a provider for this service.
	 * 
	 * @param provider
	 *            The provider to be removed.
	 */
	protected final void removeProvider(ProviderDescriptor provider) {
		assert null!= provider : "null provider"; //$NON-NLS-1$
		
		for (Iterator i = providers.values().iterator(); i.hasNext();) {
			if (((List) i.next()).remove(provider)) {
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
					"Operation '" + String.valueOf(operation) + "' executed using strategy '" + String.valueOf(strategy) + "'."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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

		for (Iterator list = providers.values().iterator(); list.hasNext();) {
			for (Iterator provider = ((List) list.next()).iterator(); provider.hasNext();) {
				if (((IProvider) provider.next()).provides(operation)) {
					return true;
				}
			}
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

		for (int i = 0; i < ExecutionStrategy.PRIORITIES.length; i++) {
			ProviderPriority priority = ExecutionStrategy.PRIORITIES[i];
			List providerList = getProviders(strategy, priority, operation);
			for (Iterator provider = providerList.iterator(); provider.hasNext();) {
				if (((IProvider) provider.next()).provides(operation)) {
					return true;
				}
			}
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
	 * Registers the service providers described by the specified configuration
	 * <code>elements</code> with this service.
	 * 
	 * @param elements
	 *            The configuration elements describing the providers.
	 */
	public final void configureProviders(IConfigurationElement[] elements) {
		assert null != elements : "null elements"; //$NON-NLS-1$

		for (int i = 0; i < elements.length; i++) {
			addProvider(
				ProviderPriority.parse(
					getPriority(elements[i])),
				newProviderDescriptor(elements[i]));
			Trace.trace(CommonCorePlugin.getDefault(), CommonCoreDebugOptions.SERVICES_CONFIG, "Provider configured from extension '" + String.valueOf(elements[i].getDeclaringExtension()) + "'."); //$NON-NLS-1$ //$NON-NLS-2$
		}
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

}
