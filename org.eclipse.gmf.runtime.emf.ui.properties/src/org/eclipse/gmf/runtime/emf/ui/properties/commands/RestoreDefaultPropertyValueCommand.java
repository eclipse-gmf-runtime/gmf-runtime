/******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.properties.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractModelCommand;

/**
 * Command to restore a property value to its default value using the
 * {@link org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)}
 * method.
 * <P>
 * This command is undoable and redoable.
 * <P>
 * @author ldamus
 */
public class RestoreDefaultPropertyValueCommand extends AbstractModelCommand {

	/**
	 * Flag to indicate that the property value was reset by this command. The
	 * property value is not reset to its default if the property source's
	 * <code>isPropertySet()</code> method returns <code>false</code>.
	 */
	private boolean valueResetOnUndo = false;

	/**
	 * The property source that owns the property to be restored to its 
	 * default value.
	 */
	private final IPropertySource propertySource;

	/**
	 * The ID of the property whose default value is to be restored.
	 */
	private final Object propertyId;

	/**
	 * The property value which is used when this command execution is undone.
	 */
	private Object undoValue;

	/**
	 * Constructs a new command with the property source and the id of the
	 * property to be reset.
	 * 
	 * @param label The label for the command. Appears in the Edit menu items.
	 * @param affectedObjects The model operation context for the new model command.
	 * @param propertySource The property source that owns the property
	 * 						 whose value is to be reset.
	 * @param propertyId The ID of the property to be reset.
	 */
	public RestoreDefaultPropertyValueCommand(
		String label,
		Object affectedObjects,
		IPropertySource propertySource,
		Object propertyId) {

		super(label, affectedObjects);
		this.propertySource = propertySource;
		this.propertyId = propertyId;
	}

	/* (non-Javadoc)
		 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#isRedoable()
		 */
	public boolean isRedoable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#isUndoable()
	 */
	public boolean isUndoable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doExecute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected CommandResult doExecute(IProgressMonitor progressMonitor) {

		CommandResult result;
		setValueResetOnUndo(getPropertySource().isPropertySet(getPropertyId()));

		if (isValueResetOnUndo()) {
			setUndoValue(getPropertySource().getPropertyValue(getPropertyId()));
			getPropertySource().resetPropertyValue(getPropertyId());
			result =
				newOKCommandResult(
					getPropertySource().getPropertyValue(getPropertyId()));

		} else {
			undoValue = null;
			result = newOKCommandResult();
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doRedo()
	 */
	protected CommandResult doRedo() {
		return doExecute(new NullProgressMonitor());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#doUndo()
	 */
	protected CommandResult doUndo() {
		CommandResult result;
		if (isValueResetOnUndo()) {
			getPropertySource().setPropertyValue(
				getPropertyId(),
				getUndoValue());
			result = newOKCommandResult(getUndoValue());
		} else {
			getPropertySource().resetPropertyValue(getPropertyId());
			result =
				newOKCommandResult(
					getPropertySource().getPropertyValue(getPropertyId()));
		}
		return result;
	}

	/**
	 * Gets the flag that indicates that the property value was reset by this
	 * command. The property value is not reset to its default if the property
	 * source's <code>isPropertySet()</code> method returns <code>false</code>.
	 * 
	 * @return <code>true</code> if the property value was reset by this command,
	 * 		   <code>false</code> otherwise.
	 */
	protected boolean isValueResetOnUndo() {
		return valueResetOnUndo;
	}

	/**
	 * Sets the flag that indicates that the property value was reset by this
	 * command. The property value is not reset to its default if the property
	 * source's <code>isPropertySet()</code> method returns <code>false</code>.
	 * 
	 * @param b <code>true</code> if the property value was reset by this command,
	 * 		    <code>false</code> otherwise.
	 */
	protected void setValueResetOnUndo(boolean b) {
		valueResetOnUndo = b;
	}

	/**
	 * Gets the property source that owns the property to be restored to its 
	 * default value.
	 * 
	 * @return the property source
	 */
	protected IPropertySource getPropertySource() {
		return propertySource;
	}

	/**
	 * Gets the ID of the property whose default value is to be restored.
	 * 
	 * @return the property ID
	 */
	protected Object getPropertyId() {
		return propertyId;
	}

	/**
	 * Gets the property value which is used when this command execution is 
	 * undone.
	 * 
	 * @return the undo property value
	 */
	protected Object getUndoValue() {
		return undoValue;
	}

	/**
	 * Sets the property value which is used when this command execution is 
	 * undone.
	 * 
	 * @param object the undo property value
	 */
	protected void setUndoValue(Object object) {
		undoValue = object;
	}

}
