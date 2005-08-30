/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2003, 2004.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.sortfilter;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceNode;

import org.eclipse.gmf.runtime.diagram.ui.dialogs.sortfilter.SortFilterPage;

/**
 * Extended to provide access from the <code>PreferenceNode</code> to the <code>
 * PreferenceDialog</code>.
 * 
 * @author jcorchis
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
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
