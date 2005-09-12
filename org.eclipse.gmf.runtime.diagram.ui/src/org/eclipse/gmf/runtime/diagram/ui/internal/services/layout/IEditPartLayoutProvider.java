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

package org.eclipse.gmf.runtime.diagram.ui.internal.services.layout;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;

import org.eclipse.gmf.runtime.diagram.ui.services.layout.ILayoutNodesProvider;




/**
 * @author sshaw
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 *
 * Internal interface for defining a more GEF based approach based on editparts and
 * returning a command.
 */
public interface IEditPartLayoutProvider
	extends ILayoutNodesProvider {
 
	/**
     * Layout the objects in this container 
     * using the specified layout type.
     * 
     * @param containerEditPart GraphicalEditPart that is the container 
     * for the layout operation.  
     * @param layoutHint IAdaptable hint to the provider to determine
	 * the layout kind.
	 * @return Command
     */
    public Command layoutEditParts(
        GraphicalEditPart containerEditPart,
		IAdaptable layoutHint);

    /**
     * Layout this list of selected objects,  
     * using the specified layout hint.
     * The selected objects all reside within the same parent container.  
     * Other elements that are part of the container but not specified in
     * the list of objects, are ignored.
     * 
     * @param selectedObjects List of objects that are to be layed out.     
     * @param layoutHint IAdaptable hint to the provider to determine
	 * the layout kind.
	 * @return Command
     */
    public Command layoutEditParts(
        List selectedObjects,
		IAdaptable layoutHint);
}
