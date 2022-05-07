/******************************************************************************
 * Copyright (c) 2002, 2005 IBM Corporation and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.common.ui.services.dnd.drop;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.ITransferAgent;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;

/**
 * Interface to be implemented by providers to handle drop target events
 * 
 * @author Vishy Ramaswamy
 */
public interface IDropTargetListener
	extends DropTargetListener {

	/**
	 * Returns a command for dropping the event data on the drop target. The
	 * event passed in corresponds to the drop event. The listener can get the
	 * current target from the context.
	 * 
	 * @param event
	 *            the event associated with the drop event
	 * @return Returns a command for dropping the event data on the drop target.
	 */
	public ICommand getExecutableContext(DropTargetEvent event);

	/**
	 * Returns a boolean indicating whether the listener can support drop
	 * operations on the current target. The target context, current event and
	 * current transfer agent information is passed to the listener. This method
	 * is invoked whenever the target changes.
	 * 
	 * @param context
	 *            The drop action context
	 * @param currentEvent
	 *            The current drop event
	 * @param currentAgent
	 *            the current transfer agent
	 * @return Returns true or false
	 */
	public boolean canSupport(IDropTargetContext context,
			IDropTargetEvent currentEvent, ITransferAgent currentAgent);

	/**
	 * Provides the listener an opportunity to set the feedback when hovering
	 * over a target. The listener can decide whether drag under effect is
	 * enabled for the current target.
	 * 
	 * @param event
	 *            The drop target event
	 */
	public void setFeedback(DropTargetEvent event);

	/**
	 * Returns the supporting transfer agent ids.
	 * 
	 * @return return the supporting transfer agent ids
	 */
	public String[] getSupportingTransferIds();
}