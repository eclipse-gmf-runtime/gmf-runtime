/******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.commands.core.commands;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.emf.type.core.commands.MoveElementsCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;

/**
 * Command to move model elements.
 * <P>
 * If the target features are not specified in the request and the old containment
 * features do not exist in the target element, the first feature in the target
 * container that can contain each moved element will be used.
 * 
 * @author ldamus
 */
public class MSLMoveElementsCommand extends MoveElementsCommand {

	/**
	 * Constructs a new command to move model elements
	 * 
	 * @param request
	 *            the move element request
	 */
	public MSLMoveElementsCommand(MoveRequest request) {

		super(request);
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

		EReference feature = super.getTargetFeature(element);

		if (feature == null) {
			feature = MetaModelUtil.findFeature(getTargetContainer().eClass(),
					element.eClass());
			setTargetFeature(element, feature);
		}
		return feature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.commands.core.internal.commands.EditElementCommand#isExecutable()
	 */
	public boolean isExecutable() {

		if (super.isExecutable()) {

			for (Iterator i = getElementsToMove().keySet().iterator(); i
					.hasNext();) {
				EObject element = (EObject) i.next();
				EReference feature = getTargetFeature(element);

				// IF the element is the parent of the target container...
				if (EObjectUtil.contains(element, getTargetContainer())) {
					// Don't allow the reparenting
					return false;
				}

				// IF the container can not contain the element...
				if (!MetaModelUtil.canContain(getTargetContainer().eClass(),
						feature, element.eClass(), false)) {
					// Don't allow the reparenting
					return false;
				}
			}
		}

		return super.isExecutable();
	}
}