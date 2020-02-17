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

package org.eclipse.gmf.runtime.common.ui.viewers;

/**
 * Defines viewer hint constants used by various viewers and their
 * collaborators.
 * 
 * @author dmisic
 */
public interface ViewerHintConstants {

	/**
	 * The id for tree expansion level hint. The value associated with this id
	 * should be an instance of Integer.
	 */
	static final String ID_TREE_EXPANSION_LEVEL = "org.eclipse.gmf.runtime.common.ui.viewers.TreeExpansionLevel"; //$NON-NLS-1$
}