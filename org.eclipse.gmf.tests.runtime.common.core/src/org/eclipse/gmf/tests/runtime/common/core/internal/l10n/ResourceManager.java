/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.tests.runtime.common.core.internal.l10n;

import org.eclipse.core.runtime.Plugin;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;

/**
 * @author Natalia Balaba 
 */
public final class ResourceManager extends AbstractResourceManager {

    /**
     * Constructor for ResourceManager.
     */
    public ResourceManager() {
        super();
    }

    /* (non-Javadoc)
     * @see com.ibm.xtools.common.l10n.AbstractResourceManager#initializeResources()
     */
    protected void initializeResources() {
        initializeMessageResources();
    }

    /**
     * @see org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager#getPlugin()
     */
    protected Plugin getPlugin() {
        return null;
    }

}
