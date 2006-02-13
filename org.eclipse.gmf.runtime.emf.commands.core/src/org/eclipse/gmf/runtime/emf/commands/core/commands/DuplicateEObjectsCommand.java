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

package org.eclipse.gmf.runtime.emf.commands.core.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.clipboard.core.ClipboardSupportUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.edit.MEditingDomain;

/**
 * This command duplicates a list of <code>EObjects</code> and adds each
 * duplicate to it's originator's container. All references between objects that
 * are duplicated are updated to refer to the new duplicated objects.
 * <p>
 * The command returns a map which contains all the <code>EObjects</code> that
 * were duplicated mapped to their new duplicated objects. This map can also be
 * retrieved prior to having executed the command (it will be populated when the
 * command is executed). NOTE: The keyset of this map is not just the original
 * objects passed in; more objects may have been duplicated as well (e.g.
 * contained objects).
 * </p>
 * 
 * @author cmahoney
 */
public abstract class DuplicateEObjectsCommand
	extends AbstractTransactionalCommand {

	/**
	 * The list of <code>EObjects</code> to be duplicated.
	 */
	private List objectsToBeDuplicated;

	/**
	 * The map of all duplicated objects to be populated at command execution time..
	 */
	private Map allDuplicatedObjects;

	/**
	 * Constructs a new duplicate EObjects command with the specified label and
	 * list of EObjects.
	 * 
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param label
	 *            The label for the new command.
	 * @param eObjectsToBeDuplicated
	 *            The list of <code>EObjects</code> to be duplicated.
	 */
	public DuplicateEObjectsCommand(TransactionalEditingDomain editingDomain, String label, List eObjectsToBeDuplicated) {
		super(editingDomain, label,
            getWorkspaceFiles(eObjectsToBeDuplicated));
        this.objectsToBeDuplicated = eObjectsToBeDuplicated;
		allDuplicatedObjects = new HashMap();
	}

	/**
	 * Constructs a new duplicate EObjects command with the specified label and
	 * list of EObjects.
	 * 
     * @param editingDomain
     *            the editing domain through which model changes are made
	 * @param label
	 *            The label for the new command.
	 * @param eObjectsToBeDuplicated
	 *            The list of <code>EObjects</code> to be duplicated.
	 * @param allDuplicatedObjectsMap
	 * 			An empty map to be populated with the duplicated objects.
	 */
	public DuplicateEObjectsCommand(TransactionalEditingDomain editingDomain, String label, List eObjectsToBeDuplicated, Map allDuplicatedObjectsMap) {
        super(editingDomain, label,
            getWorkspaceFiles(eObjectsToBeDuplicated));
		this.objectsToBeDuplicated = eObjectsToBeDuplicated;
		this.allDuplicatedObjects = allDuplicatedObjectsMap;
	}
    
    /**
     * Constructs a new duplicate EObjects command with the specified label and
     * list of EObjects.
     * 
     * @param label
     *            The label for the new command.
     * @param eObjectsToBeDuplicated
     *            The list of <code>EObjects</code> to be duplicated.
     * @param allDuplicatedObjectsMap
     *            An empty map to be populated with the duplicated objects.
     * @deprecated Use
     *             {@link #DuplicateEObjectsCommand(TransactionalEditingDomain, String, List, Map)}
     *             instead. This constructor assumed the singleton editing
     *             domain.
     */
    public DuplicateEObjectsCommand(String label, List eObjectsToBeDuplicated,
            Map allDuplicatedObjectsMap) {
        this(MEditingDomain.INSTANCE, label, eObjectsToBeDuplicated,
            allDuplicatedObjectsMap);
    }
	
	/**
	 * Returns a map which contains all the <code>EObjects</code> that were
	 * duplicated mapped to their new duplicated objects. NOTE: The keyset of
	 * this map is not just the original objects passed in; more objects may
	 * have been duplicated as well (e.g. contained objects). This map can be
	 * retrieved prior to command execution time. It will be populated when the
	 * command is executed.
	 * 
	 * @return The map of duplicated objects.
	 */
	protected Map getAllDuplicatedObjectsMap() {
		return allDuplicatedObjects;
	}

	/**
	 * Returns the list of objects to be duplicated.
	 * 
	 * @return The list of objects to be duplicated.
	 */
	protected List getObjectsToBeDuplicated() {
		return objectsToBeDuplicated;
	}

	/**
	 * Verifies that the container of all the original objects can contain
	 * multiple objects.
	 * 
	 */
	public boolean canExecute() {
		for (Iterator iter = objectsToBeDuplicated.iterator(); iter.hasNext();) {
			EObject original = (EObject) iter.next();

			EReference reference = original.eContainmentFeature();
			if (reference == null || !reference.isMany()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Executes this command by duplicating the orignal eobjects, adding the
	 * duplicates to the original's container, and populating the map of
	 * duplicates to be returned.
	 * 
	 */
	protected CommandResult doExecuteWithResult(
            IProgressMonitor progressMonitor, IAdaptable info)
        throws ExecutionException {
		
		// Remove elements whose container is getting copied.
		ClipboardSupportUtil.getCopyElements(getObjectsToBeDuplicated());

		// Perform the copy and update the references.
		EcoreUtil.Copier copier = new EcoreUtil.Copier();
		copier.copyAll(objectsToBeDuplicated);
		copier.copyReferences();

		// Update the map with all elements duplicated.
		getAllDuplicatedObjectsMap().putAll(copier);

		// Add the duplicates to the original's container.
		for (Iterator i = objectsToBeDuplicated.iterator(); i.hasNext();) {
			EObject original = (EObject) i.next();
			EObject duplicate = (EObject) copier.get(original);

			EReference reference = original.eContainmentFeature();
			if (reference != null
				&& reference.isMany()
				&& ClipboardSupportUtil.isOkToAppendEObjectAt(
					original.eContainer(), reference, duplicate)) {
				
				ClipboardSupportUtil.appendEObjectAt(original.eContainer(),
					reference, duplicate);
			}
		}
		return CommandResult.newOKCommandResult(getAllDuplicatedObjectsMap());
	}

}