/******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;


/**
 * @author tisrar
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * This interface implemented by editparts to evaluate if a 
 * note can be added as a peer connected by a note attachment to it. 
 */
public interface INoteableEditPart {

	/**
	 * This will return true if a note can be added as a peer connected by a note 
	 * attachment to the editpart which will implement this interface. 
	 *  
	 * @return boolean True: if note can be attached to it, false otherwise.
	 */
	public boolean canAttachNote();
	
}
