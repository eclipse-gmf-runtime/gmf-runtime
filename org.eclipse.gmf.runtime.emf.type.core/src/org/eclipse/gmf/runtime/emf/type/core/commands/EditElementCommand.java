/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.emf.type.core.commands;

import java.util.Collection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.gmf.runtime.common.core.command.AbstractCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Abstract superclass for commands that modify model elements.
 * 
 * @author ldamus
 */
public abstract class EditElementCommand
	extends AbstractCommand {

	/**
	 * The element to be modified.
	 */
	private EObject elementToEdit;

	/**
	 * The edit request that carries the required command parameters.
	 */
	private final IEditCommandRequest request;

	/**
	 * The kind of model element that can be modified by this command.
	 */
	private EClass eClass = null;

	/**
	 * Constructs a new command instance.
	 * 
	 * @param label
	 *            the command label
	 * @param elementToEdit
	 *            the element to be modified
	 * @param request
	 *            the edt request
	 */
	protected EditElementCommand(String label, EObject elementToEdit,
			IEditCommandRequest request) {

		super(label);
		this.elementToEdit = elementToEdit;
		this.request = request;
	}

	/**
	 * Checks that the element to be modified by this command is of the correct
	 * kind.
	 */
	public boolean isExecutable() {

		if (getEClass() != null) {
			return getEClass().isSuperTypeOf(getEClassToEdit());
		}
		return true;
	}
	
	public boolean isUndoable() {
		return true;
	}
	
	public boolean isRedoable() {
		return true;
	}

	/**
	 * Gets the element to be modified by this command.
	 * 
	 * @return the element to be modified
	 */
	protected EObject getElementToEdit() {
		return elementToEdit;
	}
	
	protected EClass getEClassToEdit() {
		return getElementToEdit().eClass();
	}
	
	/**
	 * Sets the element to be modified by this command.
	 * 
	 * @return the element to be modified
	 */
	protected void setElementToEdit(EObject element) {
		this.elementToEdit = element;
	}

	/**
	 * Gets the edit request.
	 * 
	 * @return the edit request
	 */
	protected IEditCommandRequest getRequest() {
		return request;
	}

	/**
	 * Gets the kind of element that can be modified by this command.
	 * 
	 * @return the kind of element
	 */
	protected EClass getEClass() {
		return eClass;
	}

	/**
	 * Sets the kind of element that can be modified by this command.
	 * 
	 * @param eClass
	 *            the kind of element
	 */
	protected void setEClass(EClass eClass) {
		this.eClass = eClass;
	}

	/**
	 * Convenience method to check the status of <code>commandRequest</code>.
	 * 
	 * @param commandResult
	 *            the command result to check
	 * @return <code>true</code> if the result is OK, <code>false</code>
	 *         otherwise.
	 */
	protected boolean isOK(CommandResult commandResult) {
		return commandResult.getStatus().getCode() == IStatus.OK;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.common.core.command.AbstractCommand#getAffectedObjects()
	 */
	public Collection getAffectedObjects() {
		return request.getElementsToEdit();
	}
}