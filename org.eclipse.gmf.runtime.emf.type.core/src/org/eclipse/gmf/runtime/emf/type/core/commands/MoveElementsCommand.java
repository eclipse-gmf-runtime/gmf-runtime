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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.emf.type.core.internal.l10n.EMFTypeCoreMessages;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;

/**
 * Command to move model elements using the EMF action protocol.
 * <P>
 * If the target features are not specified in the request, and the features
 * fomerly containing the moved elements exists in the target element, they will
 * be used.
 * 
 * @author ldamus
 */
public class MoveElementsCommand extends EditElementCommand {

	/**
	 * The map of <code>EObject</code>s to be moved. Keyed on
	 * <code>EObject</code>. Each value is the <code>EReference</code>
	 * feature in the target element into which the element should be moved.
	 * <P>
	 * If the feature is not specified for a given element, then a default
	 * feature is found in the target.
	 */
	private final Map elementsToMove;

	/**
	 * The target container into which the element will be moved.
	 */
	private final EObject targetContainer;

	/**
	 * Constructs a new command to move a model element
	 * 
	 * @param request
	 *            the move element request
	 */
	public MoveElementsCommand(MoveRequest request) {

		super(request.getLabel(), request.getTargetContainer(), request);
		this.elementsToMove = request.getElementsToMove();
		this.targetContainer = request.getTargetContainer();
	}

	protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
	    throws ExecutionException {

		for (Iterator i = getElementsToMove().keySet().iterator(); i.hasNext();) {
			EObject element = (EObject) i.next();
			EReference feature = getTargetFeature(element);

			if (feature != null) {
				if (FeatureMapUtil.isMany(targetContainer, feature)) {
					((Collection) targetContainer.eGet(feature)).add(element);

				} else {
					targetContainer.eSet(feature, element);
				}
			} else {
				return CommandResult.newErrorCommandResult(EMFTypeCoreMessages.moveElementsCommand_noTargetFeature);
			}
		}

		return CommandResult.newOKCommandResult();
	}

	/**
	 * Gets the feature in the target element that should contain
	 * <code>element</code> after it is moved.
	 * <P>
	 * Looks for the feature first in the elements map. If none is specified,
	 * tries to use the same feature that contained the element in its old
	 * location. If the old containment feature doesn't exist in the new target,
	 * uses the MSL utility to find the first feature in the target that can
	 * contain the element being moved.
	 * 
	 * @param element
	 *            the element to be moved
	 * @return the feature that will contain the element in the target
	 */
	protected EReference getTargetFeature(EObject element) {

		EReference feature = (EReference) getElementsToMove().get(element);

		if (feature == null) {
			EReference oldContainmentFeature = element.eContainmentFeature();

			if (getTargetContainer().eClass().getEAllReferences().contains(
					oldContainmentFeature)) {
				getElementsToMove().put(element, oldContainmentFeature);
				feature = oldContainmentFeature;
			}
		}

		if (feature == null) {
			feature = PackageUtil.findFeature(getTargetContainer().eClass(),
					element.eClass());
			setTargetFeature(element, feature);
		}

		return feature;
	}

	/**
	 * Gets the target container.
	 * 
	 * @return the target container
	 */
	protected EObject getTargetContainer() {
		return targetContainer;
	}

	/**
	 * Gets the map of elements to be moved. Each entry in the map consists of
	 * an <code>EObject</code> key, which is the element to be moved to the
	 * new target, and an <code>EReference</code> value, which is the feature
	 * in the new target that should contain the moved element.
	 * 
	 * @return the map of elements to be moved
	 */
	protected Map getElementsToMove() {
		return elementsToMove;
	}

	/**
	 * Sets the reference feature into which an element should be moved.
	 * 
	 * @param element
	 *            the element to be moved
	 * @param targetFeature
	 *            the target feature
	 */
	protected void setTargetFeature(EObject element, EReference targetFeature) {
		getElementsToMove().put(element, targetFeature);
	}

	public boolean canExecute() {

		EObject container = getTargetContainer();

		if (container == null || elementsToMove == null
				|| elementsToMove.isEmpty()) {
			return false;
		}

		for (Iterator i = getElementsToMove().keySet().iterator(); i.hasNext();) {
			EObject element = (EObject) i.next();
			EReference feature = getTargetFeature(element);

			if (feature == null
					|| !container.eClass().getEAllReferences()
							.contains(feature)) {
				// If the target feature doesn't exist in the target container,
				// don't allow the move.
				return false;
			}

			// IF the element is already in the target container...
			if (container.equals(element.eContainer())
					&& feature == element.eContainmentFeature()) {
				// Don't allow the reparenting
				return false;
			}

			// IF the element is the parent of the target container...
			if (EcoreUtil.isAncestor(element, getTargetContainer())) {
				// Don't allow the reparenting
				return false;
			}

			// IF the container can not contain the element...
			if (!PackageUtil.canContain(getTargetContainer().eClass(),
					feature, element.eClass(), false)) {
				// Don't allow the reparenting
				return false;
			}
		}

		return true;
	}

}