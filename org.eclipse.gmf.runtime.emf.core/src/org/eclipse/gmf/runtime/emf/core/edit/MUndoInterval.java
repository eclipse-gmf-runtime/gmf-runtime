/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
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