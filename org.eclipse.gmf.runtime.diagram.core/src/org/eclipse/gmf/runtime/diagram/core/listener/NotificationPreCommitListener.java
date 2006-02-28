/******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.diagram.core.listener;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;

/**
 * Defines an EMF {@link Notification} listener; any class interested in
 * listening to pre-commit events from the {@link DiagramEventBroker} which
 * implements this interface. the transactionAboutToCommit method will get
 * called by the
 * {@link DiagramEventBroker#handleTransactionAboutToCommitEvent(Notification)}
 * 
 * @author cmahoney
 */
public interface NotificationPreCommitListener {

	/**
	 * Will be called when a Notification event gets sent from the
	 * DiagramEventBroker during the firing of pre-commit events.
	 * 
	 * @param notification
	 *            the notification object
	 */
	public Command transactionAboutToCommit(Notification notification);

}
