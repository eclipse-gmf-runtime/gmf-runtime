/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 **/
package org.eclipse.gmf.runtime.diagram.ui.editparts;

/**
 * This is a HAck to avoid breaking clients who still send the Unresolved event
 * It should be remomved and replaced by a refresh edit policy 
 * that will be installed by the clients
 *
 */
interface EventType {
    public static final int UNRESOLVE = 1003;
}