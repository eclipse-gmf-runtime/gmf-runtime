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

package org.eclipse.gmf.runtime.diagram.ui.internal.parts;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.diagram.core.listener.NotificationListener;

/**
 * This class is an extension of the <code>NotificationListener</code> interface to allow clients
 * handling the listener to determine which viewer the notification is relevant for.
 * 
 * @author sshaw
 *
 */
public interface NotificationForEditPartsListener extends NotificationListener {
	
	/**
	 * @return
	 */
	public EditPartViewer getViewer();
}
