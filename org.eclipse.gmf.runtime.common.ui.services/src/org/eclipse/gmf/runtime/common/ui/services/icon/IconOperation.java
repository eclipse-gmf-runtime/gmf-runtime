/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.icon;

import org.eclipse.core.runtime.IAdaptable;

/**
 * The parent of all icon operations.
 * 
 * @author Michael Yee
 */
public abstract class IconOperation implements IIconOperation {
	
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
