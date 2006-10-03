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
import org.eclipse.gmf.runtime.common.core.service.IProvider;

/**
 * The operation used with the <code>IconService</code>.  GetIconOperation
 * is instantiated by the <code>IconService</code> with an <code>IAdaptable</code> 
 * element for which an icon is to be retrieved, and is executed using 
 * FIRST <code>ExecutionStrategy</code>.  
 *
 * @author Michael Yee
 */
public class GetIconOperation extends IconOperation {
	final private int flags;

    /**
     * Constructor for GetIconOperation.
     * @param hint argument adaptable to IElement
     * @param flags icon flags, ex. IconOptions.NONE
     */
    protected GetIconOperation(IAdaptable hint, int flags) {
        super(hint);
        this.flags = flags;
    }

    /**
     * @see org.eclipse.gmf.runtime.common.core.service.IOperation#execute(IProvider)
     */
    public Object execute(IProvider provider) {
        return ((IIconProvider) provider).getIcon(getHint(), flags);
    }
}
