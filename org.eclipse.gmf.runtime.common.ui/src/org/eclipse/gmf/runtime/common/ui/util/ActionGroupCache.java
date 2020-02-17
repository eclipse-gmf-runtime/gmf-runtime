/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.ui.actions.ActionGroup;

/**
 * Cache for holding the ActionGroups created for part descriptors. This allows
 * them to be properly disposed of when nolonger needed.
 * 
 * There are not a lot of ActionGroups so try to be most efficient when none
 * 
 * @author hfraser
 */
public class ActionGroupCache {

	// map between part descriptors and the actiongroups defined for it.
	private Map partDescriptorActionGroups;

	/**
	 * disposes all the action groups associated with the partDescriptor
	 * supplied.
	 * 
	 * @param partDescriptor -
	 *            part whose action groups are to be disposed.
	 */
	public void dispose(IWorkbenchPartDescriptor partDescriptor) {
		if (partDescriptorActionGroups != null) {
			Map actionGroupRegistry = (Map) partDescriptorActionGroups
				.get(partDescriptor);
			if (actionGroupRegistry != null) {
				for (Iterator actionGroups = actionGroupRegistry.values()
					.iterator(); actionGroups.hasNext();) {
					ActionGroup actionGroup = (ActionGroup) actionGroups.next();
					actionGroup.dispose();
				}
			}
			partDescriptorActionGroups.remove(partDescriptor);
		}
	}

	/**
	 * Identifies if there are any action groups associated with the part
	 * descriptor supplied.
	 * 
	 * @param partDescriptor
	 *            part to check if it has any action groups.
	 * @return true if there are action groups associated, false otherwise
	 */
	public boolean contains(IWorkbenchPartDescriptor partDescriptor) {
		return (partDescriptorActionGroups != null && partDescriptorActionGroups
			.containsKey(partDescriptor));
	}

	/**
	 * Get the cached ActionGroup for the part descriptor with the id supplied
	 * 
	 * @param actionGroupId
	 *            id of the action group to retrieve
	 * @param partDescriptor
	 *            part whose action group is to be retrieved
	 * @return the ActionGroup previously created for the part, otherwise null
	 */
	public ActionGroup getActionGroup(String actionGroupId,
			IWorkbenchPartDescriptor partDescriptor) {

		ActionGroup actionGroup = null;
		if (partDescriptorActionGroups != null) {
			Map registry = (Map) partDescriptorActionGroups.get(partDescriptor);
			if (registry != null) {
				actionGroup = (ActionGroup) registry.get(actionGroupId);
			}
		}
		return actionGroup;
	}

	/**
	 * Add the action group just created with the id to the cache for the part
	 * supplied
	 * 
	 * @param actionGroupId
	 *            id of the action group to be cached
	 * @param actionGroup
	 *            action group to cache
	 * @param partDescriptor
	 *            part group was created for
	 */
	public void addActionGroup(String actionGroupId, ActionGroup actionGroup,
			IWorkbenchPartDescriptor partDescriptor) {
		Map registry = null;
		if (partDescriptorActionGroups == null) {
			partDescriptorActionGroups = new HashMap();
		} else {
			registry = (Map) partDescriptorActionGroups.get(partDescriptor);
		}

		if (registry == null) {
			registry = new HashMap(3);
			partDescriptorActionGroups.put(partDescriptor, registry);
		}

		registry.put(actionGroupId, actionGroup);
	}

}