/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

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
     * @see org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager#initializeResources()
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
