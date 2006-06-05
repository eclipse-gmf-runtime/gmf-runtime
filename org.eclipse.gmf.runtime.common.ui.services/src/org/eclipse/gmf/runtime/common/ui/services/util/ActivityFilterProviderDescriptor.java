/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.IIdentifier;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;
import org.eclipse.ui.activities.WorkbenchActivityHelper;

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
	 * Returns true if and only if any matching activites are enabled.
	 */
	public boolean provides(IOperation operation) {
		return areActivitiesEnabled();
	}

	/**
	 * Checks if there are activities that have been matched to the plug-in in
	 * which the provider has been contributed and if those activities are
	 * enabled.
	 * 
	 * @return true if matching activities are enabled
	 */
	private boolean areActivitiesEnabled() {
		if (!WorkbenchActivityHelper.isFiltering())
			return true;

		IWorkbenchActivitySupport workbenchActivitySupport = PlatformUI
			.getWorkbench().getActivitySupport();
		IIdentifier id = workbenchActivitySupport.getActivityManager()
			.getIdentifier(
				WorkbenchActivityHelper
					.createUnifiedId(new IPluginContribution() {

						public String getLocalId() {
							return getElement().getDeclaringExtension()
								.getSimpleIdentifier();
						}

						public String getPluginId() {
							return getElement().getContributor().getName();
						}
					}));
		if (id != null && !id.isEnabled()) {
			return false;
		}

		return true;
	}
}
