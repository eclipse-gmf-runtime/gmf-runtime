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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.gmf.runtime.emf.core.util.EObjectUtil;
import org.eclipse.gmf.runtime.emf.core.util.MetaModelUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;

/**
 * Command to create a model element. Uses the first feature in the container
 * that can contain the new element if the containment feature is not specified.
 *
 * @author ldamus
 */
public class MSLCreateElementCommand
	extends CreateElementCommand {

	/**
	 * Constructs a new command to create a model element.
	 *
	 * @param request
	 *            the request
	 */
	public MSLCreateElementCommand(CreateElementRequest request) {
		super(request);
	}

	protected EObject doDefaultElementCreation() {
		EReference containment = getContainmentFeature();
		EClass eClass = getElementType().getEClass();

		if (containment != null) {
			EObject element = getElementToEdit();

			if (element != null)
				return EObjectUtil.create(element, containment, eClass);
		}

		return EObjectUtil.create(eClass);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gmf.runtime.emf.type.core.commands.CreateElementCommand#getContainmentFeature()
	 */
	protected EReference getContainmentFeature() {
		EReference feature = super.getContainmentFeature();

		if (feature == null) {
			EClass classToEdit = getEClassToEdit();

			if (classToEdit != null) {
				IElementType type = getElementType();

				if (type != null) {
					feature = MetaModelUtil.findFeature(classToEdit, type.getEClass());

					setContainmentFeature(feature);
				}
			}
		}

		return feature;
	}

	public boolean isExecutable() {
		return super.isExecutable()
			&& MetaModelUtil.canContain(getEClassToEdit(), getContainmentFeature(),
					getElementType().getEClass(), false);
	}
}