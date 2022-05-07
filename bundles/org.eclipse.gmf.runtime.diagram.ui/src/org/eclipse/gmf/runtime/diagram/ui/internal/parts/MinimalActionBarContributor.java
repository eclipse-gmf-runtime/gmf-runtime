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

package org.eclipse.gmf.runtime.diagram.ui.internal.parts;

import org.eclipse.gef.ui.actions.ActionBarContributor;


/**
 * Minimal action bar contributor that does not use the Contribution
 * Item Service.
 * 
 * @author wdiu, Wayne Diu
 */
public class MinimalActionBarContributor extends ActionBarContributor{

	/**
	 * Do nothing.
	 * 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions() {
		//do nothing
	}

	/**
	 * Do nothing.
	 * 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	protected void declareGlobalActionKeys() {
		//do nothing
	}

}
