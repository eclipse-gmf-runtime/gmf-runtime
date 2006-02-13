/******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.ui.properties.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * Command to restore a property value to its default value using the
 * {@link org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)}
 * method.
 * <P>
 * This command is undoable and redoable.
 * <P>
 * @author ldamus
 */
public class RestoreDefaultPropertyValueCommand extends AbstractTransactionalCommand {

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
     * @param editingDomain the editing domain in which to make the change
	 * @param label The label for the command. Appears in the Edit menu items.
	 * @param affectedObjects The model operation context for the new model command.
	 * @param propertySource The property source that owns the property
	 * 						 whose value is to be reset.
	 * @param propertyId The ID of the property to be reset.
	 */
	public RestoreDefaultPropertyValueCommand(TransactionalEditingDomain editingDomain,  
		String label,
		Object affectedObjects,
		IPropertySource propertySource,
		Object propertyId) {

		super(
            editingDomain,
            label,
            (affectedObjects instanceof EObject) ? getWorkspaceFiles((EObject) affectedObjects)
                : null);
        this.propertySource = propertySource;
		this.propertyId = propertyId;
	}

	public boolean canRedo() {
		return true;
	}

	public boolean canUndo() {
		return true;
	}

	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {

		CommandResult result;
		setValueResetOnUndo(getPropertySource().isPropertySet(getPropertyId()));

		if (isValueResetOnUndo()) {
			setUndoValue(getPropertySource().getPropertyValue(getPropertyId()));
			getPropertySource().resetPropertyValue(getPropertyId());
			result =
				CommandResult.newOKCommandResult(
					getPropertySource().getPropertyValue(getPropertyId()));

		} else {
			undoValue = null;
			result = CommandResult.newOKCommandResult();
		}
		return result;
	}

	protected CommandResult doRedoWithResult(IProgressMonitor progressMonitor, IAdaptable info) throws ExecutionException {
		return doExecuteWithResult(progressMonitor, info);
	}

	protected CommandResult doUndoWithResult(IProgressMonitor progressMonitor, IAdaptable info) throws ExecutionException {
		CommandResult result;
		if (isValueResetOnUndo()) {
			getPropertySource().setPropertyValue(
				getPropertyId(),
				getUndoValue());
			result = CommandResult.newOKCommandResult(getUndoValue());
		} else {
			getPropertySource().resetPropertyValue(getPropertyId());
			result =
                CommandResult.newOKCommandResult(
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
