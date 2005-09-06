/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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
	protected ActivityFilterProviderDescriptor(IConfigurationElement element) {
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
							return getElement().getNamespace();
						}
					}));
		if (id != null && !id.isEnabled()) {
			return false;
		}

		return true;
	}
}
