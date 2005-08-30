/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.properties;

import org.eclipse.ui.views.properties.IPropertySource;

/**
 * The composite property source is a linked list of composite property sources. 
 * This allows the property service to chain property sources contributed by the different 
 * providers
 */
public interface ICompositePropertySource
	extends IPropertySource {

	/**
	 * Add another composite property source to the linked list
	 * 
	 * @param source - a composite property source to be added to the linked list
	 */
	public void addPropertySource(ICompositePropertySource source);

}
