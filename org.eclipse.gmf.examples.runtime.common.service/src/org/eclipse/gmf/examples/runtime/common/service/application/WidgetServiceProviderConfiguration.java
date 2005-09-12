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

import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.gmf.runtime.common.core.service.AbstractProviderConfiguration;

/** 
 * Provider configuration for the WidgetService. It helps in filtering out
 * and delay loading unrelated providers.
 * 
 */
public class WidgetServiceProviderConfiguration
	extends AbstractProviderConfiguration {

	/** constants corresponding to different symbols in the extention schema */
	private static final String ORDER_SIZE = "orderSize";//$NON-NLS-1$
	private static final String MAX = "max"; //$NON-NLS-1$
	private static final String MIN = "min"; //$NON-NLS-1$	
	
	private int minOrder;
	private int maxOrder;
	
	/**
	 * Builds a new provider contribution descriptor by parsing its configuration element
	 * 
	 * @param configElement A provider configuration element
	 * @return A provider XML contribution descriptor
	 */
	public static WidgetServiceProviderConfiguration parse(IConfigurationElement configElement) {
		assert null != configElement : "null provider configuration element"; //$NON-NLS-1$
		return new WidgetServiceProviderConfiguration(configElement);
	}

	/**
	 * Creates a new <code>ProviderContributionDescriptor</code> instance
	 * given a provider configuration element
	 * 
	 * @param configElement The provider XML configuration element
	 */
	private WidgetServiceProviderConfiguration(IConfigurationElement configElement) {
		IConfigurationElement configChildren[];

		configChildren = configElement.getChildren(ORDER_SIZE);
			String max_id = configChildren[0].getAttribute(MAX);
			String min_id = configChildren[0].getAttribute(MIN);			
			if (max_id != null)
				maxOrder = Integer.parseInt(max_id);
			if (min_id != null)
				maxOrder = Integer.parseInt(max_id);

	}
	
	/**
	 * This provider will support the operation if it supports the order size.
	 * @param orderSize
	 * @return
	 */
	public boolean supports(int orderSize) {
		return (orderSize >= minOrder && orderSize <= maxOrder);
	}	
}
