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
        return ProxyUtil.getProxyClassID( (EObject)getRealObject() );
    }	
	
    public final EObject resolve() {
        return ProxyUtil.resolve((EObject)getRealObject());
    }
}