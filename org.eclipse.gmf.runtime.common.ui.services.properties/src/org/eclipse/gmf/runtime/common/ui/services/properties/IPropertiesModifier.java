/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.properties;

import org.eclipse.gmf.runtime.common.core.service.IProvider;


/**
 * An interface to be implemented by the property modifier classes.
 * 
 * A property modifier is responsible for modifying properties, contributed by 
 * a property provider(s). A property modifier is attached to a property provider(s).
 * After the propety provider is done with property contribution, the modifier is
 * applied to the properties, in order to write-protect and/or set flags and/or override
 * display names.
 * 
 * If the modifier is to be applied to all providers - the XML declaration of the modifier
 * should specify the class of the provider as a '*'. 
 * 
 * @author nbalaba
 * @canBeSeenBy %partners 
 */
public interface IPropertiesModifier extends IProvider {
	
	/**
	 * Apply code <code>ICompositePropertySource</code>
	 * 
	 * @param propertySource <code>ICompositePropertySource</code> to be applied
	 */
	public void apply(ICompositePropertySource propertySource);

}
