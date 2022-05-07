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

package org.eclipse.gmf.runtime.diagram.ui.internal.editparts;


/**
 * Interface implemented to get around problems with
 * contribution item service.  All objects that are not
 * "diagram edit parts" will implement this interface.
 * 
 * @author schafe
 */
public interface IContainedEditPart {
	//marker to denote all edit parts that are not diagram edit parts
	//TODO: remove when contribution item service supports notValue for objectclass
}
