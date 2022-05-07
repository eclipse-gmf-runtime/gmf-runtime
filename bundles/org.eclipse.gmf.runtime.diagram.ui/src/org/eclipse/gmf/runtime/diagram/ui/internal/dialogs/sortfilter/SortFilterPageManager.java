/******************************************************************************
 * Copyright (c) 2002, 2003 IBM Corporation and others.
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

import org.eclipse.jface.preference.PreferenceManager;

/**
 * <code>PageManager</code> for the Sort/Filter dialog.
 * 
 * @author jcorchis
 */
public class SortFilterPageManager extends PreferenceManager {
	
	public SortFilterPageManager() {
		super('+');
	}
	

}
