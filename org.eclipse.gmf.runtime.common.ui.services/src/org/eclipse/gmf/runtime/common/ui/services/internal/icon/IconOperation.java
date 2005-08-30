/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.internal.icon;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IOperation;

/**
 * The parent of all icon operations.
 * 
 * @author Michael Yee
 */
public abstract class IconOperation implements IOperation {
	
	/**
	 * A hint that does not adapt to anything. Used to indicate
	 * that there is no hint.
	 */
	private final static IAdaptable NULL_HINT = new IAdaptable() {
		public Object getAdapter(Class adapter) {
			return null;
		}
	};
	
    /** the object hint - adaptable to IElement */
    private final IAdaptable hint;
    
    
    /**
     * Constructor for IconOperation.
     * @param hint argument adaptable to IElement
     */
    protected IconOperation(IAdaptable hint) {
        super();
        this.hint = hint;
    }
    
    /**
     * Gets the object hint
     * @return the object hint
     */
    public final IAdaptable getHint() {
    	if (hint == null) {
    		// RATLC00529110 - we no longer assert that the hint 
    		// is not null. Return a dummy adaptable to clients of
    		// this method if the hint is null.
    		return NULL_HINT;
    	}
        return hint;
    }
}
