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


package org.eclipse.gmf.runtime.diagram.ui.editparts;


/**
 * Interface to let us know that is a Compartment Edit Part that
 * is resizable
 * @author choang@ca.ibm.com
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IResizableCompartmentEditPart extends IGraphicalEditPart {
	
	/**
	 * @return the <code>String</code> that is the title header typically
	 * displayed at the top of the compartment.  It is used to identity a meaningful
	 * description of the contents of the compartment.
	 */
	public String getCompartmentName();
}
