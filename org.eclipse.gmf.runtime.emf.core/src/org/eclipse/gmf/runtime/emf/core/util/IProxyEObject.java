/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
