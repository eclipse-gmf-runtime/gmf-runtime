/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.util;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.gmf.runtime.common.core.service.IOperation;
import org.eclipse.gmf.runtime.common.core.service.Service;
import org.eclipse.gmf.runtime.common.ui.util.ActivityUtil;

/**
 * A provider descriptor that will ignore providers that are contributed by a
 * plug-in that is matched to a disabled activity/capability.
 * 
 * @author cmahoney
 */
public class ActivityFilterProviderDescriptor
	extends Service.ProviderDescriptor {

	/**
	 * Creates a new instance.
	 * 
	 * @param element
	 */
	public ActivityFilterProviderDescriptor(IConfigurationElement element) {
		super(element);
	}

	/**
     * Returns false if and only if any matching activites are disabled.
     */
    public boolean provides(IOperation operation) {
        if (getElement().isValid()) {
            return ActivityUtil
                .isEnabled(getElement().getDeclaringExtension()
                    .getSimpleIdentifier(), getElement().getContributor()
                    .getName());
        }
        return true;
    }

}
