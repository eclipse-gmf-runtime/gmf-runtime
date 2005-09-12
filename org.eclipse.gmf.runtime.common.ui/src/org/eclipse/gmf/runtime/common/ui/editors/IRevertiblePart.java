/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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