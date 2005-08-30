/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.internal.dialogs.sortfilter;

import org.eclipse.jface.preference.PreferenceManager;

/**
 * <code>PageManager</code> for the Sort/Filter dialog.
 * 
 * @author jcorchis
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class SortFilterPageManager extends PreferenceManager {
	
	public SortFilterPageManager() {
		super('+');
	}
	

}
