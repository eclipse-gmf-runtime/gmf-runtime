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

import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * An operation which will execute providers in order of priorities (from
 * HIGHEST to LOWEST), obtain a property source from each, and append each result
 * into a linked list of <code>ICompositePropertySource</code> property source
 * objects.
 */

public class GetPropertySourceOperation implements IOperation {

    private Object object;

    private ICompositePropertySource propertySource;

    /**
     * Create a GetPropertySourceOperation instance
     * 
     * @param object -
     *            target of the properties
     */
    public GetPropertySourceOperation(Object object) {

        this.object = object;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(org.eclipse.gmf.runtime.common.core.service.IProvider)
     */
    public Object execute(IProvider provider) {
        assert null!= provider : "provider cannot be null"; //$NON-NLS-1$

        if (provider instanceof IPropertiesProvider) {
            IPropertiesProvider propertieProvider = (IPropertiesProvider) provider;
            ICompositePropertySource result = propertieProvider
                    .getPropertySource(object);

            if (result != null)
                PropertiesService.getInstance().applyModifiers(
                        propertieProvider, result);

            if (propertySource == null) {
                propertySource = result;
                return propertySource;
            }

            if (result != null)
                propertySource.addPropertySource(result);

        }
        return propertySource;
    }

    /**
     * Returns the propertySource - a linked list of
     * <code>ICompositePropertySource</code> property source objects.
     * 
     * @return ICompositePropertySource - the propertySource
     */
    public ICompositePropertySource getPropertySource() {
        return propertySource;
    }

    /**
     * Returns the object - target of the properties
     * 
     * @return Object - target of the properties
     */
    public Object getObject() {
        return object;
    }

}