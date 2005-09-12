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
 * Interface implemented to get around problems with
 * contribution item service.  All objects that are not
 * "diagram edit parts" will implement this interface.
 * 
 * @author schafe
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public interface IContainedEditPart {
	//marker to denote all edit parts that are not diagram edit parts
	//TODO: remove when contribution item service supports notValue for objectclass
}
