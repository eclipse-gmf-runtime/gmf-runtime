/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.editors;

import org.eclipse.ui.ISaveablePart;

/**
 * Workbench parts can implement or adapt to this interface to participate
 * in the enablement and execution of the <code>Revert</code> action.
 * 
 * @author ldamus
 */
public interface IRevertiblePart
	extends ISaveablePart {

	/**
	 * Reverts this part by abandoning changes made in the part since the last
	 * save operation.
	 */
	public void doRevertToSaved();
}