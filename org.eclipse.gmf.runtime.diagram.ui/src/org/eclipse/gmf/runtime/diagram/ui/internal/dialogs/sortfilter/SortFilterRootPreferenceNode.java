/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.sortfilter;

import org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter.SortFilterPage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceNode;

/**
 * Extended to provide access from the <code>PreferenceNode</code> to the <code>
 * PreferenceDialog</code>.
 * 
 * @author jcorchis
 */
public class SortFilterRootPreferenceNode extends PreferenceNode {
	
	private PreferenceDialog preferenceDialog;
	
	/**
	 * Constructor for <code>SortFilterRootPreferenceNode</code>
	 * @param rootName name of the root node
	 * @param page the page associated with the root node
	 * @param sortFilterDialog the <code>PreferenceDialog</code>
	 */
	public SortFilterRootPreferenceNode(String rootName, SortFilterPage page, PreferenceDialog sortFilterDialog) {
		super(rootName, page);
		this.preferenceDialog = sortFilterDialog;
	}
	
	/**
	 * Returns the <code>PreferenceDialog</code> associated with this <code>PreferenceNode</code>
	 * @return the <code>PreferenceDialog</code> associated with this <code>PreferenceNode</code>
	 */
	public PreferenceDialog getPreferenceDialog() {
		return this.preferenceDialog;		
	}

}
