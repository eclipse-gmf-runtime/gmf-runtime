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


package org.eclipse.gmf.runtime.diagram.ui.editpolicies;


/**
 * Concrete class that extends the <code>ComponentEditPolicy</code>.
 * This edit policy will return a command in response to delete requests.
 * 
 * @author Jody Schofield
 */
public class ViewComponentEditPolicy extends ComponentEditPolicy {

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.editpolicies.ComponentEditPolicy#shouldDeleteSemantic()
	 */
	protected boolean shouldDeleteSemantic() {

		return false;
	}
	
}
