/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.icon;

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.ui.services.internal.icon.IconOperation;

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
