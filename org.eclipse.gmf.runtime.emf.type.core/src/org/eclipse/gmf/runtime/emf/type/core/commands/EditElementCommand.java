/******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.type.core.commands;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.internal.requests.RequestCacheEntries;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Abstract superclass for commands that modify model elements.
 * 
 * @author ldamus
 */
public abstract class EditElementCommand
	extends AbstractTransactionalCommand {

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

		super(request.getEditingDomain(), label, getAffectedFiles(request));
		this.elementToEdit = elementToEdit;
		this.request = request;
	}
	
    protected static List getAffectedFiles(IEditCommandRequest request) {
		Map cacheMaps = (Map) request
			.getParameter(RequestCacheEntries.Cache_Maps);
		if (cacheMaps != null) {
			return (List) cacheMaps.get(RequestCacheEntries.Affected_Files);
		}

		List result = new ArrayList();
		List elements = request.getElementsToEdit();
		int size;
		if (elements != null && ((size = elements.size()) > 0)) {
			if (size == 1) {
				Resource resource = ((EObject) elements.get(0)).eResource();
				if (resource != null) {
					IFile file = WorkspaceSynchronizer.getFile(resource);
					if (file != null) {
						result.add(file);
					}
				}
			} else {
				Map resourcesToFileMap = new HashMap();
				for (int i = 0; i < size; ++i) {
					Resource resource = ((EObject) elements.get(i)).eResource();
					if (resource != null) {
						Object file = resourcesToFileMap.get(resource);
						//if it is in the Map, then it is in the List already as well
						if (file == null) {
							file = WorkspaceSynchronizer.getFile(resource);
							if (file != null) {
								resourcesToFileMap.put(resource, file);
								result.add(file);
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Checks that the element to be modified by this command is of the correct
	 * kind.
	 */
	public boolean canExecute() {

		if (getEClass() != null) {
			return getEClass().isSuperTypeOf(getEClassToEdit());
		}
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
}