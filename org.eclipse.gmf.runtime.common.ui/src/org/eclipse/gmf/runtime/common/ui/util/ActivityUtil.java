/******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.util;

import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.IIdentifier;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.activities.WorkbenchActivityHelper;

/**
 * Utility method for activities (also known as capabilities).
 * 
 * @author crevells
 */
public class ActivityUtil {

    /**
     * Checks if there are activities that have been matched to the plug-in or
     * id in which the item has been contributed and if at least one of those
     * matching activities are enabled.identifier is always considered enabled
     * unless it matches only disabled activities.
     * 
     * @param localID
     *            the local id of the contribution. Must not be
     *            <code>null</code>. This should correspond to the
     *            extension-specific identifier for a given contribution.
     * @param pluginID
     *            the id of the originating plugin. Can be <code>null</code>
     *            if this contribution did not originate from a plugin.
     * @return true unless the ids match only disabled activities.
     */
    public static boolean isEnabled(final String localID, final String pluginID) {
        if (!WorkbenchActivityHelper.isFiltering())
            return true;

        IWorkbenchActivitySupport workbenchActivitySupport = PlatformUI
            .getWorkbench().getActivitySupport();
        IIdentifier id = workbenchActivitySupport.getActivityManager()
            .getIdentifier(
                WorkbenchActivityHelper
                    .createUnifiedId(new IPluginContribution() {

                        public String getLocalId() {
                            return localID;
                        }

                        public String getPluginId() {
                            return pluginID;
                        }
                    }));
        if (id != null && !id.isEnabled()) {
            return false;
        }

        return true;
    }
}
