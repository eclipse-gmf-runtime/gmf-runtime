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

package org.eclipse.gmf.runtime.emf.core.edit;

/**
 * Interface that describes an undo interval. An undo interval object will be
 * returened when the currently open undo interval is closed. The cluent will
 * need to maintain the list of closed undo interval to be able to undo/redo
 * them.
 * 
 * @author rafikj
 */
public interface MUndoInterval {

	/**
	 * Gets the label associated with the interval.
	 * 
	 * @return The label.
	 */
	public String getLabel();

	/**
	 * Gets the description associated with the interval.
	 * 
	 * @return The description.
	 */
	public String getDescription();

	/**
	 * Checks if the undo interval can be undone.
	 * 
	 * @return True if the interval can be undone, false otherwise.
	 */
	public boolean canUndo();

	/**
	 * Checks if the undo interval can be redone.
	 * 
	 * @return True if the interval can be redone, false otherwise.
	 */
	public boolean canRedo();

	/**
	 * Undoes the undo interval and all intervals that were closed after it.
	 */
	public void undo();

	/**
	 * Redoes the undo interval and all intervals that were closed after it.
	 */
	public void redo();

	/**
	 * Checks if undo interval is empty.
	 * 
	 * @return True if interval is empty, false otherwise.
	 */
	public boolean isEmpty();

	/**
	 * Flushes (removes) this undo interval and all older intervals (intervals
	 * that were closed before it).
	 */
	public void flush();
}