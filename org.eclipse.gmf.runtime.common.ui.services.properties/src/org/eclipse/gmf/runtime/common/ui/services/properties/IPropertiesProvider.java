/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.properties;

import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * A <code>IPropertiesProvider</code> object. Each such provider contributes a
 * set of properties as <code>ICompositePropertySource</code> object for the
 * given target.
 * 
 * All contributions from such providers will be assembled by the properties
 * service into a property source object.
 */

public interface IPropertiesProvider extends IProvider {

    /**
     * A call to contribute a set of properties as
     * <code>ICompositePropertySource</code> object for the given target.
     * 
     * This contribution will be appended to the properties contributed by other providers.
     * 
     * @param object -
     *            target of the properties
     * @return - properties contributed by this provider
     */
    public ICompositePropertySource getPropertySource(Object object);

}