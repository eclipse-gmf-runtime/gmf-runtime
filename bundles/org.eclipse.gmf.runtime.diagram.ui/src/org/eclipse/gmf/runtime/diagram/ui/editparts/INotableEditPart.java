/******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.editparts;

/**
 * This interface implemented by editparts to evaluate if a note can be added as
 * a peer connected by a note attachment to it.
 * 
 * @author tisrar, cmahoney
 */
public interface INotableEditPart {

	/**
	 * This will return true if a note can be added as a peer connected by a
	 * note attachment to the editpart which will implement this interface.
	 * 
	 * @return True if a note can be attached to it, false otherwise.
	 */
	public boolean canAttachNote();

}
