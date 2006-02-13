/******************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.commands.core.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;

/**
 * A command that creates model elements given a parent collection,
 * a type identifier and optionally a name.
 * 
 * Optionally, the position in the
 * parent collection at which to add the new element can also be specified.
 * 
 * @author khussey
 */
public class CreateEObjectCommand
	extends AbstractTransactionalCommand {

	/**
	 * The feature to contain the new element.
	 */
	private final EReference feature;

	/**
	 * The name of the element to be created. May be null if
	 * the element should be created without a name.
	 */
	private final String name;
	
	/**
	 * The container element in which the new element will
	 * be created.
	 */
	private EObject container;

	/**
	 * The kind of the element to be created.
	 */
	private final EClass elementKind;

	/**
	 * Retrieves the value of the <code>feature</code> instance
	 * variable.
	 * 
	 * @return The value of the <code>feature</code> instance
	 *          variable.
	 */
	protected EReference getFeature() {
		return feature;
	}

	/**
	 * Retrieves the value of the <code>container</code> instance
	 * variable.
	 * 
	 * @return The value of the <code>container</code> instance
	 *          variable.
	 */
	protected EObject getContainer() {
		return container;
	}

	/**
	 * Retrieves the value of the <code>name</code> instance variable.
	 * 
	 * @return The value of the <code>name</code> instance variable.
	 */
	protected String getName() {
		return name;
	}

	/**
	 * Retrieves the value of the <code>elementKind</code> instance variable.
	 * 
	 * @return The value of the <code>elementKind</code> instance variable.
	 */
	protected EClass getElementKind() {
		return elementKind;
	}

	/**
	 * Constructs a new create object command with the specified label,
	 * container, feature, name, and element kind.
	 * 
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param label The label for the new command.
	 * @param container The parent in which the new object should be created.
	 * @param feature The feature of the container that will hold the new object.
	 * @param name The name of the object to be created.
	 * @param elementKind The kind of the element to be created.
	 */
	public CreateEObjectCommand(TransactionalEditingDomain editingDomain, String label,
            EObject container, EReference feature, String name,
            EClass elementKind) {

		super(editingDomain, label, getWorkspaceFiles(container));

		this.name = name;
		this.container = container;
		this.feature = feature;
		this.elementKind = elementKind;
	}

	/**
	 * Constructs a new create element command with the specified label,
	 * container, feature and element kind.
	 * 
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param label The label for the new command.
	 * @param container The parent collection for element to be
	 *                           created.
	 * @param feature The feature of the container that will hold the new object.
	 * @param elementKind The kind of the element to be created.
	 */
	public CreateEObjectCommand(TransactionalEditingDomain editingDomain, String label, EObject container,
		EReference feature, EClass elementKind) {

		this(editingDomain, label, container, feature, null, elementKind);
	}

	/**
	 * Executes this create object command by creating the specified kind of
	 * object with the specified name and adding it to the specified parent
	 * feature.
	 */
	protected CommandResult doExecuteWithResult(IProgressMonitor progressMonitor,
            IAdaptable info)
        throws ExecutionException {

		EObject element = null;

		if (getName() != null) {
			// Create the element with a name
			element = EMFCoreUtil.create(getContainer(), getFeature(),
				getElementKind());
            EMFCoreUtil.setName(element, getName());

		} else {
			// Create the element without a name
			element = EMFCoreUtil.create(getContainer(), getFeature(),
				getElementKind());
		}

		return CommandResult.newOKCommandResult(element);
	}
}
