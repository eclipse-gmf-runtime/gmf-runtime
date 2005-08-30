/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.core.util;

import org.eclipse.emf.ecore.EObject;



/**
 * Utility interface that provides proxy information; it is typically provided
 * by an {@linkplain org.eclipse.core.runtime.IAdaptable adapter} for some
 * instance of an EMF metaclass ({@link org.eclipse.emf.ecore.EClass}).
 * <p>
 * This interface may be implemented by clients.
 * </p>
 * 
 * @author mhanner
 */
public interface IProxyEObject {

    /**
     * Obtains the ID (fully-qualified name) of the
     * {@link org.eclipse.emf.ecore.EClass} of the proxy object.
     * 
     * @return the <code>EClass</code> ID (must not be <code>null</code>)
     * 
     * @see ProxyUtil#getProxyClassID(EObject)
     */
    Object getProxyClassID();
    
    /**
     * Resolves the proxy.
     * 
     * @return the resolved object, or <code>null</code> if the proxy could not
     *     be resolved
     * 
     * @see ProxyUtil#resolve(EObject)
     */
    EObject resolve();
}
