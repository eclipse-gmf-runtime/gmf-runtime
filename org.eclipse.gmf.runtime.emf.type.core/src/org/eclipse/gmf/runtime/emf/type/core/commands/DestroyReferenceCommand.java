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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.FeatureMapUtil;

import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;

/**
 * Command to destroy a reference from one element to another.
 * 
 * @author ldamus
 */
public class DestroyReferenceCommand
	extends EditElementCommand {

	/**
	 * The element that contains the reference.
	 */
	private EObject container;

	/**
	 * The feature in the <code>container</code> that contains the reference.
	 */
	private EReference containingFeature;

	/**
	 * The referenced object.
	 */
	private EObject referencedObject;

	/**
	 * Constructs a new command to destroy a model element.
	 * 
	 * @param request
	 *            the destroy element requestO
	 */
	public DestroyReferenceCommand(DestroyReferenceRequest request) {

		super(request.getLabel(), request.getContainer(), request);
		this.container = request.getContainer();
		this.containingFeature = request.getContainingFeature();
		this.referencedObject = request.getReferencedObject();
	}

    protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
        throws ExecutionException {

		List features = new ArrayList();

		if (getContainingFeature() == null) {
			features.addAll(getContainer().eClass().getEAllReferences());

		} else {
			features.add(getContainingFeature());
		}

		for (Iterator i = features.iterator(); i.hasNext();) {
			EReference nextReference = (EReference) i.next();
			if (nextReference.isChangeable() && !nextReference.isDerived()
				&& getContainer().eIsSet(nextReference)) {
				if (FeatureMapUtil.isMany(getContainer(), nextReference)) {
					Collection referenceCollection = (Collection) getContainer()
						.eGet(nextReference);
					referenceCollection.remove(getReferencedObject());
				} else if (getContainer().eGet(nextReference) == getReferencedObject()) {
					getContainer().eSet(nextReference, null);
				}
			}
		}
		return CommandResult.newOKCommandResult();
	}

	/**
	 * Gets element that contains the reference.
	 * 
	 * @return the element that contains the reference
	 */
	protected EObject getContainer() {
		return container;
	}

	/**
	 * Gets the feature in the <code>container</code> that contains the
	 * reference.
	 * 
	 * @return the feature in the <code>container</code> that contains the
	 *         reference.
	 */
	protected EReference getContainingFeature() {
		return containingFeature;
	}

	/**
	 * Gets the referenced object.
	 * 
	 * @return the referenced object.
	 */
	protected EObject getReferencedObject() {
		return referencedObject;
	}

	public boolean canExecute() {
		return getReferencedObject() != null && getContainer() != null;
	}

}