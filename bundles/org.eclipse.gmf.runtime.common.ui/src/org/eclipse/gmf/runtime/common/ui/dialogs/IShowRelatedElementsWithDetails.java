/******************************************************************************
 * Copyright (c) 2002, 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.dialogs;

/**
 * Show Related Elements with details interface.
 * 
 * This is for interfaces that support Show Related Elements, including details
 * instead of just presets.
 * 
 * @author wdiu, Wayne Diu
 */
public interface IShowRelatedElementsWithDetails {

	/**
	 * Toggle showing or hiding details.
	 */
	public void showOrHideDetails();

	/**
	 * Update the relationships and other settings should be updated in the
	 * details pane according to the presets.
	 * 
	 * @param preset
	 *            the ShowRelatedElementsPreset
	 */
	public void updateRelationships(ShowRelatedElementsPreset preset);

	/**
	 * Return the current settings of the details pane in a nameless
	 * ShowRelatedElementsPreset object.
	 * 
	 * @return ShowRelatedElementsPreset object containing current settings of
	 *         the details pane
	 */
	public ShowRelatedElementsPreset getCurrentSettings();

	/**
	 * This method is called as a notification that the details have changed.
	 */
	public void detailsChanged();
}