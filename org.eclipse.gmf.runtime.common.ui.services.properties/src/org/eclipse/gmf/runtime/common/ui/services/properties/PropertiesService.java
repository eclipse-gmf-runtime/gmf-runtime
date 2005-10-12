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

package org.eclipse.gmf.runtime.common.ui.services.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gmf.runtime.common.core.service.ExecutionStrategy;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.ui.services.properties.internal.PSFCommonUIPlugin;
import org.osgi.framework.Bundle;

/**
 * The property service taks all property contributions from property source
 * providers and assembles these properties into a property source object.
 * 
 * @author Tauseef A. Israr
 * @canBeSeenBy %partners 
 */
public class PropertiesService
	extends Service
	implements IPropertiesProvider {

	/**
	 * A descriptor for providers defined by a configuration element.
	 * 
	 * @author Natalia Balaba
	 */
	protected static class PropertiesProviderDescriptor
		extends Service.ProviderDescriptor {

		private static final String A_PLUGIN_LOADED = "verifyPluginLoaded"; //$NON-NLS-1$

		/**
		 * Create a instance of the <code>PropertiesProviderDescriptor</code>
		 * given the properties provider configuration element.
		 * 
		 * @param element -
		 *            a property provider configuration element
		 */
		protected PropertiesProviderDescriptor(IConfigurationElement element) {
			super(element);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gmf.runtime.common.core.service.IProvider#provides(org.eclipse.gmf.runtime.common.core.service.IOperation)
		 */
		public boolean provides(IOperation operation) {
			String attr_value = getElement().getAttribute(A_PLUGIN_LOADED);
			Boolean pluginLoadedVerify = Boolean.valueOf(attr_value);

			// instead of the policy we
			// use plugin loaded test
			if (pluginLoadedVerify.booleanValue() && !isPluginLoaded())
				return false;

			// the provider does not care if the plugin is loaded - use its
			// provides() test
			IProvider theProvider = getProvider();
			return null != theProvider && theProvider.provides(operation);

		}

		/*
		 * Verify if the declaring pluging of the propety provider is loaded.
		 * 
		 * @return - true if the declaring pluging of the propety provider is
		 * loaded, false otherwise
		 */
		private boolean isPluginLoaded() {
			String pluginId = ((IExtension) getElement().getParent())
				.getNamespace();
			Bundle bundle = Platform.getBundle(pluginId);

			return null != bundle
				&& bundle.getState() == org.osgi.framework.Bundle.ACTIVE;

		}
	}

	/**
	 * The modifiers services are created per isntance of properties provider if
	 * it has any modifiers associated with it. All instances of the
	 * ModifiersService are kept in a private cache of the PropertiesService
	 * singleton.
	 * 
	 * @author nbalaba
	 */
	static class ModifiersService
		extends Service {

		/**
		 * Create an isntance of the ModifierService
		 */
		public ModifiersService() {
			super();
		}

		/**
		 * Apply modifiers to the property source
		 * 
		 * @param propertySource -
		 *            a property source object which contains properties
		 *            contributed by a single properties provider.
		 * @return - a property source object after modifiers are applied to it
		 */
		public ICompositePropertySource applyModifiers(
				ICompositePropertySource propertySource) {
			ApplyModifiersOperation operation = new ApplyModifiersOperation(
				propertySource);
			this.execute(ExecutionStrategy.FORWARD, operation);
			return operation.getPropertySource();

		}

	}

	private static final String E_MODIFIER_PROVIDER = "Provider"; //$NON-NLS-1$

	private static final String A_CLASS = "class"; //$NON-NLS-1$

	private static final String STAR = "*"; //$NON-NLS-1$

	/**
	 * This attribute stores the instance of the Singleton class.
	 */
	private static PropertiesService uniqueInstance;

	/*
	 * a private cache of the modifier service objects. A ModifierService
	 * instance will be created one per properties provider, given that there
	 * are modifiers for that provider.
	 */
	private Map modifiersServices = new HashMap();

	/*
	 * A private cache of the modifier configuration elements per provider
	 */
	private Map modifiersMap = new HashMap();

	/**
	 * Extension point name for the properties providers extension point.
	 */
	private static final String PROPERTY_PROVIDERS_EXT_P_NAME = "propertiesProviders"; //$NON-NLS-1$

	/**
	 * Extension point name for the properties modifiers extension point.
	 */
	private static final String PROPERTY_MODIFIERS_EXT_P_NAME = "propertyModifiers"; //$NON-NLS-1$	

	/**
	 * This operation implements the logic for returning the same instance of
	 * the Singleton pattern.
	 * 
	 * @return the singleton instance of <code>PropertiesService</code>
	 */
	public static PropertiesService getInstance() {

		if (uniqueInstance == null) {
			uniqueInstance = new PropertiesService();
		}

		return uniqueInstance;
	}

	/**
	 * Constructor for PropertiesService.
	 *  
	 */
	private PropertiesService() {
		super();

		String pluginId = PSFCommonUIPlugin.getPluginId();

		configureProviders(pluginId, PROPERTY_PROVIDERS_EXT_P_NAME);

		configureModifiers(Platform.getExtensionRegistry()
									.getExtensionPoint(pluginId, PROPERTY_MODIFIERS_EXT_P_NAME)
									.getConfigurationElements());
	}

	/**
	 * Return a property source for the given object
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.services.properties.IPropertiesProvider#getPropertySource(Object)
	 */
	public ICompositePropertySource getPropertySource(Object object) {

		GetPropertySourceOperation operation = new GetPropertySourceOperation(
			object);
		this.execute(ExecutionStrategy.FORWARD, operation);
		return operation.getPropertySource();
	}

	/**
	 * Apply property source modifiers appropriate for the given provider
	 * 
	 * @param provider -
	 *            a property provider which contributed the properties
	 * @param propertySource -
	 *            a property source object containing properties, contributed by
	 *            the given provider
	 */
	public void applyModifiers(IPropertiesProvider provider,
			ICompositePropertySource propertySource) {

		ModifiersService modifiersService = getModifiersService(provider);
		if (modifiersService != null)
			modifiersService.applyModifiers(propertySource);

	}

	/**
	 * Retrieve an instance of the ModifierService, appropriate for the given
	 * provider.
	 * 
	 * @param provider an instance of <code>IPropertiesProvider</code>
	 * @return an instance of the ModifierService, appropriate for the given
	 * provider.
	 */
	protected ModifiersService getModifiersService(IPropertiesProvider provider) {
		return (ModifiersService) modifiersServices.get(provider.getClass()
			.getName());
	}

	/**
	 * Configure all modifier elements.
	 * 
	 * @param elements -
	 *            modifier configuration elements
	 */
	public final void configureModifiers(IConfigurationElement[] elements) {

		List modifiersApplicableForAllProviders = new ArrayList();

		for (int i = 0; i < elements.length; i++) {
			IConfigurationElement element = elements[i];

			IConfigurationElement[] associatedProviders = element
				.getChildren(E_MODIFIER_PROVIDER);

			for (int p = 0; p < associatedProviders.length; p++) {
				IConfigurationElement associatedProvider = associatedProviders[p];
				String providerId = associatedProvider.getAttribute(A_CLASS);

				if (providerId.equals(STAR)) // the modifier specified that it
					// is applicable to all (*)
					// providers
					modifiersApplicableForAllProviders.add(element);
				else {
					if (!modifiersMap.containsKey(providerId))
						modifiersMap.put(providerId, new ArrayList());

					((List) modifiersMap.get(providerId)).add(element);
				}
			}
		}

		for (Iterator e = modifiersMap.keySet().iterator(); e.hasNext();) {
			String providerId = (String) e.next();
			ArrayList modifierElements = (ArrayList) modifiersMap
				.get(providerId);
			modifierElements.addAll(modifiersApplicableForAllProviders); // add
			// modifiers
			// for all (*)
			// providers
			int modifiersForProvider = modifierElements.size();
			if (modifiersForProvider > 0) {
				IConfigurationElement[] elementsArray = new IConfigurationElement[modifiersForProvider];
				System.arraycopy(modifierElements.toArray(), 0, elementsArray,
					0, modifierElements.size());

				initModifierServiceFor(providerId, elementsArray);

			}
		}

	}

	/*
	 * Initialize a ModifierService for the property provider with the given Id.
	 * This provider has modifiers attached to it, so it will need a Modifier
	 * service.
	 * 
	 * @param providerId - an id (a full class name) of the properties provider
	 * who needs ModifersService @param modifierElements - configuratoion
	 * elements
	 */
	private void initModifierServiceFor(String providerId,
			IConfigurationElement[] modifierElements) {
		ModifiersService modifierService = new ModifiersService();

		modifierService.configureProviders(modifierElements);
		modifiersServices.put(providerId, modifierService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.core.internal.service.Service#newProviderDescriptor(org.eclipse.core.runtime.IConfigurationElement)
	 */
	protected ProviderDescriptor newProviderDescriptor(
			IConfigurationElement element) {

		String providerId = element.getAttribute(A_CLASS);
		modifiersMap.put(providerId, new ArrayList()); // create modifiers entry
		// for each property
		// provider
		// the entry may be empty - if there are no modifiers for the given
		// provider

		// return whatever super does
		return new PropertiesProviderDescriptor(element);
	}

}