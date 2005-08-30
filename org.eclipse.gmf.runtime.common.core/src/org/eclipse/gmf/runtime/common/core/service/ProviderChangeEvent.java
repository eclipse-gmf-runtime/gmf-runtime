/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.core.service;

import java.util.EventObject;

/**
 * An event that indicates when a provider changes.
 * 
 * Service implementers are expected to expose service specific sub-classes to
 * help listeners understand the exact nature of the state change. They are not
 * expected to instantiate these events.
 * <p>
 * </p>
 * <p>
 * </p>
 * Service provider implementers never need to subclass this interface or its
 * subclasses, they instead instantiate the service specific subclass in their
 * provider implementation when they need to notify listeners of a state change.
 * 
 * @see AbstractProvider#addProviderChangeListener(IProviderChangeListener)
 * @see IProviderChangeListener#providerChanged(ProviderChangeEvent)
 * @see IProvider#addProviderChangeListener(IProviderChangeListener)
 * @see IProvider#removeProviderChangeListener(IProviderChangeListener)
 */
public class ProviderChangeEvent
	extends EventObject {

	/**
	 * Constructs a new provider change event for the specified provider.
	 * 
	 * @param source
	 *            The provider that changed.
	 */
	public ProviderChangeEvent(IProvider source) {
		super(source);
	}

	/**
	 * Sets the <code>source</code> instance variable to the specified value.
	 * 
	 * This method is reserved for internal use.
	 * 
	 * @param source
	 *            The new value for the <code>source</code> instance variable.
	 */
	public void setSource(IProvider source) {
		assert null != source : "setSource received NULL provider as argument"; //$NON-NLS-1$
		
		assert (source instanceof org.eclipse.gmf.runtime.common.core.service.Service);

		this.source = source;
	}

}