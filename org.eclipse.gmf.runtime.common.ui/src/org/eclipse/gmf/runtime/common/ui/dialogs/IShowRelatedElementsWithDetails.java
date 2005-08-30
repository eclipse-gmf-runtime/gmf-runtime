/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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