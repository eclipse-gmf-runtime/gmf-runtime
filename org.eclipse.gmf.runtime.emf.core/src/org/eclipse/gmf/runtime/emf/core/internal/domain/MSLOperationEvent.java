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

package org.eclipse.gmf.runtime.emf.core.internal.domain;

import java.util.List;

import org.eclipse.gmf.runtime.emf.core.IOperationEvent;

/**
 * Implementation of the public API IOperationEvent interface.
 * 
 * @author rafikj
 */
public class MSLOperationEvent
	implements IOperationEvent {

	private List notifications = null;

	/**
	 * Constructor.
	 */
	public MSLOperationEvent(List notifications) {
		super();
		this.notifications = notifications;
	}

	/**
	 * @see org.eclipse.gmf.runtime.emf.core.IOperationEvent#getNotifications()
	 */
	public List getNotifications() {
		return notifications;
	}
}