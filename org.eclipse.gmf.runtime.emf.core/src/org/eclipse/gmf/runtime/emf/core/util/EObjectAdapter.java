/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.util;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.util.Proxy;

/**
 * Wraps an {@link EObject} to adapt it to the {@link IAdaptable} Eclipse
 * platform API.  This is useful for passing <code>EObject</code>s into
 * APIs that consume adaptables.
 * <p>
 * See the {@link #getAdapter(Class)} method for the supported adaptations.
 * <b>Note</b> that this implementation does not consult the Eclipse platform's
 * adapter factory manager for registered adapters.
 * </p>
 * <p>
 * <b>Note</b> that this class is not intended to be extended
 * by clients.  Clients may instantiate it.
 * </p>
 * 
 * @author Anthony Hunter
 * 
 * @see #getAdapter(Class) 
 */
public class EObjectAdapter extends Proxy implements IAdaptable, IProxyEObject {

	public EObjectAdapter(EObject element) {
		super(element);
	}
	
	/**
	 * Returns the wrapped {@link EObject} as the adapter when possible.
	 * The following adaptations are supported:
	 * <ul>
	 *   <li>if the wrapped <code>EObject</code> conforms to the
	 *       <code>adapter</code> type, then it is returned</li>
	 *   <li>if this adapter, itself, conforms to the <code>adapter</code>
	 *       type, then it is returned</li>
	 *   <li>otherwise, there is no adapter (<code>null</code> returned)</li>
	 * </ul>
	 */
	public Object getAdapter(Class adapter) {
	    if ( adapter.isInstance(getRealObject() )) {
	        return getRealObject();
	    }
	    if ( adapter.isInstance(this) ) {
	        return this;
	    }
		return null;
	}
	
    public final Object getProxyClassID() {
        return PackageUtil.getID(EMFCoreUtil.getProxyClass((EObject)getRealObject()));
    }	
	
    public final EObject resolve() {
    	// there is no editing domain or resource set context available in
    	//    which to attempt to resolve the proxy, so if it wasn't already
    	//    resolved, we can only return null
    	EObject eObject = (EObject) getRealObject();
    	
        return eObject.eIsProxy()? null : eObject;
    }
}