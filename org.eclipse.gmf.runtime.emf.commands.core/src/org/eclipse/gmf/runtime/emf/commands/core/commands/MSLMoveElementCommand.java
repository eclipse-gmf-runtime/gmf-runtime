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

import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.emf.type.core.commands.MoveElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.MoveRequest;

/**
 * Command to move a model element.
 * <P>
 * If the target feature is not specified in the request, the first feature in
 * the target container that can contain the new kind of element will be used.
 * 
 * @author ldamus
 * 
 * @deprecated Use {@link MSLMoveElementsCommand} instead. Deprecated
 *             on 12/22/2005 as per bugzilla
 *             https://bugs.eclipse.org/bugs/show_bug.cgi?id=112662.
 */
public class MSLMoveElementCommand
	extends MoveElementCommand {

	/**
	 * Constructs a new command to move a model element
	 * 
	 * @param request
	 *            the move element request
	 */
	public MSLMoveElementCommand(MoveRequest request) {

		super(request);
	}

	/**
	 * Gets the target feature into which the element will be moved.
	 * 
	 * @return the target feature
	 */
	protected EReference getTargetFeature() {

		if (super.getTargetFeature() == null) {
			setTargetFeature(MetaModelUtil.findFeature(
				getTargetContainer().eClass(), getElementToMove().eClass()));
		}
		return super.getTargetFeature();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.emf.commands.core.internal.commands.EditElementCommand#isExecutable()
	 */
	public boolean isExecutable() {

		if (super.isExecutable()) {
			// IF the element is the parent of the target container...
			if (EObjectUtil.contains(getElementToMove(), getTargetContainer())) {
				// Don't allow the reparenting
				return false;
			}
	
			// IF the container can not contain the element...
			if (!MetaModelUtil.canContain(getTargetContainer().eClass(), getElementToMove()
				.eClass(), false)) {
				// Don't allow the reparenting
				return false;
			}
		}

		return super.isExecutable();
	}

}